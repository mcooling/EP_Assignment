package model_beans;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmDAOTest {

    /**
     * fetches all films from FilmDAO and prints to console
     */

    @Test
    void getAllFilms() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getAllFilms();

        System.out.println("****** TEST BEGINS ******\n");

        for (Film film : films) {
            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                            "Title: " + film.getTitle() + "\n" +
                            "Year: " + film.getYear() + "\n" +
                            "Director: " + film.getDirector() + "\n" +
                            "Main Cast: " + film.getStars() + "\n" +
                            "Synopsis: " + film.getReview() + "\n");
        }

        System.out.println("****** TEST ENDS ******");

    }

    /**
     * sends a search string into getFilm method<br>
     * fetches all matching films and prints to console
     */

    @Test
    void getFilm() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getFilm("wars");

        System.out.println("****** TEST BEGINS ******\n");

        for (Film film : films) {
            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                            "Title: " + film.getTitle() + "\n" +
                            "Year: " + film.getYear() + "\n" +
                            "Director: " + film.getDirector() + "\n" +
                            "Main Cast: " + film.getStars() + "\n" +
                            "Synopsis: " + film.getReview() + "\n");
        }

        System.out.println("****** TEST ENDS ******");

    }

    /**
     * creates a Film object and passes into insertFilm method<br>
     * prints SQL insert executeUpdate response code to console<br>
     * 1+ = completed insert, 0 = unsuccessful insert
     */

    @Test
    void insertFilm() {

        Film film = new Film(
                101,"My Film",2021,"Me","Me Again",
                "Load of old rubbish");

        FilmDAO filmDAO = new FilmDAO();

        int returnValue = filmDAO.insertFilm(film);

        System.out.println("****** TEST BEGINS ******\n");

        if (returnValue == 0) {
            System.out.println("SQL insert failed: " + returnValue + " film records added \n");

        } else if (returnValue == 1) {
            System.out.println("SQL insert complete: " + returnValue + " film record added \n");

            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                            "Title: " + film.getTitle() + "\n" +
                            "Year: " + film.getYear() + "\n" +
                            "Director: " + film.getDirector() + "\n" +
                            "Main Cast: " + film.getStars() + "\n" +
                            "Synopsis: " + film.getReview() + "\n");

        } else if (returnValue > 1) {
            System.out.println("SQL insert complete: " + returnValue + " film records added \n");
        }

        System.out.println("****** TEST ENDS ******");

    }
}
