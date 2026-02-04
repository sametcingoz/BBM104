import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Main class that runs the University Student Management System.
 */
public class Main {
    public static void main(String[] args) {
        // Check if all required file arguments are provided
        if (args.length != 7) {
            System.out.println("Usage: java Main persons.txt departments.txt programs.txt courses.txt assignments.txt grades.txt output.txt");
            return;
        }

        // Input and output file paths are assigned
        String personFile = args[0];
        String departmentFile = args[1];
        String programFile = args[2];
        String courseFile = args[3];
        String assignmentFile = args[4];
        String gradeFile = args[5];
        String outputFile = args[6];

        // Create an instance of the management system
        StudentManagementSystem sms = new StudentManagementSystem();

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            // Write UTF-8 Byte Order Mark (BOM) to the beginning of the output file
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            // Create a chain of writers to handle UTF-8 encoded output
            try (
                    OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                    BufferedWriter bw = new BufferedWriter(osw);
                    PrintWriter pw = new PrintWriter(bw, true) // Auto-flushing enabled
            ) {
                // Load person data and handle any exceptions
                try {
                    sms.loadPersons(personFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Load department data
                try {
                    sms.loadDepartments(departmentFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Load program data
                try {
                    sms.loadPrograms(programFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Load course data
                try {
                    sms.loadCourses(courseFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Load course assignments (instructors to courses)
                try {
                    sms.loadAssignments(assignmentFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Load student grades
                try {
                    sms.loadGrades(gradeFile, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

                // Generate all output reports
                try {
                    ReportGenerator.generateReports(sms, pw);
                } catch (Exception e) {
                    pw.println(e.getMessage());
                }

            }
        } catch (IOException e) {
            // This block runs if there is a problem creating or writing to the output file
            e.printStackTrace();
        }
    }
}
