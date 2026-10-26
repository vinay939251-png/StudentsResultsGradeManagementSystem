package model;

/**
 * Represents a student in the system.
 * Encapsulates student information with private fields and public accessors.
 */
public class Student {

    private String studentId;
    private String fullName;
    private String rollNumber;
    private String department;
    private String semester;
    private String email;

    /**
     * Constructs a Student with all required and optional fields.
     * @param studentId unique identifier for the student
     * @param fullName student's full name
     * @param rollNumber student's roll number
     * @param department student's department/course
     * @param semester student's academic year/semester
     * @param email student's email (optional, can be empty)
     */
    public Student(String studentId, String fullName, String rollNumber,
                   String department, String semester, String email) {
        this.studentId = studentId != null ? studentId.trim() : "";
        this.fullName = fullName != null ? fullName.trim() : "";
        this.rollNumber = rollNumber != null ? rollNumber.trim() : "";
        this.department = department != null ? department.trim() : "";
        this.semester = semester != null ? semester.trim() : "";
        this.email = email != null ? email.trim() : "";
    }

    // ──── Getters ────

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public String getEmail() {
        return email;
    }

    // ──── Setters ────

    public void setStudentId(String studentId) {
        this.studentId = studentId != null ? studentId.trim() : "";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName != null ? fullName.trim() : "";
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber != null ? rollNumber.trim() : "";
    }

    public void setDepartment(String department) {
        this.department = department != null ? department.trim() : "";
    }

    public void setSemester(String semester) {
        this.semester = semester != null ? semester.trim() : "";
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : "";
    }

    /**
     * Converts the student to a CSV-formatted string.
     * Handles fields containing commas or quotes by enclosing them in quotes.
     * @return CSV-formatted representation of this student
     */
    public String toCsvString() {
        return escapeCsv(studentId) + "," +
               escapeCsv(fullName) + "," +
               escapeCsv(rollNumber) + "," +
               escapeCsv(department) + "," +
               escapeCsv(semester) + "," +
               escapeCsv(email);
    }

    /**
     * Escapes a CSV field value. If it contains commas, quotes, or newlines,
     * wraps the value in double quotes and escapes internal quotes.
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public String toString() {
        return studentId + " - " + fullName + " (" + rollNumber + ")";
    }
}
