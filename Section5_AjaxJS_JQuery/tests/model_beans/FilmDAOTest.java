package model_beans;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

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
    void addFilm() {

        Film film = new Film(
                "The NEW Adventures of Baxter",2021,"Me","Me Again",
                "Load of old rubbish");

        FilmDAO filmDAO = new FilmDAO();
        Film newFilm = filmDAO.addFilm(film);

        System.out.println("****** TEST BEGINS ******\n");

        System.out.println(
                "Film ID: " + newFilm.getId() + "\n" +
                        "Title: " + newFilm.getTitle() + "\n" +
                        "Year: " + newFilm.getYear() + "\n" +
                        "Director: " + newFilm.getDirector() + "\n" +
                        "Main Cast: " + newFilm.getStars() + "\n" +
                        "Synopsis: " + newFilm.getReview() + "\n");

        System.out.println("****** TEST ENDS ******");
    }

    @Test
    void deleteFilm() throws SQLException {

        FilmDAO filmDAO = new FilmDAO();
        // ArrayList<Film> films = filmDAO.getFilmByName("wars");

        int filmId = 11312;
        Film film = filmDAO.getFilmById(filmId);

        System.out.println("****** TEST BEGINS ******\n");

        filmDAO.deleteFilm(filmId);

        System.out.println(
                "The following film has been removed from the database: \n" +
                        "Film ID: " + filmId +
                        "Film title: " + film.getTitle() + "\n");

        System.out.println("****** TEST ENDS ******");

    }

    @Test
    void updateFilm() {

        Film film = new Film(
                11311,"The Adventures of Baxter",2021,"Daddy Cooling","Baxter Cooling",
                "Load of old rubbish");

        FilmDAO filmDAO = new FilmDAO();
        int returnValue = filmDAO.updateFilm(film);

        System.out.println("****** TEST BEGINS ******\n");

        if (returnValue == 0) {
            System.out.println("Database update failed: " + returnValue + " film records updated \n");

        } else {

            System.out.println("Database update complete \n");

            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                    "Title: " + film.getTitle() + "\n" +
                    "Year: " + film.getYear() + "\n" +
                    "Director: " + film.getDirector() + "\n" +
                    "Starring: " + film.getStars() + "\n" +
                    "Plot: " + film.getReview() + "\n");
        }
        System.out.println("****** TEST ENDS ******");
    }
}
