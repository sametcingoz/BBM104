import java.util.*;

// Represents a course in the university system
public class Course {
    // Unique code of the course (e.g., BBM104)
    private String code;

    // Full name of the course
    private String name;

    // Department that offers the course
    private String department;

    // Number of credits for the course
    private int credits;

    // Semester information (e.g., Fall, Spring)
    private String semester;

    // Program code that this course belongs to
    private String programCode;

    // Instructor teaching the course
    private AcademicMember instructor;

    // List of students currently enrolled in this course
    private List<Student> enrolledStudents = new ArrayList<>();

    // Map of students and their assigned grades in this course
    private Map<Student, String> grades = new HashMap<>();

    // Constructor to initialize the course object
    public Course(String code, String name, String department, int credits, String semester, String programCode) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.credits = credits;
        this.semester = semester;
        this.programCode = programCode;
    }

    // Getter for course code
    public String getCode() {
        return code;
    }

    // Getter for course name
    public String getName() {
        return name;
    }

    // Getter for department name
    public String getDepartment() {
        return department;
    }

    // Getter for number of credits
    public int getCredits() {
        return credits;
    }

    // Getter for semester info
    public String getSemester() {
        return semester;
    }

    // Getter for related program code
    public String getProgramCode() {
        return programCode;
    }

    // Getter for instructor assigned to the course
    public AcademicMember getInstructor() {
        return instructor;
    }

    // Getter for list of enrolled students
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    // Getter for map of student grades
    public Map<Student, String> getGrades() {
        return grades;
    }

    // Setter to assign an instructor to the course
    public void setInstructor(AcademicMember instructor) {
        this.instructor = instructor;
    }

    // Method to enroll a student in the course
    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }

    // Method to assign a grade to a student
    public void assignGrade(Student student, String grade) {
        grades.put(student, grade);
    }

    // Calculates average grade based on all students' grades
    public double calculateAverageGrade() {
        if (grades.isEmpty()) return 0.0;

        double total = 0.0;

        // Convert each grade to a numeric value and add to total
        for (String grade : grades.values()) {
            total += GradeUtil.getGradeValue(grade);
        }

        // Return the average grade value
        return total / grades.size();
    }

    // Returns how many students received each grade
    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> distribution = new TreeMap<>();

        // Count occurrences of each grade
        for (String grade : grades.values()) {
            distribution.put(grade, distribution.getOrDefault(grade, 0) + 1);
        }

        return distribution;
    }
}

// Represents an academic program such as Bachelor's or Master's
class Program {
    // Program code (e.g., CS, AI)
    private String code;

    // Name of the program
    private String name;

    // Short description of the program
    private String description;

    // Department offering the program
    private String department;

    // Degree level (e.g., Bachelor, Master)
    private String degreeLevel;

    // Number of credits needed to complete the program
    private int requiredCredits;

    // List of courses included in the program
    private List<Course> courses = new ArrayList<>();

    // Constructor to create a program instance
    public Program(String code, String name, String description, String department, String degreeLevel, int requiredCredits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.department = department;
        this.degreeLevel = degreeLevel;
        this.requiredCredits = requiredCredits;
    }

    // Getter for program code
    public String getCode() {
        return code;
    }

    // Getter for program name
    public String getName() {
        return name;
    }

    // Getter for department
    public String getDepartment() {
        return department;
    }

    // Getter for degree level
    public String getDegreeLevel() {
        return degreeLevel;
    }

    // Getter for required credits
    public int getRequiredCredits() {
        return requiredCredits;
    }

    // Adds a course to the program's course list
    public void addCourse(Course course) {
        courses.add(course);
    }

    // Returns the list of courses in the program
    public List<Course> getCourses() {
        return courses;
    }
}

// Represents a department within the university
class Department {
    // Unique department code (e.g., CS, BM)
    private String code;

    // Department name
    private String name;

    // Description or summary of the department
    private String description;

    // The head of the department (an academic member)
    private AcademicMember head;

    // Constructor for Department object
    public Department(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    // Getter for department code
    public String getCode() {
        return code;
    }

    // Getter for department name
    public String getName() {
        return name;
    }

    // Getter for department description
    public String getDescription() {
        return description;
    }

    // Getter for department head
    public AcademicMember getHead() {
        return head;
    }

    // Setter for assigning department head
    public void setHead(AcademicMember head) {
        this.head = head;
    }
}
