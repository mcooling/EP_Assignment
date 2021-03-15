package model_beans;

import com.google.gson.Gson;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Output {

    public Output() {
    }

    // todo - make sure this gets added into 'refactored code' section
    // previously rewritten in each servlet. shipped out to a dedicated class

    /**
     * converts array list of films into json string representation<br>
     * uses Gson library
     * @param filmList ArrayList of films
     * @return String json representation of films
     */
    public String jsonGenerator(ArrayList<Film> filmList) {

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
    public String xmlGenerator(ArrayList<Film> allFilms)
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
    public String stringGenerator(ArrayList<Film> allFilms) {

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
