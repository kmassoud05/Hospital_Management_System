package hospital_management_system;

import java.sql.*;

public class FixAdminPassword {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update admin password to use correct hash
            String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                String hashedPassword = hashPassword("admin123");
                pstmt.setString(1, hashedPassword);
                pstmt.setString(2, "admin");
                int updated = pstmt.executeUpdate();
                if (updated > 0) {
                    System.out.println("Admin password updated successfully!");
                } else {
                    System.out.println("Admin user not found!");
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
