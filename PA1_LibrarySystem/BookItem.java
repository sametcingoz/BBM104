package Library;

// This class is used to define a book in the library.
public class BookItem extends Item {

    // These fields are used to keep the author and genre of the book.
    private String author;
    private String genre;

    // This constructor is used when a book is created with its details.
    public BookItem(String id, String title, String author, String genre, String type) {
        super(id, title, type);
        this.author = author;
        this.genre = genre;
    }

    // This method checks if the book can be borrowed, and updates status.
    @Override
    public boolean borrowItem(String userId, String borrowDate) {
        if (this.type.equalsIgnoreCase("reference") || isBorrowed) return false;
        isBorrowed = true;
        setBorrowInfo(borrowDate, userId);
        return true;
    }

    // This method is called when the book is returned to the library.
    @Override
    public void returnItem(String returnDate) {
        isBorrowed = false;
        clearBorrowInfo();
    }

    // This method is used to display the current information of the book.
    @Override
    public String display() {
        String status = isBorrowed ? "Borrowed" : "Available";
        String extra = isBorrowed ? " Borrowed Date: " + borrowedDate + " Borrowed by: " + borrowedBy : "";
        return "------ Item Information for " + id + " ------\n" +
                "ID: " + id + " Name: " + title + " Status: " + status + extra + "\n" +
                "Author: " + author + " Genre: " + genre;
    }

    // This method is used to return display information as a string.
    @Override
    public String toString() {
        return display();
    }
}
