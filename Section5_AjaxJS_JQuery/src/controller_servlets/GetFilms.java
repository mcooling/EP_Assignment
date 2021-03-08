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
 *     fetches matching films from connected MySQL db<br>
 *     using a film name search string as input parameter<br>
 *     prints output to browser and console for debug
 */

@WebServlet(name = "GetFilms", value = "/GetFilms")
public class GetFilms extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String jspDisplayString = "";

        String searchFilmName = request.getParameter("filmname");

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "xml";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getFilm(searchFilmName);

        // pass films array into request object
        request.setAttribute("films", allFilms);

        // string object to pass into jsp
        String viewJspFilePath = "";

        // test json generator method call
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";
            jspDisplayString = jsonGenerator(allFilms);

        } else if (dataFormat.equals("xml")) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            // calls jaxb xml generator method
            try {
                jspDisplayString = xmlGenerator(allFilms);
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        } else if (dataFormat.equals("text")) {
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
     * converts array list of films into json string representation<br>
     * uses Gson library
     * @param filmList ArrayList of films
     * @return String json representation of films
     */
    private String jsonGenerator(ArrayList<Film> filmList) {

        Gson gson = new Gson();
        String jsonResult = gson.toJson(filmList);
        return jsonResult;
    }


    /**
     * uses JAXB library, to generate xml for Film result set<br>
     * @param allFilms Arraylist of Film objects
     * @return StringWriter string representation of xml
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    private String xmlGenerator(ArrayList<Film> allFilms)
            throws JAXBException, FileNotFoundException {

        // adds new FilmList, then populate with arraylist passed in
        FilmList filmList = new FilmList();
        filmList.setFilmList(allFilms);

        // create marshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        // set marshaller properties
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter stringWriter = new StringWriter();
        // marshaller.marshal(allFilms, stringWriter);
        marshaller.marshal(filmList, stringWriter);     // note this now resolves the JAXBError

        return stringWriter.toString();
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
            stringBuffer.append("");
        }

        // convert string buffer object to string
        // return result string to doGet
        String stringResult = stringBuffer.toString();
        return stringResult;
    }
}
