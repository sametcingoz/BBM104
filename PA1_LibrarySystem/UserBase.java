package Library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// This abstract class is used as a base for all user types.
public abstract class UserBase {

    // These fields are used to store user details and borrow info.
    protected String id;
    protected String name;
    protected String phoneNumber;
    protected double penalty;
    protected List<Item> borrowedItems;
    protected Map<Item, LocalDate> borrowDates;

    // This constructor is used to create a new user with basic info.
    public UserBase(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.penalty = 0.0;
        this.borrowedItems = new ArrayList<>();
        this.borrowDates = new HashMap<>();
    }

    // This method is used to get the name of the user.
    public String getName() { return name; }

    // These abstract methods must be defined in subclasses.
    public abstract int getMaxItems();
    public abstract int getOverdueLimit();

    // This method checks if the user can borrow another item.
    public boolean canBorrow() {
        return penalty < 6.0 && borrowedItems.size() < getMaxItems();
    }

    // This method checks overdue items and applies penalty if needed.
    public List<String> checkOverdueItems(LocalDate currentDate) {
        List<String> logs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Iterator<Item> it = borrowedItems.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            LocalDate bDate = borrowDates.get(item);
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(bDate, currentDate);
            if (daysBetween >= getOverdueLimit()) {
                item.returnItem(currentDate.format(formatter));
                logs.add("Auto-return: User " + id + " auto-returned item " + item.getId() +
                        " on " + currentDate.format(formatter) + " due to overdue, penalty applied");
                it.remove();
                borrowDates.remove(item);
                addPenalty(2.0);
            }
        }
        return logs;
    }

    // This method is used to borrow an item and store its date.
    public boolean borrowItem(Item item, LocalDate borrowDate) {
        checkOverdueItems(borrowDate);
        if (!canBorrow()) return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean success = item.borrowItem(id, borrowDate.format(formatter));
        if (success) {
            borrowedItems.add(item);
            borrowDates.put(item, borrowDate);
            return true;
        }
        return false;
    }

    // This method is used to return a borrowed item.
    public boolean returnItem(Item item, LocalDate returnDate) {
        if (borrowedItems.contains(item)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            item.returnItem(returnDate.format(formatter));
            borrowedItems.remove(item);
            borrowDates.remove(item);
            return true;
        }
        return false;
    }

    // This method adds penalty to the user's account.
    public void addPenalty(double amount) {
        penalty += amount;
    }

    // This method resets the penalty to 0.
    public void clearPenalty() {
        penalty = 0.0;
    }

    // This method shows basic info about the user.
    public String toString() {
        return id + " " + name + " " + phoneNumber + " Penalty: " + penalty;
    }
}
