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
    // String user = "coolingm";
    // String password = "Saftreal4";
    // String jdbcUrl = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

    // Google Cloud SQL format
    /* "jdbc:mysql://google/<dbname>?<connection-instance-name>&socketFactory=com.google.cloud.sql.mysql
    .SocketFactory&useSSL=false&user=<username>&password=<password>" */

    // split url into parts, to make it easier to read
    // uses socket factory connector. jar needs adding to dependencies
    // cloud sql instance credentials found in cloud sql dashboard

    String baseUrl = "jdbc:mysql://google/";
    String dbname = "mmufilms";
    String connectionName = "tensile-sorter-306610:europe-west2:mmu-assignment";
    String socketFactoryParam = "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false";
    String username = "root";
    String password = "B$xter74";

    String googleSQLUrl = baseUrl + dbname + "?cloudSqlInstance=" + connectionName +
            socketFactoryParam + "&user=" + username + "&password=" + password;

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
            // conn = DriverManager.getConnection(jdbcUrl,user,password);
            conn = DriverManager.getConnection(googleSQLUrl);
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
    public ArrayList<Film> getAllFilms() {

        // create array list to hold each film
        ArrayList<Film> allFilms = new ArrayList<>();

        openConnection();

        try {
            // add db select statement string
            String selectSQL = "select * from mmufilms.films limit 10";
            //String selectSQL = "select * from films limit 10";

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

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
            String selectSQL = "select * from mmufilms.films " +
                    "where title like '%" + searchString + "%'";

            // fetch query result set from db
            ResultSet resultSet = stmt.executeQuery(selectSQL);

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
     * fetches film object from id
     * @param filmId
     * @return film object
     */
    public Film getFilmById(int filmId) {

        // open db connection
        openConnection();

        try {
            // add db select statement string
            String selectSQL = "select * from mmufilms.films where id =" + filmId;

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
     * Adds a new Film object to the database<br>
     * @param film takes Film object in from AddFilm servlet
     * @return Film object, via call to getFilmById
     */
    public Film addFilm(Film film) {

        // todo not sure on the use of this now...had to change method sig from int to film
        // int value returned (0 or 1)
        int returnValue = 0;

        Film displayFilm = null;
        int newId = 0;

        // open db connection
        openConnection();

        try {

            // logic to automatically assign unique film id
            // add sql query to fetch last id and add to result set
            ResultSet lastId = stmt.executeQuery(
                    "select MAX(id) from mmufilms.films;");

            // convert result into an integer, then add 1

            // loop through result set
            // for each item in result set, create new Film object and add to array list
            while (lastId.next()) {
                newId = lastId.getInt(1) + 1;
            }

            // write sql insert statement string
            String insertSql = "insert into mmufilms.films" +
                    "(id, title, year, director, stars, review) values(" +
                    newId + ", " +
                    "'" + film.getTitle() + "', " +
                    film.getYear() + ", " +
                    "'" + film.getDirector() + "', " +
                    "'" + film.getStars() + "', " +
                    "'" + film.getReview() + "'" +
                    ");";

            // execute insert statement
            returnValue = stmt.executeUpdate(insertSql);

            stmt.close();
            closeConnection();

        } catch (SQLException se) {
            System.out.println(se);
        }

        // return the response from the select statement, not the insert
        return getFilmById(newId);
    }

    /**
     * deletes film object from db
     * @param filmId
     * @throws SQLException
     */
    public int deleteFilm(int filmId) throws SQLException {

        openConnection();

        // add db select statement string
        String selectSQL = "delete from mmufilms.films where id=" + filmId;

        int returnValue = stmt.executeUpdate(selectSQL);

        // close connection
        closeConnection();

        return returnValue;

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
                    "update mmufilms.films set " +
                            "title=" + "'" + filmName + "', " +
                            "year=" + "'" + filmYear + "', " +
                            "director=" + "'" + filmDirector + "', " +
                            "stars=" + "'" + filmStars + "', " +
                            "review=" + "'" + filmReview + "'" +
                    "where id=" + filmId;

            // execute update statement
            returnValue = stmt.executeUpdate(updateSql);

            // select statement to fetch updated record
            // this is the thing that gets displayed back to user NOT return value

            stmt.close();
            closeConnection();

        } catch (SQLException se) {
            System.out.println(se);
        }

        // return value
        return returnValue;

    }
}
