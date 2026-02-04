package Library;

import java.time.LocalDate;

// This class is used to define a guest user in the system.
public class Guest extends UserBase {

    // This variable is used to store the guest's occupation.
    private String occupation;

    // This constructor is used when a guest user is created with details.
    public Guest(String id, String name, String phoneNumber, String occupation) {
        super(id, name, phoneNumber);
        this.occupation = occupation;
    }

    // The maximum number of items that can be borrowed is given here.
    @Override
    public int getMaxItems() {
        return 1;
    }

    // The number of days before penalty is returned here.
    @Override
    public int getOverdueLimit() {
        return 7;
    }

    // This method blocks rare and limited items from being borrowed.
    @Override
    public boolean borrowItem(Item item, LocalDate borrowDate) {
        if (item.getType().equalsIgnoreCase("rare") || item.getType().equalsIgnoreCase("limited"))
            return false;
        return super.borrowItem(item, borrowDate);
    }

    // This method returns guest user information in a readable format.
    @Override
    public String toString() {
        return "------ User Information for " + id + " ------\n" +
                "Name: " + name + " Phone: " + phoneNumber + "\n" +
                "Occupation: " + occupation;
    }
}
