package Library;

// This class is used to start and run the library system.
public class Main {
    public static void main(String[] args) {

        // This part checks if the correct number of arguments is given.
        if(args.length != 4) {
            System.out.println("Wrong usage! Try again.");
            return;
        }

        // A new system is created and files are loaded step by step.
        LibrarySystem system = new LibrarySystem();     // Class name was updated from LibraryManagementSystem
        system.loadItems(args[0]);                      // Items are loaded from the first file.
        system.loadUsers(args[1]);                      // Users are loaded from the second file.
        system.processCommands(args[2]);                // Commands are read from the third file.
        system.writeOutput(args[3]);                    // Output is written to the fourth file.
    }
}
