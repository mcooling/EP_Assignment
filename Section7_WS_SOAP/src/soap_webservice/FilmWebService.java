package soap_webservice;

import com.google.gson.Gson;
import model_beans.Film;
import model_beans.FilmDAO;
import model_beans.FilmList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * web service controller<br>
 * contains several web methods for fetching film information<br>
 * interacts with FilmDAO model class<br>
 * abstracts away from FilmDAO, which contains implementation detail
 */
@WebService()
public class FilmWebService {

    Gson gson = new Gson();

    // todo need to review all these with jc
    // getting a bit confused with I/O

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
        // todo rest uses printwriter, does it matter that this doesn't?
        if ("json".equals(dataFormat)) {
            return gson.toJson(films);
        } else if ("xml".equals(dataFormat)) {
            return xmlGenerator(films);
        } else if ("text".equals(dataFormat) || "string".equals(dataFormat)) {
            return stringGenerator(films);
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
        // todo rest uses printwriter, does it matter that this doesn't?
        if (dataFormat.equals("json")) {
            return gson.toJson(films);
        }

        else if (dataFormat.equals("xml")) {
            return xmlGenerator(films);
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            return stringGenerator(films);
        }

        return "Data format unsupported";
    }

    // todo is this void? should we expect a return?
    @WebMethod
    public void insertFilm(Film film) {
        FilmDAO filmDAO = new FilmDAO();
        filmDAO.insertFilm(film);

    }

    // todo is this void? should we expect a return?
    // think the web method is written the same as insert
    // but underneath it calls different overloaded constructor (no id)
    @WebMethod
    public void updateFilm(Film film) {
        FilmDAO filmDAO = new FilmDAO();
        filmDAO.insertFilm(film);
    }

    // todo is this void? should we expect a return?
    @WebMethod
    public void deleteFilm(int filmId) throws SQLException {
        FilmDAO filmDAO = new FilmDAO();
        filmDAO.deleteFilm(filmId);
    }

    @WebMethod
    public String getFilmById(String dataFormat, int filmId)
            throws JAXBException {

        // Add film dao
        FilmDAO filmDAO = new FilmDAO();
        Film film = filmDAO.getFilmById(filmId);

        // todo doesn't feel right...check with jc
        // adding single film object to a list, so that generator objects work
        ArrayList<Film> films = new ArrayList<>();
        films.add(film);

        // add conditions for data formats
        // todo rest uses printwriter, does it matter that this doesn't?
        if (dataFormat.equals("json")) {
            return gson.toJson(film);
        }

        else if (dataFormat.equals("xml")) {
            return xmlGenerator(films);
        }

        else if (dataFormat.equals("text") || dataFormat.equals("string")) {
            return stringGenerator(films);
        }

        return "Data format unsupported";
    }

    private String xmlGenerator(ArrayList<Film> allFilms)
            throws JAXBException {

        // adds new FilmList, then populate with arraylist passed in
        FilmList filmList = new FilmList();
        filmList.setFilmList(allFilms);

        // create marshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(FilmList.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        // set marshaller properties
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // marshall xml content
        StringWriter stringWriter = new StringWriter();
        // marshaller.marshal(allFilms, stringWriter);
        marshaller.marshal(filmList, stringWriter);     // note this now resolves the JAXBError

        return stringWriter.toString();
    }

    private String stringGenerator(ArrayList<Film> allFilms) {

        // add string buffer
        StringBuffer stringBuffer = new StringBuffer();

        // add each film from array list, to buffer
        for (Film f : allFilms) {
            stringBuffer.append(f);
        }

        // convert string buffer object to string
        return stringBuffer.toString();
    }

    // todo rebuild wsdl

    // todo re-test in postman

}
