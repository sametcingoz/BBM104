import java.util.Locale;

public class FoodStock {

    // Amounts of food types available in the stock
    private double meat;
    private double fish;
    private double plant;

    // Constructor to initialize food amounts
    public FoodStock(double meat, double fish, double plant) {
        this.meat = meat;
        this.fish = fish;
        this.plant = plant;
    }

    // Consumes a specific amount of food from the stock
    public void consume(String type, double amount) throws ZooException.NotEnoughFoodException {
        switch (type.toLowerCase()) {
            case "meat":
                if (meat < amount) // If not enough meat, throw exception
                    throw new ZooException.NotEnoughFoodException("Not enough Meat");
                meat -= amount; // Subtract from stock
                break;
            case "fish":
                if (fish < amount)
                    throw new ZooException.NotEnoughFoodException("Not enough Fish");
                fish -= amount;
                break;
            case "plant":
                if (plant < amount)
                    throw new ZooException.NotEnoughFoodException("Not enough Plant");
                plant -= amount;
                break;
            default:
                // If unknown type is given, throw illegal argument exception
                throw new IllegalArgumentException("Unknown food type: " + type);
        }
    }

    // Returns current amount of requested food type
    public double get(String type) {
        switch (type.toLowerCase()) {
            case "meat": return meat;
            case "fish": return fish;
            case "plant": return plant;
            default: throw new IllegalArgumentException("Unknown food type: " + type);
        }
    }

    // Returns a formatted string showing remaining food stock
    public String getFormattedStock() {
        // Format values and remove unnecessary zeros from decimals
        String p = String.format(Locale.US, "%.3f", plant);

        String f = String.format(Locale.US, "%.3f", fish);

        String m = String.format(Locale.US, "%.3f", meat);

        // Combine all formatted values into one output string
        return "Plant: " + p + " kgs\n" +
                "Fish: " + f + " kgs\n" +
                "Meat: " + m + " kgs";
    }
}
