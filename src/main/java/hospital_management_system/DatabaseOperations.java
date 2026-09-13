package hospital_management_system;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseOperations {
    
    public DatabaseOperations() {
        try {
            createPatientsTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void createPatientsTable() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS patients (
                patient_id INT PRIMARY KEY,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                date_of_birth DATE,
                gender VARCHAR(1),
                phone_number VARCHAR(15),
                age INT,
                height INT,
                blood_type VARCHAR(3),
                allergies TEXT,
                medical_conditions TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Patients table created or already exists");
        } catch (SQLException e) {
            System.err.println("Error creating patients table: " + e.getMessage());
            throw e;
        }
    }
    
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void createUsersTable() {
        System.out.println("Attempting to create/update users table...");
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                role ENUM('ADMIN', 'DOCTOR') NOT NULL,
                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                phone VARCHAR(20),
                specialization VARCHAR(100),
                license_number VARCHAR(50),
                last_login TIMESTAMP,
                account_status BOOLEAN DEFAULT true,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Users table created/verified successfully");
                
                // Update existing users with hashed passwords
                updateUserPassword(conn, "admin", "admin123", "ADMIN", "System Administrator", "admin@medcare.com");
                updateUserPassword(conn, "doctor1", "doc123", "DOCTOR", "Doctor One", "doctor1@medcare.com");
            }
        } catch (SQLException e) {
            System.err.println("Error with users table: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error with users table: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int getNextUserId() {
        String sql = "SELECT COALESCE(MIN(t1.user_id + 1), 1) AS next_id FROM users t1 " +
                    "LEFT JOIN users t2 ON t1.user_id + 1 = t2.user_id " +
                    "WHERE t2.user_id IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
            return 1; // If table is empty
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting next user ID: " + e.getMessage());
        }
    }

    private static void updateUserPassword(Connection conn, String username, String password, 
            String role, String fullName, String email) throws SQLException {
        if (!userExists(username)) {
            System.out.println("Creating " + username + " user...");
            String insertSQL = """
                INSERT INTO users (user_id, username, password, role, full_name, email)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                int nextId = getNextUserId();
                pstmt.setInt(1, nextId);
                pstmt.setString(2, username);
                pstmt.setString(3, password);
                pstmt.setString(4, role);
                pstmt.setString(5, fullName);
                pstmt.setString(6, email);
                pstmt.executeUpdate();
                System.out.println(username + " user created successfully with ID: " + nextId);
            }
        } else {
            System.out.println(username + " user already exists");
        }
    }
    
    public static UserCredentials validateUser(String username, String password) throws SQLException {
        System.out.println("Attempting to validate user: " + username);
        
        // First check if users table exists
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "users", null);
            if (!tables.next()) {
                System.out.println("Users table does not exist. Creating it now...");
                createUsersTable();
            } else {
                System.out.println("Users table exists");
            }
        }

        String query = """
            SELECT user_id, username, full_name, role, account_status, password 
            FROM users 
            WHERE username = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            System.out.println("Executing query: " + query);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    System.out.println("Found user. Comparing passwords...");
                    System.out.println("Stored password: " + storedPassword);
                    
                    if (password.equals(storedPassword)) {
                        System.out.println("Password match! Login successful");
                        return new UserCredentials(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("full_name"),
                            rs.getString("role")
                        );
                    } else {
                        System.out.println("Password mismatch!");
                    }
                } else {
                    System.out.println("User not found in database");
                }
            }
        }
        return null;
    }
    
    public static void updateLastLogin(int userId) throws SQLException {
        String query = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    public static ResultSet getAllPatients() {
        String sql = "SELECT * FROM patients ORDER BY created_at DESC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ResultSet getAllPatientsSorted(String orderBy, boolean ascending) {
        String sql = "SELECT * FROM patients ORDER BY " + orderBy + (ascending ? " ASC" : " DESC");
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static int getNextPatientId() {
        String sql = "SELECT COALESCE(MIN(t1.patient_id + 1), 1) AS next_id FROM patients t1 " +
                    "LEFT JOIN patients t2 ON t1.patient_id + 1 = t2.patient_id " +
                    "WHERE t2.patient_id IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
            return 1; // If table is empty
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting next patient ID: " + e.getMessage());
        }
    }
    
    public static void addPatient(String firstName, String lastName, String dateOfBirth,
            String gender, String phoneNumber, int age, int height, String bloodType,
            String allergies, String medicalConditions) {
        String sql = """
            INSERT INTO patients (patient_id, first_name, last_name, date_of_birth, gender,
                phone_number, age, height, blood_type, allergies, medical_conditions)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            int nextId = getNextPatientId();
            pstmt.setInt(1, nextId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setDate(4, java.sql.Date.valueOf(dateOfBirth));
            pstmt.setString(5, gender);
            pstmt.setString(6, phoneNumber);
            pstmt.setInt(7, age);
            pstmt.setInt(8, height);
            pstmt.setString(9, bloodType);
            pstmt.setString(10, allergies);
            pstmt.setString(11, medicalConditions);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding patient: " + e.getMessage());
        }
    }
    
    public static void updatePatient(int patientId, String firstName, String lastName,
            String dateOfBirth, String gender, String phoneNumber, int age,
            int height, String bloodType, String allergies, String medicalConditions) {
        String sql = """
            UPDATE patients 
            SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?,
                phone_number = ?, age = ?, height = ?, blood_type = ?,
                allergies = ?, medical_conditions = ?
            WHERE patient_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setDate(3, java.sql.Date.valueOf(dateOfBirth));
            pstmt.setString(4, gender);
            pstmt.setString(5, phoneNumber);
            pstmt.setInt(6, age);
            pstmt.setInt(7, height);
            pstmt.setString(8, bloodType);
            pstmt.setString(9, allergies);
            pstmt.setString(10, medicalConditions);
            pstmt.setInt(11, patientId);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating patient: " + e.getMessage());
        }
    }
    
    public static void deletePatient(int patientId) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting patient: " + e.getMessage());
        }
    }
    
    public static ResultSet searchPatients(String searchTerm) {
        String sql = """
            SELECT * FROM patients 
            WHERE first_name LIKE ? OR last_name LIKE ? 
            OR phone_number LIKE ? OR medical_conditions LIKE ?
            ORDER BY created_at DESC
        """;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ResultSet getPatientById(int patientId) {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            pstmt.setInt(1, patientId);
            
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
