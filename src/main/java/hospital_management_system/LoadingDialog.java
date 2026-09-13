package hospital_management_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingDialog extends JDialog {
    private final JLabel loadingLabel;
    private final Timer dotTimer;
    private int dotCount = 0;
    private final Color PRIMARY_COLOR = new Color(120, 32, 110); // #78206E

    public LoadingDialog(JFrame parent, String message) {
        super(parent, true);
        setUndecorated(true); // Remove window decorations
        
        // Create main panel with shadow border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Create loading icon (spinner)
        JLabel spinnerLabel = new JLabel("🔄");
        spinnerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        spinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        spinnerLabel.setForeground(PRIMARY_COLOR);

        // Animate the spinner
        Timer spinnerTimer = new Timer(100, new ActionListener() {
            private int angle = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                angle = (angle + 30) % 360;
                spinnerLabel.setIcon(new RotatedIcon(spinnerLabel.getText(), angle));
                repaint();
            }
        });
        spinnerTimer.start();

        // Create loading text
        loadingLabel = new JLabel(message);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loadingLabel.setForeground(PRIMARY_COLOR);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Animate the dots
        dotTimer = new Timer(500, e -> {
            dotCount = (dotCount + 1) % 4;
            StringBuilder dots = new StringBuilder();
            for (int i = 0; i < dotCount; i++) {
                dots.append(".");
            }
            loadingLabel.setText(message + dots.toString());
        });
        dotTimer.start();

        // Layout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(5, 5, 5, 5);

        contentPanel.add(spinnerLabel, gbc);
        contentPanel.add(loadingLabel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        pack();
        setSize(getWidth() + 50, getHeight() + 20);
        setLocationRelativeTo(parent);
    }

    public void stopAnimation() {
        dotTimer.stop();
        dispose();
    }

    // Custom icon for rotation
    private class RotatedIcon implements Icon {
        private final String text;
        private final int angle;

        public RotatedIcon(String text, int angle) {
            this.text = text;
            this.angle = angle;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = x + getIconWidth() / 2;
            int centerY = y + getIconHeight() / 2;
            
            g2.rotate(Math.toRadians(angle), centerX, centerY);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            g2.setColor(PRIMARY_COLOR);
            
            FontMetrics fm = g2.getFontMetrics();
            int textX = centerX - fm.stringWidth(text) / 2;
            int textY = centerY + fm.getAscent() / 2;
            
            g2.drawString(text, textX, textY);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 30;
        }

        @Override
        public int getIconHeight() {
            return 30;
        }
    }
}
