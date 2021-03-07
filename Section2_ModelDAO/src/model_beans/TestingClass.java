package model_beans;

import java.util.ArrayList;

/**
 * Test class, to test methods on FilmDAO<br>
 * call test methods as required, from main method
 */
public class TestingClass {

    public static void main(String[] args) {

        // testInsertFilm();
        // testGetAllFilms();
        testGetFilm();

    }

    /**
     * test method for insertFilm method on FilmDAO class<br>
     * creates a Film object and passes into insertFilm method<br>
     * prints SQL insert executeUpdate response code to console<br>
     * 1 = successful insert, 0 = unsuccessful insert
     */
    public static void testInsertFilm(){

        Film film = new Film(
                101,"My Film",2021,"Me","Me Again",
                "Load of old rubbish");

        FilmDAO filmDAO = new FilmDAO();

        int returnValue = filmDAO.insertFilm(film);

        if (returnValue == 0) {
            System.out.println("SQL insert failed: " + returnValue + " film records added");

        } else if (returnValue == 1) {
            System.out.println("SQL insert complete: " + returnValue + " film record added");

        } else if (returnValue > 1) {
            System.out.println("SQL insert complete: " + returnValue + " film records added");
        }
    }


    /**
     * test method for getAllFilms method on FilmDAO class<br>
     * fetches all films and prints to console
     */
    public static void testGetAllFilms() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getAllFilms();

        for (Film film : films) {
            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                    "Title: " + film.getTitle() + "\n" +
                    "Year: " + film.getYear() + "\n" +
                    "Director: " + film.getDirector() + "\n" +
                    "Main Cast: " + film.getStars() + "\n" +
                    "Synopsis: " + film.getReview());
            System.out.println();
        }
    }

    /**
     * test method for getFilm method on FilmDAO class<br>
     * sends a search string, fetches all matching films and prints to console
     */
    public static void testGetFilm() {

        FilmDAO filmDAO = new FilmDAO();
        ArrayList<Film> films = filmDAO.getFilm("wars");

        for (Film film : films) {
            System.out.println(
                    "Film ID: " + film.getId() + "\n" +
                    "Title: " + film.getTitle() + "\n" +
                    "Year: " + film.getYear() + "\n" +
                    "Director: " + film.getDirector() + "\n" +
                    "Main Cast: " + film.getStars() + "\n" +
                    "Synopsis: " + film.getReview());
            System.out.println();
        }
    }
}
