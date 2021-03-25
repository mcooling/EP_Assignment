package webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.Output;
import model_beans.ParserUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "FilmRESTWebservice", value = "/FilmRESTWebservice")
public class FilmRESTWebservice extends HttpServlet {

    Output output = new Output();
    Gson gson = new Gson();
    FilmDAO filmDAO = FilmDAO.getInstance();

    /**
     * handles getAllFilms, getFilmsByName, getFilmById<br>
     * @param request http request
     * @param response http response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter printWriter = response.getWriter();
        String action = "";
        String dataFormat = "";
        int filmId = -1;
        String filmName = "";
        ArrayList<Film> filmList = new ArrayList<>();

        // sets 'get' method to call, i.e. getall, getbyid or getbyname
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }

        // add logic for handling request parameters
        if (request.getParameter("format") != null) {                   // data format, e.g. xml/json/text
            dataFormat = request.getParameter("format");
        }

        if (request.getParameter("id") != null) {                       // required for getFilmById
            filmId = Integer.parseInt(request.getParameter("id"));
        }

        if (request.getParameter("filmName") != null) {                 // required for getFilmByName
            filmName = request.getParameter("filmName");
        }

        // add logic for handling GET request type
        if (action.equals("getallfilms")) {
            filmList = filmDAO.getAllFilms();

        } else if (action.equals("getfilmbyid")) {
            filmList.add(filmDAO.getFilmById(filmId));

        } else if (action.equals("getfilmsbyname")) {
            filmList = filmDAO.getFilmByName(filmName);
        }

        // add logic for handling data format
        if (dataFormat.equals("json")) {
            response.setContentType("application/json");
            printWriter.write(gson.toJson(filmList));
        }

        else if (dataFormat.equals("xml")) {
            try {
                response.setContentType("text/xml");
                printWriter.write(output.xmlToStringGenerator(filmList));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            response.setContentType("text/plain");
            printWriter.write(output.stringGenerator(filmList));
        }
    }


    /**
     * handles addFilm<br>
     * @param request http request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter printWriter = response.getWriter();
        String dataFormat = request.getParameter("format");
        ParserUtils parser = new ParserUtils(request, response, dataFormat, printWriter);

        // todo check in with kaleeem / jc. have created a new class for the parse patterns

        // todo need a wrapper if for data format
        // i.e if no valid data format parameter is present, return error message

        // pattern b parse code: checks if param key 'film' is present
        if (request.getParameter("film") != null
                && !request.getParameter("film").isEmpty()) {

            parser.patternB();

            /* / if json, read in parameter, parse to Film object and pass to addFilm
             if (dataFormat.equals("json")) {

                String jsonInput = request.getParameter("film");
                Film jsonFilm = gson.fromJson(jsonInput, Film.class);
                filmDAO.addFilm(jsonFilm);
            }

            // if xml, read in parameter, parse to Film object and pass to addFilm
            if (dataFormat.equals("xml")) {

                String xmlInput = request.getParameter("film");
                Film xmlFilm = output.stringToXmlGenerator(xmlInput);
                filmDAO.addFilm(xmlFilm);
            } */
        }

        // pattern c parse code: checks if param key 'film' is empty
        // todo check in with jc. added 'title' to validation
        //  without it, if film was empty but title wasn't, this would still fire
        // think i need an extra check here for a standard param, e.g. title
        else if (request.getParameter("film") == null ||
                request.getParameter("film").isEmpty()
                        && (request.getParameter("title") == null ||
                        request.getParameter("title").isEmpty())) {

            parser.patternC();

        }
            /*/ todo worth checking with kaleem. is this what he's expecting to see
            // read in the request body and compile as string
            String line = null;
            BufferedReader bufferedReader = request.getReader();
            StringBuilder payloadString = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                payloadString.append(line);
            }

            bufferedReader.close();

            // if json, parse to Film object and pass to addFilm
            if (dataFormat.equals("json")) {

                Film film = gson.fromJson(payloadString.toString(), Film.class);
                filmDAO.addFilm(film);

            // if xml, parse to Film object and pass to addFilm
            } else if (dataFormat.equals("xml")) {

                Film film = output.stringToXmlGenerator(String.valueOf(payloadString));
                filmDAO.addFilm(film);
            }*/

            // todo come back to this. needs putting in right place
            // else printWriter.write("Data format unsupported. Application only supports xml or json");
        //}

        // pattern a parse code: everything passed as parameter
        else {
            parser.patternA(); // todo do i need curly braces on this?
        }

            /*String title = request.getParameter("title");
            int year = Integer.parseInt(request.getParameter("year"));
            String director = request.getParameter("director");
            String stars = request.getParameter("stars");
            String review = request.getParameter("review");

            Film film = new Film(title, year, director, stars, review);
            filmDAO.addFilm(film);*/

        // print success response
        printWriter.write("Film added. Check the database to confirm, if required");
    }

    /**
     * handles updateFilm<br>
     * @param request http request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter printWriter = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");

        Film film = new Film(id, title, year, director, stars, review);
        filmDAO.updateFilm(film);

        printWriter.write("Film updated. Check the database to confirm, if required");
    }

    /**
     * handles deleteFilm<br>
     * @param request http request
     * @param response http response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter printWriter = response.getWriter();
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            filmDAO.deleteFilm(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        printWriter.write("Film deleted. Check the database to confirm, if required");

    }
}



/* while (bufferedReader.readLine() != null) {
                payloadString.append(line);
            } */
/* while (line != null) {
                payloadString.append(line);
                line = bufferedReader.readLine();
            }*/
/* char[] charBuffer = new char[1024];
            int bytesRead;

            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                payloadString.append(charBuffer, 0, bytesRead);
            } */
// todo will this also include the 'format:json' kvp?
// if it does, i assume we want to extract that in some way?
/*while (bufferedReader.readLine() != null) {
                payloadString.append(body);
            }*/
/*/ map option...which at least is reading content
            Map<String, String[]> map = request.getParameterMap();

            // reading the Map
            // works for GET && POST Method

            for (String paramName : map.keySet()) {
                String[] paramValues = map.get(paramName);

                //Get Values of Param Name
                for (String valueOfParam : paramValues) {
                    //Output the Values
                    System.out.println("Value of Param with Name " + paramName + ": " + valueOfParam);
                }
            } */
