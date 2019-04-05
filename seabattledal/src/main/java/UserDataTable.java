import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDataTable {

    public static void main(String[] args) {
        createPersonDataTable();
    }

    private static void createPersonDataTable() {
        try {
            Properties prop = JDBCPropertiesGetter.getPropertiesFile();

            String drivers = prop.getProperty("jdbc.drivers");
            String url = prop.getProperty("jdbc.url");

            // load the sqlite-JDBC driver using the current class loader
            Class.forName(drivers);
            Connection connection = null;
            try {
                // create a database connection
                connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                // set timeout to 30 sec.
                statement.setQueryTimeout(30);

                // drop existing table "person"
                statement.executeUpdate("DROP TABLE if EXISTS person");
                // create new table "person"
                statement.executeUpdate("CREATE TABLE person (id INTEGER, username VARCHAR, password VARCHAR )");

            } catch (SQLException ex) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                Logger.getLogger(UserDataAccess.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}