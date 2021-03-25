package model_beans;

import com.google.api.client.http.HttpMediaType;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Includes set of utility methods<br>
 * Called by FilmRESTWebService<br>
 * Handles variety of data parsing patterns, based on request type
 */
public class ParserUtils {

    // pseudocode for data parsing patterns. three patterns

    // a. each value sent as a parameter, e.g. title, year etc
    // b. xml / json object sent as a parameter (where param key 'film' is present)
    // c. xml / json object sent as a body (raw) // todo check with kaleem why urlencoded doesn't work

    // top level pseudocode: three code blocks
    // check which pattern is being used (use 'not null' logic)
    // if pattern b (param 'film' not null), run xml/json as parameter code
    // if pattern c (no params), run xml/json as body code
    // else run all values as parameters code

    // todo question to kaleem. this has got quite big and difficult to read
    // do i now need to ship some of this logic into methods, e.g. for each 'parse pattern'
    // maybe even a new class, a la output?

    FilmDAO filmDAO;
    Gson gson;
    Output output;

    public ParserUtils(HttpServletRequest request, HttpServletResponse response,
                       String dataFormat, PrintWriter printWriter) {
    }

    HttpServletRequest request = this.request;
    String dataFormat = this.dataFormat;
    PrintWriter printWriter = this.printWriter;

    public void patternA() {

        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String stars = request.getParameter("stars");
        String review = request.getParameter("review");

        Film film = new Film(title, year, director, stars, review);
        filmDAO.addFilm(film);

    }

    public void patternB() {

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

    public void patternC() {

        // todo worth checking with kaleem. is this what he's expecting to see
        // read in the request body and compile as string
        String line = null;
        StringBuilder payloadString = null;

        try {
            BufferedReader bufferedReader = request.getReader();
            payloadString = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                payloadString.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if json, parse to Film object and pass to addFilm
        if (dataFormat.equals("json")) {

            Film film = gson.fromJson(payloadString.toString(), Film.class);
            filmDAO.addFilm(film);

            // if xml, parse to Film object and pass to addFilm
        } else if (dataFormat.equals("xml")) {

            Film film = output.stringToXmlGenerator(String.valueOf(payloadString));
            filmDAO.addFilm(film);
        }

        else printWriter.write("Data format unsupported. Application only supports xml or json");

    }

}
