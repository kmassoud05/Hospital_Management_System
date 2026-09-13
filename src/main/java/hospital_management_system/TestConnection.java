package hospital_management_system;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Testing database connection...");
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Connection successful!");
                System.out.println("Database Product: " + metaData.getDatabaseProductName());
                System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
                System.out.println("URL: " + metaData.getURL());
                System.out.println("Username: " + metaData.getUserName());
                
                conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
