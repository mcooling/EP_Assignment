package model_beans;

/**
 * Test class, to test methods on FilmDAO<br>
 * call test methods as required, from main method
 */
public class Main {

    public static void main(String[] args) {

        testInsertFilm();

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
            System.out.println("SQL insert failed: return value " + returnValue);
        } else {
            System.out.println("SQL insert successful: return value " + returnValue);
        }
    }
}
