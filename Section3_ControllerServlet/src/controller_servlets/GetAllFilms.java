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
 *     fetches all films from connected MySQL db<br>
 *     prints output to browser and console for debug
 */

@WebServlet(name = "GetAllFilms", value = "/GetAllFilms")
public class GetAllFilms extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getAllFilms();

        // todo do i need a separate test class, or is this basically what the servlet is doing?
        // same q applies to other servlets

        // iterate through list and print to web page
        for (Film film : allFilms) {

            out.println(
                    "Film ID: " + film.getId() + "<br>" +
                    "Name: " + film.getTitle() + "<br>" +
                    "Year: " + film.getYear() + "<br>" +
                    "Stars: " + film.getStars() + "<br>" +
                    "Director: " + film.getDirector() + "<br>" +
                    "Plot: " + film.getReview() + "<br><br>"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }
}
