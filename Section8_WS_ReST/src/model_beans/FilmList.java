package model_beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * holds an array list of films
 * used by jaxb xml generator
 */
// jaxb annotation
@XmlRootElement(namespace = "model_beans")
@XmlAccessorType(XmlAccessType.FIELD)

public class FilmList {
    @XmlElementWrapper(name = "filmList")   // add wrapper element around XML
    @XmlElement(name = "film")              // set the name of the xml entities
    private ArrayList<Film> filmList;

    public ArrayList<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(ArrayList<Film> filmList) {
        this.filmList = filmList;
    }
}
