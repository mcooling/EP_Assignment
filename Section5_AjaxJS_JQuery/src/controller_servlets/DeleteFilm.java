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

// todo code to be added

@WebServlet(name = "DeleteFilm", value = "/DeleteFilm")
public class DeleteFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // String jspDisplayString = "";

        // access parameter for format. Set default to json if none is sent
        // String dataFormat = request.getParameter("format");
        // if (dataFormat == null) dataFormat = "json";

        int filmId = Integer.parseInt(request.getParameter("filmId"));
        int filmToDelete = 0;

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();

        try {
            filmToDelete = filmDAO.deleteFilm(filmId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // ArrayList<Film> allFilms = filmDAO.getAllFilms();

        // pass films array into request object
        // request.setAttribute("films", allFilms);

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
