package webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.Output;

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

        FilmDAO filmDAO = FilmDAO.getInstance();
        PrintWriter printWriter = response.getWriter();
        ArrayList<Film> filmList = new ArrayList<>();

        String action = "";
        String dataFormat = "";
        int filmId = -1;
        String filmName = "";

        //String action = request.getParameter("action");
        //String dataFormat = request.getParameter("format");
        //int filmId = Integer.parseInt(request.getParameter("id"));
        //String filmName = request.getParameter("filmName");

        // todo do i need any of these ifs? can i just assign request.GetParameter to each var?
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
            String jsonResult = gson.toJson(filmList);

            jsonResult = "{\"films\":" + jsonResult + "}"; // required to properly form the json. not using jsp
            printWriter.write(jsonResult);
        }

        else if (dataFormat.equals("xml")) {
            try {
                response.setContentType("text/xml");
                printWriter.write(output.xmlToStringGenerator(filmList));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text")) {
            response.setContentType("text/plain");
            printWriter.write(output.stringGenerator(filmList));
        }

        else {
            printWriter.write("Data format unsupported");
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

        FilmDAO filmDAO = FilmDAO.getInstance();

        PrintWriter printWriter = response.getWriter();
        String dataFormat = request.getParameter("format");

        // pseudocode for data parsing patterns. three patterns

        // a. each value sent as a parameter, e.g. title, year etc
        // b. xml / json object sent as a parameter (where param key 'film' is present)
        // c. xml / json object sent as a body (raw)

        // wrapper if, i.e if invalid data format is passed, return error message
        // todo client code errors here
        if (dataFormat.equals("json") || dataFormat.equals("xml")
            || dataFormat.equals("text")) {

            // pattern b parse code: checks if param key 'film' is present
            if (request.getParameter("film") != null
                    && !request.getParameter("film").isEmpty()) {

                // if json, read in parameter, parse to Film object and pass to addFilm
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
                }
            }

            // pattern c parse code: checks if param key 'film' and any standard film param are empty
            // todo check in with jc. added 'title' to validation
            //  without it, i assume if film was empty but title wasn't, this would still fire...which i don't want?
            // think i need an extra check here for a standard param, e.g. title
            else if ((request.getParameter("film") == null ||
                    request.getParameter("film").isEmpty())
                    && (request.getParameter("title") == null ||
                    request.getParameter("title").isEmpty())) {

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
                }

            }

            // pattern a parse code: everything passed as parameter
            // data format not required
            else {

                String title = request.getParameter("title");
                int year = Integer.parseInt(request.getParameter("year"));
                String director = request.getParameter("director");
                String stars = request.getParameter("stars");
                String review = request.getParameter("review");

                Film film = new Film(title, year, director, stars, review);
                filmDAO.addFilm(film);
            }
            // print success response
            printWriter.write("Film added. Check the database to confirm, if required");

        } else printWriter.write("Data format unsupported. Please provide xml or json");
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

        FilmDAO filmDAO = FilmDAO.getInstance();

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

        FilmDAO filmDAO = FilmDAO.getInstance();

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

// legacy code

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
