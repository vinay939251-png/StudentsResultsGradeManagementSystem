import model.*;
import service.*;
import repository.*;
import util.*;

import java.util.List;

/**
 * Test class for the Students Results and Grade Management System.
 * Runs automated tests for grade calculation, result calculation,
 * input validation, and data persistence.
 *
 * Run:  javac -d out -sourcepath src src/TestRunner.java
 *       java -cp out TestRunner
 */
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("  Students Results and Grade Management System");
        System.out.println("  Automated Test Runner");
        System.out.println("====================================================\n");

        testGradeCalculation();
        testInputValidation();
        testResultCalculation();
        testCsvPersistence();
        testDuplicateDetection();
        testEdgeCases();

        System.out.println("\n====================================================");
        System.out.println("  Test Results: " + passed + " passed, " + failed + " failed");
        System.out.println("  Total: " + (passed + failed) + " tests");
        System.out.println("====================================================");
    }

    // ──── Grade Calculation Tests ────

    private static void testGradeCalculation() {
        System.out.println("--- Grade Calculation Tests ---");

        // T12: Grade at 35%
        assertEqual("T12: Grade at 35%",
                GradeCalculator.calculateGrade(35.0), "E");

        // T13: Grade at 90%
        assertEqual("T13: Grade at 90%",
                GradeCalculator.calculateGrade(90.0), "A+");

        // Boundary tests
        assertEqual("Grade at 100%", GradeCalculator.calculateGrade(100.0), "A+");
        assertEqual("Grade at 89.99%", GradeCalculator.calculateGrade(89.99), "A");
        assertEqual("Grade at 80%", GradeCalculator.calculateGrade(80.0), "A");
        assertEqual("Grade at 79.99%", GradeCalculator.calculateGrade(79.99), "B");
        assertEqual("Grade at 70%", GradeCalculator.calculateGrade(70.0), "B");
        assertEqual("Grade at 69.99%", GradeCalculator.calculateGrade(69.99), "C");
        assertEqual("Grade at 60%", GradeCalculator.calculateGrade(60.0), "C");
        assertEqual("Grade at 59.99%", GradeCalculator.calculateGrade(59.99), "D");
        assertEqual("Grade at 50%", GradeCalculator.calculateGrade(50.0), "D");
        assertEqual("Grade at 49.99%", GradeCalculator.calculateGrade(49.99), "E");
        assertEqual("Grade at 34.99%", GradeCalculator.calculateGrade(34.99), "F");
        assertEqual("Grade at 0%", GradeCalculator.calculateGrade(0.0), "F");

        System.out.println();
    }

    // ──── Input Validation Tests ────

    private static void testInputValidation() {
        System.out.println("--- Input Validation Tests ---");

        // T04: Negative marks
        assertNotNull("T04: Reject negative marks",
                InputValidator.validateMarks("-5", 100));

        // T05: Marks above maximum
        assertNotNull("T05: Reject marks above max",
                InputValidator.validateMarks("150", 100));

        // Valid marks
        assertNull("Accept valid marks (0)",
                InputValidator.validateMarks("0", 100));
        assertNull("Accept valid marks (50)",
                InputValidator.validateMarks("50", 100));
        assertNull("Accept valid marks (100)",
                InputValidator.validateMarks("100", 100));

        // Empty fields
        assertNotNull("Reject empty student name",
                InputValidator.validateStudentName(""));
        assertNotNull("Reject empty student ID",
                InputValidator.validateStudentId(""));
        assertNotNull("Reject empty roll number",
                InputValidator.validateRollNumber(""));

        // Invalid marks format
        assertNotNull("Reject non-numeric marks",
                InputValidator.validateMarks("abc", 100));
        assertNotNull("Reject empty marks",
                InputValidator.validateMarks("", 100));

        // Email validation
        assertNull("Accept valid email",
                InputValidator.validateEmail("test@example.com"));
        assertNull("Accept empty email (optional)",
                InputValidator.validateEmail(""));
        assertNotNull("Reject invalid email",
                InputValidator.validateEmail("not-an-email"));

        System.out.println();
    }

    // ──── Result Calculation Tests ────

    private static void testResultCalculation() {
        System.out.println("--- Result Calculation Tests ---");

        CsvFileHandler csvHandler = new CsvFileHandler();
        csvHandler.ensureDirectoryExists("data");

        StudentRepository studentRepo = new StudentRepository(csvHandler);
        SubjectRepository subjectRepo = new SubjectRepository(csvHandler);
        MarksRepository marksRepo = new MarksRepository(csvHandler);

        studentRepo.load();
        subjectRepo.load();
        marksRepo.load();

        ResultCalculator calculator = new ResultCalculator(studentRepo, subjectRepo, marksRepo);

        // T08: Verify calculation for STU001 (85+76+92+68+79 = 400/500 = 80%)
        StudentResult result1 = calculator.calculateStudentResult("STU001");
        if (result1 != null) {
            assertEqual("T08: STU001 total marks", String.format("%.0f", result1.getTotalMarks()), "400");
            assertEqual("T08: STU001 percentage", String.format("%.0f", result1.getPercentage()), "80");
            assertEqual("T08: STU001 grade", result1.getGrade(), "A");
        } else {
            reportFail("T08: STU001 result is null");
        }

        // T09: Student passes all subjects (STU001)
        if (result1 != null) {
            assertEqual("T09: STU001 passes all subjects",
                    result1.getResultStatus(), ApplicationConstants.RESULT_PASS);
        }

        // T10: Student fails one subject (STU005 has marks 30, 42, 28, 55, 33)
        StudentResult result5 = calculator.calculateStudentResult("STU005");
        if (result5 != null) {
            assertEqual("T10: STU005 overall result is FAIL",
                    result5.getResultStatus(), ApplicationConstants.RESULT_FAIL);
            assertCondition("T10: STU005 has failed subjects",
                    !result5.getFailedSubjects().isEmpty());
        } else {
            reportFail("T10: STU005 result is null");
        }

        // T11: STU009 has incomplete marks (only 3 subjects)
        StudentResult result9 = calculator.calculateStudentResult("STU009");
        if (result9 != null) {
            assertEqual("T11: STU009 result is INCOMPLETE",
                    result9.getResultStatus(), ApplicationConstants.RESULT_INCOMPLETE);
            assertCondition("T11: STU009 has missing subjects",
                    !result9.getMissingSubjects().isEmpty());
        } else {
            reportFail("T11: STU009 result is null");
        }

        // T20: All maximum marks (STU010: 100 in all 5 subjects)
        StudentResult result10 = calculator.calculateStudentResult("STU010");
        if (result10 != null) {
            assertEqual("T20: STU010 percentage is 100%",
                    String.format("%.0f", result10.getPercentage()), "100");
            assertEqual("T20: STU010 grade is A+", result10.getGrade(), "A+");
            assertEqual("T20: STU010 result is PASS",
                    result10.getResultStatus(), ApplicationConstants.RESULT_PASS);
        } else {
            reportFail("T20: STU010 result is null");
        }

        // T19: Merit list sorting
        List<StudentResult> allResults = calculator.calculateAllResults();
        assertCondition("T19: Results list is not empty", !allResults.isEmpty());

        // Check passed results are sorted by percentage descending
        boolean sortedCorrectly = true;
        double prevPercentage = Double.MAX_VALUE;
        for (StudentResult r : allResults) {
            if (ApplicationConstants.RESULT_PASS.equals(r.getResultStatus())) {
                if (r.getPercentage() > prevPercentage) {
                    sortedCorrectly = false;
                    break;
                }
                prevPercentage = r.getPercentage();
            }
        }
        assertCondition("T19: Passed results sorted by percentage descending", sortedCorrectly);

        System.out.println();
    }

    // ──── CSV Persistence Tests ────

    private static void testCsvPersistence() {
        System.out.println("--- CSV Persistence Tests ---");

        CsvFileHandler csvHandler = new CsvFileHandler();

        // T17: Test missing file handling
        List<String[]> result = csvHandler.readCsv("data/nonexistent.csv");
        assertCondition("T17: Missing file returns empty list", result.isEmpty());

        // T14: Verify data loads after restart (simulated by reloading)
        StudentRepository studentRepo = new StudentRepository(csvHandler);
        studentRepo.load();
        assertCondition("T14: Students loaded from existing file",
                studentRepo.getStudentCount() > 0);

        System.out.println();
    }

    // ──── Duplicate Detection Tests ────

    private static void testDuplicateDetection() {
        System.out.println("--- Duplicate Detection Tests ---");

        CsvFileHandler csvHandler = new CsvFileHandler();
        StudentRepository studentRepo = new StudentRepository(csvHandler);
        studentRepo.load();

        // T02: Add duplicate student ID
        Student duplicate = new Student("STU001", "Test Student", "99XX99",
                "Test", "1st", "");
        String error = studentRepo.addStudent(duplicate);
        assertNotNull("T02: Reject duplicate student ID", error);

        // T03: Add duplicate roll number
        Student dupRoll = new Student("STU999", "Test Student", "22CS101",
                "Test", "1st", "");
        error = studentRepo.addStudent(dupRoll);
        assertNotNull("T03: Reject duplicate roll number", error);

        System.out.println();
    }

    // ──── Edge Cases ────

    private static void testEdgeCases() {
        System.out.println("--- Edge Case Tests ---");

        // Grade boundary: exactly 35 should be E (Pass)
        assertEqual("Boundary: 35.0 -> E", GradeCalculator.calculateGrade(35.0), "E");

        // Grade boundary: 34.99 should be F
        assertEqual("Boundary: 34.99 -> F", GradeCalculator.calculateGrade(34.99), "F");

        // Grade boundary: 49.99 should be E
        assertEqual("Boundary: 49.99 -> E", GradeCalculator.calculateGrade(49.99), "E");

        // Grade boundary: 50.0 should be D
        assertEqual("Boundary: 50.0 -> D", GradeCalculator.calculateGrade(50.0), "D");

        // Max marks validation
        assertNotNull("Reject marks > max",
                InputValidator.validateMarks("101", 100));
        assertNull("Accept marks = max",
                InputValidator.validateMarks("100", 100));

        System.out.println();
    }

    // ──── Assertion Helpers ────

    private static void assertEqual(String testName, String actual, String expected) {
        if (expected.equals(actual)) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " — Expected: " + expected + ", Got: " + actual);
            failed++;
        }
    }

    private static void assertCondition(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName);
            failed++;
        }
    }

    private static void assertNotNull(String testName, Object value) {
        if (value != null) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " — Expected non-null");
            failed++;
        }
    }

    private static void assertNull(String testName, Object value) {
        if (value == null) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " — Expected null, Got: " + value);
            failed++;
        }
    }

    private static void reportFail(String message) {
        System.out.println("  [FAIL] " + message);
        failed++;
    }
}
