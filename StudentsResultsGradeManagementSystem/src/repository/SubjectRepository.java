package repository;

import model.Subject;
import util.ApplicationConstants;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for Subject records.
 * Manages loading, saving, and in-memory operations for academic subjects.
 * Initializes default subjects if no data file exists.
 */
public class SubjectRepository {

    private final CsvFileHandler csvHandler;
    private final Map<String, Subject> subjectsByCode; // SubjectCode -> Subject

    public SubjectRepository(CsvFileHandler csvHandler) {
        this.csvHandler = csvHandler;
        this.subjectsByCode = new LinkedHashMap<>();
    }

    /**
     * Loads subject records from CSV. If no file exists, creates default subjects.
     */
    public void load() {
        subjectsByCode.clear();

        List<String[]> records = csvHandler.readCsv(ApplicationConstants.SUBJECTS_FILE);

        if (records.isEmpty()) {
            // Initialize default subjects
            initializeDefaults();
            return;
        }

        for (String[] fields : records) {
            try {
                if (fields.length >= 4) {
                    String code = fields[0];
                    String name = fields[1];
                    double maxMarks = Double.parseDouble(fields[2]);
                    double minPassing = Double.parseDouble(fields[3]);

                    if (code.isEmpty() || name.isEmpty()) {
                        System.err.println("[WARNING] Skipping subject with missing fields.");
                        continue;
                    }
                    if (maxMarks <= 0 || minPassing < 0 || minPassing > maxMarks) {
                        System.err.println("[WARNING] Skipping subject with invalid marks: " + code);
                        continue;
                    }

                    Subject subject = new Subject(code, name, maxMarks, minPassing);
                    subjectsByCode.put(code.toUpperCase(), subject);
                }
            } catch (NumberFormatException e) {
                System.err.println("[WARNING] Skipping subject with invalid number format: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[WARNING] Error parsing subject record: " + e.getMessage());
            }
        }
        System.out.println("[INFO] Loaded " + subjectsByCode.size() + " subjects.");
    }

    /**
     * Initializes the default five subjects as specified in the requirements.
     */
    private void initializeDefaults() {
        for (String[] def : ApplicationConstants.DEFAULT_SUBJECTS) {
            Subject subject = new Subject(def[0], def[1],
                    Double.parseDouble(def[2]), Double.parseDouble(def[3]));
            subjectsByCode.put(def[0].toUpperCase(), subject);
        }
        try {
            save();
            System.out.println("[INFO] Initialized " + subjectsByCode.size() + " default subjects.");
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save default subjects: " + e.getMessage());
        }
    }

    /**
     * Saves all subject records to the CSV file.
     */
    public void save() throws IOException {
        List<String> lines = subjectsByCode.values().stream()
                .map(Subject::toCsvString)
                .collect(Collectors.toList());
        csvHandler.writeCsv(ApplicationConstants.SUBJECTS_FILE,
                ApplicationConstants.SUBJECTS_HEADER, lines);
    }

    /**
     * Adds a new subject.
     * @return error message or null if successful
     */
    public String addSubject(Subject subject) {
        String upperCode = subject.getSubjectCode().toUpperCase();
        if (subjectsByCode.containsKey(upperCode)) {
            return "Subject Code '" + subject.getSubjectCode() + "' already exists.";
        }

        subjectsByCode.put(upperCode, subject);
        try {
            save();
        } catch (IOException e) {
            subjectsByCode.remove(upperCode);
            return "Failed to save subject data: " + e.getMessage();
        }
        return null;
    }

    /**
     * Updates an existing subject.
     * @return error message or null if successful
     */
    public String updateSubject(String originalCode, Subject updated) {
        String upperOrigCode = originalCode.toUpperCase();
        String upperNewCode = updated.getSubjectCode().toUpperCase();

        Subject original = subjectsByCode.get(upperOrigCode);
        if (original == null) {
            return "Original subject not found.";
        }

        if (!upperOrigCode.equals(upperNewCode) && subjectsByCode.containsKey(upperNewCode)) {
            return "Subject Code '" + updated.getSubjectCode() + "' already exists.";
        }

        Subject backup = new Subject(original.getSubjectCode(), original.getSubjectName(),
                original.getMaxMarks(), original.getMinPassingMarks());

        subjectsByCode.remove(upperOrigCode);
        subjectsByCode.put(upperNewCode, updated);

        try {
            save();
        } catch (IOException e) {
            subjectsByCode.remove(upperNewCode);
            subjectsByCode.put(upperOrigCode, backup);
            return "Failed to save subject data: " + e.getMessage();
        }
        return null;
    }

    /**
     * Deletes a subject by code.
     * @return error message or null if successful
     */
    public String deleteSubject(String subjectCode) {
        String upperCode = subjectCode.toUpperCase();
        Subject subject = subjectsByCode.get(upperCode);
        if (subject == null) {
            return "Subject not found.";
        }

        subjectsByCode.remove(upperCode);
        try {
            save();
        } catch (IOException e) {
            subjectsByCode.put(upperCode, subject);
            return "Failed to save changes: " + e.getMessage();
        }
        return null;
    }

    public Subject getSubjectByCode(String code) {
        return subjectsByCode.get(code.toUpperCase());
    }

    public List<Subject> getAllSubjects() {
        return new ArrayList<>(subjectsByCode.values());
    }

    public int getSubjectCount() {
        return subjectsByCode.size();
    }

    public boolean subjectExists(String code) {
        return subjectsByCode.containsKey(code.toUpperCase());
    }
}
