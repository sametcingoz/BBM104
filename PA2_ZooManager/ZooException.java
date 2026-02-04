public class ZooException {

    /**
     * Thrown when there is not enough food in stock to feed an animal.
     */
    public static class NotEnoughFoodException extends Exception {
        public NotEnoughFoodException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a person without authority attempts to feed an animal.
     */
    public static class UnauthorizedAccessException extends Exception {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a given animal name is not found in the system.
     */
    public static class AnimalNotFoundException extends Exception {
        public AnimalNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a given person ID is not registered in the system.
     */
    public static class PersonNotFoundException extends Exception {
        public PersonNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when the command format is incorrect or has non-numeric input.
     */
    public static class InvalidCommandFormatException extends Exception {
        public InvalidCommandFormatException(String message) {
            super(message);
        }
    }
}
