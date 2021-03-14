package model_beans;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmDAOTest {

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

    @Test
    void getFilmByName() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getFilmByName("bronx");

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

    @Test
    void insertFilm() {

        Film film = new Film(
                101,"The Adventures of Baxter",2021,"Me","Me Again",
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
