package hospital_management_system;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox showPasswordCheckbox;
    private JLabel logoLabel;
    private JProgressBar loadingBar;
    
    // Custom colors
    private final Color PRIMARY_COLOR = new Color(120, 32, 110); // #78206E
    private final Color HOVER_COLOR = new Color(140, 42, 130);
    private final Color BACKGROUND_COLOR = Color.WHITE;
    
    public Login() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("PROCARE Clinic Login");
        setSize(1200, 700);  // Increased window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create main panel with GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        // Left Panel (Purple)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PRIMARY_COLOR);
        
        // Add logo
        logoLabel = new JLabel("🏥", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Title
        JLabel titleLabel = new JLabel("PROCARE CLINIC");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        
        // Login text
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 32));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(loginLabel);
        leftPanel.add(Box.createVerticalGlue());
        
        // Right Panel (White)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(userLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        
        usernameField = new JTextField();
        styleTextField(usernameField, "Enter your username");
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(30));
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(passLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        
        // Password field panel (includes field and show/hide checkbox)
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        styleTextField(passwordField, "Enter your password");
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.add(passwordField);
        
        // Show/Hide password checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.setBackground(BACKGROUND_COLOR);
        showPasswordCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPasswordCheckbox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? '\0' : '•');
        });
        passwordPanel.add(Box.createVerticalStrut(10));
        passwordPanel.add(showPasswordCheckbox);
        
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createVerticalStrut(30));
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setUI(new BasicButtonUI()); // Use basic UI to avoid system look and feel
        styleButton(loginButton);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(loginButton);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Loading bar
        loadingBar = new JProgressBar();
        loadingBar.setIndeterminate(true);
        loadingBar.setVisible(false);
        loadingBar.setPreferredSize(new Dimension(500, 5));
        loadingBar.setMaximumSize(new Dimension(500, 5));
        loadingBar.setBackground(BACKGROUND_COLOR);
        loadingBar.setForeground(PRIMARY_COLOR);
        loadingBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(loadingBar);
        
        // Add panels to main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        addActionListeners();
    }
    
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(500, 45));
        button.setMaximumSize(new Dimension(500, 45));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        
        // Add hover effect
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
    
    private void styleTextField(JTextField textField, String placeholder) {
        textField.setPreferredSize(new Dimension(500, 45));
        textField.setMaximumSize(new Dimension(500, 45));
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setOpaque(true);
        textField.setBackground(BACKGROUND_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Add placeholder
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField)textField).setEchoChar('•');
                        showPasswordCheckbox.setSelected(false);
                    }
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField)textField).setEchoChar((char)0);
                        showPasswordCheckbox.setSelected(false);
                    }
                }
            }
        });
        
        if (textField instanceof JPasswordField) {
            ((JPasswordField)textField).setEchoChar((char)0);
        }
    }
    
    private void addActionListeners() {
        // Login button action
        loginButton.addActionListener(e -> handleLogin());

        // Enter key listeners
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void handleLogin() {
        // Show loading indicator
        loadingBar.setVisible(true);
        loginButton.setEnabled(false);
        
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // Check if fields contain placeholders or are empty
        if (username.equals("Enter your username") || password.equals("Enter your password")
            || username.isEmpty() || password.isEmpty()) {
            loadingBar.setVisible(false);
            loginButton.setEnabled(true);
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Use SwingWorker for background processing
        SwingWorker<UserCredentials, Void> worker = new SwingWorker<>() {
            @Override
            protected UserCredentials doInBackground() throws Exception {
                return DatabaseOperations.validateUser(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    UserCredentials user = get();
                    if (user != null) {
                        DatabaseOperations.updateLastLogin(user.getUserId());
                        openDashboard(user);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(Login.this,
                            "Invalid username or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                        passwordField.setText("Enter your password");
                        passwordField.setForeground(Color.GRAY);
                        passwordField.setEchoChar((char)0);
                        passwordField.requestFocus();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Login.this,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    loadingBar.setVisible(false);
                    loginButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void openDashboard(UserCredentials user) {
        SwingUtilities.invokeLater(() -> {
            new Home_Page(user).setVisible(true);
            dispose();
        });
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Login login = new Login();
            login.setVisible(true);

            SwingWorker<Void, Void> databaseSetup = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    DatabaseSetup.createTables();
                    DatabaseSetup.insertSampleDoctors();
                    try {
                        DatabaseOperations.createPatientsTable();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            databaseSetup.execute();
        });
    }
}
