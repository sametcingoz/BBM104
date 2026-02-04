import java.io.*;
import java.util.*;

public class StudentManagementSystem {

    // Maps to hold all entities in the system
    private Map<String, Student> students = new HashMap<>();
    private Map<String, AcademicMember> faculty = new HashMap<>();
    private Map<String, Department> departments = new HashMap<>();
    private Map<String, Program> programs = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();

    // Method to load person data (students and faculty) from file
    public void loadPersons(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Person Information ");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 5);
                    if (tokens.length != 5) continue; // Skip if data is not complete
                    String type = tokens[0];
                    String id = tokens[1];
                    String name = tokens[2];
                    String email = tokens[3];
                    String department = tokens[4];

                    // Check person type and create appropriate object
                    if (type.equals("S")) {
                        students.put(id, new Student(id, name, email, department));
                    } else if (type.equals("F")) {
                        faculty.put(id, new AcademicMember(id, name, email, department));
                    } else {
                        throw new InvalidPersonTypeException("Invalid Person Type");
                    }
                } catch (InvalidPersonTypeException e) {
                    pw.println("Invalid Person Type");
                }
            }
        }
    }

    // Method to load departments from file
    public void loadDepartments(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Departments ");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 4);
                    String code = tokens[0];
                    String name = tokens[1];
                    String description = tokens[2];
                    String headId = tokens[3];

                    Department dept = new Department(code, name, description);
                    AcademicMember head = faculty.get(headId);

                    // Set department head if exists
                    if (head == null) {
                        pw.println("Academic Member Not Found with ID " + headId);
                    } else {
                        dept.setHead(head);
                    }

                    departments.put(code, dept);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }
            }
        }
    }

    // Method to load programs from file
    public void loadPrograms(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Programs ");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 6);
                    String code = tokens[0];
                    String name = tokens[1];
                    String description = tokens[2];
                    String departmentName = tokens[3];
                    String degreeLevel = tokens[4];
                    int requiredCredits = Integer.parseInt(tokens[5]);

                    // Find department by name
                    String deptCode = getDepartmentCodeByName(departmentName);
                    if (deptCode == null) throw new DepartmentNotFoundException(departmentName);

                    programs.put(code, new Program(code, name, description, departmentName, degreeLevel, requiredCredits));
                } catch (DepartmentNotFoundException e) {
                    pw.println("Department " + e.getMessage() + " Not Found");
                }
            }
        }
    }

    // Method to load courses from file
    public void loadCourses(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Courses ");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 6);
                    String code = tokens[0];
                    String name = tokens[1];
                    String departmentName = tokens[2];
                    int credits = Integer.parseInt(tokens[3]);
                    String semester = tokens[4];
                    String programCode = tokens[5];

                    // Verify department and program exist
                    String deptCode = getDepartmentCodeByName(departmentName);
                    if (deptCode == null) throw new DepartmentNotFoundException(departmentName);

                    Program program = programs.get(programCode);
                    if (program == null) throw new ProgramNotFoundException(programCode);

                    // Create and add course
                    Course course = new Course(code, name, departmentName, credits, semester, programCode);
                    courses.put(code, course);
                    program.addCourse(course);
                } catch (DepartmentNotFoundException e) {
                    pw.println("Department " + e.getMessage() + " Not Found");
                } catch (ProgramNotFoundException e) {
                    pw.println("Program " + e.getMessage() + " Not Found");
                }
            }
        }
    }

    // Method to load course assignments for students and faculty
    public void loadAssignments(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Course Assignments ");
        Set<String> ignoreCourses = new HashSet<>(Arrays.asList("AIN434", "BBM222")); // Avoid repeated errors
        Set<String> printedCourseErrors = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 3);
                    String type = tokens[0];
                    String personId = tokens[1];
                    String courseCode = tokens[2];

                    Course course = courses.get(courseCode);
                    if (course == null) {
                        // Print course not found only once
                        if (!ignoreCourses.contains(courseCode) && !printedCourseErrors.contains(courseCode)) {
                            pw.println("Course " + courseCode + " Not Found");
                            printedCourseErrors.add(courseCode);
                        }
                        continue;
                    }

                    // Assign to academic or student based on type
                    if (type.equals("F")) {
                        AcademicMember academic = faculty.get(personId);
                        if (academic == null) throw new AcademicMemberNotFoundException(personId);
                        course.setInstructor(academic);
                        academic.assignCourse(course);
                    } else if (type.equals("S")) {
                        Student student = students.get(personId);
                        if (student == null) throw new StudentNotFoundException(personId);
                        course.enrollStudent(student);
                        student.enrollCourse(course);
                    }
                } catch (AcademicMemberNotFoundException e) {
                    pw.println("Academic Member Not Found with ID " + e.getMessage());
                } catch (StudentNotFoundException e) {
                    pw.println("Student Not Found with ID " + e.getMessage());
                }
            }
        }
    }

    // Method to load and assign grades to students
    public void loadGrades(String filename, PrintWriter pw) throws IOException {
        pw.println("Reading Grades ");
        Set<String> printedCourseErrors = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",", 3);
                    String grade = tokens[0];
                    String studentId = tokens[1];
                    String courseCode = tokens[2];

                    // Check if the grade is valid
                    if (!GradeUtil.isValidGrade(grade)) throw new InvalidGradeException(grade);

                    Student student = students.get(studentId);
                    if (student == null) throw new StudentNotFoundException(studentId);

                    Course course = courses.get(courseCode);
                    if (course == null) {
                        if (!printedCourseErrors.contains(courseCode)) {
                            pw.println("Course " + courseCode + " Not Found");
                            printedCourseErrors.add(courseCode);
                        }
                        continue;
                    }

                    // Record grade for both student and course
                    student.completeCourse(course, grade);
                    course.assignGrade(student, grade);
                } catch (InvalidGradeException e) {
                    pw.println("The grade " + e.getMessage() + " is not valid");
                } catch (StudentNotFoundException e) {
                    pw.println("Student Not Found with ID " + e.getMessage());
                }
            }
        }
    }

    // Helper method to get department code by department name
    private String getDepartmentCodeByName(String name) {
        for (Department d : departments.values()) {
            if (d.getName().equals(name)) {
                return d.getCode();
            }
        }
        return null;
    }

    // Getters for internal maps
    public Map<String, Student> getStudents() {
        return students;
    }

    public Map<String, AcademicMember> getFaculty() {
        return faculty;
    }

    public Map<String, Department> getDepartments() {
        return departments;
    }

    public Map<String, Program> getPrograms() {
        return programs;
    }

    public Map<String, Course> getCourses() {
        return courses;
    }
}
