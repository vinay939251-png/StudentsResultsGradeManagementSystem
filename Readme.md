# MASTER PROMPT FOR ANTIGRAVITY
## Students Results and Grade Management System — Java Course-End Project

### 1. Your Role and Objective

Act as a senior Java developer, software engineer, software tester, and academic project report writer.

Build a **complete, functional, beginner-friendly, well-organized Java desktop application** titled:

**“STUDENTS RESULTS AND GRADE MANAGEMENT SYSTEM”**

This is a college course-end project. It must demonstrate a practical application of Java programming, object-oriented programming, exception handling, collections, file handling, and GUI development.

The project should be polished enough for a live demonstration to faculty members and suitable for submission as an academic project report.

There are TWO equally important deliverables:

1. A fully working Java application with complete source code.
2. A complete LaTeX academic project report that strictly follows the supplied template ZIP.

**Do not start by writing generic code or creating a generic report. First inspect the supplied template, then plan and implement the project around the requirements below.**

---

# PART A — MANDATORY TEMPLATE INSPECTION

### 2. Inspect the Supplied LaTeX ZIP Before Doing Anything Else

I will provide a ZIP file named:

`Course End Report Latex Template.zip`

You must locate and inspect this uploaded ZIP in the project workspace before creating or modifying any report files.

#### Required inspection procedure

1. Locate the uploaded ZIP file.
2. Extract its contents into a dedicated template-inspection directory.
3. Identify the main `.tex` document.
4. Inspect every relevant `.tex`, `.cls`, `.sty`, `.bib`, image, configuration, and supporting file.
5. Examine the title page, certificate, declaration, acknowledgement, abstract, table of contents, chapter organization, headings, page numbering, margins, spacing, fonts, tables, figures, bibliography, and appendices wherever present.
6. Identify all placeholders that require project-specific information.
7. Identify the intended LaTeX compiler and any special package or file dependencies.
8. Determine how the original template is supposed to be compiled.
9. Record the template's exact section order and formatting requirements in your development notes.

If the ZIP contains multiple possible main documents, inspect them and determine which one is the intended report entry point. Do not arbitrarily choose one without checking.

### 3. Strict Template Compliance — Highest Priority

**The supplied LaTeX template is authoritative. Do not substitute your own report format.**

You must:

- Preserve the original document structure and section order.
- Preserve the original title-page design and institutional formatting.
- Preserve all existing mandatory front matter and back matter.
- Preserve the original chapter hierarchy and numbering conventions.
- Preserve existing page dimensions, margins, fonts, line spacing, paragraph spacing, and heading styles.
- Preserve the original table of contents, list of figures, and list of tables wherever included.
- Preserve the existing citation and bibliography format.
- Preserve required declaration, certificate, acknowledgement, and approval pages.
- Retain template-specific commands, environments, custom macros, and class files.
- Fill placeholders with the project's actual content.
- Add project chapters only in a way that complies with the template's existing structure.
- Use the template's existing formatting commands rather than introducing competing styles.

Do not delete a section just because it appears unnecessary. If a section is mandatory in the template but its required information is unavailable, preserve the section and use an explicit placeholder for that information.

Do not invent college names, faculty names, university details, signatures, approval dates, registration numbers, or personal details. Ask for missing mandatory information or leave clearly marked placeholders.

**Do not create a fresh `report.tex` from scratch and merely imitate the template. Modify and populate the supplied template itself.**

If the supplied template conflicts with a suggested report structure in this prompt, follow the template and adapt the project content to it.

Before implementation, create a short internal checklist of template requirements. Before delivery, verify each requirement against the final PDF and source files.

---

# PART B — PROJECT OVERVIEW

### 4. Project Purpose

The Students Results and Grade Management System is a desktop application for maintaining student information, entering examination marks, calculating academic results, assigning grades, and generating individual or class-level result reports.

The application should reduce manual calculation errors and simplify the management of student academic records.

It should be designed for a college or school administrator, teacher, or faculty member.

### 5. Core Objectives

The project must:

1. Register and maintain student records.
2. Assign a unique student ID to every student.
3. Store student details and subject information.
4. Enter and update examination marks.
5. Calculate total marks, average, percentage, and result status automatically.
6. Assign grades according to a documented grading scale.
7. Identify passed and failed subjects.
8. Generate individual student result reports.
9. Display a complete class result table.
10. Search and filter students.
11. Save data persistently between application sessions.
12. Validate inputs and handle invalid data gracefully.
13. Demonstrate Java OOP, collections, file handling, exception handling, and Swing GUI development.

The application must be simple enough for a student to understand, explain, and maintain during a course-end project viva.

Do not overengineer the application or introduce unnecessary technologies.

---

# PART C — TECHNOLOGY RESTRICTIONS

### 6. Use Java Only for the Application

The application must be implemented using Java and standard Java Development Kit libraries.

Use the following:

- **Programming language:** Java.
- **GUI:** Java Swing.
- **Layout management:** Swing layout managers.
- **Data structures:** Java Collections Framework.
- **Persistence:** Java file handling, preferably CSV files.
- **Object-oriented design:** Classes, objects, encapsulation, inheritance or interfaces where appropriate, and abstraction.
- **Exception handling:** `try`, `catch`, `finally` where appropriate, and custom exceptions if useful.
- **Input validation:** Java methods and validation logic.
- **Date handling:** Java standard date/time API if required.
- **Testing:** Java-based test classes and reproducible manual test cases.

Use only standard Java APIs.

Examples of permitted packages include:

- `java.lang`
- `java.util`
- `java.io`
- `java.nio.file`
- `java.time`
- `javax.swing`
- `java.awt`
- `java.awt.event`

Use a Java version supported by the available environment, preferably JDK 17 or newer. Document the actual version used.

### Strictly prohibited

Do not use:

- Python.
- JavaScript or TypeScript.
- HTML or CSS as application technologies.
- React, Angular, or other frontend frameworks.
- Spring Boot.
- Django, Flask, or other backend frameworks.
- MySQL, PostgreSQL, SQLite, or any external database.
- JDBC or database servers.
- Firebase or cloud databases.
- External APIs.
- Third-party Java libraries.
- External UI frameworks.
- Authentication services.
- Build systems or dependency managers that introduce unnecessary dependencies.

Do not build a web application.

Do not use a browser interface.

Do not make the application depend on an internet connection.

The application must run locally on a computer with a compatible JDK.

**Important:** LaTeX is permitted and required for the academic report because the supplied report template uses it. The Java-only restriction applies to the application implementation.

---

# PART D — APPLICATION FEATURES

### 7. Application Startup and Main Dashboard

Create a professional desktop interface using Java Swing.

The main window should have:

- Application title.
- A clean navigation area.
- A dashboard summary.
- Student management navigation.
- Marks and results navigation.
- Result reports navigation.
- Application information section.
- Exit option.

The dashboard should display useful summary cards or panels, such as:

- Total registered students.
- Total subjects.
- Students who passed.
- Students who failed.
- Overall class pass percentage.

Calculate summary statistics from actual stored records. Do not hardcode dashboard figures.

Use `JFrame`, `JPanel`, `JLabel`, `JButton`, `JTable`, `JTextField`, `JComboBox`, `JOptionPane`, and other relevant Swing components.

Use appropriate layout managers such as `BorderLayout`, `GridLayout`, `GridBagLayout`, or `BoxLayout`.

Avoid absolute positioning with `null` layouts.

Make the window resizable and ensure that the interface remains usable at ordinary desktop resolutions.

### 8. Student Management Module

Provide a complete student management screen.

Each student record should contain:

- Unique student ID.
- Full name.
- Roll number.
- Course or department.
- Academic year or semester.
- Optional email address.

Avoid collecting unnecessary personal information.

#### Required operations

**Add student**

- Open a form for entering student details.
- Validate mandatory fields.
- Reject duplicate student IDs and roll numbers.
- Save the new record.
- Refresh the student table.

**View students**

Display student information in a `JTable`.

Provide appropriate column headings and readable column widths.

**Search student**

Allow searches by student ID, roll number, or student name.

Search should be case-insensitive where applicable.

**Update student**

Allow the user to select a student and edit their details.

Prevent modifications that create duplicate IDs or roll numbers.

**Delete student**

Allow deletion only after a confirmation dialog.

If the student has recorded examination results, handle the related records consistently. Either prevent deletion until those results are removed or delete associated results safely. Document the selected policy.

**Clear form**

Provide a button that clears form fields without deleting stored records.

Display understandable success and validation messages.

### 9. Subject Management Module

Create a subject management module.

Each subject should contain:

- Unique subject code.
- Subject name.
- Maximum marks.
- Minimum passing marks.

Provide operations to:

- Add a subject.
- View available subjects.
- Update subject information.
- Delete a subject if it has no dependent results, or otherwise prevent deletion with an explanation.
- Validate subject codes and mark limits.

For the initial configuration, support five subjects with a maximum of 100 marks and a minimum passing mark of 35.

Allow the application to use the configured subject records rather than hardcoding subject names throughout the code.

If the subject configuration changes, ensure that the result calculation uses the corresponding subject settings.

### 10. Examination Marks Entry Module

Create a screen for entering examination marks.

The user should be able to:

1. Select a student.
2. Select a subject.
3. Enter marks obtained.
4. Save the marks.
5. Update existing marks when necessary.
6. View marks already recorded for that student.

Validation rules:

- Marks cannot be negative.
- Marks cannot exceed the subject's maximum marks.
- Invalid numeric values must be rejected.
- Empty mandatory fields must be rejected.
- A student-subject pair must not have duplicate mark records.
- Editing existing marks must update the existing record rather than create a duplicate.
- Only valid registered students and subjects may be selected.

Use a meaningful message when an operation fails.

Do not silently ignore invalid entries.

### 11. Result Calculation Engine

Implement result calculation in a dedicated Java class, separate from the GUI.

For each student, calculate:

**Total marks**

Sum all valid recorded subject marks for the selected examination set.

**Maximum possible marks**

Sum the maximum marks for the subjects included in that examination set.

**Percentage**

Percentage = (Total Marks / Maximum Possible Marks) × 100

Use floating-point arithmetic correctly. Avoid integer division and round only for presentation where appropriate.

**Average marks per subject**

Average = Total Marks / Number of Subjects

**Subject pass status**

A subject is passed if marks obtained are greater than or equal to the subject's configured minimum passing marks.

**Overall result**

A student passes only if all required subjects have valid marks and the student meets the passing requirement for every required subject.

If any required subject is failed, the overall result is `FAIL`.

If the required marks are incomplete, the result must be marked `INCOMPLETE` rather than incorrectly classified as PASS or FAIL.

Do not calculate a final percentage using missing subjects as though they were scored zero. An incomplete result must remain clearly distinguishable.

Ensure that the calculation engine checks the number of required subjects before producing a final result.

### 12. Grade Assignment

Use the following default grading scale for a completed examination.

| Percentage | Grade | Description |
|---|---|---|
| 90–100 | A+ | Outstanding |
| 80–89.99 | A | Excellent |
| 70–79.99 | B | Very Good |
| 60–69.99 | C | Good |
| 50–59.99 | D | Satisfactory |
| 35–49.99 | E | Pass |
| Below 35 | F | Fail |

Apply the grading scale consistently and document it in the application and academic report.

For an incomplete result, display `INCOMPLETE` instead of a final grade.

For an overall failed result, display the appropriate failed status and clearly identify the subjects that were not passed. If the overall grade is `F`, explain the rule that produces it.

The application must not confuse the overall percentage grade with an individual subject's pass status.

Make grade calculation a reusable method in the result calculation class.

Boundary conditions such as 34.99, 35, 49.99, 50, 89.99, 90, and 100 must be tested.

### 13. Individual Student Result Report

Create an individual result screen that displays:

- Student ID.
- Student name.
- Roll number.
- Course and semester.
- Subject-wise marks.
- Subject maximum marks.
- Subject passing marks.
- Subject pass/fail status.
- Total marks.
- Maximum possible marks.
- Percentage.
- Average marks.
- Overall grade.
- Overall result status.

The result screen should be readable and suitable for a faculty demonstration.

Provide a button to open a student result by searching for or selecting a student.

Do not display a final result when mandatory marks are missing.

Where a result is incomplete, identify the missing subjects.

Optionally, provide a print-friendly view using standard Java facilities if it can be implemented reliably without external dependencies.

### 14. Class Result and Merit List

Create a class-level result screen using a `JTable`.

Include columns such as:

- Rank.
- Student ID.
- Roll number.
- Student name.
- Total marks.
- Maximum marks.
- Percentage.
- Grade.
- Result status.

Requirements:

- Calculate results from actual records.
- Sort completed results by percentage in descending order.
- Place incomplete records separately or at the bottom.
- Do not rank incomplete results as if they were completed.
- Use a deterministic tie-handling policy. Students with equal percentages may share the same rank.
- Provide filters for PASS, FAIL, and INCOMPLETE.
- Provide a search facility.
- Refresh the table after marks are updated.

Document the ranking method in the report.

### 15. Data Persistence

Use CSV files for persistent storage.

Create a dedicated data directory inside the application's working directory.

Suggested structure:

`data/students.csv`

`data/subjects.csv`

`data/marks.csv`

The exact internal representation may differ if another standard-Java file format provides a clear advantage, but it must remain simple and dependency-free.

Persistence requirements:

- Load existing data at application startup.
- Save data after successful create, update, and delete operations.
- Preserve records after the application closes.
- Handle missing files gracefully.
- Create required files or initialize default subject records where appropriate.
- Use UTF-8 consistently.
- Correctly handle commas, quotes, and other special characters in CSV fields.
- Avoid corrupting existing data when saving.
- Report file read/write failures through understandable messages.
- Keep file handling separate from the GUI and business logic.

Do not use a database disguised as a file.

Include sample data so that the application can be demonstrated immediately.

Ensure the sample data is clearly identified as fictional demonstration data.

### 16. Input Validation and Exception Handling

Implement validation centrally wherever practical.

Examples of invalid input include:

- Empty student name.
- Duplicate student ID.
- Duplicate roll number.
- Duplicate subject code.
- Negative marks.
- Marks above the maximum.
- Invalid numeric values.
- Missing examination marks.
- Invalid CSV records.
- Missing data files.
- File permission errors.

Requirements:

- Use appropriate exception handling.
- Do not expose raw stack traces to ordinary users.
- Print useful debugging information to the console where appropriate.
- Avoid broad, empty `catch` blocks.
- Never silently swallow an exception.
- Do not allow invalid data to enter the application's core collections.
- Preserve existing data when an operation fails.
- Validate data loaded from disk as well as data entered through the GUI.

---

# PART E — SOFTWARE ARCHITECTURE

### 17. Use a Simple Layered Architecture

Organize the code into logical packages.

Recommended structure:

`src/`

- `model/`
  - `Student.java`
  - `Subject.java`
  - `MarkRecord.java`
  - `StudentResult.java`

- `service/`
  - `StudentService.java`
  - `SubjectService.java`
  - `MarksService.java`
  - `ResultCalculator.java`
  - `GradeCalculator.java`

- `repository/`
  - `StudentRepository.java`
  - `SubjectRepository.java`
  - `MarksRepository.java`
  - `CsvFileHandler.java`

- `ui/`
  - `MainFrame.java`
  - `DashboardPanel.java`
  - `StudentPanel.java`
  - `SubjectPanel.java`
  - `MarksPanel.java`
  - `ResultsPanel.java`
  - `ResultReportPanel.java`

- `util/`
  - `InputValidator.java`
  - `ApplicationConstants.java`

- `Main.java`

You may modify this structure if there is a compelling reason, but keep the design understandable for a beginner.

Do not create unnecessary classes just to increase the number of files.

### 18. Object-Oriented Programming Requirements

Demonstrate OOP meaningfully.

**Encapsulation**

- Use private fields in model classes.
- Provide suitable constructors and accessors.
- Keep object state valid.

**Abstraction**

- Separate business operations from GUI implementation.
- Hide file-handling details behind repository classes.

**Inheritance**

- Use inheritance only when it represents a genuine relationship.
- Do not create artificial inheritance merely to claim that inheritance was used.

**Polymorphism**

- Use interfaces or method overriding where appropriate and useful.
- If polymorphism is not needed for this project's design, do not force it unnecessarily.

**Collections**

Use collections such as `ArrayList`, `HashMap`, or `Map` where appropriate.

For example, use maps to look up students by their unique IDs efficiently.

**Exception handling**

Use suitable exception handling in input validation, file access, and application operations.

**Separation of concerns**

Keep UI code, business logic, model classes, and persistence logic separate.

The GUI must not directly implement all grading calculations and file parsing.

### 19. Code Quality Requirements

- Use meaningful class, method, and variable names.
- Follow standard Java naming conventions.
- Include useful comments for non-obvious logic.
- Use constants for grading boundaries and repeated configuration values.
- Avoid duplicate code.
- Avoid unnecessary global mutable state.
- Avoid giant methods and monolithic classes.
- Close file resources safely using try-with-resources.
- Ensure that the application starts through a single, documented entry point.
- Ensure that the project compiles without third-party dependencies.

Do not produce pseudocode in place of implementation.

Do not leave critical functions unfinished.

Do not include placeholder methods that simply return hardcoded results.

---

# PART F — USER INTERFACE AND PRESENTATION

### 20. Visual Design

Create a clean, professional desktop application appropriate for a college project.

The design should prioritize clarity and usability over unnecessary decoration.

Include:

- Consistent typography.
- Clear page titles.
- Properly aligned forms.
- Readable tables.
- Consistent button styling.
- Appropriate margins and spacing.
- Clear validation messages.
- Confirmation dialogs for destructive actions.
- Empty-state messages when no data is available.
- Status indicators for PASS, FAIL, and INCOMPLETE.

Use standard Swing components and Java drawing/layout facilities only.

Do not introduce external UI libraries.

Ensure that long student tables can be scrolled.

Ensure that buttons are correctly connected to their actions.

All navigation controls must work.

All form submission controls must work.

Do not build a visually attractive interface with non-functional controls.

---

# PART G — TESTING AND VALIDATION

### 21. Compile and Test the Application

After implementation, compile the entire application using the available JDK.

Resolve all compilation errors before delivery.

Test the major workflows manually and, where practical, create automated Java tests that do not require third-party libraries.

At a minimum, test the following scenarios.

| Test | Scenario | Expected result |
|---|---|---|
| T01 | Add a valid student | Student is saved |
| T02 | Add duplicate student ID | Operation is rejected |
| T03 | Add duplicate roll number | Operation is rejected |
| T04 | Enter negative marks | Operation is rejected |
| T05 | Enter marks above maximum | Operation is rejected |
| T06 | Enter valid marks | Marks are saved |
| T07 | Update existing marks | Existing record is updated |
| T08 | Calculate a completed result | Totals and percentage are correct |
| T09 | Student passes every required subject | Overall result is PASS |
| T10 | Student fails one required subject | Overall result is FAIL |
| T11 | Required marks are missing | Result is INCOMPLETE |
| T12 | Calculate grade at 35% | Grade is E |
| T13 | Calculate grade at 90% | Grade is A+ |
| T14 | Restart the application | Previously saved records remain |
| T15 | Search by roll number | Correct student is found |
| T16 | Delete a student with dependent results | Defined deletion policy is enforced |
| T17 | Load missing data files | Application initializes or reports the issue gracefully |
| T18 | Load malformed CSV data | Invalid records are handled safely |
| T19 | Generate the class merit list | Completed results are sorted correctly |
| T20 | Calculate the percentage for all maximum marks | Percentage is exactly 100% |

Add further tests where needed.

### 22. Verify Mathematical Correctness

Use independently calculated test examples.

Example:

A student obtains 85, 76, 92, 68, and 79 marks in five subjects, each with a maximum of 100.

Expected total:

400 marks.

Expected maximum:

500 marks.

Expected percentage:

80%.

Expected average:

80 marks per subject.

Expected overall result:

PASS, assuming every subject meets the passing requirement.

Expected overall grade:

A.

Add examples for a failed subject, an incomplete examination, and grade-boundary cases.

Verify the actual outputs of the application against the expected values.

Do not claim that a test passed unless it was actually executed and the observed output was checked.

### 23. Reproducible Demo Workflow

Prepare a short demonstration sequence for a faculty presentation:

1. Start the application.
2. Show the dashboard.
3. Add or view a student.
4. View the subject list.
5. Enter marks for the student.
6. Open the student's result.
7. Demonstrate automatic total and percentage calculation.
8. Demonstrate grade assignment.
9. Show a failed-subject case.
10. Show an incomplete-result case.
11. Open the class merit list.
12. Close and restart the application.
13. Demonstrate that the saved records remain available.

Include sufficient fictional sample records to make the demonstration meaningful.

Provide a way to restore the original demo dataset without requiring the user to manually edit multiple files. Clearly warn if resetting the demo data will overwrite current records.

---

# PART H — ACADEMIC REPORT

### 24. Generate the Report From the Supplied Template

Once the template has been inspected and the software architecture has been established, populate the supplied LaTeX project.

The report must describe the actual implementation, not an imaginary or planned implementation.

Follow the template's original structure and required chapter sequence. Adapt the following suggested topics to the sections already present in the template.

#### Project introduction

Explain:

- Background.
- Problem statement.
- Motivation.
- Objectives.
- Scope.
- Limitations.

#### Existing system and proposed system

Discuss the challenges of manual student result management, including calculation errors, difficulty searching records, inconsistent grade assignment, and manual record maintenance.

Explain how the proposed Java application addresses these problems.

Avoid unsupported claims that the application is deployed in a real institution.

#### Requirements analysis

Include:

- Functional requirements.
- Non-functional requirements.
- Hardware requirements.
- Software requirements.
- User requirements.

Keep the hardware and software requirements realistic for a local Java desktop application.

#### System design

Include appropriate diagrams if the template allows them:

- System architecture diagram.
- Use-case diagram.
- Activity diagram.
- Data-flow diagram, if appropriate.
- Class diagram.
- Application workflow.
- CSV data organization.

The diagrams must represent the actual implemented architecture.

Use accurate class names and relationships.

Do not draw database tables or server components that the project does not use.

#### Implementation

Explain the real implementation of:

- Student management.
- Subject management.
- Marks entry.
- Result calculation.
- Grade calculation.
- File storage.
- Search and filtering.
- Class merit list.
- Input validation.
- Exception handling.
- Swing interface.

Include concise, relevant Java code snippets if permitted by the template.

Explain the important methods and their purpose.

#### Algorithms

Provide clear step-by-step algorithms or pseudocode for:

- Adding a student.
- Recording examination marks.
- Calculating totals and percentages.
- Assigning grades.
- Determining overall results.
- Generating the merit list.
- Saving and loading records.

Algorithms must agree with the implemented code.

#### Testing

Include the actual test cases, expected results, observed results, and pass/fail status.

Do not fabricate execution evidence.

Include only results supported by actual testing.

#### Results and discussion

Describe what the completed application can do.

Include real screenshots of the implemented application wherever appropriate and permitted by the template.

Possible screenshots:

- Main dashboard.
- Student management.
- Subject management.
- Marks entry.
- Individual result.
- Class merit list.
- Validation error.
- Incomplete result.

Screenshots must be genuine application screenshots. Do not create mock screenshots that pretend to show working functionality.

#### Conclusion and future scope

Summarize the completed work and its limitations.

Future enhancements may include database integration, authentication, PDF report generation, and web-based access, but these must be identified as future work rather than features already implemented.

#### References

Follow the exact citation and bibliography format required by the supplied template.

Use genuine, relevant references where external sources are needed. Do not invent publications, authors, page numbers, or URLs.

Include appropriate references for Java and Swing documentation if required.

#### Appendices

Include relevant supplementary material if the template supports it, such as:

- Source code excerpts.
- Additional test cases.
- Sample CSV structure.
- User instructions.

Do not duplicate entire source files unnecessarily if the template has strict page limits.

### 25. Report Accuracy

Every statement in the report must match the implementation.

Specifically:

- Do not claim that a database is used.
- Do not claim that the application is web-based.
- Do not claim that authentication exists if it was not implemented.
- Do not claim that PDF export exists if it was not implemented.
- Do not include screenshots from a different application.
- Do not report tests as successful without executing them.
- Do not include invented performance measurements.
- Do not invent faculty approval or institutional details.
- Do not use inconsistent project names across chapters.

Use the same project title throughout the application, source code comments where appropriate, and report.

### 26. LaTeX Compilation and PDF Verification

Preserve the original LaTeX source files.

Compile the report using the compiler and process required by the supplied template.

If bibliography tools or multiple compilation passes are needed, execute them in the correct order.

Check for:

- Missing LaTeX packages.
- Missing images.
- Undefined references.
- Missing bibliography entries.
- Unresolved citations.
- Incorrect page numbering.
- Empty sections.
- Broken tables.
- Figures extending beyond margins.
- Overflowing code listings.
- Incorrect chapter numbering.
- Unexpected changes to the original title page.

If a PDF is generated, inspect it visually and verify that its formatting follows the supplied template.

If compilation is not possible in the environment, explain why and provide the complete source files and exact compilation instructions. Do not claim that a PDF was successfully compiled if it was not.

---

# PART I — REQUIRED PROJECT DELIVERABLES

### 27. Final Directory Structure

Organize the deliverables cleanly. A suggested structure is:

`StudentsResultsGradeManagementSystem/`

- `README.md`
- `src/`
- `data/`
- `tests/`
- `docs/`
- `report/`
  - Original template-derived LaTeX files.
  - Required `.cls` and `.sty` files.
  - Bibliography file if required.
  - Required report images.
  - Generated PDF if compilation succeeds.
- `screenshots/`
- `run.bat` if appropriate for the Windows environment.
- `compile.bat` if appropriate for the Windows environment.

Adapt the structure to the original template and the available environment.

Keep the project self-contained and easy to copy to another computer.

### 28. README.md Requirements

Create a complete `README.md` containing:

1. Project title.
2. Project description.
3. Objectives.
4. Features.
5. Technology stack.
6. Java version.
7. Project directory structure.
8. Prerequisites.
9. Compilation instructions.
10. Execution instructions.
11. Data storage explanation.
12. Default grading scale.
13. Result calculation formulas.
14. Sample demonstration workflow.
15. Testing instructions.
16. Known limitations.
17. Report compilation instructions.
18. Troubleshooting guidance.

Also maintain a concise development journal in the README documenting significant implementation milestones, decisions, issues encountered, and actual test outcomes. Update it during development rather than writing a fictional history at the end.

### 29. Run Instructions

Provide simple instructions for a beginner using Windows.

Prefer direct JDK compilation if the project does not require a build tool.

For example, if the final source structure supports this approach, provide equivalent commands:

```bash
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

If the application uses Java packages, adjust the commands and entry-point name to match the actual project structure.

Test the documented commands before delivering them.

If necessary, provide a Windows batch file that compiles the source and launches the application.

Do not require the user to configure a database, install a web server, or obtain API keys.

### 30. Final Verification Checklist

Before considering the project complete, verify all of the following:

- [ ] The supplied ZIP was located and inspected.
- [ ] The original LaTeX template was preserved and populated.
- [ ] The report's structure matches the supplied template.
- [ ] All required project information is consistent.
- [ ] The Java application compiles.
- [ ] The application starts successfully.
- [ ] All major GUI buttons and navigation options work.
- [ ] Student operations work.
- [ ] Subject operations work.
- [ ] Marks entry and editing work.
- [ ] Grade calculations are correct.
- [ ] Incomplete results are handled correctly.
- [ ] Merit-list sorting and ranking work.
- [ ] CSV persistence survives application restarts.
- [ ] Invalid input is handled safely.
- [ ] Testing has been performed and documented honestly.
- [ ] Screenshots show the real application.
- [ ] README instructions have been tested.
- [ ] The LaTeX report compiles, if the required tools are available.
- [ ] The generated PDF has been inspected, if available.
- [ ] All deliverables are organized in the final project directory.

---

# PART J — EXECUTION RULES

### 31. Development Workflow

Follow this sequence.

**Phase 1 — Inspect**

Inspect the supplied ZIP, identify the actual report template, and document its mandatory structure.

**Phase 2 — Plan**

Define the architecture, model classes, file formats, grading rules, GUI screens, and report integration.

**Phase 3 — Implement**

Build the application in small, logically separated components. Prioritize a reliable working application over unnecessary visual complexity.

**Phase 4 — Test**

Compile and run the project. Test calculations, validation, persistence, and GUI workflows.

**Phase 5 — Capture evidence**

Capture genuine screenshots of the completed application for the report, if the environment supports it.

**Phase 6 — Populate the report**

Fill the original LaTeX template using verified implementation details, actual screenshots, and executed test results.

**Phase 7 — Verify**

Compile the report if possible, inspect its output, and compare it with the original template.

**Phase 8 — Deliver**

Provide the complete project directory, the report source, the generated report PDF if available, and concise instructions for running and demonstrating the project.

### 32. Important Rules for Antigravity

- Do not stop after producing a project plan.
- Do not return only sample code.
- Do not leave core functionality as TODO comments.
- Do not generate a generic report before inspecting the template.
- Do not replace the template with a different layout.
- Do not invent unavailable institutional information.
- Do not introduce prohibited technologies.
- Do not claim successful compilation or testing without evidence.
- Do not ask unnecessary questions when a reasonable implementation decision can be made.
- If a genuinely essential detail is missing, use a clearly marked placeholder or ask a focused question.
- Prioritize correctness, simplicity, academic suitability, and ease of demonstration.

## Final Objective

Deliver a complete, runnable, Java-only **Students Results and Grade Management System**, together with a detailed, accurate academic project report that follows the supplied LaTeX template as strictly as possible.

The result must be something I can open, run, demonstrate to my faculty, explain during my viva, and submit as a course-end project.

**Begin by locating and inspecting `Course End Report Latex Template.zip`. Do not begin report authoring until you have examined the actual template files.**