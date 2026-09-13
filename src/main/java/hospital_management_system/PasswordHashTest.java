package hospital_management_system;

import java.sql.*;

public class PasswordHashTest {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Print the stored hashed password from the database
            String query = "SELECT username, password FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "admin");
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password");
                        System.out.println("Stored hash in database: " + storedHash);
                        
                        // Calculate hash of "admin123" for comparison
                        String testPassword = "admin123";
                        String calculatedHash = hashPassword(testPassword);
                        System.out.println("Calculated hash of 'admin123': " + calculatedHash);
                        
                        System.out.println("Hashes match: " + storedHash.equals(calculatedHash));
                    } else {
                        System.out.println("Admin user not found in database");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static String hashPassword(String password) {
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
}
