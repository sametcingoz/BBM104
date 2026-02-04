import java.util.*;

/**
 * Interface representing a person in the university system.
 * Implemented by both Student and AcademicMember classes.
 * Defines basic attributes and getters.
 */
public interface Person {

    /**
     * Returns the unique ID of the person.
     */
    String getId();

    /**
     * Returns the full name of the person.
     */
    String getName();

    /**
     * Returns the email address of the person.
     */
    String getEmail();

    /**
     * Returns the department of the person.
     */
    String getDepartment();
}

/**
 * Represents a student in the system.
 * Students can enroll and complete courses and have a GPA and status.
 */
class Student implements Person {
    private final String id;
    private final String name;
    private final String email;
    private final String department;

    private final String status = "Active";
    private final List<Course> enrolledCourses = new ArrayList<>();
    private final Map<Course, String> completedCourses =
            new TreeMap<>(Comparator.comparing(Course::getCode).reversed());

    public Student(String id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // Person interface methods
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    // Student-specific methods
    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
    }

    public void completeCourse(Course course, String letterGrade) {
        completedCourses.put(course, letterGrade);
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Map<Course, String> getCompletedCourses() {
        return completedCourses;
    }

    public String getStatus() {
        return status;
    }

    public double calculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<Course, String> entry : completedCourses.entrySet()) {
            double gradeValue = GradeUtil.getGradeValue(entry.getValue());
            int credit = entry.getKey().getCredits();
            totalPoints += gradeValue * credit;
            totalCredits += credit;
        }

        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }
}

/**
 * Represents an academic member (faculty) in the system.
 * Academic members can be assigned to teach courses.
 */
class AcademicMember implements Person {
    private final String id;
    private final String name;
    private final String email;
    private final String department;

    private final List<Course> coursesTaught = new ArrayList<>();

    public AcademicMember(String id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // Person interface methods
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    // AcademicMember-specific methods
    public void assignCourse(Course course) {
        coursesTaught.add(course);
    }

    public List<Course> getCoursesTaught() {
        return coursesTaught;
    }
}
