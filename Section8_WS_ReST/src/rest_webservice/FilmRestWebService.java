package rest_webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.FilmList;
import model_beans.Output;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Rest web service<br>
 * Handles each FilmDAO crud method
 */
@WebServlet(name = "FilmRestWebService", value = "/FilmRestWebService")
public class FilmRestWebService extends HttpServlet {

    Output output = new Output();

    // todo for extra marks - accept sending full json object and xml, as well as key / values
    // see video @ 40:30

    /**
     * restful web service for FilmDAO 'get' methods<br>
     *     getAllFilms, getFilmsByName & getFilmById
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FilmDAO filmDAO = FilmDAO.getInstance();
        Gson gson = new Gson();

        PrintWriter printWriter = response.getWriter();
        String dataFormat = "json";
        int filmId = -1;
        String action = "getallfilms";
        String filmName = "";
        ArrayList<Film> filmList = new ArrayList<>();

        // sets 'get' method to call, i.e. getall, getbyid or getbyname
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        // add logic for handling request parameters
        if (request.getParameter("format") != null) {                   // data format, e.g. xml/json/text
            dataFormat = request.getParameter("format");
        }

        if (request.getParameter("id") != null) {                       // required for getFilmById
            filmId = Integer.parseInt(request.getParameter("id"));
        }

        if (request.getParameter("filmName") != null) {                 // required for getFilmByName
            filmName = request.getParameter("filmName");
        }

        // add logic for handling GET request type
        if (action.equals("getallfilms")) {
            filmList = filmDAO.getAllFilms();

        } else if (action.equals("getfilmbyid")) {
            filmList.add(filmDAO.getFilmById(filmId));

        } else if (action.equals("getfilmsbyname")) {
            filmList = filmDAO.getFilmByName(filmName);
        }

        // add logic for handling data format
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            printWriter.write(gson.toJson(filmList));
        }

        else if (dataFormat.equals("xml")) {
            try {
                response.setContentType("text/xml");
                printWriter.write(output.xmlGenerator(filmList));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            response.setContentType("text/plain");
            printWriter.write(output.stringGenerator(filmList));
        }
    }


    /**
     * restful web service for FilmDAO addFilm method
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FilmDAO filmDAO = FilmDAO.getInstance();
        PrintWriter printWriter = response.getWriter();

        String title = request.getParameter("title");
        int year = Integer.valueOf(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");

        // using overloaded Film constructor, without passing id
        // addFilm method automatically generates
        Film film = new Film(title, year, director, stars, review);
        filmDAO.addFilm(film);

        printWriter.write("Film added. Check the database to confirm, if required");
    }


    /**
     * restful web service for FilmDAO updateFilm method<br>
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FilmDAO filmDAO = FilmDAO.getInstance();
        PrintWriter printWriter = response.getWriter();

        int id = Integer.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");
        int year = Integer.valueOf(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");

        Film film = new Film(id, title, year, director, stars, review);
        filmDAO.updateFilm(film);

        printWriter.write("Film updated. Check the database to confirm, if required");
    }


    /**
     * restful web service for FilmDAO deleteFilm method<br>
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FilmDAO filmDAO = FilmDAO.getInstance();
        PrintWriter printWriter = response.getWriter();
        int id = Integer.valueOf(request.getParameter("id"));

        try {
            filmDAO.deleteFilm(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        printWriter.write("Film deleted. Check the database to confirm, if required");
    }
}
