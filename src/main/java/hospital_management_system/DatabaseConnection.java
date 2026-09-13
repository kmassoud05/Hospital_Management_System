package hospital_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // TODO: replace PASSWORD with your local MySQL root password (or a dedicated user you create)
    private static final String URL = "jdbc:mysql://localhost:3306/hospitalmgmt?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Keemo2005";
    
    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Set connection properties
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("autoReconnect", "true");
            props.setProperty("maxReconnects", "4");
            props.setProperty("connectTimeout", "10000");
            
            System.out.println("Attempting to connect to remote database...");
            Connection conn = DriverManager.getConnection(URL, props);
            System.out.println("Database connection successful!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw e;
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
