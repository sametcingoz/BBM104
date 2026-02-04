import java.io.*;
import java.util.*;

public class ZooManager {

    // Map to hold animal name and corresponding Animal object
    private Map<String, Animal> animals = new HashMap<>();
    // Map to hold person ID and corresponding Person object
    private Map<Integer, Person> people = new HashMap<>();
    // Food stock object for managing available food
    private FoodStock stock;
    // List to collect all output messages
    private List<String> output = new ArrayList<>();

    // Loads animal information from file
    public void loadAnimals(String fileName) throws IOException {
        output.add("***********************************");
        output.add("***Initializing Animal information***");
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(","); // Split line by comma
            String type = parts[0];
            String name = parts[1];
            int age = Integer.parseInt(parts[2]);

            Animal animal = null;
            // Create specific animal object based on type
            switch (type.toLowerCase()) {
                case "lion":
                    animal = new Lion(name, age);
                    break;
                case "elephant":
                    animal = new Elephant(name, age);
                    break;
                case "penguin":
                    animal = new Penguin(name, age);
                    break;
                case "chimpanzee":
                    animal = new Chimpanzee(name, age);
                    break;
            }

            // Add the animal to the map if valid
            if (animal != null) {
                animals.put(name, animal);
                output.add("Added new " + type + " with name " + name + " aged " + age + ".");
            }
        }
        reader.close();
    }

    // Loads people (visitors and personnel) from file
    public void loadPersons(String fileName) throws IOException {
        output.add("***********************************");
        output.add("***Initializing Visitor and Personnel information***");
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0];
            String name = parts[1];
            int id = Integer.parseInt(parts[2]);

            Person person = null;
            // Create specific person object based on type
            switch (type.toLowerCase()) {
                case "visitor":
                    person = new Visitor(name, id);
                    break;
                case "personnel":
                    person = new Personnel(name, id);
                    break;
            }

            // Add person to the map if valid
            if (person != null) {
                people.put(id, person);
                output.add("Added new " + type + " with id " + id + " and name " + name + ".");
            }
        }
        reader.close();
    }

    // Loads initial food stock from file
    public void loadFoods(String fileName) throws IOException {
        output.add("***********************************");
        output.add("***Initializing Food Stock***");
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        double meat = 0, fish = 0, plant = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String type = parts[0].toLowerCase();
            double amount = Double.parseDouble(parts[1]);
            // Assign amount based on food type
            switch (type) {
                case "meat":
                    meat = amount;
                    break;
                case "fish":
                    fish = amount;
                    break;
                case "plant":
                    plant = amount;
                    break;
            }
        }
        // Create the FoodStock object with given values
        stock = new FoodStock(meat, fish, plant);
        output.add("There are " + String.format(Locale.US, "%.3f", meat) + " kg of Meat in stock");
        output.add("There are " + String.format(Locale.US, "%.3f", fish) + " kg of Fish in stock");
        output.add("There are " + String.format(Locale.US, "%.3f", plant) + " kg of Plant in stock");
        reader.close();
    }

    // Executes all commands from the commands file
    public void executeCommands(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue; // Skip empty lines
            output.add("***********************************");
            output.add("***Processing new Command***");
            try {
                String[] parts = line.split(",");
                String op = parts[0];

                if (op.equals("List Food Stock")) {
                    output.add("Listing available Food Stock:");
                    output.add(stock.getFormattedStock());
                } else if (op.equals("Animal Visitation")) {
                    int id = Integer.parseInt(parts[1]);
                    String animalName = parts[2];
                    Person p = getPersonById(id); // Find person by ID
                    p.visit(animalName, animals, output); // Call visit method
                } else if (op.equals("Feed Animal")) {
                    int id = Integer.parseInt(parts[1]);
                    String animalName = parts[2];
                    int meals = Integer.parseInt(parts[3]);
                    Person p = getPersonById(id); // Find person by ID
                    p.feed(animalName, meals, animals, stock, output); // Call feed method
                } else {
                    output.add("Unknown command: " + line); // Invalid command
                }

            } catch (NumberFormatException e) {
                // Handle case where number cannot be parsed
                output.add("Error processing command: " + line);
                output.add("Error:" + e.getMessage());
            } catch (ZooException.PersonNotFoundException |
                     ZooException.AnimalNotFoundException |
                     ZooException.NotEnoughFoodException |
                     ZooException.UnauthorizedAccessException e) {
                // Catch custom zoo-related exceptions
                output.add("Error: " + e.getMessage());
            } catch (Exception e) {
                // Catch any other unexpected exception
                output.add("Unexpected error: " + e.getMessage());
            }
        }
        reader.close();
    }

    // Helper method to get person by ID or throw exception
    private Person getPersonById(int id) throws ZooException.PersonNotFoundException {
        if (!people.containsKey(id)) {
            throw new ZooException.PersonNotFoundException("There are no visitors or personnel with the id " + id);
        }
        return people.get(id);
    }

    // Writes collected output lines to the output file
    public void writeOutput(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName);
        for (String line : output) {
            writer.println(line); // Write each line to file
        }
        writer.close();
    }
}
