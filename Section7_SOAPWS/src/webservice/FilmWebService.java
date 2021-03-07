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

@WebService()
public class FilmWebService {

    Gson gson = new Gson();

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
