package controller_servlets;

import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "GetFilmById", value = "/GetFilmById")
public class GetFilmById extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String temp = request.getParameter("filmId");

        int filmId = Integer.parseInt(request.getParameter("filmId"));

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = FilmDAO.getInstance();
        Film film = filmDAO.getFilmById(filmId);

        ArrayList<Film> allFilms = new ArrayList<>();
        allFilms.add(film);

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
