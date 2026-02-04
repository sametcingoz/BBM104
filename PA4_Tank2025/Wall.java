import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Bounds;

/**
 * Represents an indestructible wall on the game map.
 */
public class Wall {

    // Visual representation of the wall
    private ImageView imageView;

    /**
     * Constructs a wall at given (x, y) position.
     */
    public Wall(double x, double y) {
        imageView = new ImageView(Assets.wall); // Load wall image
        imageView.setFitWidth(40);              // Set fixed tile size
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(false);      // Stretch to fit exactly
        imageView.setX(x);                      // Set position X
        imageView.setY(y);                      // Set position Y
    }

    /**
     * Adds the wall to the game scene.
     */
    public void addToPane(Pane pane) {
        pane.getChildren().add(imageView);
    }

    /**
     * Returns the bounding box for collision detection.
     */
    public Bounds getBounds() {
        return imageView.getBoundsInParent();
    }

    /**
     * Returns the ImageView of the wall.
     */
    public ImageView getImageView() {
        return imageView;
    }
}
