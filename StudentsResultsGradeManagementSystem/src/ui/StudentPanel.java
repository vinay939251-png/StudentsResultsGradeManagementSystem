package ui;

import model.Student;
import repository.MarksRepository;
import repository.StudentRepository;
import ui.theme.H2ATheme;
import util.ApplicationConstants;
import util.InputValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Student management panel with CRUD operations.
 * Styled with the luxury dark aesthetic of https://www.h2a.lu/ .
 */
public class StudentPanel extends JPanel {

    private final StudentRepository studentRepo;
    private final MarksRepository marksRepo;

    // Form fields
    private JTextField studentIdField;
    private JTextField fullNameField;
    private JTextField rollNumberField;
    private JTextField departmentField;
    private JTextField semesterField;
    private JTextField emailField;
    private JTextField searchField;

    // Table
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton addBtn, updateBtn, deleteBtn, clearBtn, searchBtn, resetBtn;

    // Currently selected student ID for editing
    private String selectedStudentId = null;

    public StudentPanel(StudentRepository studentRepo, MarksRepository marksRepo) {
        this.studentRepo = studentRepo;
        this.marksRepo = marksRepo;

        setLayout(new BorderLayout());
        setBackground(H2ATheme.BG_DARK);

        buildUI();
        refresh();
    }

    private void buildUI() {
        // Header
        JPanel headerPanel = H2ATheme.createHeader("Student Directory",
                "Enroll, update, search, and manage registered student profiles");
        add(headerPanel, BorderLayout.NORTH);

        // Main body split: Form (left) and Table (right)
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

        JLabel title = new JLabel("Student Information");
        title.setFont(H2ATheme.FONT_TITLE);
        title.setForeground(H2ATheme.TEXT_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        // Fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        studentIdField = H2ATheme.createTextField();
        fullNameField = H2ATheme.createTextField();
        rollNumberField = H2ATheme.createTextField();
        departmentField = H2ATheme.createTextField();
        semesterField = H2ATheme.createTextField();
        emailField = H2ATheme.createTextField();

        fieldsPanel.add(H2ATheme.createFormLabel("STUDENT ID (e.g. STU001)"));
        fieldsPanel.add(studentIdField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldsPanel.add(H2ATheme.createFormLabel("FULL NAME"));
        fieldsPanel.add(fullNameField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldsPanel.add(H2ATheme.createFormLabel("ROLL NUMBER (e.g. 22CS101)"));
        fieldsPanel.add(rollNumberField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldsPanel.add(H2ATheme.createFormLabel("DEPARTMENT"));
        fieldsPanel.add(departmentField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldsPanel.add(H2ATheme.createFormLabel("SEMESTER"));
        fieldsPanel.add(semesterField);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        fieldsPanel.add(H2ATheme.createFormLabel("EMAIL ADDRESS"));
        fieldsPanel.add(emailField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        // Action Buttons Grid
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        addBtn = H2ATheme.createGradientButton("+ Add Student");
        updateBtn = H2ATheme.createOutlineButton("\u270E Update");
        deleteBtn = H2ATheme.createDangerButton("\u2718 Delete");
        clearBtn = H2ATheme.createOutlineButton("\u27F2 Clear");

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
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

        // Top Search Bar
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);

        JLabel searchLabel = new JLabel("Quick Search:");
        searchLabel.setFont(H2ATheme.FONT_BODY_BOLD);
        searchLabel.setForeground(H2ATheme.TEXT_SECONDARY);
        searchBar.add(searchLabel, BorderLayout.WEST);

        searchField = H2ATheme.createTextField();
        searchField.setToolTipText("Filter by Student ID, Name, Roll No, or Department");
        searchField.addActionListener(e -> searchStudents());
        searchBar.add(searchField, BorderLayout.CENTER);

        JPanel searchActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchActions.setOpaque(false);

        searchBtn = H2ATheme.createGradientButton("Search");
        searchBtn.setPreferredSize(new Dimension(90, 36));
        searchBtn.addActionListener(e -> searchStudents());
        searchActions.add(searchBtn);

        resetBtn = H2ATheme.createOutlineButton("Show All");
        resetBtn.setPreferredSize(new Dimension(90, 36));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            refresh();
        });
        searchActions.add(resetBtn);

        searchBar.add(searchActions, BorderLayout.EAST);
        card.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Student ID", "Full Name", "Roll Number", "Department", "Semester", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        H2ATheme.stylizeTable(studentTable);

        // Selection listener to populate form
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromRow(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = H2ATheme.createScrollPane(studentTable);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void populateFormFromRow(int row) {
        selectedStudentId = (String) tableModel.getValueAt(row, 0);
        studentIdField.setText(selectedStudentId);
        studentIdField.setEditable(false);
        fullNameField.setText((String) tableModel.getValueAt(row, 1));
        rollNumberField.setText((String) tableModel.getValueAt(row, 2));
        departmentField.setText((String) tableModel.getValueAt(row, 3));
        semesterField.setText((String) tableModel.getValueAt(row, 4));
        emailField.setText((String) tableModel.getValueAt(row, 5));
    }

    private void addStudent() {
        String studentId = studentIdField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String department = departmentField.getText().trim();
        String semester = semesterField.getText().trim();
        String email = emailField.getText().trim();

        // Validation
        String err = InputValidator.validateStudentId(studentId);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateStudentName(fullName);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRollNumber(rollNumber);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRequired(department, "Department");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRequired(semester, "Semester");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateEmail(email);
        if (err != null) { showError(err); return; }

        Student student = new Student(studentId, fullName, rollNumber, department, semester, email);
        String error = studentRepo.addStudent(student);
        if (error != null) {
            showError(error);
        } else {
            showSuccess("Student '" + fullName + "' added successfully.");
            clearForm();
            refresh();
        }
    }

    private void updateStudent() {
        if (selectedStudentId == null) {
            showError("Please select a student from the table to update.");
            return;
        }

        String fullName = fullNameField.getText().trim();
        String rollNumber = rollNumberField.getText().trim();
        String department = departmentField.getText().trim();
        String semester = semesterField.getText().trim();
        String email = emailField.getText().trim();

        String err = InputValidator.validateStudentName(fullName);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRollNumber(rollNumber);
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRequired(department, "Department");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateRequired(semester, "Semester");
        if (err != null) { showError(err); return; }

        err = InputValidator.validateEmail(email);
        if (err != null) { showError(err); return; }

        Student updated = new Student(selectedStudentId, fullName, rollNumber, department, semester, email);
        String error = studentRepo.updateStudent(selectedStudentId, updated);
        if (error != null) {
            showError(error);
        } else {
            showSuccess("Student record updated successfully.");
            clearForm();
            refresh();
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == null) {
            showError("Please select a student from the table to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student " + selectedStudentId + "?\n" +
                "All associated marks for this student will also be removed.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            marksRepo.deleteMarksByStudentId(selectedStudentId);
            String error = studentRepo.deleteStudent(selectedStudentId);
            if (error != null) {
                showError(error);
            } else {
                showSuccess("Student and associated marks deleted successfully.");
                clearForm();
                refresh();
            }
        }
    }

    private void searchStudents() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refresh();
            return;
        }

        List<Student> results = studentRepo.searchStudents(query);
        tableModel.setRowCount(0);
        for (Student s : results) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(), s.getFullName(), s.getRollNumber(),
                    s.getDepartment(), s.getSemester(), s.getEmail()
            });
        }
    }

    private void clearForm() {
        selectedStudentId = null;
        studentIdField.setText("");
        studentIdField.setEditable(true);
        fullNameField.setText("");
        rollNumberField.setText("");
        departmentField.setText("");
        semesterField.setText("");
        emailField.setText("");
        studentTable.clearSelection();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Student> students = studentRepo.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(), s.getFullName(), s.getRollNumber(),
                    s.getDepartment(), s.getSemester(), s.getEmail()
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
