package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents the computed result for a student.
 * Contains calculated totals, percentage, grade, subject-wise results, and overall status.
 * This is a data transfer object — it is computed by ResultCalculator, not stored directly.
 */
public class StudentResult {

    private Student student;
    private List<SubjectResult> subjectResults;
    private double totalMarks;
    private double maxPossibleMarks;
    private double percentage;
    private double averageMarks;
    private String grade;
    private String resultStatus; // PASS, FAIL, or INCOMPLETE
    private List<String> failedSubjects;
    private List<String> missingSubjects;
    private int rank;

    /**
     * Inner class representing a single subject's result within a student's overall result.
     */
    public static class SubjectResult {
        private String subjectCode;
        private String subjectName;
        private double marksObtained;
        private double maxMarks;
        private double minPassingMarks;
        private boolean passed;

        public SubjectResult(String subjectCode, String subjectName,
                             double marksObtained, double maxMarks,
                             double minPassingMarks, boolean passed) {
            this.subjectCode = subjectCode;
            this.subjectName = subjectName;
            this.marksObtained = marksObtained;
            this.maxMarks = maxMarks;
            this.minPassingMarks = minPassingMarks;
            this.passed = passed;
        }

        public String getSubjectCode() { return subjectCode; }
        public String getSubjectName() { return subjectName; }
        public double getMarksObtained() { return marksObtained; }
        public double getMaxMarks() { return maxMarks; }
        public double getMinPassingMarks() { return minPassingMarks; }
        public boolean isPassed() { return passed; }
    }

    public StudentResult(Student student) {
        this.student = student;
        this.subjectResults = new ArrayList<>();
        this.failedSubjects = new ArrayList<>();
        this.missingSubjects = new ArrayList<>();
        this.totalMarks = 0;
        this.maxPossibleMarks = 0;
        this.percentage = 0;
        this.averageMarks = 0;
        this.grade = "";
        this.resultStatus = "";
        this.rank = 0;
    }

    // ──── Getters ────

    public Student getStudent() { return student; }
    public List<SubjectResult> getSubjectResults() { return subjectResults; }
    public double getTotalMarks() { return totalMarks; }
    public double getMaxPossibleMarks() { return maxPossibleMarks; }
    public double getPercentage() { return percentage; }
    public double getAverageMarks() { return averageMarks; }
    public String getGrade() { return grade; }
    public String getResultStatus() { return resultStatus; }
    public List<String> getFailedSubjects() { return failedSubjects; }
    public List<String> getMissingSubjects() { return missingSubjects; }
    public int getRank() { return rank; }

    // ──── Setters ────

    public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }
    public void setMaxPossibleMarks(double maxPossibleMarks) { this.maxPossibleMarks = maxPossibleMarks; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public void setAverageMarks(double averageMarks) { this.averageMarks = averageMarks; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
    public void setRank(int rank) { this.rank = rank; }

    public void addSubjectResult(SubjectResult sr) {
        this.subjectResults.add(sr);
    }

    public void addFailedSubject(String subjectName) {
        this.failedSubjects.add(subjectName);
    }

    public void addMissingSubject(String subjectName) {
        this.missingSubjects.add(subjectName);
    }

    /**
     * Checks if this result is complete (all required subjects have marks).
     */
    public boolean isComplete() {
        return missingSubjects.isEmpty();
    }
}
