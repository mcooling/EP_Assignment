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

    // Google Cloud SQL format
    /* "jdbc:mysql://google/<dbname>?<connection-instance-name>&socketFactory=com.google.cloud.sql.mysql
    .SocketFactory&useSSL=false&user=<username>&password=<password>" */

    // split url into parts, to make it easier to read
    // uses socket factory connector. jar needs adding to dependencies
    // cloud sql instance credentials found in cloud sql dashboard

    /* String baseUrl = "jdbc:mysql://google/";
    String dbname = "mmufilms";
    String connectionName = "tensile-sorter-306610:europe-west2:mmu-assignment";
    String socketFactoryParam = "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false";
    String username = "root";
    String password = "B$xter74";

    String googleSQLUrl = baseUrl + dbname + "?cloudSqlInstance=" + connectionName +
            socketFactoryParam + "&user=" + username + "&password=" + password; */

    public FilmDAO() {
    }

    /**
     * opens MySQL db connection<br>
     * used in each of the CRUD operation methods
     */

    private void openConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
        }

        // connecting to database
        try {
            conn = DriverManager.getConnection(jdbcUrl, user, password);
            // conn = DriverManager.getConnection(googleSQLUrl);
            stmt = conn.createStatement();

        } catch (SQLException se) {
            System.out.println(se);
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

    // todo come back to this!! kaleem's video on 5/3 shows much slicker way to do this
    // uses getNext film method to create Film object, then adds that to list
    // rather than extracting film body and compiling new film object
    public ArrayList<Film> getAllFilms() {

        // create array list to hold each film
        ArrayList<Film> allFilms = new ArrayList<>();

        openConnection();

        try {
            // add db select statement string
            //String selectSQL = "select * from mmufilms.films limit 10";
            String selectSQL = "select * from films limit 10";

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

            // for each item in result set, create new Film object and add to array list
            while (resultSet.next()) {

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
            String selectSQL = "select * from films " +
                    "where title like '%" + searchString + "%'";
            //String selectSQL = "select * from mmufilms.films " +
            //        "where title like '%" + searchString + "%'";

            // todo revisit this. brief asks for debug
            // print to console to test
            System.out.println(selectSQL);

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

            // for each item in result set, create new Film object and add to array list
            while (resultSet.next()) {

                int filmID = resultSet.getInt(1);
                String filmName = resultSet.getString(2);
                int filmYear = resultSet.getInt(3);
                String filmDirector = resultSet.getString(4);
                String filmStars = resultSet.getString(5);
                String filmReview = resultSet.getString(6);

                Film film = new Film(filmID, filmName, filmYear, filmDirector,
                        filmStars, filmReview);

                allFilms.add(film);

                // print to console to test
                // System.out.println(film.toString());
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

    // todo need to confirm the ask on this
    // section 2 tests the method / response code
    // only way to test this will be to pass a Film object in through index.jsp
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

            // write sql insert statement string
            String insertSql =
                    "insert into films(id, title, year, director, stars, review) values ("
                            + filmId + ", "
                            + "'" + filmName + "', "
                            + filmYear + ", "
                            + "'" + filmDirector + "', "
                            + "'" + filmStars + "', "
                            + "'" + filmReview + "');";

            /*String insertSql =
                    "insert into mmufilms.films(id, title, year, director, stars, review) values ("
                            + filmId + ", "
                            + "'" + filmName + "', "
                            + filmYear + ", "
                            + "'" + filmDirector + "', "
                            + "'" + filmStars + "', "
                            + "'" + filmReview + "');";*/

            // todo print to console to test
            // see previous comments. unclear what the ask is to test this

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