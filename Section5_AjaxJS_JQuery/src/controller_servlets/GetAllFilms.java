package controller_servlets;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.FilmList;
import model_beans.Output;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Web servlet controller<br>
 *     interacts with FilmDAO model bean<br>
 *     fetches all films from connected MySQL db<br>
 *     sends content to viewer jsp, as either xml or json<br>
 *     uses JAXB / GSON libraries, to generate xml / json data<br>
 */

@WebServlet(name = "GetAllFilms", value = "/GetAllFilms")
public class GetAllFilms extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String jspDisplayString = "";

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "json";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getAllFilms();

        // pass films array into request object
        request.setAttribute("films", allFilms);

        String viewJspFilePath = "";

        Output output = new Output();

        // set content type in response object, depending on format sent
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";

            // calls gson json generator method
            jspDisplayString = output.jsonGenerator(allFilms);

        } else if (dataFormat.equals("xml")) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            // calls jaxb xml generator method
            try {
                jspDisplayString = output.xmlGenerator(allFilms);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        } else {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";

            // calls string generator method
            jspDisplayString = output.stringGenerator(allFilms);
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
