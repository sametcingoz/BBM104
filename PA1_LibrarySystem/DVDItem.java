package Library;

// This class is used to represent a DVD item in the library system.
public class DVDItem extends Item {

    // These fields are used to store information about the DVD.
    private String director;
    private String category;
    private int runtime;

    // This constructor is called when a DVD object is created with details.
    public DVDItem(String id, String title, String director, String category, int runtime, String type) {
        super(id, title, type);
        this.director = director;
        this.category = category;
        this.runtime = runtime;
    }

    // This method checks if the DVD can be borrowed and sets borrow info.
    @Override
    public boolean borrowItem(String userId, String borrowDate) {
        if (isBorrowed) return false;
        isBorrowed = true;
        setBorrowInfo(borrowDate, userId);
        return true;
    }

    // This method is used to return the DVD and clear its borrow data.
    @Override
    public void returnItem(String returnDate) {
        isBorrowed = false;
        clearBorrowInfo();
    }

    // This method shows the DVD information in formatted text.
    @Override
    public String display() {
        String status = isBorrowed ? "Borrowed" : "Available";
        String extra = isBorrowed ? " Borrowed Date: " + borrowedDate + " Borrowed by: " + borrowedBy : "";
        return "------ Item Information for " + id + " ------\n" +
                "ID: " + id + " Name: " + title + " Status: " + status + extra + "\n" +
                "Director: " + director + " Category: " + category + " Runtime: " + runtime + " min";
    }

    // This method returns the same info as display() as a string.
    @Override
    public String toString() {
        return display();
    }
}
