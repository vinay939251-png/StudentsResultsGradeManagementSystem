package service;

import util.ApplicationConstants;

/**
 * Dedicated class for grade calculation.
 * Applies the documented grading scale consistently.
 *
 * Grading Scale:
 *   90–100    -> A+ (Outstanding)
 *   80–89.99  -> A  (Excellent)
 *   70–79.99  -> B  (Very Good)
 *   60–69.99  -> C  (Good)
 *   50–59.99  -> D  (Satisfactory)
 *   35–49.99  -> E  (Pass)
 *   Below 35  -> F  (Fail)
 */
public class GradeCalculator {

    /**
     * Calculates the grade based on the given percentage.
     * @param percentage the student's overall percentage (0 to 100)
     * @return the grade string (A+, A, B, C, D, E, or F)
     */
    public static String calculateGrade(double percentage) {
        if (percentage >= ApplicationConstants.GRADE_A_PLUS_MIN) {
            return ApplicationConstants.GRADE_A_PLUS;
        } else if (percentage >= ApplicationConstants.GRADE_A_MIN) {
            return ApplicationConstants.GRADE_A;
        } else if (percentage >= ApplicationConstants.GRADE_B_MIN) {
            return ApplicationConstants.GRADE_B;
        } else if (percentage >= ApplicationConstants.GRADE_C_MIN) {
            return ApplicationConstants.GRADE_C;
        } else if (percentage >= ApplicationConstants.GRADE_D_MIN) {
            return ApplicationConstants.GRADE_D;
        } else if (percentage >= ApplicationConstants.GRADE_E_MIN) {
            return ApplicationConstants.GRADE_E;
        } else {
            return ApplicationConstants.GRADE_F;
        }
    }

    /**
     * Gets the description for a given grade.
     * @param grade the grade string
     * @return a descriptive label for the grade
     */
    public static String getGradeDescription(String grade) {
        switch (grade) {
            case "A+": return "Outstanding";
            case "A":  return "Excellent";
            case "B":  return "Very Good";
            case "C":  return "Good";
            case "D":  return "Satisfactory";
            case "E":  return "Pass";
            case "F":  return "Fail";
            default:   return "Unknown";
        }
    }
}
