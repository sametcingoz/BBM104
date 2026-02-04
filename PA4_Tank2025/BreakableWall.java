import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Bounds;

/**
 * Represents a breakable wall that can be destroyed by bullets.
 */
public class BreakableWall {

    private ImageView imageView;
    private boolean destroyed = false;

    /**
     * Constructor: Initializes wall image and position.
     */
    public BreakableWall(double x, double y) {
        imageView = new ImageView(Assets.wall);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(false);
        imageView.setX(x);
        imageView.setY(y);
    }

    /**
     * Adds wall image to game screen.
     */
    public void addToPane(Pane pane) {
        pane.getChildren().add(imageView);
    }

    /**
     * Returns bounding box for collision detection.
     */
    public Bounds getBounds() {
        return imageView.getBoundsInParent();
    }

    /**
     * Handles destruction logic when hit by bullet.
     */
    public void hit(Pane pane) {
        if (destroyed) return;
        destroyed = true;
        showExplosion(pane);
        pane.getChildren().remove(imageView);
        Main.getInstance().breakableWalls.remove(this);
    }

    /**
     * Displays explosion effect briefly at wall's location.
     */
    private void showExplosion(Pane pane) {
        ImageView explosion = new ImageView(Assets.explosion);
        explosion.setFitWidth(20);
        explosion.setFitHeight(20);
        explosion.setX(imageView.getX());
        explosion.setY(imageView.getY());
        pane.getChildren().add(explosion);

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> pane.getChildren().remove(explosion));
        }).start();
    }
}
