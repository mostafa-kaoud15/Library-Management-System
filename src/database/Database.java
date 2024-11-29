package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {
    private static final String url;
    private static final String userName;
    private static final String password;
    private static Connection connection;

    static {
        url = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
        userName = "root";
        password = "2003";
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {

            connection = DriverManager.getConnection(url, userName, password);
        }
        return connection;
    }

}
