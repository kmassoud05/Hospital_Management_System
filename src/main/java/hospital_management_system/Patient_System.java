package hospital_management_system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Patient_System extends JFrame {
    private final Color PRIMARY_COLOR = new Color(120, 32, 110);
    private JTable patientTable;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private final UserCredentials userCredentials;
    
    public Patient_System(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        // Initialize database
        new DatabaseOperations();
        
        initializeUI();
        loadPatientData();
    }
    
    public Patient_System() {
        this.userCredentials = new UserCredentials("Admin"); // Default UserCredentials initialization
        // Initialize database
        new DatabaseOperations();
        initializeUI();
        loadPatientData();
    }
    
    private void initializeUI() {
        setTitle("Patient Record Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        // Back button
        JButton backButton = new JButton("← BACK");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            dispose();
            new Home_Page(userCredentials).setVisible(true);
        });
        
        // Title
        JLabel titleLabel = new JLabel("Patient Record Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Clinic name
        JLabel clinicLabel = new JLabel("PROCARE CLINIC");
        clinicLabel.setForeground(Color.WHITE);
        clinicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        clinicLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(clinicLabel, BorderLayout.EAST);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 30));
        
        JButton sortButton = new JButton("Sort");
        styleButton(sortButton);
        
        JButton createButton = new JButton("Create +");
        styleButton(createButton);
        createButton.setVisible(!"DOCTOR".equals(userCredentials.getRole()));
        
        JButton exportPdfButton = new JButton("Export to PDF");
        styleButton(exportPdfButton);  // Apply the same style as other buttons
        exportPdfButton.addActionListener(e -> PDFGenerator.generatePatientReport());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(sortButton);
        searchPanel.add(createButton);
        searchPanel.add(exportPdfButton);
        
        // Add sort button functionality
        sortButton.addActionListener(e -> {
            String[] options = {"Patient ID", "First Name", "Last Name", "Date Of Birth", "Gender", "Phone No#"};
            String[] orderOptions = {"Ascending", "Descending"};
            
            JComboBox<String> columnBox = new JComboBox<>(options);
            JComboBox<String> orderBox = new JComboBox<>(orderOptions);
            
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Sort by:"));
            panel.add(columnBox);
            panel.add(new JLabel("Order:"));
            panel.add(orderBox);
            
            int result = JOptionPane.showConfirmDialog(
                this, panel, "Sort Patients",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String column = switch(columnBox.getSelectedItem().toString()) {
                    case "Patient ID" -> "patient_id";
                    case "First Name" -> "first_name";
                    case "Last Name" -> "last_name";
                    case "Date Of Birth" -> "date_of_birth";
                    case "Gender" -> "gender";
                    case "Phone No#" -> "phone_number";
                    default -> "patient_id";
                };
                
                boolean ascending = orderBox.getSelectedItem().toString().equals("Ascending");
                loadPatientData(column, ascending);
            }
        });
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Table
        String[] columns = {"Patient ID", "First Name", "Last Name", "Date Of Birth",
                          "Gender", "Phone No#", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(35);
        patientTable.setShowGrid(true);
        patientTable.setGridColor(Color.LIGHT_GRAY);
        patientTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Style header
        JTableHeader header = patientTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                label.setBackground(PRIMARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                label.setOpaque(true);
                return label;
            }
        });
        
        // Set column widths
        int[] columnWidths = {80, 150, 150, 120, 80, 120, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = patientTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
            column.setMinWidth(columnWidths[i]);
        }
        
        // Add action buttons to the table
        TableColumn actionColumn = patientTable.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        // Add action listeners
        createButton.addActionListener(e -> {
            PatientDetails patientDetails = new PatientDetails(-1, this);
            patientDetails.setVisible(true);
            setVisible(false);
        });
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchPatients(searchField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchPatients(searchField.getText()); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchPatients(searchField.getText()); }
        });
        
        add(mainPanel);
    }
    
    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    }
    
    private void searchPatients(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadPatientData(); // Load all patients if search is empty
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String sql = "SELECT * FROM patients WHERE " +
                               "LOWER(first_name) LIKE ? OR " +
                               "LOWER(last_name) LIKE ? OR " +
                               "LOWER(phone_number) LIKE ? OR " +
                               "LOWER(CONVERT(patient_id, CHAR)) LIKE ? " +
                               "ORDER BY patient_id DESC";
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        String searchPattern = "%" + searchTerm.toLowerCase() + "%";
                        for (int i = 1; i <= 4; i++) {
                            pstmt.setString(i, searchPattern);
                        }
                        
                        try (ResultSet rs = pstmt.executeQuery()) {
                            DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
                            model.setRowCount(0);
                            
                            while (rs.next()) {
                                model.addRow(new Object[]{
                                    rs.getInt("patient_id"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name"),
                                    rs.getDate("date_of_birth"),
                                    rs.getString("gender"),
                                    rs.getString("phone_number"),
                                    "Actions"
                                });
                            }
                        }
                    }
                } catch (SQLException ex) {
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(Patient_System.this,
                            "Error searching patients: " + ex.getMessage(),
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

    public void loadPatientData() {
        try {
            // Clear existing table data
            tableModel.setRowCount(0);
            
            ResultSet rs = DatabaseOperations.getAllPatients();
            
            if (rs != null) {
                while (rs.next()) {
                    Object[] rowData = {
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("date_of_birth"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        null // Action column
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading patient data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadPatientData(String orderBy, boolean ascending) {
        try {
            // Clear existing table data
            tableModel.setRowCount(0);
            
            ResultSet rs = DatabaseOperations.getAllPatientsSorted(orderBy, ascending);
            
            if (rs != null) {
                while (rs.next()) {
                    Object[] rowData = {
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("date_of_birth"),
                        rs.getString("gender"),
                        rs.getString("phone_number"),
                        null // Action column
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading patient data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
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

    private class ButtonEditor extends DefaultCellEditor {
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
            loadingDialog = new JDialog(Patient_System.this, "Loading...", false);
            loadingDialog.setSize(200, 100);
            loadingDialog.setLocationRelativeTo(Patient_System.this);
            JPanel loadingPanel = new JPanel(new BorderLayout(10, 10));
            loadingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            JLabel loadingLabel = new JLabel("Opening patient record...");
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            loadingPanel.add(loadingLabel, BorderLayout.NORTH);
            loadingPanel.add(progressBar, BorderLayout.CENTER);
            loadingDialog.add(loadingPanel);
            
            viewButton.addActionListener(e -> {
                int patientId = (int) patientTable.getValueAt(currentRow, 0);
                loadingDialog.setVisible(true);
                
                // Use SwingWorker to load patient details in background
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
                        new PatientDetails(patientId, Patient_System.this).setVisible(true);
                        setVisible(false);
                        fireEditingStopped();
                    }
                };
                worker.execute();
            });
            
            if ("ADMIN".equals(userCredentials.getRole())) {
                deleteButton.addActionListener(e -> {
                    int patientId = (int) patientTable.getValueAt(currentRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                        Patient_System.this,
                        "Are you sure you want to delete this patient?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection conn = DatabaseConnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement("DELETE FROM patients WHERE patient_id = ?")) {
                            stmt.setInt(1, patientId);
                            stmt.executeUpdate();
                            loadPatientData();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Patient_System.this,
                                "Error deleting patient: " + ex.getMessage(),
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
            return null;
        }
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Patient_System(new UserCredentials("Admin")).setVisible(true);
        });
    }
}
