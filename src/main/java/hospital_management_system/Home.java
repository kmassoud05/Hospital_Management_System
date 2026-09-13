package hospital_management_system;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(120, 32, 110);  // Purple color
    private static final Color HOVER_COLOR = new Color(140, 42, 130); // Slightly lighter purple for hover

    public Home() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("PROCARE CLINIC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Add title to center of header
        JLabel titleLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Create main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create menu buttons with icons
        JButton patientRecordsBtn = createMenuButton("Patient Records");
        JButton doctorsBtn = createMenuButton("Doctors");
        JButton hospitalInfoBtn = createMenuButton("Hospital Information");

        // Add buttons to main panel
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(patientRecordsBtn, gbc);
        gbc.gridx = 1;
        mainPanel.add(doctorsBtn, gbc);
        gbc.gridx = 2;
        mainPanel.add(hospitalInfoBtn, gbc);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Set frame properties
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton();
        
        // Create a JPanel with purple background
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        
        // Create icon and text labels
        JLabel iconLabel = new JLabel();
        JLabel textLabel = new JLabel(text);
        
        // Set icon based on text
        if (text.contains("Patient Records")) {
            iconLabel.setText("📋");
        } else if (text.contains("Doctors")) {
            iconLabel.setText("👨‍⚕️");
        } else if (text.contains("Hospital Information")) {
            iconLabel.setText("🏥");
        }
        
        // Style the labels
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setForeground(Color.WHITE);
        
        // Add components to panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 0);
        buttonPanel.add(iconLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        buttonPanel.add(textLabel, gbc);
        
        // Configure the button
        button.setLayout(new BorderLayout());
        button.add(buttonPanel);
        button.setPreferredSize(new Dimension(200, 120));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(HOVER_COLOR);
                buttonPanel.setBorder(BorderFactory.createLineBorder(HOVER_COLOR, 1));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(PRIMARY_COLOR);
                buttonPanel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Home();
        });
    }
}
