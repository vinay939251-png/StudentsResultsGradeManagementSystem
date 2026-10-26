package util;

/**
 * Centralized input validation utility.
 * Validates student data, subject data, and marks before they enter the system.
 * All validation methods return descriptive error messages or null if valid.
 */
public final class InputValidator {

    private InputValidator() {
        // Prevent instantiation
    }

    /**
     * Validates that a string is not null or blank.
     * @param value the string to check
     * @param fieldName the name of the field for error messages
     * @return error message or null if valid
     */
    public static String validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return fieldName + " is required and cannot be empty.";
        }
        return null;
    }

    /**
     * Validates a student ID format. Must be non-empty and alphanumeric.
     * @param studentId the student ID to validate
     * @return error message or null if valid
     */
    public static String validateStudentId(String studentId) {
        String required = validateRequired(studentId, "Student ID");
        if (required != null) return required;
        if (!studentId.trim().matches("[A-Za-z0-9]+")) {
            return "Student ID must contain only letters and numbers.";
        }
        return null;
    }

    /**
     * Validates a student name.
     * @param name the name to validate
     * @return error message or null if valid
     */
    public static String validateStudentName(String name) {
        String required = validateRequired(name, "Student Name");
        if (required != null) return required;
        if (name.trim().length() < 2) {
            return "Student Name must be at least 2 characters long.";
        }
        return null;
    }

    /**
     * Validates a roll number. Must be non-empty.
     * @param rollNumber the roll number to validate
     * @return error message or null if valid
     */
    public static String validateRollNumber(String rollNumber) {
        String required = validateRequired(rollNumber, "Roll Number");
        if (required != null) return required;
        if (!rollNumber.trim().matches("[A-Za-z0-9\\-]+")) {
            return "Roll Number must contain only letters, numbers, and hyphens.";
        }
        return null;
    }

    /**
     * Validates an email address (optional field).
     * @param email the email to validate
     * @return error message or null if valid (empty is valid since email is optional)
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null; // Email is optional
        }
        // Simple email pattern check
        if (!email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Email address format is invalid.";
        }
        return null;
    }

    /**
     * Validates a subject code.
     * @param code the subject code to validate
     * @return error message or null if valid
     */
    public static String validateSubjectCode(String code) {
        String required = validateRequired(code, "Subject Code");
        if (required != null) return required;
        if (!code.trim().matches("[A-Za-z0-9]+")) {
            return "Subject Code must contain only letters and numbers.";
        }
        return null;
    }

    /**
     * Validates marks obtained against a subject's maximum marks.
     * @param marksStr the marks string entered by the user
     * @param maxMarks the maximum marks allowed for the subject
     * @return error message or null if valid
     */
    public static String validateMarks(String marksStr, double maxMarks) {
        String required = validateRequired(marksStr, "Marks");
        if (required != null) return required;

        double marks;
        try {
            marks = Double.parseDouble(marksStr.trim());
        } catch (NumberFormatException e) {
            return "Marks must be a valid number.";
        }

        if (marks < 0) {
            return "Marks cannot be negative.";
        }
        if (marks > maxMarks) {
            return "Marks cannot exceed the maximum marks (" + (int) maxMarks + ").";
        }
        return null;
    }

    /**
     * Validates that a string represents a positive number.
     * @param value the string to validate
     * @param fieldName the name of the field for error messages
     * @return error message or null if valid
     */
    public static String validatePositiveNumber(String value, String fieldName) {
        String required = validateRequired(value, fieldName);
        if (required != null) return required;

        double num;
        try {
            num = Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return fieldName + " must be a valid number.";
        }

        if (num <= 0) {
            return fieldName + " must be a positive number.";
        }
        return null;
    }

    /**
     * Validates maximum marks value.
     * @param maxMarksStr the max marks string
     * @return error message or null if valid
     */
    public static String validateMaxMarks(String maxMarksStr) {
        String result = validatePositiveNumber(maxMarksStr, "Maximum Marks");
        if (result != null) return result;
        double maxMarks = Double.parseDouble(maxMarksStr.trim());
        if (maxMarks > 1000) {
            return "Maximum Marks cannot exceed 1000.";
        }
        return null;
    }

    /**
     * Validates minimum passing marks against maximum marks.
     * @param minMarksStr the minimum passing marks string
     * @param maxMarks the maximum marks for the subject
     * @return error message or null if valid
     */
    public static String validateMinPassingMarks(String minMarksStr, double maxMarks) {
        String result = validatePositiveNumber(minMarksStr, "Minimum Passing Marks");
        if (result != null) return result;
        double minMarks = Double.parseDouble(minMarksStr.trim());
        if (minMarks > maxMarks) {
            return "Minimum Passing Marks cannot exceed Maximum Marks (" + (int) maxMarks + ").";
        }
        return null;
    }
}
