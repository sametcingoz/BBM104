import java.util.*;

// Utility class for grade-related operations
/**
 * Utility class to convert letter grades to 4-point system values.
 */
class GradeUtil {

    // A map to store the mapping between letter grades and GPA values
    private static final Map<String, Double> gradeMap = new HashMap<>();

    // Static block to initialize the grade map with letter-grade to GPA conversions
    static {
        gradeMap.put("A1", 4.0);  // Highest grade
        gradeMap.put("A2", 3.5);
        gradeMap.put("B1", 3.0);
        gradeMap.put("B2", 2.5);
        gradeMap.put("C1", 2.0);
        gradeMap.put("C2", 1.5);
        gradeMap.put("D1", 1.0);
        gradeMap.put("D2", 0.5);
        gradeMap.put("F3", 0.0);  // Failing grade
    }

    // This method returns the GPA value of a given letter grade
    public static double getGradeValue(String letter) {
        // If the grade does not exist in the map, throw an exception
        if (!gradeMap.containsKey(letter)) {
            throw new InvalidGradeException(letter);
        }
        // Otherwise, return the corresponding GPA value
        return gradeMap.get(letter);
    }

    // This method checks if a given letter grade is valid
    public static boolean isValidGrade(String letter) {
        // Returns true if the grade exists in the map, false otherwise
        return gradeMap.containsKey(letter);
    }
}

// === Custom Exception Classes ===

// This exception is thrown when an unknown person type is encountered
class InvalidPersonTypeException extends RuntimeException {
    public InvalidPersonTypeException(String message) {
        super(message);
    }
}

// This exception is thrown when a student ID cannot be found
class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String studentId) {
        super(studentId);
    }
}

// This exception is thrown when a faculty ID cannot be found
class AcademicMemberNotFoundException extends RuntimeException {
    public AcademicMemberNotFoundException(String facultyId) {
        super(facultyId);
    }
}

// This exception is thrown when a department code is not found
class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String departmentCode) {
        super(departmentCode);
    }
}

// This exception is thrown when a program code is not found
class ProgramNotFoundException extends RuntimeException {
    public ProgramNotFoundException(String programCode) {
        super(programCode);
    }
}

// This exception is thrown when a course code is not found
class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String courseCode) {
        super(courseCode);
    }
}

// This exception is thrown when an invalid grade is used
class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(String grade) {
        super(grade);
    }
}
