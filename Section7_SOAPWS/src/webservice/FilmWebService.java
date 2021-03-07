package webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;

// todo need to think about what other web methods to write
// assume they would be based on the methods in filmDAO

/**
 * web service controller<br>
 * contains several web methods for fetching film information<br>
 * interacts with FilmDAO model class<br>
 * abstracts away from FilmDAO, which contains implementation detail
 */
@WebService()
public class FilmWebService {

    Gson gson = new Gson();

    /**
     * web method - gets all films, via FilmDAO<br>
     * @param format data format required to be sent back in web service
     * @return either formatted data string, or error message
     */
    @WebMethod
    public String getAllFilms(String format) {

        // Add film dao
        FilmDAO filmDAO = new FilmDAO();

        // Add array list, call get all films
        ArrayList<Film> films = filmDAO.getAllFilms();

        // todo need to extend this for xml and text
        // if format equals json, xml or text, return body
        if (format.equals("json")) {
            return gson.toJson(films);
        }

        return "Format unsupported";
    }
}
