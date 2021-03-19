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

/**
 * Web servlet controller<br>
 *     interacts with FilmDAO model bean<br>
 *     inserts film record into connected MySQL db<br>
 *     displays new film in browser
 */
@WebServlet(name = "AddFilm", value = "/AddFilm")
public class AddFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

        // extract film object parameters from request body
        // int filmId = Integer.parseInt(request.getParameter("filmid"));
        String filmName = request.getParameter("name");
        int filmYear = Integer.parseInt(request.getParameter("year"));
        String filmStars = request.getParameter("stars");
        String filmDirector = request.getParameter("director");
        String filmReview = request.getParameter("review");

        // todo refactor FilmDAO call, to handle new FilmDAO singleton class

        // instantiate FilmDAO
        FilmDAO filmDAO = new FilmDAO();

        // create Film object, passing request params
        Film film = new Film(filmName, filmYear, filmStars,
                filmDirector, filmReview);

        // call insert film method
        Film newFilm = filmDAO.addFilm(film);
        ArrayList<Film> filmList = new ArrayList<>();
        filmList.add(newFilm);

        // pass films array into request object
        request.setAttribute("films", filmList);

        String viewJspFilePath = "";            // string object to pass into jsp

        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";

        } else if (dataFormat.equals("xml")) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

        } else if (dataFormat.equals("text")){
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
