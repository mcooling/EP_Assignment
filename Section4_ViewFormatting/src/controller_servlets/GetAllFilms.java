package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        // iterate through list and print to console for debug
        for (Film film : allFilms) {

            System.out.println(film.getTitle());

            out.println(film.getId());
            out.println(film.getTitle());
            out.println(film.getYear());
            out.println(film.getStars());
            out.println(film.getDirector());
            out.println(film.getReview());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
