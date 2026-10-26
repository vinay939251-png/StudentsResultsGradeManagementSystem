import ui.MainFrame;
import util.ApplicationConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for the Students Results and Grade Management System.
 * Launches the Swing GUI application with system look-and-feel.
 *
 * Usage:
 *   javac -d out -sourcepath src src/Main.java
 *   java -cp out Main
 */
public class Main {

    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Improve font rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            // Set default font for a cleaner look
            setUIFont(new javax.swing.plaf.FontUIResource(
                    new Font("SansSerif", Font.PLAIN, 13)));

        } catch (Exception e) {
            System.err.println("[WARNING] Could not set system look and feel: " + e.getMessage());
        }

        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("====================================================");
                System.out.println("  " + ApplicationConstants.APP_TITLE);
                System.out.println("  Version " + ApplicationConstants.APP_VERSION);
                System.out.println("====================================================");
                System.out.println("[INFO] Starting application...");

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);

                System.out.println("[INFO] Application started successfully.");

            } catch (Exception e) {
                System.err.println("[FATAL] Application failed to start: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "The application failed to start.\n\nError: " + e.getMessage(),
                        "Fatal Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    /**
     * Sets the default font for all Swing components.
     */
    private static void setUIFont(javax.swing.plaf.FontUIResource font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
