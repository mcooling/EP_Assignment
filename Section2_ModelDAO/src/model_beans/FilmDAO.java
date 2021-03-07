package model_beans;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Accessor Object class (DAO)<br>
 *     interacts with Film base class and remote MySQL db connection<br>
 *     includes several methods, to perform various db CRUD operations
 */
public class FilmDAO {

    // mudfoot database connection details
    Film film = null;
    Connection conn = null;
    Statement stmt = null;
    String user = "coolingm";
    String password = "Saftreal4";
    String jdbcUrl = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

    public FilmDAO() {
    }

    /**
     * opens MySQL db connection<br>
     * used in each of the CRUD operation methods
     */
    private void openConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, user, password);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * closes MySQL db connection<br>
     * used in each of the CRUD operation methods
     */
    private void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * fetches all films held in a connected MySQL db<br>
     *     populates films into a result set<br>
     *     for each result, creates a Film object and adds to an array list
     * @return ArrayList of Film objects
     */

    public ArrayList<Film> getAllFilms() {

        // create array list to hold each film
        ArrayList<Film> allFilms = new ArrayList<>();

        openConnection();

        try {
            // add db select statement string
            String selectSQL = "select * from films";

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

            // loop through result set
            // for each item in result set, create new Film object and add to array list
            while (resultSet.next()) {

                // todo FIX THIS. also gets picked up later in assignment
                // lab uses method call to 'getNextFilm'. cleaner way to do it

                int filmID = resultSet.getInt(1);
                String filmName = resultSet.getString(2);
                int filmYear = resultSet.getInt(3);
                String filmDirector = resultSet.getString(4);
                String filmStars = resultSet.getString(5);
                String filmReview = resultSet.getString(6);

                Film film = new Film(filmID, filmName, filmYear, filmDirector,
                        filmStars, filmReview);

                allFilms.add(film);

            }

            stmt.close();
            closeConnection();
        } catch (SQLException se) {
            System.out.println(se);
        }

        return allFilms;
    }

    /**
     * fetches films held in a connected MySQL db, with matching name<br>
     *     populates films into a result set<br>
     *     for each result, creates a Film object and adds to an array list
     * @param searchString name of film(s) to search for<br>
     * @return ArrayList of Film objects
     */

    public ArrayList<Film> getFilm(String searchString) {

        ArrayList<Film> allFilms = new ArrayList<>();

        openConnection();

        try {
            // add db select statement string
            String selectSQL = "select * from films where title like '%" + searchString + "%'";

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

            // loop through result set
            // for each item in result set, create new Film object and add to array list
            while (resultSet.next()) {

                // todo FIX THIS. also gets picked up later in assignment
                // lab uses method call to 'getNextFilm'. cleaner way to do it

                int filmID = resultSet.getInt(1);
                String filmName = resultSet.getString(2);
                int filmYear = resultSet.getInt(3);
                String filmDirector = resultSet.getString(4);
                String filmStars = resultSet.getString(5);
                String filmReview = resultSet.getString(6);

                Film film = new Film(filmID, filmName, filmYear, filmDirector,
                        filmStars, filmReview);

                allFilms.add(film);

            }

            stmt.close();
            closeConnection();
        } catch (SQLException se) {
            System.out.println(se);
        }

        return allFilms;
    }

    /**
     * inserts a film into a connected MySQL db<br>
     *     performs a check to confirm if the record was inserted successfully
     * @param f Film object
     * @return integer, with value of 0 (failed) or 1 (success)
     */

    public int insertFilm(Film f) {

        // int value returned (0 or 1)
        int returnValue = 0;

        // open db connection
        openConnection();

        try {
            // extract object values from film object
            int filmId = f.getId();
            String filmName = f.getTitle();
            int filmYear = f.getYear();
            String filmDirector = f.getDirector();
            String filmStars = f.getStars();
            String filmReview = f.getReview();

            // the sql insert string that we need to compile
            // insert into films(id, title, year, director, stars, review) values (filmId, 'filmName', filmYear, 'filmDirector', 'filmStars', 'filmReview');

            // write sql insert statement string
            String insertSql =
                    "insert into films(id, title, year, director, stars, review) values ("
                    + filmId + ", "
                    + "'" + filmName + "', "
                    + filmYear + ", "
                    + "'" + filmDirector + "', "
                    + "'" + filmStars + "', "
                    + "'" + filmReview + "');";

            // print to console to test
            // added main method test class to test this

            // execute insert statement
            returnValue = stmt.executeUpdate(insertSql);
            
            stmt.close();
            closeConnection();

        } catch (SQLException se) {
            System.out.println(se);
        }

        // return value
        return returnValue;

        // todo come back to this
        // create suitable amend/delete methods to complete CRUD set of ops
    }
}
