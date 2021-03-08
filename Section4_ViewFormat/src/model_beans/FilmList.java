package model_beans;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * holds an array list of films
 * used by jaxb xml generator
 */
// jaxb annotation
// @XmlRootElement(namespace = "model_beans")
@XmlRootElement(name="filmList")
@XmlAccessorType(XmlAccessType.FIELD)

public class FilmList {

    // add wrapper element around XML
    @XmlElementWrapper(name = "filmList")
    // set the name of the xml entities
    @XmlElement(name = "film")
    private ArrayList<Film> filmArrayList;

    public ArrayList<Film> getFilmArrayList() {
        return filmArrayList;
    }

    public void setFilmArrayList(ArrayList<Film> filmArrayList) {
        this.filmArrayList = filmArrayList;
    }
}
