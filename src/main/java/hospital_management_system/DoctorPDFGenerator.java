package hospital_management_system;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class DoctorPDFGenerator {
    public static void generateDoctorReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "Doctor_Report_" + timestamp + ".pdf";
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("PROCARE CLINIC - Staff Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);
            
            // Add timestamp
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph dateTime = new Paragraph(
                "Generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()),
                normalFont
            );
            dateTime.setAlignment(Element.ALIGN_RIGHT);
            dateTime.setSpacingAfter(20f);
            document.add(dateTime);
            
            // Create table
            PdfPTable table = new PdfPTable(7); // Number of columns
            table.setWidthPercentage(100);
            float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 2f, 1.5f}; // Proportional widths
            table.setWidths(columnWidths);
            
            // Add table headers
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            String[] headers = {"ID", "First Name", "Last Name", "Date of Birth", "Specialty", "Phone", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // Add data from database
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM doctors ORDER BY doctor_id")) {
                
                while (rs.next()) {
                    table.addCell(new Phrase(String.valueOf(rs.getInt("doctor_id")), normalFont));
                    table.addCell(new Phrase(rs.getString("first_name"), normalFont));
                    table.addCell(new Phrase(rs.getString("last_name"), normalFont));
                    Date dob = rs.getDate("date_of_birth");
                    table.addCell(new Phrase(
                        dob != null ? new SimpleDateFormat("dd-MM-yyyy").format(dob) : "",
                        normalFont
                    ));
                    table.addCell(new Phrase(rs.getString("specialty"), normalFont));
                    table.addCell(new Phrase(rs.getString("phone_number"), normalFont));
                    table.addCell(new Phrase(rs.getString("status"), normalFont));
                }
            }
            
            document.add(table);
            
            // Add footer
            Paragraph footer = new Paragraph("This is a system-generated report.", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20f);
            document.add(footer);
            
            document.close();
            
            JOptionPane.showMessageDialog(null, 
                "PDF report has been generated successfully: " + fileName,
                "Report Generated",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error generating PDF: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
