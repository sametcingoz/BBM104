import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;

public class PlayerTank {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private ImageView imageView;
    private Direction direction;
    private double x, y;
    private static final double MOVE_SPEED = 5;

    private long[] lastFireTimes = new long[10];
    private int fireIndex = 0;
    private boolean isCoolingDown = false;
    private Label calmLabel = null;
    private boolean frameToggle = false; // used to alternate tank image on movement

    // Constructor to initialize player tank at specified position
    public PlayerTank(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.direction = Direction.UP;

        imageView = new ImageView(Assets.yellowTank1);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        imageView.setRotate(270);
        imageView.setX(x);
        imageView.setY(y);
    }

    // Adds the player tank to the game pane
    public void addToPane(Pane pane) {
        pane.getChildren().add(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    // Moves the tank in a specified direction unless blocked by walls or enemy tanks
    // Automatically fires a bullet while moving
    public void move(Direction dir, List<Wall> walls, List<BreakableWall> breakables, Pane pane) {
        double nextX = x, nextY = y;
        switch (dir) {
            case UP:    nextY -= MOVE_SPEED; break;
            case DOWN:  nextY += MOVE_SPEED; break;
            case LEFT:  nextX -= MOVE_SPEED; break;
            case RIGHT: nextX += MOVE_SPEED; break;
        }

        Bounds nextBounds = imageView.localToParent(imageView.getBoundsInLocal());
        nextBounds = new BoundingBox(nextX, nextY, nextBounds.getWidth(), nextBounds.getHeight());

        for (Wall wall : walls)
            if (wall.getBounds().intersects(nextBounds)) return;

        for (BreakableWall bw : breakables)
            if (bw.getBounds().intersects(nextBounds)) return;

        for (EnemyTank enemy : Main.enemies)
            if (enemy.getImageView().getBoundsInParent().intersects(nextBounds)) return;

        // Toggle tank image for animation
        if (imageView.getImage() == Assets.yellowTank1) {
            imageView.setImage(Assets.yellowTank2);
        } else {
            imageView.setImage(Assets.yellowTank1);
        }

        x = nextX;
        y = nextY;
        imageView.setX(x);
        imageView.setY(y);
        direction = dir;
        rotateTank();
    }


    // Rotates the tank image based on current direction
    private void rotateTank() {
        switch (direction) {
            case UP:    imageView.setRotate(270); break;
            case RIGHT: imageView.setRotate(0); break;
            case DOWN:  imageView.setRotate(90); break;
            case LEFT:  imageView.setRotate(180); break;
        }
    }

    // Fires a bullet if not in cooldown, otherwise triggers calm-down logic
    public void fireBullet(Pane pane) {
        if (isCoolingDown) return;

        long now = System.currentTimeMillis();
        lastFireTimes[fireIndex] = now;
        fireIndex = (fireIndex + 1) % lastFireTimes.length;

        if (now - lastFireTimes[(fireIndex + 1) % lastFireTimes.length] < 2000) {
            isCoolingDown = true;
            showCalmDownMessage(pane);
            new Thread(() -> {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                isCoolingDown = false;
                javafx.application.Platform.runLater(() -> {
                    if (calmLabel != null) {
                        pane.getChildren().remove(calmLabel);
                        calmLabel = null;
                    }
                });
            }).start();
            return;
        }

        double bulletX = x + imageView.getFitWidth() / 2 - 4;
        double bulletY = y + imageView.getFitHeight() / 2 - 4;

        switch (direction) {
            case UP:    bulletY -= 28; break;
            case DOWN:  bulletY += 28; break;
            case LEFT:  bulletX -= 28; break;
            case RIGHT: bulletX += 28; break;
        }

        new Bullet(bulletX, bulletY, direction, pane, true);
    }

    // Displays warning label when shooting too frequently
    private void showCalmDownMessage(Pane pane) {
        javafx.application.Platform.runLater(() -> {
            if (calmLabel == null) {
                calmLabel = new Label("   Too many shots!\nRestarting in 2 secs...");
                calmLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-family: Consolas; -fx-font-weight: bold;");
                calmLabel.setLayoutX(290);
                calmLabel.setLayoutY(460);
                pane.getChildren().add(calmLabel);
            }
        });
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }
}
