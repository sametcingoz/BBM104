import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Check if the program received exactly 5 arguments
        if (args.length != 5) {
            System.out.println("Usage: java Main animals.txt persons.txt foods.txt commands.txt output.txt");
            return; // Stop the program if argument count is wrong
        }

        // Assign command-line arguments to variables
        String animalsFile = args[0];
        String personsFile = args[1];
        String foodsFile = args[2];
        String commandsFile = args[3];
        String outputFile = args[4];

        // Create a ZooManager object to handle all logic
        ZooManager zooManager = new ZooManager();

        try {
            // Load animals, people, and food data from input files
            zooManager.loadAnimals(animalsFile);
            zooManager.loadPersons(personsFile);
            zooManager.loadFoods(foodsFile);

            // Execute commands from the commands file
            zooManager.executeCommands(commandsFile);

            // Write the final output to the output file
            zooManager.writeOutput(outputFile);
        } catch (IOException e) {
            // Catch file-related errors and print a message
            System.out.println("I/O Error: " + e.getMessage());
        }
    }
}
