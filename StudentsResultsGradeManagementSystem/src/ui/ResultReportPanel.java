package ui;

import model.Student;
import model.StudentResult;
import repository.StudentRepository;
import service.GradeCalculator;
import service.ResultCalculator;
import ui.theme.H2ATheme;
import util.ApplicationConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Individual student result report card panel.
 * Styled like an official, luxury academic transcript inspired by https://www.h2a.lu/ .
 */
public class ResultReportPanel extends JPanel {

    private final StudentRepository studentRepo;
    private final ResultCalculator resultCalculator;

    private JComboBox<String> studentCombo;
    private JPanel reportContainer;
    private JLabel emptyLabel;

    public ResultReportPanel(StudentRepository studentRepo, ResultCalculator resultCalculator) {
        this.studentRepo = studentRepo;
        this.resultCalculator = resultCalculator;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("Student Transcript & Report Card",
                "Generate authenticated individual academic transcripts, course grades, and merit status");
        add(headerPanel, BorderLayout.NORTH);

        // Center Wrapper
        JPanel centerWrapper = new JPanel(new BorderLayout(0, 16));
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(22, 28, 22, 28));

        // Selector Toolbar
        JPanel toolbarCard = H2ATheme.createCardPanel(16);
        toolbarCard.setLayout(new BorderLayout(14, 0));

        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftBox.setOpaque(false);

        JLabel selectLabel = new JLabel("Select Student:");
        selectLabel.setFont(H2ATheme.FONT_BODY_BOLD);
        selectLabel.setForeground(H2ATheme.TEXT_SECONDARY);
        leftBox.add(selectLabel);

        studentCombo = new JComboBox<>();
        studentCombo.setFont(H2ATheme.FONT_BODY);
        studentCombo.setBackground(H2ATheme.BG_INPUT);
        studentCombo.setForeground(H2ATheme.TEXT_PRIMARY);
        studentCombo.setPreferredSize(new Dimension(420, 36));
        studentCombo.setBorder(BorderFactory.createLineBorder(H2ATheme.BORDER_INPUT, 1));
        studentCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? H2ATheme.GRADIENT_PURPLE : H2ATheme.BG_INPUT);
                setForeground(H2ATheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });
        leftBox.add(studentCombo);
        toolbarCard.add(leftBox, BorderLayout.WEST);

        JButton viewBtn = H2ATheme.createGradientButton("\u25B6 Generate Transcript");
        viewBtn.setPreferredSize(new Dimension(200, 36));
        viewBtn.addActionListener(e -> showReport());
        toolbarCard.add(viewBtn, BorderLayout.EAST);

        centerWrapper.add(toolbarCard, BorderLayout.NORTH);

        // Report display container
        reportContainer = new JPanel(new BorderLayout());
        reportContainer.setOpaque(false);

        emptyLabel = new JLabel("<html><center>\u25C8<br><br>Select a student from the dropdown above and click<br><b>'Generate Transcript'</b> to view their comprehensive academic report card.</center></html>", SwingConstants.CENTER);
        emptyLabel.setFont(H2ATheme.FONT_BODY);
        emptyLabel.setForeground(H2ATheme.TEXT_MUTED);
        reportContainer.add(emptyLabel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(reportContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private void showReport() {
        String selected = (String) studentCombo.getSelectedItem();
        if (selected == null || selected.equals("-- Select Student --")) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student from the dropdown.",
                    "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = selected.split(" - ")[0].trim();
        StudentResult result = resultCalculator.calculateStudentResult(studentId);

        if (result == null) {
            JOptionPane.showMessageDialog(this,
                    "Student record not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayResultCard(result);
    }

    private void displayResultCard(StudentResult result) {
        reportContainer.removeAll();

        JPanel card = H2ATheme.createCardPanel(20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 34, 26, 34));

        // ──── Banner Header ────
        JPanel bannerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, H2ATheme.GRADIENT_WINE,
                        w, 0, H2ATheme.GRADIENT_PURPLE
                );
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));

                g2.setColor(new Color(255, 255, 255, 40));
                g2.draw(new RoundRectangle2D.Float(0, 0, w, h, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bannerPanel.setOpaque(false);
        bannerPanel.setLayout(new BorderLayout());
        bannerPanel.setBorder(new EmptyBorder(16, 24, 16, 24));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel bannerTitle = new JLabel("OFFICIAL ACADEMIC TRANSCRIPT REPORT");
        bannerTitle.setFont(H2ATheme.FONT_TITLE);
        bannerTitle.setForeground(Color.WHITE);
        bannerPanel.add(bannerTitle, BorderLayout.WEST);

        JLabel authLabel = new JLabel("AUTHENTICATED RECORD \u2022 H2A OS");
        authLabel.setFont(H2ATheme.FONT_BODY_SM);
        authLabel.setForeground(new Color(255, 255, 255, 200));
        bannerPanel.add(authLabel, BorderLayout.EAST);

        card.add(bannerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // ──── Student Information Chips ────
        Student student = result.getStudent();
        JPanel infoGrid = new JPanel(new GridLayout(2, 3, 14, 10));
        infoGrid.setOpaque(false);
        infoGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        infoGrid.add(createDetailItem("STUDENT ID", student.getStudentId()));
        infoGrid.add(createDetailItem("ROLL NUMBER", student.getRollNumber()));
        infoGrid.add(createDetailItem("FULL NAME", student.getFullName()));
        infoGrid.add(createDetailItem("DEPARTMENT", student.getDepartment()));
        infoGrid.add(createDetailItem("SEMESTER", student.getSemester()));
        infoGrid.add(createDetailItem("EMAIL", student.getEmail().isEmpty() ? "-" : student.getEmail()));

        card.add(infoGrid);
        card.add(Box.createRigidArea(new Dimension(0, 22)));

        // ──── Subject-wise Marks Table ────
        JLabel sectionTitle = new JLabel("Curriculum Subject Marks Breakdown");
        sectionTitle.setFont(H2ATheme.FONT_BODY_BOLD);
        sectionTitle.setForeground(H2ATheme.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sectionTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] columns = {"Subject Code", "Subject Name", "Max Marks", "Passing Threshold", "Marks Obtained", "Status"};
        DefaultTableModel subjectTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (StudentResult.SubjectResult sr : result.getSubjectResults()) {
            subjectTableModel.addRow(new Object[]{
                    sr.getSubjectCode(),
                    sr.getSubjectName(),
                    (int) sr.getMaxMarks(),
                    (int) sr.getMinPassingMarks(),
                    sr.getMarksObtained(),
                    sr.isPassed() ? "PASS" : "FAIL"
            });
        }

        JTable subjectTable = new JTable(subjectTableModel);
        H2ATheme.stylizeTable(subjectTable);

        JScrollPane tableScroll = H2ATheme.createScrollPane(subjectTable);
        int tableHeight = Math.min(38 * (subjectTableModel.getRowCount() + 1) + 8, 220);
        tableScroll.setPreferredSize(new Dimension(700, tableHeight));
        tableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, tableHeight));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(tableScroll);

        // Missing Subjects Notice
        if (!result.getMissingSubjects().isEmpty()) {
            card.add(Box.createRigidArea(new Dimension(0, 12)));
            JLabel missingLabel = new JLabel("\u26A0 Incomplete Evaluation: Missing marks for " +
                    String.join(", ", result.getMissingSubjects()));
            missingLabel.setFont(H2ATheme.FONT_BODY_BOLD);
            missingLabel.setForeground(H2ATheme.STATUS_WARN);
            missingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(missingLabel);
        }

        card.add(Box.createRigidArea(new Dimension(0, 22)));

        // ──── Summary KPI Cards ────
        JPanel summaryRow = new JPanel(new GridLayout(1, 4, 14, 0));
        summaryRow.setOpaque(false);
        summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        boolean isIncomplete = ApplicationConstants.RESULT_INCOMPLETE.equals(result.getResultStatus());

        summaryRow.add(createSummaryChip("TOTAL MARKS",
                String.format("%.0f / %.0f", result.getTotalMarks(), result.getMaxPossibleMarks()),
                H2ATheme.TEXT_PRIMARY));

        summaryRow.add(createSummaryChip("PERCENTAGE",
                isIncomplete ? "N/A" : String.format("%.2f%%", result.getPercentage()),
                H2ATheme.ACCENT_CYAN));

        summaryRow.add(createSummaryChip("FINAL GRADE",
                isIncomplete ? "INC" : result.getGrade() + " (" + GradeCalculator.getGradeDescription(result.getGrade()) + ")",
                H2ATheme.GRADIENT_MAGENTA));

        Color statusColor = H2ATheme.STATUS_PASS;
        if (ApplicationConstants.RESULT_FAIL.equals(result.getResultStatus())) {
            statusColor = H2ATheme.STATUS_FAIL;
        } else if (isIncomplete) {
            statusColor = H2ATheme.STATUS_WARN;
        }

        summaryRow.add(createSummaryChip("OVERALL RESULT", result.getResultStatus(), statusColor));
        card.add(summaryRow);

        // Failed subjects detail note
        if (!result.getFailedSubjects().isEmpty()) {
            card.add(Box.createRigidArea(new Dimension(0, 14)));
            JLabel failedDetail = new JLabel("\u2718 Failed Courses: " +
                    String.join(", ", result.getFailedSubjects()) +
                    " (Passing requires scoring >= minimum passing mark in every subject)");
            failedDetail.setFont(H2ATheme.FONT_BODY_SM);
            failedDetail.setForeground(H2ATheme.STATUS_FAIL);
            failedDetail.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(failedDetail);
        }

        reportContainer.add(card, BorderLayout.NORTH);
        reportContainer.revalidate();
        reportContainer.repaint();
    }

    private JPanel createDetailItem(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        p.setBackground(H2ATheme.BG_INPUT);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(H2ATheme.BORDER_SUBTLE, 1),
                new EmptyBorder(6, 12, 6, 12)
        ));

        JLabel l = new JLabel(label);
        l.setFont(H2ATheme.FONT_BODY_SM);
        l.setForeground(H2ATheme.TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(H2ATheme.FONT_BODY_BOLD);
        v.setForeground(H2ATheme.TEXT_PRIMARY);

        p.add(l, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    private JPanel createSummaryChip(String label, String value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(true);
        p.setBackground(H2ATheme.BG_INPUT);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60), 1),
                new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel l = new JLabel(label);
        l.setFont(H2ATheme.FONT_BODY_SM);
        l.setForeground(H2ATheme.TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 16));
        v.setForeground(color);

        p.add(l, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    public void refresh() {
        studentCombo.removeAllItems();
        studentCombo.addItem("-- Select Student --");
        for (Student s : studentRepo.getAllStudents()) {
            studentCombo.addItem(s.getStudentId() + " - " + s.getFullName() + " (" + s.getRollNumber() + ")");
        }
    }
}
