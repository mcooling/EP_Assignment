package model_beans;

// holds an array list of films
// used by jaxb xml generator

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

// jaxb annotation
@XmlRootElement(namespace = "model_beans")

public class FilmList {

    private ArrayList<Film> filmList;

    // add wrapper element around XML
    @XmlElementWrapper(name = "filmList")

    // set the name of the xml entities
    @XmlElement(name = "film")

    public ArrayList<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(ArrayList<Film> filmList) {
        this.filmList = filmList;
    }
}
