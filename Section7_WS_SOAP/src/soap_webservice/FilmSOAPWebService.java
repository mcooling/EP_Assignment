package soap_webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.Output;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import javax.xml.ws.Endpoint;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@WebService()
public class FilmSOAPWebService {

    Gson gson = new Gson();
    Output output = new Output();

    /**
     * web method - gets all films, via FilmDAO<br>
     * @param dataFormat data format required to be sent back in web service
     * @return either formatted data string, or error message
     */
    @WebMethod
    public String getAllFilms(String dataFormat)
            throws JAXBException {

        // Add film dao
        FilmDAO filmDAO = new FilmDAO();

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

    @WebMethod
    public String getFilmByName(String dataFormat, String searchString)
            throws JAXBException {

        // Add film dao
        FilmDAO filmDAO = new FilmDAO();

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

    @WebMethod
    public String getFilmById(String dataFormat, int filmId)
            throws JAXBException {

        // Add film dao & call FilmDAO method
        FilmDAO filmDAO = new FilmDAO();
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

    @WebMethod
    public String addFilm(String dataFormat, String name, int year,
                          String director, String stars, String review) {

        // add film dao & call FilmDAO method
        FilmDAO filmDAO = new FilmDAO();
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


    /*/ todo doesn't like this - deployment error
    // takes in Film object attributes as params
    // requests response code back from updateFilm method call
    @WebMethod
    public String updateFilm(String name, int year, String director,
                           String stars, String review) {

        // name, year(int), director, stars, review
        // add film dao & call FilmDAO method
        FilmDAO filmDAO = new FilmDAO();
        Film filmToAdd = new Film(name, year, director, stars, review);

        int responseCode = filmDAO.updateFilm(filmToAdd);

        if (responseCode == 0) {
            return "Film update failed";
        } else
            return "Film update complete";

    }

    // todo doesn't like this - deployment error
    // takes in Film object attributes as params
    // requests response code back from deleteFilm method call
    @WebMethod
    public String deleteFilm(String name, int year, String director,
                             String stars, String review) {

        // name, year(int), director, stars, review
        // add film dao & call FilmDAO method
        FilmDAO filmDAO = new FilmDAO();
        Film filmToAdd = new Film(name, year, director, stars, review);

        int responseCode = filmDAO.updateFilm(filmToAdd);

        if (responseCode == 0) {
            return "Film delete request failed";
        } else
            return "Film deleted";
    }*/
}
