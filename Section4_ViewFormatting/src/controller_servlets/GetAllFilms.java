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
import java.io.IOException;
import java.io.PrintWriter;
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

        // PrintWriter printWriter = response.getWriter();
        String printWriter = "";

        // access parameter for format. Set default to json if none is sent
        String dataFormat = request.getParameter("format");
        if (dataFormat == null) dataFormat = "json";

        // create array list and populate with db films, using FilmDAO
        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> allFilms = filmDAO.getAllFilms();

        // pass films array into request object
        request.setAttribute("films", allFilms);

        String viewJspFilePath;

        // todo something not quite right. printing two sets of content
        // creates a browser error 'XML declaration allowed only at the start of the document'

        // set content type in response object, depending on format sent
        if ("json".equals(dataFormat)) {
            response.setContentType("application/json");
            viewJspFilePath = "/WEB-INF/results/films-json.jsp";

            // calls gson json generator method
            jsonGenerator(allFilms, printWriter);

        } else if ("xml".equals(dataFormat)) {
            response.setContentType("text/xml");
            viewJspFilePath = "/WEB-INF/results/films-xml.jsp";

            /*/ calls jaxb xml generator method
            try {
                xmlGenerator(allFilms, printWriter);
            } catch (JAXBException e) {
                e.printStackTrace();
            }*/

        } else {
            response.setContentType("text/plain");
            viewJspFilePath = "/WEB-INF/results/films-string.jsp";

            // todo add method call to string generator method
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
     * @param printWriter PrintWriter object
     * @throws JAXBException
     */
    // todo review format for doing this. Kaleem suggested alternative
    // method used for generating xml, using jaxb library
    // takes in arraylist and print writer object
    private void xmlGenerator(ArrayList<Film> allFilms,
                              PrintWriter printWriter)
        throws JAXBException {

        // adds new FilmList, then populate with arraylist passed in
        FilmList filmList = new FilmList();
        filmList.setFilmList(allFilms);

        // adds new JAXB object, to generate xml for FilmList object
        // passes generated xml to print writer, to display in browser
        JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(filmList, printWriter);

    }

    /**
     * uses GSON library, to generate json for Film result set<br>
     * @param allFilms Arraylist of Film objects
     * @param printWriter PrintWriter object
     */
    // method used for generating json, using gson library
    // takes in arraylist and print writer object
    private void jsonGenerator(ArrayList<Film> allFilms,
                               String printWriter) {

        Gson gson = new Gson();
        String jsonResult;
        jsonResult = gson.toJson(allFilms);

        // printWriter.println(jsonResult);
    }

    // todo method for generating string content
    private void stringGenerator() {

    }
}
