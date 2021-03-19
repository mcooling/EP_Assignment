package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * called by JQuery ajax function<br>
 * updates existing db Film record, via FilmDAO method<br>
 * returns SQL success code (0 or 1)
 */
@WebServlet(name = "UpdateFilm", value = "/UpdateFilm")
public class UpdateFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // extract film object parameters from request body
        int filmId = Integer.parseInt(request.getParameter("filmid"));
        String filmName = request.getParameter("name");
        int filmYear = Integer.parseInt(request.getParameter("year"));
        String filmDirector = request.getParameter("director");
        String filmStars = request.getParameter("stars");
        String filmReview = request.getParameter("review");

        // create Film object, passing request params
        Film film = new Film(filmId, filmName, filmYear, filmDirector,
                filmStars, filmReview);

        // todo refactor FilmDAO call, to handle new FilmDAO singleton class

        // instantiate FilmDAO
        FilmDAO filmDAO = FilmDAO.getInstance();

        int updatedFilm = 0;

        // call update film method
        updatedFilm = filmDAO.updateFilm(film);

        String returnMessage = "";

        // response expected back to jquery updateFilm function
        if (updatedFilm == 0) {
            returnMessage = "Film update failed";

        } else {
            returnMessage = "Film updated";
        }
        response.getWriter().write(returnMessage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
