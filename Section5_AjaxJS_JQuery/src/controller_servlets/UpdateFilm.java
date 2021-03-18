package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.Output;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// todo code to be added

@WebServlet(name = "UpdateFilm", value = "/UpdateFilm")
public class UpdateFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // todo not quite got this working
        // probably don't want to display updated table, just a message
        // compiles film object from values in the request
        // passes into filmUpdate dao method
        // gets return code back
        // displays that return code in webform div

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // access parameter for format. Set default to json if none is sent
        // String dataFormat = request.getParameter("format");
        // if (dataFormat == null) dataFormat = "xml";

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

        // instantiate FilmDAO
        FilmDAO filmDAO = new FilmDAO();

        int updatedFilm = 0;

        // call update film method
        updatedFilm = filmDAO.updateFilm(film);

        String returnMessage = "";

        // todo this is the response expected back to jquery updateFilm function
        if (updatedFilm == 0) {
            returnMessage = "Film update failed";

        } else {
            returnMessage = "Film updated";
        }

        response.getWriter().write(returnMessage);  // this should get returned to JQ updateFilm fnc

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
