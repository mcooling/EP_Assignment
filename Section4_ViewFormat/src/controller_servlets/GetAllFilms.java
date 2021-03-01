package controller_servlets;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.FilmList;

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
        if (dataFormat == null) dataFormat = "xml";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getAllFilms();

        // pass films array into request object
        request.setAttribute("films", allFilms);

        String viewJspFilePath = "";

        // set content type in response object, depending on format sent
        if ("json".equals(dataFormat)) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";

            // calls gson json generator method
            jspDisplayString = jsonGenerator(allFilms);

        } else if ("xml".equals(dataFormat)) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            // calls jaxb xml generator method
            try {
                jspDisplayString = xmlGenerator(allFilms);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        } else {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";

            // calls string generator method
            jspDisplayString = stringGenerator(allFilms);
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

    /**
     * uses JAXB library, to generate xml for Film result set<br>
     * @param allFilms Arraylist of Film objects
     * @throws JAXBException
     */
    // todo xml generated in browser, but JAXBException in console
    private String xmlGenerator(ArrayList<Film> allFilms)
            throws JAXBException, FileNotFoundException {

        // adds new FilmList, then populate with arraylist passed in
        FilmList filmList = new FilmList();
        filmList.setFilmArrayList(allFilms);

        // create marshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        // set marshaller properties
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // marshall xml content
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(allFilms, stringWriter);

        return stringWriter.toString();
    }

    /**
     * converts array list of films into json string representation<br>
     * uses Gson library
     * @param allFilms ArrayList of films
     * @return String json representation of films
     */

    private String jsonGenerator(ArrayList<Film> allFilms) {

        Gson gson = new Gson();
        String jsonResult = gson.toJson(allFilms);
        return jsonResult;
    }

    /**
     * converts array list of films into string representation<br>
     * @param allFilms ArrayList of films
     * @return String representation of films
     */
    private String stringGenerator(ArrayList<Film> allFilms) {

        // add string buffer
        StringBuffer stringBuffer = new StringBuffer();

        // add each film from array list, to buffer
        for (Film f : allFilms) {
            stringBuffer.append(f);
        }

        // convert string buffer object to string
        String stringResult = stringBuffer.toString();
        return stringResult;
    }
}
