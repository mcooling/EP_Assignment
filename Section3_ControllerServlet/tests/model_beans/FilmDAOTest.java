package model_beans;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

class FilmDAOTest {

    // todo do i need to add servlet classes in here as well
    // or are the servlets themselves basically doing the test, by displaying output?

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
    void getFilmByName() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getFilmByName("wars");

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

    /**
     * fetches film object from db, using film id code<br>
     * print film details to console
     */
    @Test
    void getFilmById() {

        FilmDAO filmDAO = new FilmDAO();
        Film film = filmDAO.getFilmById(10008);

        System.out.println("****** TEST BEGINS ******\n");

        System.out.println(
                "Film ID: " + film.getId() + "\n" +
                        "Title: " + film.getTitle() + "\n" +
                        "Year: " + film.getYear() + "\n" +
                        "Director: " + film.getDirector() + "\n" +
                        "Main Cast: " + film.getStars() + "\n" +
                        "Synopsis: " + film.getReview() + "\n"
        );

        System.out.println("****** TEST ENDS ******");

    }

    /**
     * deletes film entry from db, using film id code<br>
     * prints message to console, include name of film deleted
     * @throws SQLException
     */
    @Test
    void deleteFilm() throws SQLException {

        FilmDAO filmDAO = new FilmDAO();
        // ArrayList<Film> films = filmDAO.getFilmByName("wars");

        int filmId = 101;
        Film film = filmDAO.getFilmById(filmId);

        System.out.println("****** TEST BEGINS ******\n");

        filmDAO.deleteFilm(101);

        // filmDAO.deleteFilm(filmId);

        System.out.println(
                "The following film has been removed from the database: \n" +
                        "Film title: " + film.getTitle() + "\n");

        System.out.println("****** TEST ENDS ******");

    }

    /**
     * updates Film object and passes into updateFilm method<br>
     * prints SQL executeUpdate response code to console<br>
     * 1+ = completed update, 0 = unsuccessful update
     */
    @Test
    void updateFilm() {

        Film film = new Film(
                101,"The Adventures of Baxter",2021,"Daddy Cooling","Baxter Cooling",
                "Load of old rubbish");

        FilmDAO filmDAO = new FilmDAO();

        int returnValue = filmDAO.updateFilm(film);

        System.out.println("****** TEST BEGINS ******\n");

        if (returnValue == 0) {
            System.out.println("SQL update failed: " + returnValue + " film records added \n");

        } else if (returnValue == 1) {
            System.out.println("SQL update complete: " + returnValue + " film record added \n");

            System.out.println(
                    "Title: " + film.getTitle() + "\n" +
                            "Year: " + film.getYear() + "\n" +
                            "Director: " + film.getDirector() + "\n" +
                            "Starring: " + film.getStars() + "\n" +
                            "Plot: " + film.getReview() + "\n");

        } else if (returnValue > 1) {
            System.out.println("SQL update complete: " + returnValue + " film records added \n");
        }

        System.out.println("****** TEST ENDS ******");

    }
}
