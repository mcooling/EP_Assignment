package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Web servlet controller<br>
 *     interacts with FilmDAO model bean<br>
 *     inserts film record into connected MySQL db<br>
 *     prints validation response to browser and console
 */

@WebServlet(name = "InsertFilm", value = "/InsertFilm")
public class InsertFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // extract film object parameters from request body
        int filmId = Integer.parseInt(request.getParameter("filmid"));
        String filmName = request.getParameter("name");
        int filmYear = Integer.parseInt(request.getParameter("year"));
        String filmStars = request.getParameter("stars");
        String filmDirector = request.getParameter("director");
        String filmReview = request.getParameter("review");

        // instantiate FilmDAO
        FilmDAO filmDAO = new FilmDAO();

        // create Film object, passing request params
        Film film = new Film(filmId, filmName, filmYear, filmStars,
                filmDirector, filmReview);

        // call insert film method
        filmDAO.insertFilm(film);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
