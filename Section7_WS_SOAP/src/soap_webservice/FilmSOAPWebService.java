package soap_webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.Output;
import org.jetbrains.annotations.NotNull;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import javax.xml.ws.Endpoint;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebService()
public class FilmSOAPWebService {

    Gson gson = new Gson();
    Output output = new Output();

    /**
     * webmethod: gets all films held in db<br>
     * @param dataFormat required to be sent back in web service
     * @return film objects in formatted data string, or error message
     */
    @WebMethod
    public String getAllFilms(String dataFormat)
            throws JAXBException {

        // Add film dao
        FilmDAO filmDAO = FilmDAO.getInstance();

        // Add array list, call get all films
        ArrayList<Film> films = filmDAO.getAllFilms();

        // add conditions for data formats
        if ("json".equals(dataFormat)) {
            return gson.toJson(films);

        } else if ("xml".equals(dataFormat)) {
            try {
                return output.xmlGenerator(films);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if ("text".equals(dataFormat) || "string".equals(dataFormat)) {
            return output.stringGenerator(films);
        }
        return "Format unsupported";
    }

    /**
     * webmethod: gets all films from search name<br>
     * @param dataFormat required to be sent back in web service
     * @param searchString film name to search
     * @return film objects in formatted data string, or error message
     * @throws JAXBException
     */
    @WebMethod
    public String getFilmByName(String dataFormat, String searchString)
            throws JAXBException {

        // Add film dao
        FilmDAO filmDAO = FilmDAO.getInstance();

        // Add array list, call get all films
        ArrayList<Film> films = filmDAO.getFilmByName(searchString);

        // add conditions for data formats
        if (dataFormat.equals("json")) {
            return gson.toJson(films);
        }

        else if (dataFormat.equals("xml")) {
            try {
                return output.xmlGenerator(films);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            return output.stringGenerator(films);
        }

        return "Data format unsupported";
    }

    /**
     * webmethod: gets film from ID<br>
     * @param dataFormat required to be sent back in web service
     * @param filmId to search
     * @return film object in formatted data string, or error message
     * @throws JAXBException
     */
    @WebMethod
    public String getFilmById(String dataFormat, int filmId)
            throws JAXBException {

        // Add film dao & call FilmDAO method
        FilmDAO filmDAO = FilmDAO.getInstance();
        Film film = filmDAO.getFilmById(filmId);

        // add returned film object to array list
        ArrayList<Film> films = new ArrayList<>();
        films.add(film);

        // add conditions for data formats
        if (dataFormat.equals("json")) {
            return gson.toJson(film);
        }

        else if (dataFormat.equals("xml")) {
            try {
                return output.xmlGenerator(films);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            return output.stringGenerator(films);
        }

        return "Data format unsupported";
    }

    /**
     * webmethod: adds film object to db<br>
     * @param dataFormat required to be sent back in web service
     * @param name
     * @param year
     * @param director
     * @param stars
     * @param review
     * @return film object in formatted data string, or error message
     */
    @WebMethod
    public String addFilm(@NotNull String dataFormat, String name, int year,
                          String director, String stars, String review) {

        // add film dao & call FilmDAO method
        FilmDAO filmDAO = FilmDAO.getInstance();
        Film filmToAdd = new Film(name, year, director, stars, review);
        Film newFilm = filmDAO.addFilm(filmToAdd);

        // add returned film object to array list
        ArrayList<Film> films = new ArrayList<>();
        films.add(newFilm);

        // add conditions for data formats
        if (dataFormat.equals("json")) {
            return gson.toJson(films);
        }

        else if (dataFormat.equals("xml")) {
            try {
                return output.xmlGenerator(films);
            } catch (FileNotFoundException | JAXBException e) {
                e.printStackTrace();
            }
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            return output.stringGenerator(films);
        }

        return "Data format unsupported";
    }

    /**
     * webmethod: update film in db<br>
     * @param name
     * @param year
     * @param director
     * @param stars
     * @param review
     * @return success or fail message, based on sql response code
     */
    @WebMethod
    public String updateFilm(String name, int year, String director,
                           String stars, String review) {

        // name, year(int), director, stars, review
        // add film dao & call FilmDAO method
        FilmDAO filmDAO = FilmDAO.getInstance();
        Film filmToAdd = new Film(name, year, director, stars, review);

        int responseCode = filmDAO.updateFilm(filmToAdd);

        if (responseCode == 0) {
            return "Film update failed";
        }

        return "Film update complete";
    }

    /**
     * webmethod: delete film record from db<br>
     * @param filmId
     * @return success or fail message, based on sql response code
     */
    @WebMethod
    public String deleteFilm(int filmId) {

        // name, year(int), director, stars, review
        // add film dao & call FilmDAO method
        FilmDAO filmDAO = FilmDAO.getInstance();

        int responseCode = 0;
        try {
            responseCode = filmDAO.deleteFilm(filmId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (responseCode == 0) {
            return "Film delete request failed";
        } else
            return "Film deleted";
    }
}
