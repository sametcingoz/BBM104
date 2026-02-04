package Library;

// This class is used to represent a magazine in the library.
public class MagazineItem extends Item {

    // These fields are used to keep the magazine’s publisher and category.
    private String publisher;
    private String category;

    // This constructor is used when a new magazine is created.
    public MagazineItem(String id, String title, String publisher, String category, String type) {
        super(id, title, type);
        this.publisher = publisher;
        this.category = category;
    }

    // This method checks if the magazine can be borrowed.
    @Override
    public boolean borrowItem(String userId, String borrowDate) {
        if (isBorrowed) return false;
        isBorrowed = true;
        setBorrowInfo(borrowDate, userId);
        return true;
    }

    // This method is used to return the magazine and reset info.
    @Override
    public void returnItem(String returnDate) {
        isBorrowed = false;
        clearBorrowInfo();
    }

    // This method is used to show all magazine details.
    @Override
    public String display() {
        String status = isBorrowed ? "Borrowed Borrowed Date: " + borrowedDate + " Borrowed by: " + borrowedBy : "Available";
        return "------ Item Information for " + id + " ------\n" +
                "ID: " + id + " Name: " + title + " Status: " + status + "\n" +
                "Publisher: " + publisher + " Category: " + category;
    }

    // This method gives the same result as display().
    @Override
    public String toString() {
        return display();
    }
}
