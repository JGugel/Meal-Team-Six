package PerfectPantryApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Josh
 * Reference: http://www.mysqltutorial.org/connecting-to-mysql-using-jdbc-driver/
 * 
 */
public class JDBC {

   /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
 
        try (FileInputStream f = new FileInputStream("db.properties")) {
 
            // load the properties file
            Properties pros = new Properties();
            pros.load(f);
 
            // assign db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
}


//USE THIS IN PERFECTPANTRYGUI
//        // create a new connection from MySQLJDBCUtil
//        try (Connection conn = MySQLJDBCUtil.getConnection()) {
//            
//            // print out a message
//            System.out.println(String.format("Connected to database %s "
//                    + "successfully.", conn.getCatalog()));
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
