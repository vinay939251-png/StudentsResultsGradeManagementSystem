package ui;

import repository.*;
import service.ResultCalculator;
import ui.theme.H2ATheme;
import util.ApplicationConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Dashboard panel displaying real-time academic statistics and performance metrics.
 * Styled with the luxury dark and vibrant gradient aesthetic of https://www.h2a.lu/ .
 */
public class DashboardPanel extends JPanel {

    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final MarksRepository marksRepo;
    private final ResultCalculator resultCalculator;

    private JLabel totalStudentsLabel;
    private JLabel totalSubjectsLabel;
    private JLabel passedLabel;
    private JLabel failedLabel;
    private JLabel passPercentLabel;
    private JProgressBar passProgressBar;

    public DashboardPanel(StudentRepository studentRepo, SubjectRepository subjectRepo,
                          MarksRepository marksRepo, ResultCalculator resultCalculator) {
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.marksRepo = marksRepo;
        this.resultCalculator = resultCalculator;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("The signal, at a glance",
                "A focused overview of enrollment, curriculum, and academic outcomes");
        add(headerPanel, BorderLayout.NORTH);

        // Scrollable content area
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(H2ATheme.BG_DARK);
        contentContainer.setBorder(new EmptyBorder(26, 30, 26, 30));

        // Row 1: Top 4 KPI Metric Cards
        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 18, 0));
        metricsGrid.setOpaque(false);
        metricsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        metricsGrid.setPreferredSize(new Dimension(800, 130));

        totalStudentsLabel = new JLabel("0");
        metricsGrid.add(createMetricCard("Students", totalStudentsLabel, H2ATheme.TEXT_PRIMARY, "\u25C7", "Registered profiles"));

        totalSubjectsLabel = new JLabel("0");
        metricsGrid.add(createMetricCard("Subjects", totalSubjectsLabel, H2ATheme.ACCENT_CYAN, "\u25CE", "Configured curriculum"));

        passedLabel = new JLabel("0");
        metricsGrid.add(createMetricCard("Passed", passedLabel, H2ATheme.STATUS_PASS, "\u2714", "All minimums met"));

        failedLabel = new JLabel("0");
        metricsGrid.add(createMetricCard("Needs review", failedLabel, H2ATheme.STATUS_FAIL, "\u2718", "One or more subjects failed"));

        contentContainer.add(metricsGrid);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 22)));

        // Row 2: Pass Rate Meter Card & Grading Policy Card
        JPanel middleRow = new JPanel(new GridLayout(1, 2, 18, 0));
        middleRow.setOpaque(false);
        middleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        middleRow.setPreferredSize(new Dimension(800, 240));

        // Pass Percentage Card with custom painted gradient progress bar
        JPanel passCard = H2ATheme.createCardPanel(18);
        passCard.setLayout(new BorderLayout(0, 14));

        JLabel passCardTitle = new JLabel("Class passing rate");
        passCardTitle.setFont(H2ATheme.FONT_TITLE);
        passCardTitle.setForeground(H2ATheme.TEXT_PRIMARY);
        passCard.add(passCardTitle, BorderLayout.NORTH);

        JPanel passCenterPanel = new JPanel();
        passCenterPanel.setOpaque(false);
        passCenterPanel.setLayout(new BoxLayout(passCenterPanel, BoxLayout.Y_AXIS));

        passPercentLabel = new JLabel("0.0%");
        passPercentLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        passPercentLabel.setForeground(H2ATheme.GRADIENT_MAGENTA);
        passCenterPanel.add(passPercentLabel);

        JLabel passSub = new JLabel("Completed results only. Incomplete records remain visible in Results.");
        passSub.setFont(H2ATheme.FONT_BODY);
        passSub.setForeground(H2ATheme.TEXT_SECONDARY);
        passSub.setBorder(new EmptyBorder(4, 0, 14, 0));
        passCenterPanel.add(passSub);

        // Custom Gradient Progress Bar
        passProgressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Track background
                g2.setColor(new Color(0xE2, 0xDD, 0xD2));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, h, h));

                // Progress fill
                int progressW = (int) (w * (getPercentComplete()));
                if (progressW > 0) {
                    GradientPaint gp = new GradientPaint(
                            0, 0, H2ATheme.GRADIENT_MAGENTA,
                            w, 0, H2ATheme.GRADIENT_PURPLE
                    );
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Float(0, 0, progressW, h, h, h));
                }

                // Subtle outline
                g2.setColor(new Color(255, 255, 255, 30));
                g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, h, h));

                g2.dispose();
            }
        };
        passProgressBar.setPreferredSize(new Dimension(300, 14));
        passProgressBar.setOpaque(false);
        passProgressBar.setBorderPainted(false);
        passCenterPanel.add(passProgressBar);

        passCard.add(passCenterPanel, BorderLayout.CENTER);
        middleRow.add(passCard);

        // Grading Scale Card
        JPanel scaleCard = H2ATheme.createCardPanel(18);
        scaleCard.setLayout(new BorderLayout(0, 10));

        JLabel scaleTitle = new JLabel("Grading scale");
        scaleTitle.setFont(H2ATheme.FONT_TITLE);
        scaleTitle.setForeground(H2ATheme.TEXT_PRIMARY);
        scaleCard.add(scaleTitle, BorderLayout.NORTH);

        JPanel scaleGrid = new JPanel(new GridLayout(4, 2, 10, 6));
        scaleGrid.setOpaque(false);

        scaleGrid.add(createScaleItem("A+ (Distinction)", ">= 90%", H2ATheme.ACCENT_CYAN));
        scaleGrid.add(createScaleItem("A (Excellent)", ">= 80%", H2ATheme.GRADIENT_MAGENTA));
        scaleGrid.add(createScaleItem("B (Very Good)", ">= 70%", H2ATheme.ACCENT_VIOLET));
        scaleGrid.add(createScaleItem("C (Good)", ">= 60%", H2ATheme.TEXT_SECONDARY));
        scaleGrid.add(createScaleItem("D (Average)", ">= 50%", H2ATheme.TEXT_SECONDARY));
        scaleGrid.add(createScaleItem("E (Passing)", ">= 35%", H2ATheme.STATUS_WARN));
        scaleGrid.add(createScaleItem("F (Fail)", "< 35%", H2ATheme.STATUS_FAIL));
        scaleGrid.add(createScaleItem("Passing Rule", "All subjects >= 35", H2ATheme.STATUS_PASS));

        scaleCard.add(scaleGrid, BorderLayout.CENTER);

        JLabel dataInfo = new JLabel("\u25CF Data saved in local CSV storage \u2022 Changes are auto-persisted");
        dataInfo.setFont(H2ATheme.FONT_BODY_SM);
        dataInfo.setForeground(H2ATheme.TEXT_MUTED);
        scaleCard.add(dataInfo, BorderLayout.SOUTH);

        middleRow.add(scaleCard);
        contentContainer.add(middleRow);

        add(contentContainer, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor, String icon, String footerText) {
        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 6));

        // Top: title + icon
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(H2ATheme.FONT_BODY_BOLD);
        titleLabel.setForeground(H2ATheme.TEXT_SECONDARY);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        iconLabel.setForeground(accentColor);
        topPanel.add(iconLabel, BorderLayout.EAST);

        card.add(topPanel, BorderLayout.NORTH);

        // Center: large value
        valueLabel.setFont(H2ATheme.FONT_METRIC);
        valueLabel.setForeground(accentColor);
        card.add(valueLabel, BorderLayout.CENTER);

        // Bottom: footer note
        JLabel footerLabel = new JLabel(footerText);
        footerLabel.setFont(H2ATheme.FONT_BODY_SM);
        footerLabel.setForeground(H2ATheme.TEXT_MUTED);
        card.add(footerLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createScaleItem(String gradeName, String criteria, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel nameLbl = new JLabel(gradeName);
        nameLbl.setFont(H2ATheme.FONT_BODY_SM);
        nameLbl.setForeground(H2ATheme.TEXT_PRIMARY);

        JLabel critLbl = new JLabel(criteria);
        critLbl.setFont(H2ATheme.FONT_BODY_BOLD);
        critLbl.setForeground(color);

        p.add(nameLbl, BorderLayout.WEST);
        p.add(critLbl, BorderLayout.EAST);
        return p;
    }

    public void refresh() {
        int studentCount = studentRepo.getStudentCount();
        int subjectCount = subjectRepo.getSubjectCount();
        int passCount = resultCalculator.getPassedCount();
        int failCount = resultCalculator.getFailedCount();
        double passPercentage = resultCalculator.getClassPassPercentage();

        totalStudentsLabel.setText(String.valueOf(studentCount));
        totalSubjectsLabel.setText(String.valueOf(subjectCount));
        passedLabel.setText(String.valueOf(passCount));
        failedLabel.setText(String.valueOf(failCount));
        passPercentLabel.setText(String.format("%.1f%%", passPercentage));
        passProgressBar.setValue((int) Math.round(passPercentage));
    }
}
