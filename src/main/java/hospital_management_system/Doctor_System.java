package hospital_management_system;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor_System extends JFrame {
    private final Color PRIMARY_COLOR = new Color(120, 32, 110);
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private final UserCredentials userCredentials;
    
    public Doctor_System(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        DatabaseSetup.createTables();  // Ensure tables exist
        DatabaseSetup.insertSampleDoctors();  // Insert sample doctors if table is empty
        initializeUI();
        loadDoctorData();
    }
    
    public Doctor_System() {
        this(new UserCredentials(0, "defaultUser", "Default User", "USER_ROLE"));
    }
    
    private void initializeUI() {
        setTitle("Staff Record Management System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Back Button
        JButton backButton = new JButton("← BACK");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new Home_Page(userCredentials).setVisible(true);
        });
        
        // Title
        JLabel titleLabel = new JLabel("Staff Record Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Clinic Name
        JLabel clinicLabel = new JLabel("PROCARE CLINIC");
        clinicLabel.setForeground(Color.WHITE);
        clinicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        clinicLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(clinicLabel, BorderLayout.EAST);
        
        // Search Panel with centered components
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField searchField = new JTextField(40);
        searchField.setPreferredSize(new Dimension(400, 30));
        
        JButton sortButton = new JButton("Sort");
        styleButton(sortButton);
        
        // Create Button (only visible for ADMIN)
        JButton createButton = new JButton("Create +");
        createButton.setBackground(PRIMARY_COLOR);
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        createButton.setVisible("ADMIN".equals(userCredentials.getRole()));
        
        // Add Export to PDF button
        JButton exportPdfButton = new JButton("Export to PDF");
        styleButton(exportPdfButton);
        exportPdfButton.addActionListener(e -> DoctorPDFGenerator.generateDoctorReport());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(sortButton);
        searchPanel.add(createButton);
        searchPanel.add(exportPdfButton);
        
        // Add sort button functionality
        sortButton.addActionListener(e -> {
            String[] options = {"Staff ID", "First Name", "Last Name", "Date Of Birth", "Specialty", "Status"};
            String[] orderOptions = {"Ascending", "Descending"};
            
            JComboBox<String> columnBox = new JComboBox<>(options);
            JComboBox<String> orderBox = new JComboBox<>(orderOptions);
            
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Sort by:"));
            panel.add(columnBox);
            panel.add(new JLabel("Order:"));
            panel.add(orderBox);
            
            int result = JOptionPane.showConfirmDialog(
                this, panel, "Sort Staff Records",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String column = switch(columnBox.getSelectedItem().toString()) {
                    case "Staff ID" -> "staff_id";
                    case "First Name" -> "first_name";
                    case "Last Name" -> "last_name";
                    case "Date Of Birth" -> "date_of_birth";
                    case "Specialty" -> "specialty";
                    case "Status" -> "status";
                    default -> "staff_id";
                };
                
                boolean ascending = orderBox.getSelectedItem().toString().equals("Ascending");
                loadDoctorData(column, ascending);
            }
        });
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Initialize table with column names
        String[] columnNames = {
            "Staff ID", "First Name", "Last Name", "Date Of Birth",
            "Specialty", "Status", "Actions"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only allow editing the Actions column
            }
        };
        doctorTable = new JTable(tableModel);
        
        // Set table properties
        doctorTable.setRowHeight(40);
        doctorTable.setShowGrid(true);
        doctorTable.setGridColor(Color.LIGHT_GRAY);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < doctorTable.getColumnCount(); i++) {
            doctorTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set column widths
        doctorTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // Staff ID
        doctorTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // First Name
        doctorTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Last Name
        doctorTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Date of Birth
        doctorTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Specialty
        doctorTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // Status
        doctorTable.getColumnModel().getColumn(6).setPreferredWidth(120);  // Actions
        
        // Style header
        JTableHeader header = doctorTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // Set purple background for header cells
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                c.setBackground(PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        
        // Apply the custom header renderer to all columns
        for (int i = 0; i < doctorTable.getColumnCount(); i++) {
            doctorTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Custom button renderer for the actions column
        class ButtonRenderer extends JPanel implements TableCellRenderer {
            private final JButton viewButton;
            private final JButton deleteButton;
            
            public ButtonRenderer() {
                setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
                viewButton = new JButton("👁");
                deleteButton = new JButton("🗑");
                
                viewButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                deleteButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                
                add(viewButton);
                // Only show delete button for admin users
                if ("ADMIN".equals(userCredentials.getRole())) {
                    add(deleteButton);
                }
                setBackground(Color.WHITE);
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                return this;
            }
        }
        
        // Custom button editor for the actions column
        class ButtonEditor extends DefaultCellEditor {
            private final JPanel panel;
            private final JButton viewButton;
            private final JButton deleteButton;
            private int currentRow;
            private final JDialog loadingDialog;
            
            public ButtonEditor(JCheckBox checkBox) {
                super(checkBox);
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                viewButton = new JButton("👁");
                deleteButton = new JButton("🗑");
                
                viewButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                deleteButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                
                // Create loading dialog
                loadingDialog = new JDialog(Doctor_System.this, "Loading...", false);
                loadingDialog.setSize(200, 100);
                loadingDialog.setLocationRelativeTo(Doctor_System.this);
                JPanel loadingPanel = new JPanel(new BorderLayout(10, 10));
                loadingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                JLabel loadingLabel = new JLabel("Opening doctor record...");
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                loadingPanel.add(loadingLabel, BorderLayout.NORTH);
                loadingPanel.add(progressBar, BorderLayout.CENTER);
                loadingDialog.add(loadingPanel);
                
                viewButton.addActionListener(e -> {
                    int staffId = (int) doctorTable.getValueAt(currentRow, 0);
                    loadingDialog.setVisible(true);
                    
                    SwingWorker<Void, Void> worker = new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                            return null;
                        }
                        
                        @Override
                        protected void done() {
                            loadingDialog.setVisible(false);
                            new DoctorDetails(staffId, Doctor_System.this).setVisible(true);
                            setVisible(false);
                            fireEditingStopped();
                        }
                    };
                    worker.execute();
                });
                
                if ("ADMIN".equals(userCredentials.getRole())) {
                    deleteButton.addActionListener(e -> {
                        int staffId = (int) doctorTable.getValueAt(currentRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(
                            Doctor_System.this,
                            "Are you sure you want to delete this doctor?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            try (Connection conn = DatabaseConnection.getConnection();
                                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM doctors WHERE staff_id = ?")) {
                                stmt.setInt(1, staffId);
                                stmt.executeUpdate();
                                loadDoctorData();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(Doctor_System.this,
                                    "Error deleting doctor: " + ex.getMessage(),
                                    "Database Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        fireEditingStopped();
                    });
                }
                
                panel.add(viewButton);
                // Only show delete button for admin users
                if ("ADMIN".equals(userCredentials.getRole())) {
                    panel.add(deleteButton);
                }
                panel.setBackground(Color.WHITE);
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row;
                return panel;
            }
            
            @Override
            public Object getCellEditorValue() {
                return "Actions";
            }
        }
        
        // Add action buttons to the table
        TableColumn actionColumn = doctorTable.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tablePanel.add(scrollPane);
        
        // Add all panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        // Action listeners
        createButton.addActionListener(e -> {
            new DoctorDetails(-1, this).setVisible(true);
            dispose();
        });
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchDoctors(searchField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchDoctors(searchField.getText()); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchDoctors(searchField.getText()); }
        });
        
        add(mainPanel);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void searchDoctors(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadDoctorData(); // Load all doctors if search is empty
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "SELECT * FROM doctors WHERE " +
                               "LOWER(first_name) LIKE ? OR " +
                               "LOWER(last_name) LIKE ? OR " +
                               "LOWER(specialty) LIKE ? OR " +
                               "LOWER(CONVERT(staff_id, CHAR)) LIKE ? " +
                               "ORDER BY staff_id DESC";
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        String searchPattern = "%" + searchTerm.toLowerCase() + "%";
                        for (int i = 1; i <= 4; i++) {
                            pstmt.setString(i, searchPattern);
                        }
                        
                        // Store results in a list before updating UI
                        List<Object[]> results = new ArrayList<>();
                        try (ResultSet rs = pstmt.executeQuery()) {
                            while (rs.next()) {
                                results.add(new Object[]{
                                    rs.getInt("staff_id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getDate("date_of_birth"),
                                    rs.getString("specialty"),
                                    rs.getString("status"),
                                    "Actions"
                                });
                            }
                        }
                        
                        // Update UI with stored results
                        SwingUtilities.invokeLater(() -> {
                            tableModel.setRowCount(0);  // Clear existing rows
                            for (Object[] row : results) {
                                tableModel.addRow(row);
                            }
                        });
                    }
                } catch (SQLException ex) {
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(Doctor_System.this,
                            "Error searching doctors: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE)
                    );
                }
                return null;
            }
            
            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
            }
        };
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        worker.execute();
    }
    
    public void loadDoctorData() {
        loadDoctorData("staff_id", true);
    }
    
    public void loadDoctorData(String orderBy, boolean ascending) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM doctors ORDER BY " + orderBy + (ascending ? " ASC" : " DESC"))) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("staff_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("specialty"),
                        rs.getString("status"),
                        "Actions"
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading doctor data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Doctor_System(new UserCredentials(0, "defaultUser", "Default User", "USER_ROLE")).setVisible(true);
        });
    }
}
