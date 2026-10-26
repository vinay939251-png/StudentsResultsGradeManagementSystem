# Students Results and Grade Management System

## Formal Project Report

**Course:** Java Programming and Object-Oriented Software Development Laboratory  
**Batch:** 2024-CSE-JAVA-B1  
**Department:** Department of Computer Science and Engineering  
**Institution:** Vardhaman College of Engineering, Hyderabad  
**Report status:** Concise implementation report

## Abstract

The Students Results and Grade Management System is a standalone Java Swing desktop application for maintaining student records, subjects, examination marks, grades, and class results. It uses local UTF-8 CSV files for persistence and requires no database or internet connection. The system supports student and subject management, marks entry and update, automatic result calculation, grade assignment, individual result reports, class merit lists, search, filters, validation, and confirmation dialogs.

The implementation follows a simple layered architecture with model, repository, service, user-interface, and utility packages. The executed Java test runner reported **50 passed tests and 0 failed tests**, covering grade boundaries, input validation, result calculation, duplicate detection, CSV loading, and merit-list ordering.

## Objectives and Scope

The project was developed to:

- maintain student profiles with unique student IDs and roll numbers;
- configure subjects, maximum marks, and minimum passing marks;
- enter and update subject-wise examination marks;
- calculate totals, averages, percentages, grades, and result status;
- identify failed and missing subjects clearly;
- produce individual report cards and class-level results;
- preserve data between sessions using CSV files; and
- demonstrate Java OOP, collections, exception handling, Swing GUI development, validation, and file handling.

The application is intended for local single-computer use. Authentication, database servers, cloud synchronization, web access, and internal PDF export are outside the implemented scope.

## Technology Stack

- **Language:** Java
- **GUI:** Java Swing
- **Persistence:** UTF-8 CSV files
- **Libraries:** Standard JDK libraries only
- **Database:** None
- **Network dependency:** None
- **Build method:** Direct `javac` compilation

## System Architecture

The project uses a simple layered design:

- **model:** `Student`, `Subject`, `MarkRecord`, and `StudentResult`.
- **repository:** CSV handling and repositories for students, subjects, and marks.
- **service:** `GradeCalculator` and `ResultCalculator`.
- **ui:** `MainFrame`, dashboard, student, subject, marks, results, and report panels.
- **util:** Application constants and reusable input validation.

The GUI receives user input, validates it, calls repository or service methods, and refreshes the relevant Swing panel. Repositories handle CSV loading and saving separately from the GUI.

## Main Features

### Student Management

Student records contain student ID, full name, roll number, department, semester, and optional email. The application prevents duplicate student IDs and roll numbers. Users can add, search, update, clear, and delete records. Deleting a student also removes the associated marks after confirmation.

### Subject Management

Subjects contain a unique subject code, subject name, maximum marks, and minimum passing marks. The default demonstration dataset contains five subjects, each with a maximum of 100 and a minimum passing mark of 35.

### Marks Entry

The user selects a registered student and subject, enters marks, and saves the record. Negative marks, values above the configured maximum, empty values, invalid numeric input, and unregistered students or subjects are rejected. Saving an existing student-subject pair updates the existing record.

### Result Calculation

For a completed set of required subjects:

```text
Total Marks = Sum of valid subject marks
Percentage = (Total Marks / Maximum Possible Marks) × 100
Average = Total Marks / Number of Subjects
```

A student is marked `PASS` only when all required subjects have marks and meet their minimum passing marks. A missing required mark produces `INCOMPLETE`. A completed record with one or more failed subjects produces `FAIL`.

### Grading Scale

| Percentage | Grade | Description |
|---:|:---:|---|
| 90–100 | A+ | Outstanding |
| 80–89.99 | A | Excellent |
| 70–79.99 | B | Very Good |
| 60–69.99 | C | Good |
| 50–59.99 | D | Satisfactory |
| 35–49.99 | E | Pass |
| Below 35 | F | Fail |

### Class Results

Completed results are sorted by percentage in descending order. Incomplete records are displayed separately or after completed results and are not ranked as completed results. The class-results screen supports searching and filtering by `PASS`, `FAIL`, and `INCOMPLETE`.

### User Interface

The current Java Swing visual system uses a warm paper background, ink-black navigation, a restrained vermilion action color, warm yellow highlights, flat cards, consistent primary/outline/danger buttons, readable tables, and clear status colors. The visual direction is an editorial desktop interpretation inspired by the reference style of h2a.lu while remaining strictly Java Swing.

## Data Storage

The application stores data in the local `data` directory:

```text
data/students.csv
data/subjects.csv
data/marks.csv
```

Files are loaded at startup and saved after successful create, update, and delete operations. Missing files are handled gracefully, and default subjects are initialized where appropriate.

## Testing and Verification

The Java test runner was executed after compiling all project sources.

| Measure | Result |
|---|---:|
| Total tests | 50 |
| Passed | 50 |
| Failed | 0 |

The tests verified:

- grade boundaries at 34.99, 35, 49.99, 50, 89.99, and 90;
- rejection of negative, excessive, empty, and non-numeric marks;
- required-field and email validation;
- duplicate student ID and roll-number rejection;
- completed, failed, incomplete, and perfect results;
- merit-list ordering;
- missing CSV-file handling; and
- loading of saved student data.

The Swing application was also launched successfully in a startup smoke test.

## Demonstration Workflow

1. Start the application using `run.bat`.
2. Review dashboard statistics.
3. Open Students and view or add a student.
4. Review configured subjects.
5. Open Marks Entry and enter marks for a student.
6. Open the individual report card.
7. Review calculated totals, percentage, grade, and status.
8. Open Class Results and inspect the merit list.
9. Demonstrate validation using an invalid mark or duplicate ID.
10. Restart the application and confirm that CSV data remains available.

## Limitations and Future Scope

The current system is local and single-user. It does not include authentication, multi-user access, database integration, cloud backup, web access, or internal PDF export. Future versions could add those features without changing the core result-calculation rules.

## Conclusion

The completed application provides a functional, dependency-free Java Swing solution for student result and grade management. Its layered architecture keeps file handling, calculation logic, validation, and presentation separate. The system meets its core academic-project objectives and has been verified by 50 passing automated tests.

## References

1. Oracle Corporation, [Java Platform, Standard Edition Documentation](https://docs.oracle.com/en/java/javase/21/).
2. Oracle Corporation, [Creating a GUI With Swing](https://docs.oracle.com/javase/tutorial/uiswing/).
3. h2a Agency Luxembourg, [h2a — Creative Communication, Strategy and Interactive Digital Portfolios](https://www.h2a.lu/).
