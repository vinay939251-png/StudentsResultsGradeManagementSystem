package repository;

import model.Student;
import util.ApplicationConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for Student records.
 * Handles loading, saving, and in-memory management of student data.
 * Uses CsvFileHandler for file operations.
 */
public class StudentRepository {

    private final CsvFileHandler csvHandler;
    private final Map<String, Student> studentsById;       // StudentID -> Student
    private final Map<String, String> rollNumberToId;      // RollNumber -> StudentID

    public StudentRepository(CsvFileHandler csvHandler) {
        this.csvHandler = csvHandler;
        this.studentsById = new LinkedHashMap<>();
        this.rollNumberToId = new HashMap<>();
    }

    /**
     * Loads student records from the CSV file into memory.
     */
    public void load() {
        studentsById.clear();
        rollNumberToId.clear();

        List<String[]> records = csvHandler.readCsv(ApplicationConstants.STUDENTS_FILE);
        for (String[] fields : records) {
            try {
                if (fields.length >= 5) {
                    String id = fields[0];
                    String name = fields[1];
                    String roll = fields[2];
                    String dept = fields[3];
                    String sem = fields[4];
                    String email = fields.length > 5 ? fields[5] : "";

                    if (id.isEmpty() || name.isEmpty() || roll.isEmpty()) {
                        System.err.println("[WARNING] Skipping student with missing required fields: " + Arrays.toString(fields));
                        continue;
                    }

                    Student student = new Student(id, name, roll, dept, sem, email);
                    studentsById.put(id.toUpperCase(), student);
                    rollNumberToId.put(roll.toUpperCase(), id.toUpperCase());
                }
            } catch (Exception e) {
                System.err.println("[WARNING] Error parsing student record: " + e.getMessage());
            }
        }
        System.out.println("[INFO] Loaded " + studentsById.size() + " students.");
    }

    /**
     * Saves all student records to the CSV file.
     * @throws IOException if file writing fails
     */
    public void save() throws IOException {
        List<String> lines = studentsById.values().stream()
                .map(Student::toCsvString)
                .collect(Collectors.toList());
        csvHandler.writeCsv(ApplicationConstants.STUDENTS_FILE,
                ApplicationConstants.STUDENTS_HEADER, lines);
    }

    /**
     * Adds a new student. Rejects duplicates.
     * @param student the student to add
     * @return error message or null if successful
     */
    public String addStudent(Student student) {
        String upperCaseId = student.getStudentId().toUpperCase();
        String upperCaseRoll = student.getRollNumber().toUpperCase();

        if (studentsById.containsKey(upperCaseId)) {
            return "Student ID '" + student.getStudentId() + "' already exists.";
        }
        if (rollNumberToId.containsKey(upperCaseRoll)) {
            return "Roll Number '" + student.getRollNumber() + "' is already assigned to another student.";
        }

        studentsById.put(upperCaseId, student);
        rollNumberToId.put(upperCaseRoll, upperCaseId);

        try {
            save();
        } catch (IOException e) {
            // Rollback
            studentsById.remove(upperCaseId);
            rollNumberToId.remove(upperCaseRoll);
            return "Failed to save student data: " + e.getMessage();
        }
        return null;
    }

    /**
     * Updates an existing student record.
     * @param originalId the original student ID (in case it hasn't changed)
     * @param updated the updated student object
     * @return error message or null if successful
     */
    public String updateStudent(String originalId, Student updated) {
        String upperOrigId = originalId.toUpperCase();
        String upperNewId = updated.getStudentId().toUpperCase();
        String upperNewRoll = updated.getRollNumber().toUpperCase();

        Student original = studentsById.get(upperOrigId);
        if (original == null) {
            return "Original student not found.";
        }

        // Check for ID conflict with a different student
        if (!upperOrigId.equals(upperNewId) && studentsById.containsKey(upperNewId)) {
            return "Student ID '" + updated.getStudentId() + "' is already used by another student.";
        }

        // Check for roll number conflict with a different student
        String existingIdForRoll = rollNumberToId.get(upperNewRoll);
        if (existingIdForRoll != null && !existingIdForRoll.equals(upperOrigId)) {
            return "Roll Number '" + updated.getRollNumber() + "' is already assigned to another student.";
        }

        // Backup for rollback
        Student backup = new Student(original.getStudentId(), original.getFullName(),
                original.getRollNumber(), original.getDepartment(),
                original.getSemester(), original.getEmail());

        // Remove old entries
        rollNumberToId.remove(original.getRollNumber().toUpperCase());
        studentsById.remove(upperOrigId);

        // Add updated entries
        studentsById.put(upperNewId, updated);
        rollNumberToId.put(upperNewRoll, upperNewId);

        try {
            save();
        } catch (IOException e) {
            // Rollback
            studentsById.remove(upperNewId);
            rollNumberToId.remove(upperNewRoll);
            studentsById.put(upperOrigId, backup);
            rollNumberToId.put(backup.getRollNumber().toUpperCase(), upperOrigId);
            return "Failed to save student data: " + e.getMessage();
        }
        return null;
    }

    /**
     * Deletes a student by ID.
     * @param studentId the student ID to delete
     * @return error message or null if successful
     */
    public String deleteStudent(String studentId) {
        String upperCaseId = studentId.toUpperCase();
        Student student = studentsById.get(upperCaseId);
        if (student == null) {
            return "Student not found.";
        }

        studentsById.remove(upperCaseId);
        rollNumberToId.remove(student.getRollNumber().toUpperCase());

        try {
            save();
        } catch (IOException e) {
            // Rollback
            studentsById.put(upperCaseId, student);
            rollNumberToId.put(student.getRollNumber().toUpperCase(), upperCaseId);
            return "Failed to save changes: " + e.getMessage();
        }
        return null;
    }

    /**
     * Gets a student by ID.
     */
    public Student getStudentById(String studentId) {
        return studentsById.get(studentId.toUpperCase());
    }

    /**
     * Gets all students.
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentsById.values());
    }

    /**
     * Searches students by ID, roll number, or name (case-insensitive).
     */
    public List<Student> searchStudents(String query) {
        String lowerQuery = query.trim().toLowerCase();
        return studentsById.values().stream()
                .filter(s -> s.getStudentId().toLowerCase().contains(lowerQuery)
                        || s.getRollNumber().toLowerCase().contains(lowerQuery)
                        || s.getFullName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total number of registered students.
     */
    public int getStudentCount() {
        return studentsById.size();
    }

    /**
     * Checks if a student exists.
     */
    public boolean studentExists(String studentId) {
        return studentsById.containsKey(studentId.toUpperCase());
    }
}
