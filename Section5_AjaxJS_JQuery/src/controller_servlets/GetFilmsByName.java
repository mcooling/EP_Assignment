package controller_servlets;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.FilmList;
import model_beans.Output;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

// todo broken. needs fixing

/**
 * Web servlet controller<br>
 *     interacts with FilmDAO model bean<br>
 *     fetches matching films from connected MySQL db<br>
 *     using a film name search string as input parameter<br>
 *     prints output to browser and console for debug
 */

@WebServlet(name = "GetFilms", value = "/GetFilms")
public class GetFilmsByName extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String jspDisplayString = "";

        String searchFilmName = request.getParameter("filmname");

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getFilmByName(searchFilmName);

        // pass films array into request object
        request.setAttribute("films", allFilms);

        // string object to pass into jsp
        String viewJspFilePath = "";

        Output output = new Output();

        // test json generator method call
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";
            jspDisplayString = output.jsonGenerator(allFilms);

        } else if (dataFormat.equals("xml")) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            // calls jaxb xml generator method
            try {
                jspDisplayString = output.xmlGenerator(allFilms);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        } else if (dataFormat.equals("text")) {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";

            // calls string generator method
            jspDisplayString = output.stringGenerator(allFilms);
        }

        // add dispatcher, to forward content to view jsp
        RequestDispatcher dispatcher =
                request.getRequestDispatcher(viewJspFilePath);
        dispatcher.include(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
