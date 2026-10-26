# Students Results and Grade Management System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![GUI](https://img.shields.io/badge/GUI-Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Storage](https://img.shields.io/badge/Storage-CSV%20Persistence-28A745?style=for-the-badge)](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/data)
[![Tests](https://img.shields.io/badge/Tests-50%2F50%20Passing-success?style=for-the-badge)](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/TestRunner.java)
[![Dependencies](https://img.shields.io/badge/Dependencies-Zero%20(Pure%20JDK)-blue?style=for-the-badge)](#system-requirements)

A standalone, professional desktop application built in Java Swing for academic institutions, faculty members, and administrators to manage student records, configure course subjects, record examination marks, compute academic performance metrics, assign grades, and generate individual or class-level result cards.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Grading System](#grading-system)
- [System Architecture](#system-architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Compiling and Running (Quick Start)](#compiling-and-running-quick-start)
  - [Manual Compilation & Execution](#manual-compilation--execution)
- [Automated Testing](#automated-testing)
- [Data Storage Schema](#data-storage-schema)
- [Academic Project Report](#academic-project-report)

---

## Overview

The **Students Results and Grade Management System** eliminates manual arithmetic errors and streamlines student evaluation in academic institutions. Built strictly with standard Java (JDK), the application runs locally without external database installations, third-party libraries, or web server dependencies.

All data persists in human-readable CSV format, enabling seamless portability, easy backup, and auditability.

---

## Key Features

### 1. Executive Dashboard
- Real-time statistics displaying **Total Registered Students**, **Configured Subjects**, **Passed Students**, **Failed Students**, and **Overall Class Pass Percentage**.
- Dynamically recalculates metrics as records and marks change.

### 2. Student Management Module
- Register new students with validation (Student ID, Full Name, Roll Number, Department, Semester, and optional Email).
- Prevents duplicate Student IDs and Roll Numbers.
- Search and filter students by ID, Roll Number, or Name.
- Update and delete student records with relational integrity checks.

### 3. Subject Management Module
- Add and configure academic subjects with unique subject codes, names, maximum marks, and minimum passing marks.
- Pre-configured with standard 5-subject curriculum (Maximum: 100, Passing: 35).
- Prevents accidental deletion of subjects with recorded marks.

### 4. Examination Marks Entry
- Subject-wise mark recording and updating per student.
- Range validation ($0 \le \text{marks} \le \text{max marks}$) and numeric format checks.
- Enforces uniqueness per student-subject pair to prevent duplicate score entries.

### 5. Automated Result & Grade Engine
- Separated business logic layer computing **Total Marks**, **Maximum Marks**, **Average**, and **Percentage**.
- Accurately identifies passed and failed subjects against passing thresholds.
- Distinguishes incomplete score entries (`INCOMPLETE`) from final failures (`FAIL`) or passes (`PASS`).
- Precise floating-point arithmetic with strict boundary checking.

### 6. Comprehensive Result Reports
- **Class-Level Results**: Filterable and searchable tabular view showing status, rank, percentage, and grade for all enrolled students.
- **Individual Student Grade Card**: Detailed report view presenting student demographics, subject-by-subject score breakdown, pass/fail status, total aggregate, and final letter grade.

---

## Grading System

The application implements the standard academic grading scale:

| Percentage Range | Grade | Description | Status |
|---|:---:|---|:---:|
| **90.00% – 100.00%** | **A+** | Outstanding | PASS |
| **80.00% – 89.99%** | **A** | Excellent | PASS |
| **70.00% – 79.99%** | **B** | Very Good | PASS |
| **60.00% – 69.99%** | **C** | Good | PASS |
| **50.00% – 59.99%** | **D** | Satisfactory | PASS |
| **35.00% – 49.99%** | **E** | Pass | PASS |
| **Below 35.00%** | **F** | Fail | FAIL |
| *Missing Subjects* | **INCOMPLETE** | Marks Pending | INCOMPLETE |

> **Rule**: An overall status of `PASS` is awarded **only if** the student achieves $\ge 35$ marks in **every** enrolled subject and all required marks are present.

---

## System Architecture

The project adheres to the **Model-View-Service-Repository** architectural pattern:

```
[ UI Layer (Swing Panels) ]
         │
         ▼
[ Service Layer (Calculations & Business Logic) ]
         │
         ▼
[ Repository Layer (CSV File Handlers) ]
         │
         ▼
[ Data Files (*.csv in /data) ]
```

- **`model`**: Domain entities ([Student](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/model/Student.java), [Subject](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/model/Subject.java), [MarkRecord](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/model/MarkRecord.java), [StudentResult](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/model/StudentResult.java)).
- **`repository`**: File persistence layer handling reading, writing, and parsing CSV files ([StudentRepository](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/repository/StudentRepository.java), [SubjectRepository](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/repository/SubjectRepository.java), [MarksRepository](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/repository/MarksRepository.java), [CsvFileHandler](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/repository/CsvFileHandler.java)).
- **`service`**: Core evaluation engines ([ResultCalculator](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/service/ResultCalculator.java), [GradeCalculator](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/service/GradeCalculator.java)).
- **`ui`**: Swing graphical interface, customized dialogs, and panels ([MainFrame](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/MainFrame.java), [DashboardPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/DashboardPanel.java), [StudentPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/StudentPanel.java), [SubjectPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/SubjectPanel.java), [MarksPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/MarksPanel.java), [ResultsPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/ResultsPanel.java), [ResultReportPanel](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/ResultReportPanel.java)).
- **`ui.theme`**: Custom modern aesthetic styling ([H2ATheme](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/ui/theme/H2ATheme.java)).
- **`util`**: Centralized constants and input validation utilities ([ApplicationConstants](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/util/ApplicationConstants.java), [InputValidator](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/StudentsResultsGradeManagementSystem/src/util/InputValidator.java)).

---

## Project Structure

```
├── .gitignore                                     # Git ignore patterns
├── compile.bat                                    # Root batch build script
├── run.bat                                        # Root batch launch script
├── Readme.md                                      # Project documentation
├── StudentsResultsGradeManagementSystem/          # Main application root
│   ├── compile.bat                                # Application build script
│   ├── run.bat                                    # Application launch script
│   ├── data/                                      # Persistent CSV storage
│   │   ├── students.csv                           # Enrolled students
│   │   ├── subjects.csv                           # Subject configurations
│   │   └── marks.csv                              # Examination marks records
│   └── src/                                       # Java source code
│       ├── Main.java                              # Application entry point
│       ├── TestRunner.java                        # Automated unit & integration tests
│       ├── model/                                 # Data models
│       ├── repository/                            # Data access & CSV handlers
│       ├── service/                               # Business calculation engines
│       ├── ui/                                    # Swing views and panels
│       │   └── theme/                             # UI color & typography theme
│       └── util/                                  # Constants and validators
└── extracted_template/                            # Academic Report Documentation
    └── Course End Project Report/
        ├── main.tex                               # LaTeX report source
        ├── main.pdf                               # Compiled academic report
        └── College Logo Grayscale.jpg             # Report media asset
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17** or higher installed.
- Verify your Java environment:
  ```bash
  java -version
  javac -version
  ```

### Compiling and Running (Quick Start)

On Windows, you can compile and launch the application directly using the included batch scripts:

1. **Compile**: Double-click [`compile.bat`](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/compile.bat)
2. **Launch**: Double-click [`run.bat`](file:///c:/Users/vinay/OneDrive/Desktop/26885A0549/run.bat)

### Manual Compilation & Execution

#### On Windows (PowerShell / Command Prompt):

```powershell
# Navigate to the application directory
cd StudentsResultsGradeManagementSystem

# Compile all source files into the out directory
javac -d out (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)

# Launch the application
java -cp out Main
```

#### On Linux / macOS:

```bash
cd StudentsResultsGradeManagementSystem

# Compile
javac -d out $(find src -name "*.java")

# Run
java -cp out Main
```

---

## Automated Testing

The project includes a standalone test suite covering grade calculation boundary cases, data validation, duplicate detection, and file persistence.

To execute the test suite:

```bash
cd StudentsResultsGradeManagementSystem
java -cp out TestRunner
```

**Test Coverage Summary:**
- **Grade Calculation**: Boundary thresholds (0%, 34.99%, 35%, 49.99%, 50%, 89.99%, 90%, 100%).
- **Input Validation**: Null/empty rejection, non-numeric checks, range checks, email formatting.
- **Result Logic**: Pass/Fail criteria, incomplete mark handling, percentage sorting.
- **Persistence & Integrity**: Missing file handling, duplicate student IDs, and duplicate roll numbers.
- **Result**: `50 passed, 0 failed (100% pass rate)`.

---

## Data Storage Schema

Data is stored in the `StudentsResultsGradeManagementSystem/data/` folder as comma-separated values:

- **`students.csv`**:
  ```csv
  StudentID,FullName,RollNumber,Department,Semester,Email
  STU001,Aarav Sharma,21CS101,Computer Science,Semester 4,aarav.sharma@example.edu
  ```

- **`subjects.csv`**:
  ```csv
  SubjectCode,SubjectName,MaxMarks,PassingMarks
  CS401,Java Programming,100,35
  ```

- **`marks.csv`**:
  ```csv
  StudentID,SubjectCode,MarksObtained
  STU001,CS401,88.5
  ```

---

## Academic Project Report

A complete project report conforming to institutional formatting guidelines is provided:
- **LaTeX Source**: `extracted_template/Course End Project Report/main.tex`
- **Compiled PDF**: `extracted_template/Course End Project Report/main.pdf`

---

## License

Developed as an academic course-end project for student result and academic grade evaluation.