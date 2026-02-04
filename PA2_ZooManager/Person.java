import java.util.List;
import java.util.Map;

// Base abstract class for all people in the zoo (Visitor or Personnel)
public abstract class Person {
    protected String name;
    protected int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // Returns the name of the person
    public String getName() {
        return name;
    }

    // Returns the ID of the person
    public int getId() {
        return id;
    }

    // Abstract method for visiting an animal
    public abstract void visit(String animalName, Map<String, Animal> animals, List<String> output)
            throws ZooException.AnimalNotFoundException;

    // Abstract method for feeding an animal
    public abstract void feed(String animalName, int mealCount, Map<String, Animal> animals,
                              FoodStock stock, List<String> output)
            throws ZooException.UnauthorizedAccessException,
            ZooException.AnimalNotFoundException,
            ZooException.NotEnoughFoodException;
}

// ------------------ Visitor ------------------
class Visitor extends Person {

    public Visitor(String name, int id) {
        super(name, id);
    }

    @Override
    public void visit(String animalName, Map<String, Animal> animals, List<String> output)
            throws ZooException.AnimalNotFoundException {
        Animal animal = animals.get(animalName); // Try to get the animal from the map
        if (animal == null)
            throw new ZooException.AnimalNotFoundException("There are no animals with the name " + animalName + ".");
        output.add(name + " tried  to register for a visit to " + animalName + "."); // Log the attempt
        output.add(name + " successfully visited " + animalName + "."); // Log the success
    }

    @Override
    public void feed(String animalName, int mealCount, Map<String, Animal> animals,
                     FoodStock stock, List<String> output)
            throws ZooException.UnauthorizedAccessException {
        output.add(name + " tried to feed " + animalName); // Log the attempt
        throw new ZooException.UnauthorizedAccessException("Visitors do not have the authority to feed animals.");
    }
}

// ------------------ Personnel ------------------
class Personnel extends Person {

    public Personnel(String name, int id) {
        super(name, id);
    }

    @Override
    public void visit(String animalName, Map<String, Animal> animals, List<String> output)
            throws ZooException.AnimalNotFoundException {
        Animal animal = animals.get(animalName); // Try to get the animal
        if (animal == null)
            throw new ZooException.AnimalNotFoundException("There are no animals with the name " + animalName + ".");
        output.add(name + " attempts to clean " + animalName + "'s habitat."); // Log the attempt
        output.add(name + " started cleaning " + animalName + "'s habitat."); // Log the start of cleaning
        animal.cleanHabitat(output); // Call the animal's cleaning method
    }

    @Override
    public void feed(String animalName, int mealCount, Map<String, Animal> animals,
                     FoodStock stock, List<String> output)
            throws ZooException.AnimalNotFoundException, ZooException.NotEnoughFoodException {
        Animal animal = animals.get(animalName); // Try to get the animal
        if (animal == null)
            throw new ZooException.AnimalNotFoundException("There are no animals with the name " + animalName + ".");
        output.add(name + " attempts to feed " + animalName + "."); // Log the attempt
        animal.feed(mealCount, stock, output); // Feed the animal
    }
}
