package repository;

import model.MarkRecord;
import util.ApplicationConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for examination mark records.
 * Manages the student-subject marks with duplicate prevention and update support.
 */
public class MarksRepository {

    private final CsvFileHandler csvHandler;
    // Key: "STUDENTID_SUBJECTCODE" -> MarkRecord
    private final Map<String, MarkRecord> marksByKey;

    public MarksRepository(CsvFileHandler csvHandler) {
        this.csvHandler = csvHandler;
        this.marksByKey = new LinkedHashMap<>();
    }

    /**
     * Loads mark records from the CSV file.
     */
    public void load() {
        marksByKey.clear();

        List<String[]> records = csvHandler.readCsv(ApplicationConstants.MARKS_FILE);
        for (String[] fields : records) {
            try {
                if (fields.length >= 3) {
                    String studentId = fields[0];
                    String subjectCode = fields[1];
                    double marks = Double.parseDouble(fields[2]);

                    if (studentId.isEmpty() || subjectCode.isEmpty()) {
                        System.err.println("[WARNING] Skipping mark record with missing fields.");
                        continue;
                    }
                    if (marks < 0) {
                        System.err.println("[WARNING] Skipping mark record with negative marks for: " +
                                studentId + ", " + subjectCode);
                        continue;
                    }

                    MarkRecord record = new MarkRecord(studentId, subjectCode, marks);
                    marksByKey.put(record.getKey().toUpperCase(), record);
                }
            } catch (NumberFormatException e) {
                System.err.println("[WARNING] Skipping mark record with invalid marks: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[WARNING] Error parsing mark record: " + e.getMessage());
            }
        }
        System.out.println("[INFO] Loaded " + marksByKey.size() + " mark records.");
    }

    /**
     * Saves all mark records to the CSV file.
     */
    public void save() throws IOException {
        List<String> lines = marksByKey.values().stream()
                .map(MarkRecord::toCsvString)
                .collect(Collectors.toList());
        csvHandler.writeCsv(ApplicationConstants.MARKS_FILE,
                ApplicationConstants.MARKS_HEADER, lines);
    }

    /**
     * Adds or updates a mark record. If a record for the student-subject pair exists,
     * it is updated; otherwise a new record is created.
     * @return error message or null if successful
     */
    public String saveOrUpdateMark(MarkRecord record) {
        String key = record.getKey().toUpperCase();
        MarkRecord backup = marksByKey.get(key);

        marksByKey.put(key, record);

        try {
            save();
        } catch (IOException e) {
            // Rollback
            if (backup != null) {
                marksByKey.put(key, backup);
            } else {
                marksByKey.remove(key);
            }
            return "Failed to save marks: " + e.getMessage();
        }
        return null;
    }

    /**
     * Gets all mark records for a specific student.
     */
    public List<MarkRecord> getMarksByStudentId(String studentId) {
        String upperStudentId = studentId.toUpperCase();
        return marksByKey.values().stream()
                .filter(m -> m.getStudentId().toUpperCase().equals(upperStudentId))
                .collect(Collectors.toList());
    }

    /**
     * Gets a specific mark record for a student-subject pair.
     */
    public MarkRecord getMark(String studentId, String subjectCode) {
        String key = (studentId.trim() + "_" + subjectCode.trim()).toUpperCase();
        return marksByKey.get(key);
    }

    /**
     * Checks if a mark record exists for a student-subject pair.
     */
    public boolean markExists(String studentId, String subjectCode) {
        String key = (studentId.trim() + "_" + subjectCode.trim()).toUpperCase();
        return marksByKey.containsKey(key);
    }

    /**
     * Deletes all mark records for a specific student.
     * Used when deleting a student.
     * @return error message or null if successful
     */
    public String deleteMarksByStudentId(String studentId) {
        String upperStudentId = studentId.toUpperCase();
        Map<String, MarkRecord> backup = new LinkedHashMap<>(marksByKey);

        marksByKey.entrySet().removeIf(e ->
                e.getValue().getStudentId().toUpperCase().equals(upperStudentId));

        try {
            save();
        } catch (IOException e) {
            marksByKey.clear();
            marksByKey.putAll(backup);
            return "Failed to delete student marks: " + e.getMessage();
        }
        return null;
    }

    /**
     * Checks if any marks exist for a given subject code.
     * Used to prevent deletion of subjects that have dependent results.
     */
    public boolean hasMarksForSubject(String subjectCode) {
        String upperCode = subjectCode.toUpperCase();
        return marksByKey.values().stream()
                .anyMatch(m -> m.getSubjectCode().toUpperCase().equals(upperCode));
    }

    /**
     * Gets all mark records.
     */
    public List<MarkRecord> getAllMarks() {
        return new ArrayList<>(marksByKey.values());
    }

    /**
     * Returns the count of unique students who have any marks recorded.
     */
    public long getStudentsWithMarksCount() {
        return marksByKey.values().stream()
                .map(m -> m.getStudentId().toUpperCase())
                .distinct()
                .count();
    }
}
