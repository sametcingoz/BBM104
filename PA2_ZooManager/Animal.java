import java.util.List;
import java.util.Locale;

public abstract class Animal {
    // Common properties for all animals
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Get the name of the animal
    public String getName() {
        return name;
    }

    // Abstract method for feeding the animal
    public abstract void feed(int mealCount, FoodStock stock, List<String> output) throws ZooException.NotEnoughFoodException;

    // Abstract method for cleaning the animal's habitat
    public abstract void cleanHabitat(List<String> output);
}

// ------------------ Lion ------------------
class Lion extends Animal {
    public Lion(String name, int age) {
        super(name, age); // Call parent constructor
    }

    @Override
    public void feed(int mealCount, FoodStock stock, List<String> output) throws ZooException.NotEnoughFoodException {
        // Calculate meal size for lion based on age
        double mealPer = 5.0 + 0.25 * (age - 5);
        double total = mealPer * mealCount;
        stock.consume("Meat", total); // Consume meat from stock

        // Format number: remove unnecessary zeros
        String display = String.format(Locale.US, "%.3f", total);

        // Add result to output
        output.add(name + " has been given " + display + " kgs of meat");
    }

    @Override
    public void cleanHabitat(List<String> output) {
        // Output cleaning message specific to lion
        output.add("Cleaning " + name + "'s habitat: Removing bones and refreshing sand.");
    }
}

// ------------------ Elephant ------------------
class Elephant extends Animal {
    public Elephant(String name, int age) {
        super(name, age);
    }

    @Override
    public void feed(int mealCount, FoodStock stock, List<String> output) throws ZooException.NotEnoughFoodException {
        // Calculate meal size for elephant
        double mealPer = 10.0 + 0.18 * (age - 20);
        double total = mealPer * mealCount;
        stock.consume("Plant", total); // Elephants eat plants

        String display = String.format(Locale.US, "%.3f", total);

        output.add(name + " has been given " + display + " kgs assorted fruits and hay");
    }

    @Override
    public void cleanHabitat(List<String> output) {
        output.add("Cleaning " + name + "'s habitat: Washing the water area.");
    }
}

// ------------------ Penguin ------------------
class Penguin extends Animal {
    public Penguin(String name, int age) {
        super(name, age);
    }

    @Override
    public void feed(int mealCount, FoodStock stock, List<String> output) throws ZooException.NotEnoughFoodException {
        // Calculate meal size for penguin
        double mealPer = 3.0 + 0.04 * (age - 4);
        double total = mealPer * mealCount;
        stock.consume("Fish", total); // Penguins eat fish

        String display = String.format(Locale.US, "%.3f", total);

        output.add(name + " has been given " + display + " kgs of various kinds of fish");
    }

    @Override
    public void cleanHabitat(List<String> output) {
        output.add("Cleaning " + name + "'s habitat: Replenishing ice and scrubbing walls.");
    }
}

// ------------------ Chimpanzee ------------------
class Chimpanzee extends Animal {
    public Chimpanzee(String name, int age) {
        super(name, age);
    }

    @Override
    public void feed(int mealCount, FoodStock stock, List<String> output) throws ZooException.NotEnoughFoodException {
        // Calculate total food amount based on age
        double total = 6.0 + 0.15 * (age - 10);
        double each = total / 2.0; // Split between meat and plants
        double totalEach = each * mealCount;

        stock.consume("Meat", totalEach);
        stock.consume("Plant", totalEach);

        String display = String.format(Locale.US, "%.3f", totalEach);

        output.add(name + " has been given " + display + " kgs of meat and " + display + " kgs of leaves");
    }

    @Override
    public void cleanHabitat(List<String> output) {
        output.add("Cleaning " + name + "'s habitat: Sweeping the enclosure and replacing branches.");
    }
}
