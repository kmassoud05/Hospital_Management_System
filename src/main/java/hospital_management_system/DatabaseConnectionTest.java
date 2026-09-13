package hospital_management_system;

import java.sql.Connection;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Connection successful!");
            System.out.println("Database product name: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("Database product version: " + conn.getMetaData().getDatabaseProductVersion());
            DatabaseConnection.closeConnection(conn);
        } catch (Exception e) {
            System.out.println("Connection failed!");
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
