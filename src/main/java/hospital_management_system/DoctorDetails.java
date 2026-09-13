package hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoctorDetails extends JFrame {
    private final Color PRIMARY_COLOR = new Color(120, 32, 110);
    private JTextField firstNameField, lastNameField, phoneField, dobField, ageField, heightField;
    private JComboBox<String> genderCombo, specialtyCombo;
    private JCheckBox onDutyCheckBox;
    private JButton saveButton;
    private final int doctorId;
    private final Doctor_System parentFrame;
    
    public DoctorDetails(int doctorId) {
        this.doctorId = doctorId;
        this.parentFrame = null;
        initializeUI();
        if (doctorId != -1) {
            loadDoctorData();
        }
    }
    
    public DoctorDetails(int doctorId, Doctor_System parentFrame) {
        this.doctorId = doctorId;
        this.parentFrame = parentFrame;
        initializeUI();
        if (doctorId != -1) {
            loadDoctorData();
        }
    }
    
    private void initializeUI() {
        setTitle("Staff Record Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        JLabel titleLabel = new JLabel("Staff Record Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = createTextField();
        formPanel.add(firstNameField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        lastNameField = createTextField();
        formPanel.add(lastNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Phone No#"), gbc);
        gbc.gridx = 1;
        phoneField = createTextField();
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createLabel("Date Of Birth:"), gbc);
        gbc.gridx = 3;
        dobField = createTextField();
        formPanel.add(dobField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"M", "F"});
        styleComboBox(genderCombo);
        formPanel.add(genderCombo, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createLabel("Age:"), gbc);
        gbc.gridx = 3;
        ageField = createTextField();
        formPanel.add(ageField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Height (cm):"), gbc);
        gbc.gridx = 1;
        heightField = createTextField();
        formPanel.add(heightField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createLabel("Specialty:"), gbc);
        gbc.gridx = 3;
        specialtyCombo = new JComboBox<>(new String[]{
            "Cardiologist", "Dermatologist", "Psychiatrist", "Pediatrician",
            "Neurologist", "Orthopedist", "Oncologist", "General Practitioner"
        });
        styleComboBox(specialtyCombo);
        formPanel.add(specialtyCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("On Duty:"), gbc);
        gbc.gridx = 1;
        onDutyCheckBox = new JCheckBox();
        formPanel.add(onDutyCheckBox, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Changes");
        saveButton.setPreferredSize(new Dimension(150, 40));
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(150, 40));
        
        // Style the buttons with purple theme
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effects
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(PRIMARY_COLOR.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(PRIMARY_COLOR.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        saveButton.addActionListener(e -> {
            updateDoctorRecord();
            if (parentFrame != null) {
                parentFrame.loadDoctorData(); 
                parentFrame.setVisible(true);
            }
            this.dispose();
        });
        
        closeButton.addActionListener(e -> {
            this.dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        
        // Add all panels to main panel
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(150, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void updateDoctorRecord() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String phoneNumber = phoneField.getText();
            
            // Convert date from dd-MM-yyyy to yyyy-MM-dd
            String dateOfBirth = dobField.getText();
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(dateOfBirth);
                dateOfBirth = outputFormat.format(date);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Please enter date in DD-MM-YYYY format");
                return;
            }
            
            String gender = (String) genderCombo.getSelectedItem();
            int age = Integer.parseInt(ageField.getText());
            int height = Integer.parseInt(heightField.getText());
            String specialty = (String) specialtyCombo.getSelectedItem();
            String status = onDutyCheckBox.isSelected() ? "On Duty" : "Off Duty";
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                if (doctorId == -1) {
                    // Insert new doctor
                    try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO doctors (first_name, last_name, phone_number, " +
                        "date_of_birth, gender, age, height, specialty, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                        
                        stmt.setString(1, firstName);
                        stmt.setString(2, lastName);
                        stmt.setString(3, phoneNumber);
                        stmt.setString(4, dateOfBirth);
                        stmt.setString(5, gender);
                        stmt.setInt(6, age);
                        stmt.setInt(7, height);
                        stmt.setString(8, specialty);
                        stmt.setString(9, status);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "New doctor added successfully!");
                    }
                } else {
                    // Update existing doctor
                    try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE doctors SET first_name=?, last_name=?, phone_number=?, " +
                        "date_of_birth=?, gender=?, age=?, height=?, specialty=?, status=? " +
                        "WHERE staff_id=?")) {
                        
                        stmt.setString(1, firstName);
                        stmt.setString(2, lastName);
                        stmt.setString(3, phoneNumber);
                        stmt.setString(4, dateOfBirth);
                        stmt.setString(5, gender);
                        stmt.setInt(6, age);
                        stmt.setInt(7, height);
                        stmt.setString(8, specialty);
                        stmt.setString(9, status);
                        stmt.setInt(10, doctorId);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
                    }
                }
                
                // Refresh the parent frame's data if it exists
                if (parentFrame != null) {
                    parentFrame.loadDoctorData();
                }
                
                // Close the form
                this.dispose();
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age and height.");
        }
    }
    
    private void loadDoctorData() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctors WHERE staff_id = ?")) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                firstNameField.setText(rs.getString("first_name"));
                lastNameField.setText(rs.getString("last_name"));
                phoneField.setText(rs.getString("phone_number"));
                dobField.setText(rs.getString("date_of_birth"));
                genderCombo.setSelectedItem(rs.getString("gender"));
                ageField.setText(String.valueOf(rs.getInt("age")));
                heightField.setText(String.valueOf(rs.getInt("height")));
                specialtyCombo.setSelectedItem(rs.getString("specialty"));
                onDutyCheckBox.setSelected(rs.getString("status").equals("On Duty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctor data: " + e.getMessage());
        }
    }
}
