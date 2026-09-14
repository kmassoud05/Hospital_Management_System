package hospital_management_system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class HospitalInformation extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(120, 32, 110);  // #78206E
    private static final Color LIGHTER_PURPLE = new Color(188, 140, 183);  // For cards background
    private JLabel totalDoctorsLabel;
    private JLabel onDutyLabel;
    private JLabel totalPatientsLabel;
    private final UserCredentials userCredentials;

    public HospitalInformation(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeUI();
        setVisible(true);
        // Load statistics after window is visible
        new Thread(this::loadStatistics).start();
    }

    public HospitalInformation() {
        this.userCredentials = new UserCredentials(); // Default initialization
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeUI();
        setVisible(true);
        // Load statistics after window is visible
        new Thread(this::loadStatistics).start();
    }

    private void initializeUI() {
        setTitle("Hospital Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Main panel with white background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Back Button
        JButton backButton = new JButton("← BACK");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new Home_Page(userCredentials).setVisible(true);
        });

        // Title Label
        JLabel titleLabel = new JLabel("Hospital Information", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Add components to header
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setBackground(Color.WHITE);

        // Create statistics cards
        cardsPanel.add(createStatCard("Total Doctors", "👨‍⚕️"));
        cardsPanel.add(createStatCard("Total Patients", "🏥"));

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private JPanel createStatCard(String title, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(LIGHTER_PURPLE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Emoji Label
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Number Label
        JLabel numberLabel = new JLabel("0");
        numberLabel.setFont(new Font("Arial", Font.BOLD, 36));
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // On Duty Label (only for doctors)
        if (title.equals("Total Doctors")) {
            totalDoctorsLabel = numberLabel;
            onDutyLabel = new JLabel("On Duty: 0");
            onDutyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            onDutyLabel.setForeground(Color.WHITE);
            onDutyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(Box.createVerticalGlue());
            card.add(emojiLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(numberLabel);
            card.add(Box.createVerticalStrut(5));
            card.add(titleLabel);
            card.add(Box.createVerticalStrut(5));
            card.add(onDutyLabel);
            card.add(Box.createVerticalGlue());
        } else {
            totalPatientsLabel = numberLabel;
            card.add(Box.createVerticalGlue());
            card.add(emojiLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(numberLabel);
            card.add(Box.createVerticalStrut(5));
            card.add(titleLabel);
            card.add(Box.createVerticalGlue());
        }

        return card;
    }

    private void loadStatistics() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get total doctors
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM doctors")) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int totalDoctors = rs.getInt(1);
                        SwingUtilities.invokeLater(() -> 
                            totalDoctorsLabel.setText(String.valueOf(totalDoctors)));
                    }
                }
            }
            
            // Get doctors on duty
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM doctors WHERE status = 'On Duty'")) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int onDutyDoctors = rs.getInt(1);
                        SwingUtilities.invokeLater(() -> 
                            onDutyLabel.setText("On Duty: " + onDutyDoctors));
                    }
                }
            }
            
            // Get total patients
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM patients")) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int totalPatients = rs.getInt(1);
                        SwingUtilities.invokeLater(() -> 
                            totalPatientsLabel.setText(String.valueOf(totalPatients)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> 
                JOptionPane.showMessageDialog(this,
                    "Error loading statistics: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalInformation(new UserCredentials()).setVisible(true);
        });
    }
}
