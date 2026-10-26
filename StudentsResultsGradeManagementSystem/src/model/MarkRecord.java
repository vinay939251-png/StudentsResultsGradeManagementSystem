package model;

/**
 * Represents a single mark record linking a student to a subject with the marks obtained.
 * A student-subject pair must be unique (no duplicate mark records).
 */
public class MarkRecord {

    private String studentId;
    private String subjectCode;
    private double marksObtained;

    /**
     * Constructs a MarkRecord.
     * @param studentId the student's unique ID
     * @param subjectCode the subject's unique code
     * @param marksObtained the marks obtained by the student in the subject
     */
    public MarkRecord(String studentId, String subjectCode, double marksObtained) {
        this.studentId = studentId != null ? studentId.trim() : "";
        this.subjectCode = subjectCode != null ? subjectCode.trim() : "";
        this.marksObtained = marksObtained;
    }

    // ──── Getters ────

    public String getStudentId() {
        return studentId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    // ──── Setters ────

    public void setStudentId(String studentId) {
        this.studentId = studentId != null ? studentId.trim() : "";
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode != null ? subjectCode.trim() : "";
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    /**
     * Creates a unique key for this mark record (student-subject pair).
     * Used to prevent duplicate entries.
     * @return a unique key string
     */
    public String getKey() {
        return studentId + "_" + subjectCode;
    }

    /**
     * Converts the mark record to a CSV-formatted string.
     * @return CSV-formatted representation
     */
    public String toCsvString() {
        return studentId + "," + subjectCode + "," + marksObtained;
    }

    @Override
    public String toString() {
        return "Student: " + studentId + ", Subject: " + subjectCode + ", Marks: " + marksObtained;
    }
}
