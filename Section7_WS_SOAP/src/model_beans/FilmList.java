package model_beans;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * used by jaxb xml generator<br>
 * holds an array list of films
 */

// jaxb annotation
@XmlRootElement(namespace = "model_beans")
@XmlAccessorType(XmlAccessType.FIELD)

public class FilmList {
    @XmlElementWrapper(name = "filmList")
    @XmlElement(name = "film")
    private ArrayList<Film> filmList;

    public ArrayList<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(ArrayList<Film> filmList) {
        this.filmList = filmList;
    }
}
