package ui;

import model.Subject;
import repository.MarksRepository;
import repository.SubjectRepository;
import ui.theme.H2ATheme;
import util.ApplicationConstants;
import util.InputValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Subject management panel with CRUD operations.
 * Styled with the luxury dark aesthetic of https://www.h2a.lu/ .
 */
public class SubjectPanel extends JPanel {

    private final SubjectRepository subjectRepo;
    private final MarksRepository marksRepo;

    private JTextField subjectCodeField;
    private JTextField subjectNameField;
    private JTextField maxMarksField;
    private JTextField minPassingField;

    private JTable subjectTable;
    private DefaultTableModel tableModel;

    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
    private String selectedSubjectCode = null;

    public SubjectPanel(SubjectRepository subjectRepo, MarksRepository marksRepo) {
        this.subjectRepo = subjectRepo;
        this.marksRepo = marksRepo;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("Subject Curriculum",
                "Configure academic courses, maximum scores, and passing threshold boundaries");
        add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(22, 28, 22, 28));

        // Left Form Panel
        JPanel formContainer = createFormPanel();
        formContainer.setPreferredSize(new Dimension(380, 0));
        mainContent.add(formContainer, BorderLayout.WEST);

        // Right Table Panel
        JPanel tableContainer = createTablePanel();
        mainContent.add(tableContainer, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Subject Parameters");
        title.setFont(H2ATheme.FONT_TITLE);
        title.setForeground(H2ATheme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        // Fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        subjectCodeField = H2ATheme.createTextField();
        subjectNameField = H2ATheme.createTextField();
        maxMarksField = H2ATheme.createTextField();
        minPassingField = H2ATheme.createTextField();

        fieldsPanel.add(H2ATheme.createFormLabel("SUBJECT CODE (e.g. CS101)"));
        fieldsPanel.add(subjectCodeField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        fieldsPanel.add(H2ATheme.createFormLabel("SUBJECT NAME"));
        fieldsPanel.add(subjectNameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        fieldsPanel.add(H2ATheme.createFormLabel("MAXIMUM MARKS (e.g. 100)"));
        fieldsPanel.add(maxMarksField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        fieldsPanel.add(H2ATheme.createFormLabel("MINIMUM PASSING MARKS (e.g. 35)"));
        fieldsPanel.add(minPassingField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        addBtn = H2ATheme.createGradientButton("+ Add Subject");
        updateBtn = H2ATheme.createOutlineButton("\u270E Update");
        deleteBtn = H2ATheme.createDangerButton("\u2718 Delete");
        clearBtn = H2ATheme.createOutlineButton("\u27F2 Clear");

        addBtn.addActionListener(e -> addSubject());
        updateBtn.addActionListener(e -> updateSubject());
        deleteBtn.addActionListener(e -> deleteSubject());
        clearBtn.addActionListener(e -> clearForm());

        buttonsPanel.add(addBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(clearBtn);

        card.add(buttonsPanel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createTablePanel() {
        JPanel card = H2ATheme.createCardPanel(18);
        card.setLayout(new BorderLayout(0, 14));

        JLabel title = new JLabel("Curriculum Subject Catalog");
        title.setFont(H2ATheme.FONT_TITLE);
        title.setForeground(H2ATheme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Subject Code", "Subject Name", "Max Marks", "Passing Threshold"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        subjectTable = new JTable(tableModel);
        H2ATheme.stylizeTable(subjectTable);

        subjectTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = subjectTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromRow(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = H2ATheme.createScrollPane(subjectTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void populateFormFromRow(int row) {
        selectedSubjectCode = (String) tableModel.getValueAt(row, 0);
        subjectCodeField.setText(selectedSubjectCode);
        subjectCodeField.setEditable(false);
        subjectNameField.setText((String) tableModel.getValueAt(row, 1));
        maxMarksField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        minPassingField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void addSubject() {
        String code = subjectCodeField.getText().trim();
        String name = subjectNameField.getText().trim();
        String maxStr = maxMarksField.getText().trim();
        String minStr = minPassingField.getText().trim();

        String err = InputValidator.validateSubjectCode(code);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRequired(name, "Subject Name");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateMaxMarks(maxStr);
        if (err != null) { showError(err); return; }

        double maxMarks = Double.parseDouble(maxStr);
        err = InputValidator.validateMinPassingMarks(minStr, maxMarks);
        if (err != null) { showError(err); return; }

        double minPassing = Double.parseDouble(minStr);

        Subject subject = new Subject(code, name, maxMarks, minPassing);
        String error = subjectRepo.addSubject(subject);
        if (error != null) {
            showError(error);
        } else {
            showSuccess("Subject '" + name + "' added successfully.");
            clearForm();
            refresh();
        }
    }

    private void updateSubject() {
        if (selectedSubjectCode == null) {
            showError("Please select a subject from the table to update.");
            return;
        }

        String name = subjectNameField.getText().trim();
        String maxStr = maxMarksField.getText().trim();
        String minStr = minPassingField.getText().trim();

        String err = InputValidator.validateRequired(name, "Subject Name");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateMaxMarks(maxStr);
        if (err != null) { showError(err); return; }

        double maxMarks = Double.parseDouble(maxStr);
        err = InputValidator.validateMinPassingMarks(minStr, maxMarks);
        if (err != null) { showError(err); return; }

        double minPassing = Double.parseDouble(minStr);

        Subject updated = new Subject(selectedSubjectCode, name, maxMarks, minPassing);
        String error = subjectRepo.updateSubject(selectedSubjectCode, updated);
        if (error != null) {
            showError(error);
        } else {
            showSuccess("Subject updated successfully.");
            clearForm();
            refresh();
        }
    }

    private void deleteSubject() {
        if (selectedSubjectCode == null) {
            showError("Please select a subject from the table to delete.");
            return;
        }

        // Integrity check: verify no marks depend on this subject
        if (marksRepo.hasMarksForSubject(selectedSubjectCode)) {
            showError("Cannot delete subject '" + selectedSubjectCode + "' because students have marks recorded for it.\n" +
                    "Delete or reassign those marks before deleting the subject.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subject " + selectedSubjectCode + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String error = subjectRepo.deleteSubject(selectedSubjectCode);
            if (error != null) {
                showError(error);
            } else {
                showSuccess("Subject deleted successfully.");
                clearForm();
                refresh();
            }
        }
    }

    private void clearForm() {
        selectedSubjectCode = null;
        subjectCodeField.setText("");
        subjectCodeField.setEditable(true);
        subjectNameField.setText("");
        maxMarksField.setText("");
        minPassingField.setText("");
        subjectTable.clearSelection();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Subject> subjects = subjectRepo.getAllSubjects();
        for (Subject s : subjects) {
            tableModel.addRow(new Object[]{
                    s.getSubjectCode(), s.getSubjectName(), s.getMaxMarks(), s.getMinPassingMarks()
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
