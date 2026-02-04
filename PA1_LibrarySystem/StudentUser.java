package Library;

// This class is used to represent a student user in the library.
public class StudentUser extends UserBase {

    // These fields are used to store student’s academic info.
    private String department;
    private String faculty;
    private int grade;

    // This constructor is used when a new student user is created.
    public StudentUser(String id, String name, String phoneNumber, String department, String faculty, int grade) {
        super(id, name, phoneNumber);
        this.department = department;
        this.faculty = faculty;
        this.grade = grade;
    }

    // This method returns the maximum number of items allowed.
    @Override
    public int getMaxItems() {
        return 5;
    }

    // This method gives the overdue day limit for students.
    @Override
    public int getOverdueLimit() {
        return 30;
    }

    // This method prevents reference items from being borrowed.
    @Override
    public boolean borrowItem(Item item, java.time.LocalDate borrowDate) {
        if (item.getType().equalsIgnoreCase("reference"))
            return false;
        return super.borrowItem(item, borrowDate);
    }

    // This method returns the formatted student information.
    @Override
    public String toString() {
        return "------ User Information for " + id + " ------\n" +
                "Name: " + name + " Phone: " + phoneNumber + "\n" +
                "Faculty: " + faculty + " Department: " + department + " Grade: " + grade + "th";
    }
}
