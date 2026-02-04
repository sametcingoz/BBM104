import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utility class for generating reports to output file.
 */
public class ReportGenerator {

    public static void generateReports(StudentManagementSystem sms, PrintWriter pw) {
        // ===== Academic Members =====
        pw.println("----------------------------------------");
        pw.println("            Academic Members");
        pw.println("----------------------------------------");

        // Get all academic members and sort by ID
        List<AcademicMember> sortedFaculty = new ArrayList<>(sms.getFaculty().values());
        sortedFaculty.sort(Comparator.comparing(AcademicMember::getId));
        for (AcademicMember am : sortedFaculty) {
            pw.println("Faculty ID: " + am.getId());
            // Ensure UTF-8 encoding for names
            pw.println("Name: " + new String(am.getName().getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8));
            pw.println("Email: " + am.getEmail());
            pw.println("Department: " + am.getDepartment());
            pw.println();
        }

        // ===== Students =====
        pw.println("----------------------------------------\n");
        pw.println("----------------------------------------");
        pw.println("                STUDENTS");
        pw.println("----------------------------------------");

        // Get all students and sort by ID
        List<Student> sortedStudents = new ArrayList<>(sms.getStudents().values());
        sortedStudents.sort(Comparator.comparing(Student::getId));
        for (Student st : sortedStudents) {
            pw.println("Student ID: " + st.getId());
            pw.println("Name: " + st.getName());
            pw.println("Email: " + st.getEmail());
            pw.println("Major: " + st.getDepartment());
            pw.println("Status: " + st.getStatus());
            pw.println();
        }

        // ===== Departments =====
        pw.println("----------------------------------------\n");
        pw.println("---------------------------------------");
        pw.println("              DEPARTMENTS");
        pw.println("---------------------------------------");

        // Get all departments and sort by code
        List<Department> sortedDepartments = new ArrayList<>(sms.getDepartments().values());
        sortedDepartments.sort(Comparator.comparing(Department::getCode));
        for (Department d : sortedDepartments) {
            pw.println("Department Code: " + d.getCode());
            pw.println("Name: " + d.getName());
            // Print head of department or say "Not Assigned"
            pw.println("Head: " + (d.getHead() != null
                    ? new String(d.getHead().getName().getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8)
                    : "Not assigned"));
            pw.println();
        }

        // ===== Programs =====
        pw.println("----------------------------------------\n");
        pw.println("--------------------------------------");
        pw.println("                PROGRAMS");
        pw.println("---------------------------------------");

// Get all programs and sort by code
        List<Program> sortedPrograms = new ArrayList<>(sms.getPrograms().values());
        sortedPrograms.sort(Comparator.comparing(Program::getCode));
        for (Program p : sortedPrograms) {
            pw.println("Program Code: " + p.getCode());
            pw.println("Name: " + p.getName());
            pw.println("Department: " + p.getDepartment());
            pw.println("Degree Level: " + p.getDegreeLevel());
            pw.println("Required Credits: " + p.getRequiredCredits());

            // If there are no courses assigned to the program
            if (p.getCourses().isEmpty()) {
                pw.println("Courses: - ");
            } else {
                List<String> courseCodes = new ArrayList<>();
                for (Course c : p.getCourses()) {
                    courseCodes.add(c.getCode());
                }
                pw.println("Courses: {" + String.join(",", courseCodes) + "}");
            }
            pw.println();
        }

        // ===== Courses (summary) =====
        pw.println("----------------------------------------\n");
        pw.println("---------------------------------------");
        pw.println("                COURSES");
        pw.println("---------------------------------------");

        // Get all courses and sort by code
        List<Course> sortedCourses = new ArrayList<>(sms.getCourses().values());
        sortedCourses.sort(Comparator.comparing(Course::getCode));
        for (Course c : sortedCourses) {
            pw.println("Course Code: " + c.getCode());
            pw.println("Name: " + c.getName());
            pw.println("Department: " + c.getDepartment());
            pw.println("Credits: " + c.getCredits());
            pw.println("Semester: " + c.getSemester());
            pw.println();
        }

        // ===== Course Reports =====
        pw.println("----------------------------------------\n");
        pw.println("----------------------------------------");
        pw.println("             COURSE REPORTS");
        pw.println("----------------------------------------");

        for (Course c : sortedCourses) {
            // Basic course info
            pw.println("Course Code: " + c.getCode());
            pw.println("Name: " + c.getName());
            pw.println("Department: " + c.getDepartment());
            pw.println("Credits: " + c.getCredits());
            pw.println("Semester: " + c.getSemester());
            pw.println();

            // Instructor info
            if (c.getInstructor() != null) {
                pw.println("Instructor: " + new String(
                        c.getInstructor().getName().getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8));
            } else {
                pw.println("Instructor: Not assigned");
            }
            pw.println();

            // List of enrolled students
            pw.println("Enrolled Students:");
            List<Student> enrolled = c.getEnrolledStudents();
            enrolled.sort(Comparator.comparing(Student::getId));
            for (Student s : enrolled) {
                pw.println("- " + s.getName() + " (ID: " + s.getId() + ")");
            }
            pw.println();

            // Grade distribution section
            pw.println("Grade Distribution:");
            Map<String, Integer> dist = c.getGradeDistribution();

            if (dist.isEmpty()) {
                pw.println(); // Leave a blank line if no grades
            } else {
                // Print grades in alphabetical order
                for (String grade : new TreeSet<>(dist.keySet())) {
                    pw.println(grade + ": " + dist.get(grade));
                }
                pw.println(); // Blank line after grades
            }

            // Print average grade with two decimal places
            pw.printf(Locale.ENGLISH, "Average Grade: %.2f%n", c.calculateAverageGrade());
            pw.println();
            pw.println("----------------------------------------\n");
        }

        // ===== Student Reports =====
        pw.println("----------------------------------------");
        pw.println("            STUDENT REPORTS");
        pw.println("----------------------------------------");

        for (Student s : sortedStudents) {
            // Basic student info
            pw.println("Student ID: " + s.getId());
            pw.println("Name: " + s.getName());
            pw.println("Email: " + s.getEmail());
            pw.println("Major: " + s.getDepartment());
            pw.println("Status: " + s.getStatus());
            pw.println();
            pw.println();

            // Print enrolled (but not completed) courses
            pw.println("Enrolled Courses:");
            List<Course> enrolledCourses = new ArrayList<>(s.getEnrolledCourses());
            enrolledCourses.removeAll(s.getCompletedCourses().keySet()); // Remove completed courses
            enrolledCourses.sort(Comparator.comparing(Course::getCode));
            for (Course c : enrolledCourses) {
                pw.println("- " + c.getName() + " (" + c.getCode() + ")");
            }

            pw.println();

            // Print completed courses with grades
            pw.println("Completed Courses:");
            Map<Course, String> completed = s.getCompletedCourses();
            List<Course> completedSorted = new ArrayList<>(completed.keySet());
            completedSorted.sort(Comparator.comparing(Course::getCode));
            for (Course c : completedSorted) {
                pw.println("- " + c.getName() + " (" + c.getCode() + "): " + completed.get(c));
            }
            pw.println();

            // Print GPA
            pw.printf(Locale.ENGLISH, "GPA: %.2f%n", s.calculateGPA());
            pw.println("----------------------------------------\n");
        }
    }
}
