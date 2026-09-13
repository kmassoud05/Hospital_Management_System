package hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Home_Page extends JFrame {
    private JLabel userLabel;
    private final Color PRIMARY_COLOR = new Color(120, 32, 110); // #78206E
    private final Color HOVER_COLOR = new Color(140, 42, 130);
    private final String username;
    private final String userRole;
    
    public Home_Page(UserCredentials user) {
        this.username = user.getUsername();
        this.userRole = user.getRole();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("PROCARE CLINIC - Home");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // User info (left)
        userLabel = new JLabel("User: " + username);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        // Welcome text (center)
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Clinic name (right)
        JLabel clinicLabel = new JLabel("PROCARE CLINIC");
        clinicLabel.setForeground(Color.WHITE);
        clinicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        clinicLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        clinicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(clinicLabel, BorderLayout.EAST);
        
        // Content Panel with cards
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Create cards
        JPanel patientCard = createCard("Patient Records", "📋");
        JPanel doctorsCard = createCard("Doctors", "👨‍⚕️");
        JPanel hospitalCard = createCard("Hospital Information", "🏥");
        
        // Layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Add cards
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(patientCard, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(doctorsCard, gbc);
        
        gbc.gridx = 2;
        contentPanel.add(hospitalCard, gbc);
        
        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        bottomPanel.add(logoutButton);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        logoutButton.addActionListener(e -> handleLogout());
        
        setVisible(true);
    }
    
    private JPanel createCard(String title, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(200, 120));
        card.setBackground(PRIMARY_COLOR);
        card.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Add emoji
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        emojiLabel.setForeground(Color.WHITE);
        card.add(emojiLabel, gbc);

        // Add title
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        card.add(titleLabel, gbc);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(HOVER_COLOR);
                card.setBorder(BorderFactory.createLineBorder(HOVER_COLOR, 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(PRIMARY_COLOR);
                card.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(title);
            }
        });

        return card;
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }
    
    private void handleCardClick(String cardTitle) {
        LoadingDialog loadingDialog = new LoadingDialog(this, "Opening " + cardTitle);
        
        // Create a new thread to handle the window opening
        new Thread(() -> {
            try {
                // Show loading dialog
                SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
                
                // Simulate a brief loading time (500ms)
                Thread.sleep(500);
                
                // Open the appropriate window
                SwingUtilities.invokeLater(() -> {
                    try {
                        switch (cardTitle) {
                            case "Patient Records":
                                new Patient_System(new UserCredentials(0, username, "", userRole)).setVisible(true);
                                break;
                            case "Doctors":
                                // Allow both ADMIN and DOCTOR roles to access
                                if ("ADMIN".equals(userRole) || "DOCTOR".equals(userRole)) {
                                    new Doctor_System(new UserCredentials(0, username, "", userRole)).setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(this,
                                        "You don't have permission to access this section.",
                                        "Access Denied",
                                        JOptionPane.WARNING_MESSAGE);
                                    loadingDialog.stopAnimation();
                                    return;
                                }
                                break;
                            case "Hospital Information":
                                new HospitalInformation(new UserCredentials(0, username, "", userRole)).setVisible(true);
                                break;
                        }
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                            "Error opening " + cardTitle + ": " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } finally {
                        loadingDialog.stopAnimation();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                loadingDialog.stopAnimation();
            }
        }).start();
    }
    
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Home_Page(new UserCredentials(1, "admin", "System Administrator", "ADMIN")).setVisible(true);
        });
    }
}
