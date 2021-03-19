package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        String searchFilmName = request.getParameter("filmname");

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

        // todo refactor FilmDAO call, to handle new FilmDAO singleton class

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = FilmDAO.getInstance();
        ArrayList<Film> allFilms = filmDAO.getFilmByName(searchFilmName);

        // pass films array into request object
        request.setAttribute("films", allFilms);

        // string object to pass into jsp
        String viewJspFilePath = "";

        // test json generator method call
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";

        } else if (dataFormat.equals("xml")) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

        } else if (dataFormat.equals("text")) {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";
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
