package hospital_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseSetup {
    public static void createTables() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // First create users table
            DatabaseOperations.createUsersTable();
            
            // Create doctors table
            String createDoctorsTable = """
                CREATE TABLE IF NOT EXISTS doctors (
                    staff_id INT AUTO_INCREMENT PRIMARY KEY,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    phone_number VARCHAR(20),
                    date_of_birth DATE,
                    gender VARCHAR(1),
                    age INT,
                    height INT,
                    specialty VARCHAR(50),
                    status VARCHAR(20) DEFAULT 'Off Duty',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    active_status BOOLEAN DEFAULT TRUE
                )
            """;
            
            stmt.executeUpdate(createDoctorsTable);
            System.out.println("Doctors table created successfully");
            
            // Create a sample doctor user if it doesn't exist
            if (!userExists("doctor1")) {
                String insertDoctorUser = """
                    INSERT INTO users (username, password, role, full_name, email, specialization)
                    VALUES ('doctor1', 'doc123', 'DOCTOR', 'Dr. John Smith', 'john.smith@medcare.com', 'General Medicine')
                """;
                try (PreparedStatement pstmt = conn.prepareStatement(insertDoctorUser)) {
                    pstmt.executeUpdate();
                    System.out.println("Sample doctor user created successfully");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error creating database tables: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertSampleDoctors() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check if doctors already exist
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM doctors");
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert sample doctors only if the table is empty
                String insertDoctors = """
                    INSERT INTO doctors (first_name, last_name, phone_number, date_of_birth, gender, age, height, specialty, status) VALUES
                    ('Edward', 'Jayne', '+971-55-123-4567', '1992-04-04', 'M', 31, 175, 'Psychiatrist', 'On Duty'),
                    ('Philips', 'Gabriel', '+971-55-234-5678', '2000-10-11', 'M', 23, 180, 'Dermatologist', 'Off Duty'),
                    ('Jodis', 'Yaqoub', '+971-55-345-6789', '1991-11-01', 'M', 32, 178, 'Psychiatrist', 'On Duty'),
                    ('Mogra', 'Ben', '+971-55-456-7890', '1979-03-05', 'M', 44, 182, 'Cardiologist', 'On Duty')
                """;
                stmt.executeUpdate(insertDoctors);
                System.out.println("Sample doctors inserted successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error inserting sample doctors: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean userExists(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
