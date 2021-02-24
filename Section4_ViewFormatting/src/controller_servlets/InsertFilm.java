package controller_servlets;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Web servlet controller<br>
 *     interacts with FilmDAO model bean<br>
 *     inserts film record into connected MySQL db<br>
 *     prints validation response to browser and console
 */

// todo not sure what the ask is here
    // how can i test output when nothing has been passed in?
    // wouldn't i have to pass a film object in through index.jsp?
    // no clear requirement in assignment spec to pass input
    // would also only return a response code, not film info
    // check back with Nick / Kaleem

@WebServlet(name = "InsertFilm", value = "/InsertFilm")
public class InsertFilm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        String jspDisplayString = "";

        // String searchFilmName = request.getParameter("filmname");

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

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
        int insertFilmReturnValue = filmDAO.insertFilm(film);

        // pass films array into request object
        request.setAttribute("films", film);

        // string object to pass into jsp
        String viewJspFilePath;

        /*/ test json generator method call
        if ("json".equals(dataFormat)) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";
            jspDisplayString = jsonGenerator(filmList);

        } else if ("xml".equals(dataFormat)) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            // calls jaxb xml generator method
            try {
                jspDisplayString = xmlGenerator(filmList);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        } else {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";

            // calls string generator method
            jspDisplayString = stringGenerator(filmList);
        }

        // add dispatcher, to forward content to view jsp
        RequestDispatcher dispatcher =
                request.getRequestDispatcher(viewJspFilePath);
        dispatcher.include(request, response);*/

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // method used for generating json, using gson library
    private String jsonGenerator(ArrayList<Film> filmList) {

        Gson gson = new Gson();
        String jsonResult = gson.toJson(filmList);
        return jsonResult;
    }
}
