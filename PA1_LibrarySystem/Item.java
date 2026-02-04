package Library;

// This abstract class is used for all types of items in the library.
public abstract class Item {

    // These fields are used to keep basic information about the item.
    protected String id;
    protected String title;
    protected String type;
    protected boolean isBorrowed;
    protected String borrowedDate;
    protected String borrowedBy;

    // This constructor is used when a new item is created.
    public Item(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.isBorrowed = false;
        this.borrowedDate = "";
        this.borrowedBy = "";
    }

    // These methods return information about the item.
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public boolean isBorrowed() { return isBorrowed; }

    // This method is used to change the borrow status.
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    // This method is used to record who borrowed the item and when.
    public void setBorrowInfo(String date, String borrower) {
        this.borrowedDate = date;
        this.borrowedBy = borrower;
    }

    // This method is used to clear the borrow information.
    public void clearBorrowInfo() {
        this.borrowedDate = "";
        this.borrowedBy = "";
    }

    // These methods must be written by the subclasses.
    public abstract boolean borrowItem(String userId, String borrowDate);
    public abstract void returnItem(String returnDate);

    // This method will be used to show item info in subclasses.
    public abstract String display();
}
