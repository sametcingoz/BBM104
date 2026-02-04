package Library;

// This class is used for defining academic staff in the system.
public class AcademicStaff extends UserBase {

    // These variables are used for storing department, faculty and title.
    private String department;
    private String faculty;
    private String title;

    // This constructor is called when a new academic staff is created.
    public AcademicStaff(String id, String name, String phoneNumber, String department, String faculty, String title) {
        super(id, name, phoneNumber);
        this.department = department;
        this.faculty = faculty;
        this.title = title;
    }

    // The maximum number of items allowed is given here.
    @Override
    public int getMaxItems() {
        return 3;
    }

    // The limit of days before penalty is returned here.
    @Override
    public int getOverdueLimit() {
        return 15;
    }

    // This method is used to show the academic staff's information clearly.
    @Override
    public String toString() {
        return "------ User Information for " + id + " ------\n" +
                "Name: " + title + " " + name + " Phone: " + phoneNumber + "\n" +
                "Faculty: " + faculty + " Department: " + department;
    }
}
