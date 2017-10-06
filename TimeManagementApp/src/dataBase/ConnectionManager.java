package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private final static String URL = "jdbc:mysql://localhost/schedule?useSSL=true";
    private final static String USER = "dbuser";
    private final static String PASSWORD = "dbpassword";
   
    public static Connection connect() throws SQLException{
        return   DriverManager.getConnection(URL, USER, PASSWORD );
    }
}
