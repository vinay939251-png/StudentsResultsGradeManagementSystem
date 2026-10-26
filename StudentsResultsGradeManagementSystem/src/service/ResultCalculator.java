package service;

import model.*;
import repository.MarksRepository;
import repository.SubjectRepository;
import repository.StudentRepository;
import util.ApplicationConstants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Dedicated class for calculating student results.
 * Separated from GUI to maintain proper separation of concerns.
 *
 * Calculates total marks, percentage, average, subject pass status,
 * overall result, and grade for individual students and entire classes.
 */
public class ResultCalculator {

    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final MarksRepository marksRepo;

    public ResultCalculator(StudentRepository studentRepo,
                            SubjectRepository subjectRepo,
                            MarksRepository marksRepo) {
        this.studentRepo = studentRepo;
        this.subjectRepo = subjectRepo;
        this.marksRepo = marksRepo;
    }

    /**
     * Calculates the complete result for a single student.
     *
     * Algorithm:
     * 1. Get all registered subjects.
     * 2. For each subject, look up the student's marks.
     * 3. If marks exist, check if the student passed that subject.
     * 4. If marks are missing, mark the subject as missing.
     * 5. Calculate total, max possible, percentage, and average from available marks.
     * 6. Determine overall status: INCOMPLETE if any subjects missing,
     *    FAIL if any subject failed, PASS otherwise.
     * 7. Assign grade only for complete results.
     *
     * @param studentId the student's unique ID
     * @return a StudentResult object, or null if the student doesn't exist
     */
    public StudentResult calculateStudentResult(String studentId) {
        Student student = studentRepo.getStudentById(studentId);
        if (student == null) {
            return null;
        }

        List<Subject> allSubjects = subjectRepo.getAllSubjects();
        List<MarkRecord> studentMarks = marksRepo.getMarksByStudentId(studentId);

        // Build a map of subject code -> marks for quick lookup
        Map<String, Double> marksMap = new HashMap<>();
        for (MarkRecord mark : studentMarks) {
            marksMap.put(mark.getSubjectCode().toUpperCase(), mark.getMarksObtained());
        }

        StudentResult result = new StudentResult(student);
        double totalMarks = 0;
        double maxPossible = 0;
        int subjectsWithMarks = 0;
        boolean hasFailedSubject = false;

        for (Subject subject : allSubjects) {
            String code = subject.getSubjectCode().toUpperCase();
            Double marks = marksMap.get(code);

            if (marks != null) {
                // Marks exist for this subject
                boolean passed = marks >= subject.getMinPassingMarks();
                StudentResult.SubjectResult sr = new StudentResult.SubjectResult(
                        subject.getSubjectCode(),
                        subject.getSubjectName(),
                        marks,
                        subject.getMaxMarks(),
                        subject.getMinPassingMarks(),
                        passed
                );
                result.addSubjectResult(sr);

                totalMarks += marks;
                maxPossible += subject.getMaxMarks();
                subjectsWithMarks++;

                if (!passed) {
                    hasFailedSubject = true;
                    result.addFailedSubject(subject.getSubjectName());
                }
            } else {
                // Missing marks for this subject
                result.addMissingSubject(subject.getSubjectName());
                maxPossible += subject.getMaxMarks();
            }
        }

        result.setTotalMarks(totalMarks);
        result.setMaxPossibleMarks(maxPossible);

        if (subjectsWithMarks == 0) {
            // No marks at all
            result.setPercentage(0);
            result.setAverageMarks(0);
            result.setResultStatus(ApplicationConstants.RESULT_INCOMPLETE);
            result.setGrade(ApplicationConstants.RESULT_INCOMPLETE);
        } else if (!result.isComplete()) {
            // Some subjects missing — do NOT calculate percentage as if missing = 0
            // Calculate percentage only from available subjects for informational purposes
            double availableMax = 0;
            for (StudentResult.SubjectResult sr : result.getSubjectResults()) {
                availableMax += sr.getMaxMarks();
            }
            double partialPercentage = availableMax > 0 ? (totalMarks / availableMax) * 100.0 : 0;
            result.setPercentage(partialPercentage);
            result.setAverageMarks(totalMarks / subjectsWithMarks);
            result.setResultStatus(ApplicationConstants.RESULT_INCOMPLETE);
            result.setGrade(ApplicationConstants.RESULT_INCOMPLETE);
        } else {
            // All subjects present — complete result
            double percentage = maxPossible > 0 ? (totalMarks / maxPossible) * 100.0 : 0;
            result.setPercentage(percentage);
            result.setAverageMarks(totalMarks / subjectsWithMarks);

            if (hasFailedSubject) {
                result.setResultStatus(ApplicationConstants.RESULT_FAIL);
                result.setGrade(GradeCalculator.calculateGrade(percentage));
            } else {
                result.setResultStatus(ApplicationConstants.RESULT_PASS);
                result.setGrade(GradeCalculator.calculateGrade(percentage));
            }
        }

        return result;
    }

    /**
     * Calculates results for all students and returns them sorted for a merit list.
     *
     * Ranking Policy:
     * - Only completed and passed results are ranked.
     * - Ranked by percentage in descending order.
     * - Students with equal percentages share the same rank (dense ranking).
     * - Failed students with complete results appear after passed students (unranked).
     * - Incomplete results appear at the bottom (unranked).
     *
     * @return sorted list of all student results
     */
    public List<StudentResult> calculateAllResults() {
        List<Student> allStudents = studentRepo.getAllStudents();
        List<StudentResult> results = new ArrayList<>();

        for (Student student : allStudents) {
            StudentResult result = calculateStudentResult(student.getStudentId());
            if (result != null) {
                results.add(result);
            }
        }

        // Separate into complete-pass, complete-fail, and incomplete
        List<StudentResult> passed = results.stream()
                .filter(r -> ApplicationConstants.RESULT_PASS.equals(r.getResultStatus()))
                .sorted((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()))
                .collect(Collectors.toList());

        List<StudentResult> failed = results.stream()
                .filter(r -> ApplicationConstants.RESULT_FAIL.equals(r.getResultStatus()))
                .sorted((a, b) -> Double.compare(b.getPercentage(), a.getPercentage()))
                .collect(Collectors.toList());

        List<StudentResult> incomplete = results.stream()
                .filter(r -> ApplicationConstants.RESULT_INCOMPLETE.equals(r.getResultStatus()))
                .collect(Collectors.toList());

        // Assign ranks to passed students (dense ranking)
        int rank = 1;
        for (int i = 0; i < passed.size(); i++) {
            if (i > 0 && Math.abs(passed.get(i).getPercentage() - passed.get(i - 1).getPercentage()) > 0.001) {
                rank = i + 1;
            }
            passed.get(i).setRank(rank);
        }

        // Combine: passed first, then failed, then incomplete
        List<StudentResult> sortedResults = new ArrayList<>();
        sortedResults.addAll(passed);
        sortedResults.addAll(failed);
        sortedResults.addAll(incomplete);

        return sortedResults;
    }

    /**
     * Counts the number of students who passed (all subjects passed with complete marks).
     */
    public int getPassedCount() {
        return (int) calculateAllResults().stream()
                .filter(r -> ApplicationConstants.RESULT_PASS.equals(r.getResultStatus()))
                .count();
    }

    /**
     * Counts the number of students who failed.
     */
    public int getFailedCount() {
        return (int) calculateAllResults().stream()
                .filter(r -> ApplicationConstants.RESULT_FAIL.equals(r.getResultStatus()))
                .count();
    }

    /**
     * Calculates overall class pass percentage.
     * Only considers students with complete results.
     */
    public double getClassPassPercentage() {
        List<StudentResult> allResults = calculateAllResults();
        long completeCount = allResults.stream()
                .filter(r -> !ApplicationConstants.RESULT_INCOMPLETE.equals(r.getResultStatus()))
                .count();
        if (completeCount == 0) return 0;

        long passCount = allResults.stream()
                .filter(r -> ApplicationConstants.RESULT_PASS.equals(r.getResultStatus()))
                .count();

        return (passCount * 100.0) / completeCount;
    }
}
