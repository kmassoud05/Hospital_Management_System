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
        String[][] doctors = {
            {"Edward", "Jayne", "050-100-0001", "1992-04-04", "M", "31", "175", "Psychiatrist", "On Duty"},
            {"Philips", "Gabriel", "050-100-0002", "2000-10-11", "M", "23", "180", "Dermatologist", "Off Duty"},
            {"Jodis", "Yaqoub", "050-100-0003", "1991-11-01", "M", "32", "178", "Psychiatrist", "On Duty"},
            {"Mogra", "Ben", "050-100-0004", "1979-03-05", "M", "44", "182", "Cardiologist", "On Duty"},
            {"Amelia", "Hart", "050-100-0005", "1988-07-19", "F", "35", "168", "Pediatrician", "On Duty"},
            {"Omar", "Khalid", "050-100-0006", "1985-02-14", "M", "39", "176", "Neurologist", "Off Duty"},
            {"Sofia", "Marin", "050-100-0007", "1993-09-28", "F", "30", "165", "General Practitioner", "On Duty"},
            {"Nathan", "Cole", "050-100-0008", "1976-12-02", "M", "47", "181", "Orthopedist", "Off Duty"},
            {"Lina", "Farouk", "050-100-0009", "1990-05-23", "F", "33", "170", "Oncologist", "On Duty"},
            {"Daniel", "Reed", "050-100-0010", "1982-08-30", "M", "41", "179", "Cardiologist", "On Duty"}
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement exists = conn.prepareStatement(
                 "SELECT COUNT(*) FROM doctors WHERE first_name = ? AND last_name = ?");
             PreparedStatement insert = conn.prepareStatement("""
                 INSERT INTO doctors (first_name, last_name, phone_number, date_of_birth,
                     gender, age, height, specialty, status)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
             """)) {
            int inserted = 0;
            for (String[] doctor : doctors) {
                exists.setString(1, doctor[0]);
                exists.setString(2, doctor[1]);
                try (ResultSet rs = exists.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) != 0) {
                        continue;
                    }
                }
                insert.setString(1, doctor[0]);
                insert.setString(2, doctor[1]);
                insert.setString(3, doctor[2]);
                insert.setString(4, doctor[3]);
                insert.setString(5, doctor[4]);
                insert.setInt(6, Integer.parseInt(doctor[5]));
                insert.setInt(7, Integer.parseInt(doctor[6]));
                insert.setString(8, doctor[7]);
                insert.setString(9, doctor[8]);
                insert.executeUpdate();
                inserted++;
            }
            System.out.println("Sample doctors inserted successfully: " + inserted);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error inserting sample doctors: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertSamplePatients() {
        String[][] patients = {
            {"Ava", "Bennett", "1998-01-12", "F", "050-200-0001", "26", "165", "A+", "Penicillin", "Asthma"},
            {"Liam", "Carter", "1987-03-25", "M", "050-200-0002", "37", "178", "O+", "None", "Hypertension"},
            {"Mia", "Collins", "2002-06-08", "F", "050-200-0003", "22", "160", "B+", "Peanuts", "None"},
            {"Noah", "Dawson", "1975-09-17", "M", "050-200-0004", "49", "182", "AB+", "Latex", "Diabetes"},
            {"Isla", "Ellis", "1991-11-04", "F", "050-200-0005", "32", "168", "O-", "None", "Migraine"},
            {"Ethan", "Foster", "1983-02-21", "M", "050-200-0006", "41", "176", "A-", "Sulfa drugs", "High cholesterol"},
            {"Sophia", "Grant", "1996-04-29", "F", "050-200-0007", "28", "163", "B-", "Shellfish", "Anemia"},
            {"Oliver", "Hayes", "1968-07-13", "M", "050-200-0008", "56", "174", "A+", "None", "Arthritis"},
            {"Grace", "Irwin", "2000-10-02", "F", "050-200-0009", "24", "170", "AB-", "Dust", "Seasonal allergies"},
            {"Lucas", "Jordan", "1989-12-19", "M", "050-200-0010", "34", "180", "O+", "Aspirin", "Back pain"},
            {"Ella", "Khan", "1994-05-06", "F", "050-200-0011", "30", "158", "A+", "None", "Thyroid disorder"},
            {"James", "Lewis", "1979-08-24", "M", "050-200-0012", "45", "177", "B+", "Eggs", "Gastritis"},
            {"Chloe", "Morgan", "1985-01-31", "F", "050-200-0013", "39", "166", "O-", "Penicillin", "Eczema"},
            {"Mason", "Nelson", "1999-03-14", "M", "050-200-0014", "25", "183", "A-", "None", "None"},
            {"Lily", "Owens", "1972-06-27", "F", "050-200-0015", "52", "162", "AB+", "Contrast dye", "Heart disease"},
            {"Henry", "Parker", "1990-09-09", "M", "050-200-0016", "33", "185", "O+", "None", "Kidney stones"},
            {"Emily", "Quinn", "2004-11-22", "F", "050-200-0017", "19", "157", "B+", "Milk", "None"},
            {"Benjamin", "Ross", "1981-02-05", "M", "050-200-0018", "43", "179", "A+", "Codeine", "Sleep apnea"},
            {"Harper", "Stewart", "1997-04-18", "F", "050-200-0019", "27", "164", "O-", "None", "Iron deficiency"},
            {"Alexander", "Turner", "1965-07-30", "M", "050-200-0020", "59", "171", "AB-", "Shellfish", "COPD"},
            {"Sofia", "Underwood", "1988-10-16", "F", "050-200-0021", "35", "169", "A-", "None", "Polycystic ovary syndrome"},
            {"William", "Vega", "1993-12-03", "M", "050-200-0022", "30", "184", "B-", "Peanuts", "Tendonitis"},
            {"Amelia", "Walsh", "1977-05-28", "F", "050-200-0023", "47", "161", "O+", "Latex", "Osteoporosis"},
            {"Michael", "Xavier", "2001-08-11", "M", "050-200-0024", "23", "175", "A+", "None", "None"},
            {"Charlotte", "Young", "1984-01-07", "F", "050-200-0025", "40", "167", "AB+", "Aspirin", "Endometriosis"},
            {"Daniel", "Zimmer", "1992-03-20", "M", "050-200-0026", "32", "181", "O-", "None", "Asthma"},
            {"Evelyn", "Adams", "1970-06-15", "F", "050-200-0027", "54", "159", "B+", "Sulfa drugs", "Diabetes"},
            {"Sebastian", "Brooks", "1995-09-26", "M", "050-200-0028", "28", "186", "A-", "None", "Anxiety"},
            {"Scarlett", "Cooper", "1986-11-10", "F", "050-200-0029", "37", "172", "O+", "Penicillin", "High blood pressure"},
            {"Matthew", "Davis", "1974-04-02", "M", "050-200-0030", "50", "178", "AB-", "None", "Gout"}
        };

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement exists = conn.prepareStatement(
                 "SELECT COUNT(*) FROM patients WHERE first_name = ? AND last_name = ?");
             PreparedStatement nextId = conn.prepareStatement(
                 "SELECT COALESCE(MAX(patient_id), 0) + 1 FROM patients");
             PreparedStatement insert = conn.prepareStatement("""
                INSERT INTO patients (patient_id, first_name, last_name, date_of_birth,
                    gender, phone_number, age, height, blood_type, allergies, medical_conditions)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """)) {
            int inserted = 0;
            for (String[] patient : patients) {
                exists.setString(1, patient[0]);
                exists.setString(2, patient[1]);
                try (ResultSet rs = exists.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) != 0) {
                        continue;
                    }
                }

                int patientId;
                try (ResultSet rs = nextId.executeQuery()) {
                    rs.next();
                    patientId = rs.getInt(1);
                }

                insert.setInt(1, patientId);
                insert.setString(2, patient[0]);
                insert.setString(3, patient[1]);
                insert.setString(4, patient[2]);
                insert.setString(5, patient[3]);
                insert.setString(6, patient[4]);
                insert.setInt(7, Integer.parseInt(patient[5]));
                insert.setInt(8, Integer.parseInt(patient[6]));
                insert.setString(9, patient[7]);
                insert.setString(10, patient[8]);
                insert.setString(11, patient[9]);
                insert.executeUpdate();
                inserted++;
            }
            System.out.println("Sample patients inserted successfully: " + inserted);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error inserting sample patients: " + e.getMessage(),
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
