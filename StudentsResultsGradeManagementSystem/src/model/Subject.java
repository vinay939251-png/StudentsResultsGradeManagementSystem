package model;

/**
 * Represents an academic subject with its grading parameters.
 * Each subject has a unique code, name, maximum marks, and minimum passing marks.
 */
public class Subject {

    private String subjectCode;
    private String subjectName;
    private double maxMarks;
    private double minPassingMarks;

    /**
     * Constructs a Subject with all required fields.
     * @param subjectCode unique code identifying the subject
     * @param subjectName name of the subject
     * @param maxMarks maximum obtainable marks
     * @param minPassingMarks minimum marks required to pass
     */
    public Subject(String subjectCode, String subjectName, double maxMarks, double minPassingMarks) {
        this.subjectCode = subjectCode != null ? subjectCode.trim() : "";
        this.subjectName = subjectName != null ? subjectName.trim() : "";
        this.maxMarks = maxMarks;
        this.minPassingMarks = minPassingMarks;
    }

    // ──── Getters ────

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public double getMinPassingMarks() {
        return minPassingMarks;
    }

    // ──── Setters ────

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode != null ? subjectCode.trim() : "";
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName != null ? subjectName.trim() : "";
    }

    public void setMaxMarks(double maxMarks) {
        this.maxMarks = maxMarks;
    }

    public void setMinPassingMarks(double minPassingMarks) {
        this.minPassingMarks = minPassingMarks;
    }

    /**
     * Converts the subject to a CSV-formatted string.
     * @return CSV-formatted representation of this subject
     */
    public String toCsvString() {
        return escapeCsv(subjectCode) + "," +
               escapeCsv(subjectName) + "," +
               (int) maxMarks + "," +
               (int) minPassingMarks;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public String toString() {
        return subjectCode + " - " + subjectName;
    }
}
