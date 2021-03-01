package model_beans;

// todo getting jaxb exception error in console
// raised post in stack, see what happens

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * holds an array list of films
 * used by jaxb xml generator
 */
// jaxb annotation
// @XmlRootElement(namespace = "model_beans")
@XmlRootElement(name="filmList")

public class FilmList {

    private ArrayList<Film> filmArrayList;

    // add wrapper element around XML
    @XmlElementWrapper(name = "filmList")

    // set the name of the xml entities
    @XmlElement(name = "film")

    public ArrayList<Film> getFilmArrayList() {
        return filmArrayList;
    }

    public void setFilmArrayList(ArrayList<Film> filmArrayList) {
        this.filmArrayList = filmArrayList;
    }
}
