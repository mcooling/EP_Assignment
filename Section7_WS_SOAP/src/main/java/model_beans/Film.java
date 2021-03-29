package model_beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// jaxb annotation, sets root element for XML DOM
@XmlRootElement(name = "film")
@XmlAccessorType(XmlAccessType.FIELD)

// set order of xml tags
@XmlType(propOrder = { "id", "title", "year", "director", "stars", "review" })

public class Film {

    // added no args constructor, used by jaxb
    public Film() {

    }

    /**
     * Base Model Java bean class, for creating a Film object<br>
     * @param id
     * @param title
     * @param year
     * @param director
     * @param stars
     * @param review
     */

    public Film(int id, String title, int year, String director, String stars,
                String review) {
        super();
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.stars = stars;
        this.review = review;
    }

    public Film(String title, int year, String director, String stars,
                String review) {
        super();
        this.id = id; // default id. logic added in addFilm method, to auto-create unique id
        this.title = title;
        this.year = year;
        this.director = director;
        this.stars = stars;
        this.review = review;
    }

    private int id;
    private String title;
    private int year;
    private String director;
    private String stars;
    private String review;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "Film [id=" + id + ", title=" + title + ", year=" + year
                + ", director=" + director + ", stars=" + stars + ", review="
                + review + "]";
    }
}
