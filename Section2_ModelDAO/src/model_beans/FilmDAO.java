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

                film = getNextFilm(resultSet);
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
     * utility method, used by getAllFilms & getFilmByName
     * @param rs sql query result set, for each film in array list
     * @return film object values
     */
    private Film getNextFilm(ResultSet rs){
        Film thisFilm = null;
        try {
            thisFilm = new Film(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getInt("year"),
                    rs.getString("director"),
                    rs.getString("stars"),
                    rs.getString("review"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thisFilm;
    }

    /**
     * fetches films held in a connected MySQL db, with matching name<br>
     *     populates films into a result set<br>
     *     for each result, creates a Film object and adds to an array list
     * @param searchString name of film(s) to search for<br>
     * @return ArrayList of Film objects
     */
    public ArrayList<Film> getFilmByName(String searchString) {

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

                film = getNextFilm(resultSet);
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
     * @param film Film object
     * @return integer, with value of 0 (failed) or 1 (success)
     */
    public int insertFilm(Film film) {

        // int value returned (0 or 1)
        int returnValue = 0;

        // open db connection
        openConnection();

        try {
            // extract object values from film object
            int filmId = film.getId();
            String filmName = film.getTitle();
            int filmYear = film.getYear();
            String filmDirector = film.getDirector();
            String filmStars = film.getStars();
            String filmReview = film.getReview();

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

    }

    /**
     * fetches film object from id
     * @param filmId
     * @return film object
     */
    public Film getFilmById(int filmId) {

        // open db connection
        openConnection();

        try {
            // add db select statement string
            String selectSQL = "select * from films where id =" + filmId;

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

            // loop through result set
            // for each item in result set, create new Film object and add to array list
            while (resultSet.next()) {
                film = getNextFilm(resultSet);
            }

            // close connection
            stmt.close();
            closeConnection();

        } catch (SQLException se) {
            System.out.println(se);
        }

        // return Film object
        return film;
    }

    /**
     * deletes film object from db
     * @param filmId
     * @throws SQLException
     */
    public void deleteFilm(int filmId) throws SQLException {

        openConnection();

        // add db select statement string
        String selectSQL = "delete from films where id=" + filmId;

        int returnValue = stmt.executeUpdate(selectSQL);

        // close connection
        closeConnection();

    }

    /**
     * updates details of existing film object in db
     * @param film film object to update
     * @return success code (0 or 1) from sql update
     */
    public int updateFilm(Film film) {

        // int value returned (0 or 1)
        int returnValue = 0;

        // open db connection
        openConnection();

        try {
            // extract object values from film object
            int filmId = film.getId();
            String filmName = film.getTitle();
            int filmYear = film.getYear();
            String filmDirector = film.getDirector();
            String filmStars = film.getStars();
            String filmReview = film.getReview();

            // write sql update statement string
            String updateSql =
                    "update films set " +
                            "title=" + "'" + filmName + "', " +
                            "year=" + "'" + filmYear + "', " +
                            "director=" + "'" + filmDirector + "', " +
                            "stars=" + "'" + filmStars + "', " +
                            "review=" + "'" + filmReview + "'" +
                    "where id=" + filmId;

            // execute update statement
            returnValue = stmt.executeUpdate(updateSql);

            stmt.close();
            closeConnection();

        } catch (SQLException se) {
            System.out.println(se);
        }

        // return value
        return returnValue;

    }
}
