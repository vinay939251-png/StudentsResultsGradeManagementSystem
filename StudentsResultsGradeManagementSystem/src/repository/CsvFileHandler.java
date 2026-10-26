package repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Low-level CSV file handler providing read and write operations.
 * Handles file creation, UTF-8 encoding, and CSV parsing with proper
 * handling of commas, quotes, and special characters within fields.
 */
public class CsvFileHandler {

    /**
     * Reads all lines from a CSV file, skipping the header row.
     * If the file does not exist, returns an empty list and logs a message.
     *
     * @param filePath the path to the CSV file
     * @return a list of string arrays, each representing a parsed CSV row
     */
    public List<String[]> readCsv(String filePath) {
        List<String[]> records = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            System.out.println("[INFO] File not found, will be created on save: " + filePath);
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                try {
                    String[] fields = parseCsvLine(line);
                    records.add(fields);
                } catch (Exception e) {
                    System.err.println("[WARNING] Skipping malformed CSV line: " + line);
                    System.err.println("  Reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to read file: " + filePath);
            System.err.println("  Reason: " + e.getMessage());
        }

        return records;
    }

    /**
     * Writes all records to a CSV file, creating the file and directories if needed.
     * Overwrites the existing file content to ensure data integrity.
     *
     * @param filePath the path to the CSV file
     * @param header   the header row string
     * @param lines    the data lines to write
     * @throws IOException if writing fails
     */
    public void writeCsv(String filePath, String header, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);

        // Ensure the parent directory exists
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        // Write to a temporary file first, then move (atomic write to prevent corruption)
        Path tempFile = path.resolveSibling(path.getFileName() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(header);
            writer.newLine();
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }

        // Replace the original file atomically
        try {
            Files.move(tempFile, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            // Fallback for filesystems that don't support atomic move
            Files.move(tempFile, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Ensures the data directory exists.
     * @param dirPath the directory path to create
     */
    public void ensureDirectoryExists(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("[INFO] Created data directory: " + dirPath);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to create directory: " + dirPath);
            System.err.println("  Reason: " + e.getMessage());
        }
    }

    /**
     * Parses a single CSV line, properly handling quoted fields that may contain
     * commas, newlines, or escaped quotes.
     *
     * @param line the CSV line to parse
     * @return array of field values
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    // Check for escaped quote (double quote)
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // Skip next quote
                    } else {
                        inQuotes = false; // End of quoted field
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(current.toString().trim());
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString().trim());

        return fields.toArray(new String[0]);
    }
}
