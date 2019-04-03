import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDataAccess {

    private String drivers;
    private String url;
    private Connection connection = null;

    private void setUrlAndDrivers(){
        Properties prop = JDBCPropertiesGetter.getPropertiesFile();

        drivers = prop.getProperty("jdbc.drivers");
        url = prop.getProperty("jdbc.url");
    }

    public UserResultDto getExistingUser(String usermame, String password) {
        try {
            setUrlAndDrivers();
            // load the sqlite-JDBC driver using the current class loader
            Class.forName(drivers);
            try {
                UserResultDto user = new UserResultDto();
                // create a database connection
                connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE username = ? AND password = ?");
                statement.setString(1, usermame);
                statement.setString(2, password);

                // execute query: get all persons
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    // read the result set
                    System.out.println("user id = " + rs.getString("id"));
                    System.out.println("username = " + rs.getString("username"));

                    if(rs.getString("username").equals(""))
                        user.username = rs.getString("username");
                        user.password = rs.getString("password");
                    return user;
                }
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
        return null;
    }
    public boolean getUsernameInUse(String username) {
        try {
            setUrlAndDrivers();
            // load the sqlite-JDBC driver using the current class loader
            Class.forName(drivers);
            try {
                // create a database connection
                connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE username = ?");
                statement.setString(1, username);

                // execute query: get all persons
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    // read the result set
                    System.out.println("user id = " + rs.getString("id"));
                    System.out.println("username = " + rs.getString("username"));

                    return !rs.getString("username").equals("");
                }
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
        return false;
    }
    public boolean resgisterUser(String username, String password) {
        boolean success = false;
        try {
            setUrlAndDrivers();

            // load the sqlite-JDBC driver using the current class loader
            Class.forName(drivers);
            try {
                // create a database connection
                connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement(" INSERT INTO person (username, password)" + " values (?, ?)");
                statement.setString(1, username);
                statement.setString(2, password);
                // set timeout to 30 sec.
                statement.setQueryTimeout(30);

                statement.execute();
                success = true;

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
        return success;
    }
}
