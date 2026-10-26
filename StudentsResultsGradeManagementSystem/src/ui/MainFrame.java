package ui;

import repository.*;
import service.ResultCalculator;
import ui.theme.H2ATheme;
import util.ApplicationConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Main application window styled with the award-winning agency aesthetics of https://www.h2a.lu/ .
 * Features:
 * - Ultra-sleek obsidian dark background
 * - Signature H2A dual-tone gradient active states (#FF0055 to #7B2CBF)
 * - Rounded pill navigation items with smooth hover interactions
 * - Dynamic CardLayout presentation
 */
public class MainFrame extends JFrame {

    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final MarksRepository marksRepo;
    private final ResultCalculator resultCalculator;

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;

    // Panels
    private DashboardPanel dashboardPanel;
    private StudentPanel studentPanel;
    private SubjectPanel subjectPanel;
    private MarksPanel marksPanel;
    private ResultsPanel resultsPanel;
    private ResultReportPanel resultReportPanel;

    // Sidebar buttons for highlighting
    private JButton[] navButtons;
    private int activeNavIndex = 0;

    private static final String[] NAV_LABELS = {
        "\u25C8  Dashboard",
        "\u25C7  Students",
        "\u25CE  Subjects",
        "\u270E  Marks Entry",
        "\u2605  Results",
        "\u2611  Report Card"
    };

    private static final String[] CARD_NAMES = {
        "dashboard", "students", "subjects", "marks", "results", "report"
    };

    public MainFrame() {
        // Initialize data layer
        CsvFileHandler csvHandler = new CsvFileHandler();
        csvHandler.ensureDirectoryExists(ApplicationConstants.DATA_DIR);

        studentRepo = new StudentRepository(csvHandler);
        subjectRepo = new SubjectRepository(csvHandler);
        marksRepo = new MarksRepository(csvHandler);

        // Load data from files
        subjectRepo.load();
        studentRepo.load();
        marksRepo.load();

        resultCalculator = new ResultCalculator(studentRepo, subjectRepo, marksRepo);

        // Configure frame
        setTitle(ApplicationConstants.APP_TITLE + " — Student OS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ApplicationConstants.MAIN_WINDOW_WIDTH, ApplicationConstants.MAIN_WINDOW_HEIGHT);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(H2ATheme.BG_DARK);

        // Build UI
        buildUI();
    }

    private void buildUI() {
        // Main layout
        setLayout(new BorderLayout());

        // Create sidebar
        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // Create content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(H2ATheme.BG_DARK);
        contentPanel.setOpaque(true);

        // Create panels
        dashboardPanel = new DashboardPanel(studentRepo, subjectRepo, marksRepo, resultCalculator);
        studentPanel = new StudentPanel(studentRepo, marksRepo);
        subjectPanel = new SubjectPanel(subjectRepo, marksRepo);
        marksPanel = new MarksPanel(studentRepo, subjectRepo, marksRepo);
        resultsPanel = new ResultsPanel(resultCalculator);
        resultReportPanel = new ResultReportPanel(studentRepo, resultCalculator);

        contentPanel.add(dashboardPanel, CARD_NAMES[0]);
        contentPanel.add(studentPanel, CARD_NAMES[1]);
        contentPanel.add(subjectPanel, CARD_NAMES[2]);
        contentPanel.add(marksPanel, CARD_NAMES[3]);
        contentPanel.add(resultsPanel, CARD_NAMES[4]);
        contentPanel.add(resultReportPanel, CARD_NAMES[5]);

        add(contentPanel, BorderLayout.CENTER);

        // Show dashboard by default
        cardLayout.show(contentPanel, CARD_NAMES[0]);
        highlightNavButton(0);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(H2ATheme.BG_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Right border line
                g2.setColor(new Color(255, 255, 255, 15));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());

                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Brand badge in sidebar
        JPanel brandPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 30;
                int h = 48;
                // Flat editorial brand block.
                g2.setColor(H2ATheme.GRADIENT_MAGENTA);
                g2.fill(new RoundRectangle2D.Float(15, 18, w, h, 14, 14));

                g2.setColor(new Color(255, 255, 255, 40));
                g2.draw(new RoundRectangle2D.Float(15, 18, w, h, 14, 14));

                g2.dispose();
                super.paintComponent(g);
            }
        };
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(24, 28, 20, 20));
        brandPanel.setMaximumSize(new Dimension(240, 85));

        JLabel brandLogo = new JLabel("STUDENT OS  \u2022  JAVA");
        brandLogo.setForeground(Color.WHITE);
        brandLogo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brandLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandLogo);

        JLabel brandSub = new JLabel("Results & Grade Management");
        brandSub.setForeground(new Color(255, 255, 255, 180));
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandPanel.add(brandSub);

        sidebar.add(brandPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // Navigation section label
        JLabel navSectionLabel = new JLabel("  WORKSPACE");
        navSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navSectionLabel.setForeground(new Color(170, 166, 156));
        navSectionLabel.setBorder(new EmptyBorder(0, 22, 6, 0));
        sidebar.add(navSectionLabel);

        // Navigation buttons
        navButtons = new JButton[NAV_LABELS.length];
        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int index = i;
            JButton btn = createNavButton(NAV_LABELS[i], index);
            btn.addActionListener(e -> {
                cardLayout.show(contentPanel, CARD_NAMES[index]);
                highlightNavButton(index);
                refreshPanel(index);
            });
            navButtons[i] = btn;
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Exit button at bottom
        JButton exitBtn = new JButton("\u2716  Exit Application") {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 30;
                int h = getHeight();

                g2.setColor(isHovered ? new Color(185, 47, 47) : new Color(143, 37, 37));
                g2.fill(new RoundRectangle2D.Float(15, 0, w, h, 20, 20));
                g2.setColor(new Color(255, 255, 255, 150));
                g2.draw(new RoundRectangle2D.Float(15, 0, w, h, 20, 20));

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = 15 + (w - fm.stringWidth(getText())) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        exitBtn.setFont(H2ATheme.FONT_BODY_BOLD);
        exitBtn.setContentAreaFilled(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setFocusPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.setMaximumSize(new Dimension(240, 38));
        exitBtn.setPreferredSize(new Dimension(240, 38));

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit the application?",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        sidebar.add(exitBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 14)));

        // Version & branding footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footerPanel.setOpaque(false);
        footerPanel.setMaximumSize(new Dimension(240, 30));

        JLabel versionLabel = new JLabel("v" + ApplicationConstants.APP_VERSION + " \u2022 Java Swing \u2022 Local CSV");
        versionLabel.setForeground(new Color(145, 141, 132));
        versionLabel.setFont(H2ATheme.FONT_BODY_SM);
        footerPanel.add(versionLabel);
        sidebar.add(footerPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        return sidebar;
    }

    private JButton createNavButton(String text, int buttonIndex) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth() - 28;
                int h = getHeight();
                int x = 14;

                boolean isActive = (buttonIndex == activeNavIndex);

                if (isActive) {
                    // Flat accent block for the active workspace.
                    g2.setColor(H2ATheme.GRADIENT_MAGENTA);
                    g2.fill(new RoundRectangle2D.Float(x, 0, w, h, 20, 20));

                    // Glowing border
                    g2.setColor(new Color(255, 255, 255, 60));
                    g2.draw(new RoundRectangle2D.Float(x, 0, w, h, 20, 20));
                } else if (isHovered) {
                    // Quiet warm hover state.
                    g2.setColor(new Color(255, 255, 255, 24));
                    g2.fill(new RoundRectangle2D.Float(x, 0, w, h, 20, 20));
                    g2.setColor(new Color(255, 255, 255, 25));
                    g2.draw(new RoundRectangle2D.Float(x, 0, w, h, 20, 20));
                }

                // Text rendering
                g2.setFont(isActive ? H2ATheme.FONT_BODY_BOLD : H2ATheme.FONT_BODY);
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + 18;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(isActive ? Color.WHITE : (isHovered ? Color.WHITE : new Color(205, 201, 191)));
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        btn.setFont(H2ATheme.FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 42));
        btn.setPreferredSize(new Dimension(240, 42));
        return btn;
    }

    private void highlightNavButton(int activeIndex) {
        this.activeNavIndex = activeIndex;
        for (JButton btn : navButtons) {
            btn.repaint();
        }
    }

    /**
     * Refreshes the target panel when navigating to it.
     */
    private void refreshPanel(int index) {
        switch (index) {
            case 0: dashboardPanel.refresh(); break;
            case 1: studentPanel.refresh(); break;
            case 2: subjectPanel.refresh(); break;
            case 3: marksPanel.refresh(); break;
            case 4: resultsPanel.refresh(); break;
            case 5: resultReportPanel.refresh(); break;
        }
    }
}
