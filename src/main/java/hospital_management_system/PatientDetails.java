package hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientDetails extends JFrame {
    private final Color PRIMARY_COLOR = new Color(120, 32, 110);
    private JTextField firstNameField, lastNameField, phoneField, dobField;
    private JTextField heightField, ageField;
    private JComboBox<String> genderCombo, bloodTypeCombo;
    private JTextArea allergiesArea, medicalConditionsArea;
    private JButton saveButton;
    private int patientId = -1;
    private final Patient_System parentFrame;
    
    public PatientDetails() {
        this(-1, null); // For new patient
    }
    
    public PatientDetails(int patientId) {
        this(patientId, null);
    }
    
    public PatientDetails(int patientId, Patient_System parentFrame) {
        this.patientId = patientId;
        this.parentFrame = parentFrame;
        initializeUI();
        if (patientId != -1) {
            loadPatientData();
        }
    }
    
    private void initializeUI() {
        setTitle(patientId == -1 ? "Add New Patient" : "Edit Patient");
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
        JLabel titleLabel = new JLabel("Patient Record Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // First row
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
        
        // Second row
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
        
        // Third row
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
        
        // Fourth row
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Height (cm):"), gbc);
        gbc.gridx = 1;
        heightField = createTextField();
        formPanel.add(heightField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(createLabel("Blood Type:"), gbc);
        gbc.gridx = 3;
        bloodTypeCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        styleComboBox(bloodTypeCombo);
        formPanel.add(bloodTypeCombo, gbc);
        
        // Fifth row - Allergies
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Allergies"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        allergiesArea = createTextArea();
        JScrollPane allergiesScroll = new JScrollPane(allergiesArea);
        allergiesScroll.setPreferredSize(new Dimension(0, 100));
        formPanel.add(allergiesScroll, gbc);
        
        // Sixth row - Medical Conditions
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        formPanel.add(createLabel("Known Medical Condition(s)"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        medicalConditionsArea = createTextArea();
        JScrollPane medicalScroll = new JScrollPane(medicalConditionsArea);
        medicalScroll.setPreferredSize(new Dimension(0, 100));
        formPanel.add(medicalScroll, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton(patientId == -1 ? "Add" : "Save Changes");
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
            handleSave();
        });
        
        closeButton.addActionListener(e -> {
            this.dispose();
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
    
    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
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
    
    private void loadPatientData() {
        try (ResultSet rs = DatabaseOperations.getPatientById(patientId)) {
            if (rs.next()) {
                firstNameField.setText(rs.getString("first_name"));
                lastNameField.setText(rs.getString("last_name"));
                phoneField.setText(rs.getString("phone_number"));
                dobField.setText(rs.getDate("date_of_birth").toString());
                genderCombo.setSelectedItem(rs.getString("gender"));
                ageField.setText(String.valueOf(rs.getInt("age")));
                heightField.setText(String.valueOf(rs.getInt("height")));
                bloodTypeCombo.setSelectedItem(rs.getString("blood_type"));
                allergiesArea.setText(rs.getString("allergies"));
                medicalConditionsArea.setText(rs.getString("medical_conditions"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage());
        }
    }
    
    private void handleSave() {
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
            String bloodType = (String) bloodTypeCombo.getSelectedItem();
            String allergies = allergiesArea.getText();
            String medicalConditions = medicalConditionsArea.getText();
            
            if (patientId == -1) {
                DatabaseOperations.addPatient(firstName, lastName, dateOfBirth, gender,
                    phoneNumber, age, height, bloodType, allergies, medicalConditions);
                if (parentFrame != null) {
                    parentFrame.loadPatientData(); // Refresh the patient list immediately
                }
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                this.dispose(); // Close the form after successful save
            } else {
                DatabaseOperations.updatePatient(patientId, firstName, lastName, dateOfBirth,
                    gender, phoneNumber, age, height, bloodType, allergies, medicalConditions);
                if (parentFrame != null) {
                    parentFrame.loadPatientData(); // Refresh the patient list immediately
                }
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                this.dispose(); // Close the form after successful save
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for age and height.");
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Error adding patient")) {
                JOptionPane.showMessageDialog(this, 
                    "Database error: " + e.getCause().getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error saving patient: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
            parentFrame.loadPatientData(); // Refresh the table
        }
    }
}
