package util;

/**
 * Central class for application-wide constants.
 * All grading boundaries, file paths, and configuration values are defined here
 * to avoid magic numbers and duplicated values across the codebase.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
        // Prevent instantiation
    }

    // ──── Application Info ────
    public static final String APP_TITLE = "Students Results and Grade Management System";
    public static final String APP_VERSION = "1.0";

    // ──── Data Directory and File Paths ────
    public static final String DATA_DIR = "data";
    public static final String STUDENTS_FILE = DATA_DIR + "/students.csv";
    public static final String SUBJECTS_FILE = DATA_DIR + "/subjects.csv";
    public static final String MARKS_FILE = DATA_DIR + "/marks.csv";

    // ──── CSV Headers ────
    public static final String STUDENTS_HEADER = "StudentID,FullName,RollNumber,Department,Semester,Email";
    public static final String SUBJECTS_HEADER = "SubjectCode,SubjectName,MaxMarks,MinPassingMarks";
    public static final String MARKS_HEADER = "StudentID,SubjectCode,MarksObtained";

    // ──── CSV Delimiter ────
    public static final String CSV_DELIMITER = ",";

    // ──── Default Subjects ────
    public static final String[][] DEFAULT_SUBJECTS = {
        {"CS101", "Mathematics", "100", "35"},
        {"CS102", "Data Structures", "100", "35"},
        {"CS103", "Object Oriented Programming", "100", "35"},
        {"CS104", "Database Management Systems", "100", "35"},
        {"CS105", "Computer Networks", "100", "35"}
    };

    // ──── Grading Scale ────
    public static final double GRADE_A_PLUS_MIN = 90.0;
    public static final double GRADE_A_MIN = 80.0;
    public static final double GRADE_B_MIN = 70.0;
    public static final double GRADE_C_MIN = 60.0;
    public static final double GRADE_D_MIN = 50.0;
    public static final double GRADE_E_MIN = 35.0;
    // Below 35 is F

    // ──── Result Statuses ────
    public static final String RESULT_PASS = "PASS";
    public static final String RESULT_FAIL = "FAIL";
    public static final String RESULT_INCOMPLETE = "INCOMPLETE";

    // ──── Grade Labels ────
    public static final String GRADE_A_PLUS = "A+";
    public static final String GRADE_A = "A";
    public static final String GRADE_B = "B";
    public static final String GRADE_C = "C";
    public static final String GRADE_D = "D";
    public static final String GRADE_E = "E";
    public static final String GRADE_F = "F";

    // ──── UI Colors (H2A Luxury Dark & Gradient Palette) ────
    public static final int[] COLOR_PRIMARY = {255, 0, 85};       // H2A Electric Neon Magenta (#FF0055)
    public static final int[] COLOR_SUCCESS = {16, 185, 129};      // Vibrant Emerald Green (#10B981)
    public static final int[] COLOR_DANGER = {244, 63, 94};        // Rose Crimson (#F43F5E)
    public static final int[] COLOR_WARNING = {245, 158, 11};      // Warm Amber (#F59E0B)
    public static final int[] COLOR_INFO = {123, 44, 191};         // Electric Violet (#7B2CBF)
    public static final int[] COLOR_DARK = {9, 7, 16};             // Deep Obsidian (#090710)
    public static final int[] COLOR_LIGHT_BG = {19, 15, 34};       // Dark Surface (#130F22)
    public static final int[] COLOR_WHITE = {255, 255, 255};       // Pure White
    public static final int[] COLOR_CARD_BG = {25, 20, 43};        // Elevated Card Surface (#19142B)
    public static final int[] COLOR_SIDEBAR_BG = {14, 10, 24};     // Dark Sidebar Slate (#0E0A18)

    // ──── UI Dimensions ────
    public static final int MAIN_WINDOW_WIDTH = 1200;
    public static final int MAIN_WINDOW_HEIGHT = 750;
    public static final int SIDEBAR_WIDTH = 220;

    // ──── Sample Data Student IDs ────
    public static final String SAMPLE_DATA_PREFIX = "STU";
}
