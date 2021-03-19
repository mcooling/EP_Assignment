package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Web servlet controller<br>
 * called by ajax JQuery function
 * interacts with FilmDAO model bean<br>
 * deletes film record from connected MySQL db<br>
 * returns success / failed message<br>
 * message based on SQL response code (0 or 1)
 */
@WebServlet(name = "DeleteFilm", value = "/DeleteFilm")
public class DeleteFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        int filmId = Integer.parseInt(request.getParameter("filmId"));
        int filmToDelete = 0;

        // todo refactor FilmDAO call, to handle new FilmDAO singleton class

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();

        try {
            filmToDelete = filmDAO.deleteFilm(filmId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String returnMessage = "";

        if (filmToDelete == 0) {
            returnMessage = "Delete request failed";

        } else {
            returnMessage = "Delete request successful";
        }
        response.getWriter().write(returnMessage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
