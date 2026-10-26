package ui;

import model.MarkRecord;
import model.Student;
import model.Subject;
import repository.MarksRepository;
import repository.StudentRepository;
import repository.SubjectRepository;
import ui.theme.H2ATheme;
import util.InputValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Examination marks entry panel.
 * Styled with the luxury dark aesthetic of https://www.h2a.lu/ .
 */
public class MarksPanel extends JPanel {

    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final MarksRepository marksRepo;

    private JComboBox<String> studentCombo;
    private JComboBox<String> subjectCombo;
    private JTextField marksField;
    private JTable marksTable;
    private DefaultTableModel tableModel;
    private JLabel maxMarksLabel;
    private JLabel minPassLabel;
    private JLabel existingMarksLabel;

    public MarksPanel(StudentRepository studentRepo, SubjectRepository subjectRepo,
                      MarksRepository marksRepo) {
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.marksRepo = marksRepo;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("Examination Marks Entry",
                "Record examination scores, update subject evaluations, and inspect student transcripts");
        add(headerPanel, BorderLayout.NORTH);

        // Split container
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(22, 28, 22, 28));

        mainContent.add(createEntryPanel(), BorderLayout.WEST);
        mainContent.add(createTablePanel(), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createEntryPanel() {
        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 14));
        card.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Score Entry Form");
        title.setFont(H2ATheme.FONT_TITLE);
        title.setForeground(H2ATheme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        // Fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        // Student Combo
        fieldsPanel.add(H2ATheme.createFormLabel("SELECT REGISTERED STUDENT"));
        studentCombo = new JComboBox<>();
        styleComboBox(studentCombo);
        fieldsPanel.add(studentCombo);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Subject Combo
        fieldsPanel.add(H2ATheme.createFormLabel("SELECT COURSE SUBJECT"));
        subjectCombo = new JComboBox<>();
        styleComboBox(subjectCombo);
        fieldsPanel.add(subjectCombo);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Info card container
        JPanel infoCard = new JPanel(new GridLayout(3, 1, 0, 4));
        infoCard.setOpaque(true);
        infoCard.setBackground(H2ATheme.BG_INPUT);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(H2ATheme.BORDER_SUBTLE, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        maxMarksLabel = new JLabel("Maximum Marks: -");
        maxMarksLabel.setFont(H2ATheme.FONT_BODY_SM);
        maxMarksLabel.setForeground(H2ATheme.TEXT_SECONDARY);

        minPassLabel = new JLabel("Passing Threshold: -");
        minPassLabel.setFont(H2ATheme.FONT_BODY_SM);
        minPassLabel.setForeground(H2ATheme.TEXT_SECONDARY);

        existingMarksLabel = new JLabel("Current Record: None");
        existingMarksLabel.setFont(H2ATheme.FONT_BODY_BOLD);
        existingMarksLabel.setForeground(H2ATheme.ACCENT_CYAN);

        infoCard.add(maxMarksLabel);
        infoCard.add(minPassLabel);
        infoCard.add(existingMarksLabel);

        fieldsPanel.add(infoCard);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Marks Input
        fieldsPanel.add(H2ATheme.createFormLabel("MARKS OBTAINED (Numeric)"));
        marksField = H2ATheme.createTextField();
        fieldsPanel.add(marksField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton saveBtn = H2ATheme.createGradientButton("\u2714 Save Marks");
        JButton clearBtn = H2ATheme.createOutlineButton("\u27F2 Clear");

        saveBtn.addActionListener(e -> saveMarks());
        clearBtn.addActionListener(e -> clearForm());

        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        studentCombo.addActionListener(e -> onStudentSelected());
        subjectCombo.addActionListener(e -> onSubjectSelected());

        return card;
    }

    private JPanel createTablePanel() {
        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Marks Ledger for Selected Student");
        title.setFont(H2ATheme.FONT_TITLE);
        title.setForeground(H2ATheme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        String[] columns = {"Subject Code", "Subject Name", "Max Marks", "Passing Min", "Marks Obtained", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        marksTable = new JTable(tableModel);
        H2ATheme.stylizeTable(marksTable);

        // Click row to edit
        marksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = marksTable.getSelectedRow();
                if (row >= 0) {
                    String subjectCode = (String) tableModel.getValueAt(row, 0);
                    for (int i = 0; i < subjectCombo.getItemCount(); i++) {
                        String item = subjectCombo.getItemAt(i);
                        if (item.startsWith(subjectCode + " -")) {
                            subjectCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    marksField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                }
            }
        });

        JScrollPane scrollPane = H2ATheme.createScrollPane(marksTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(H2ATheme.FONT_BODY);
        combo.setBackground(H2ATheme.BG_INPUT);
        combo.setForeground(H2ATheme.TEXT_PRIMARY);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        combo.setPreferredSize(new Dimension(300, 36));
        combo.setBorder(BorderFactory.createLineBorder(H2ATheme.BORDER_INPUT, 1));
        combo.setRenderer(new DefaultListCellRenderer() {
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
    }

    private void onStudentSelected() {
        refreshMarksTable();
        onSubjectSelected();
    }

    private void onSubjectSelected() {
        String selectedSubject = (String) subjectCombo.getSelectedItem();
        if (selectedSubject == null || selectedSubject.equals("-- Select Subject --")) {
            maxMarksLabel.setText("Maximum Marks: -");
            minPassLabel.setText("Passing Threshold: -");
            existingMarksLabel.setText("Current Record: None");
            return;
        }

        String subjectCode = selectedSubject.split(" - ")[0].trim();
        Subject subject = subjectRepo.getSubjectByCode(subjectCode);
        if (subject != null) {
            maxMarksLabel.setText("Maximum Marks: " + (int) subject.getMaxMarks());
            minPassLabel.setText("Passing Threshold: " + (int) subject.getMinPassingMarks());

            String selectedStudent = (String) studentCombo.getSelectedItem();
            if (selectedStudent != null && !selectedStudent.equals("-- Select Student --")) {
                String studentId = selectedStudent.split(" - ")[0].trim();
                MarkRecord existing = marksRepo.getMark(studentId, subjectCode);
                if (existing != null) {
                    existingMarksLabel.setText("Current Record: " + existing.getMarksObtained() + " (Will Update)");
                    existingMarksLabel.setForeground(H2ATheme.STATUS_WARN);
                } else {
                    existingMarksLabel.setText("Current Record: None (New Record)");
                    existingMarksLabel.setForeground(H2ATheme.STATUS_PASS);
                }
            }
        }
    }

    private void saveMarks() {
        String selectedStudent = (String) studentCombo.getSelectedItem();
        if (selectedStudent == null || selectedStudent.equals("-- Select Student --")) {
            showError("Please select a student first.");
            return;
        }

        String selectedSubject = (String) subjectCombo.getSelectedItem();
        if (selectedSubject == null || selectedSubject.equals("-- Select Subject --")) {
            showError("Please select a subject.");
            return;
        }

        String studentId = selectedStudent.split(" - ")[0].trim();
        String subjectCode = selectedSubject.split(" - ")[0].trim();

        if (!studentRepo.studentExists(studentId)) {
            showError("Selected student is not registered.");
            return;
        }

        Subject subject = subjectRepo.getSubjectByCode(subjectCode);
        if (subject == null) {
            showError("Selected subject is not registered.");
            return;
        }

        String error = InputValidator.validateMarks(marksField.getText(), subject.getMaxMarks());
        if (error != null) {
            showError(error);
            return;
        }

        double marks = Double.parseDouble(marksField.getText().trim());
        MarkRecord record = new MarkRecord(studentId, subjectCode, marks);

        boolean isUpdate = marksRepo.markExists(studentId, subjectCode);
        String result = marksRepo.saveOrUpdateMark(record);

        if (result != null) {
            showError(result);
        } else {
            String action = isUpdate ? "updated" : "saved";
            showSuccess("Marks " + action + " successfully!\n" +
                    "Student ID: " + studentId + "\nSubject: " + subjectCode + "\nScore: " + marks);
            marksField.setText("");
            refreshMarksTable();
            onSubjectSelected();
        }
    }

    private void refreshMarksTable() {
        tableModel.setRowCount(0);

        String selectedStudent = (String) studentCombo.getSelectedItem();
        if (selectedStudent == null || selectedStudent.equals("-- Select Student --")) {
            return;
        }

        String studentId = selectedStudent.split(" - ")[0].trim();
        List<MarkRecord> marks = marksRepo.getMarksByStudentId(studentId);

        for (MarkRecord mark : marks) {
            Subject subject = subjectRepo.getSubjectByCode(mark.getSubjectCode());
            String subjectName = subject != null ? subject.getSubjectName() : "Unknown";
            double maxMarks = subject != null ? subject.getMaxMarks() : 0;
            double minPass = subject != null ? subject.getMinPassingMarks() : 0;
            String status = mark.getMarksObtained() >= minPass ? "PASS" : "FAIL";

            tableModel.addRow(new Object[]{
                    mark.getSubjectCode(), subjectName,
                    (int) maxMarks, (int) minPass,
                    mark.getMarksObtained(), status
            });
        }
    }

    private void clearForm() {
        if (studentCombo.getItemCount() > 0) {
            studentCombo.setSelectedIndex(0);
        }
        if (subjectCombo.getItemCount() > 0) {
            subjectCombo.setSelectedIndex(0);
        }
        marksField.setText("");
        tableModel.setRowCount(0);
    }

    public void refresh() {
        studentCombo.removeAllItems();
        studentCombo.addItem("-- Select Student --");
        for (Student s : studentRepo.getAllStudents()) {
            studentCombo.addItem(s.getStudentId() + " - " + s.getFullName() + " (" + s.getRollNumber() + ")");
        }

        subjectCombo.removeAllItems();
        subjectCombo.addItem("-- Select Subject --");
        for (Subject s : subjectRepo.getAllSubjects()) {
            subjectCombo.addItem(s.getSubjectCode() + " - " + s.getSubjectName());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
