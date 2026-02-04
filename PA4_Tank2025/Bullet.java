import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Represents a bullet shot by the player or enemy.
 */
public class Bullet {

    private ImageView imageView;
    private PlayerTank.Direction direction;
    private static final double SPEED = 8;
    private boolean isAlive = true;
    private boolean fromPlayer;

    /**
     * Constructs a new bullet, sets its direction, image, and starts movement.
     */
    public Bullet(double startX, double startY, PlayerTank.Direction direction, Pane pane, boolean fromPlayer) {
        this.direction = direction;
        this.fromPlayer = fromPlayer;

        imageView = new ImageView(Assets.bullet);
        imageView.setFitWidth(10);
        imageView.setFitHeight(10);

        switch (direction) {
            case UP: imageView.setRotate(270); break;
            case DOWN: imageView.setRotate(90); break;
            case LEFT: imageView.setRotate(180); break;
            case RIGHT: imageView.setRotate(0); break;
        }

        imageView.setX(startX);
        imageView.setY(startY);
        pane.getChildren().add(imageView);

        moveLoop(pane);
    }

    /**
     * Moves the bullet and handles all collisions.
     */
    private void moveLoop(Pane pane) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isAlive) {
                    pane.getChildren().remove(imageView);
                    stop();
                    return;
                }

                // Move bullet
                switch (direction) {
                    case UP: imageView.setY(imageView.getY() - SPEED); break;
                    case DOWN: imageView.setY(imageView.getY() + SPEED); break;
                    case LEFT: imageView.setX(imageView.getX() - SPEED); break;
                    case RIGHT: imageView.setX(imageView.getX() + SPEED); break;
                }

                // Out of bounds
                if (imageView.getX() < 0 || imageView.getX() > 800 ||
                        imageView.getY() < 0 || imageView.getY() > 600) {
                    destroy();
                    return;
                }

                // Breakable wall
                for (BreakableWall bw : Main.getInstance().breakableWalls) {
                    if (bw != null && bw.getBounds().intersects(imageView.getBoundsInParent())) {
                        destroy();
                        bw.hit(pane);
                        return;
                    }
                }

                // Indestructible wall → small explosion
                for (javafx.scene.Node node : pane.getChildren()) {
                    if (node instanceof ImageView && ((ImageView) node).getImage() == Assets.wall) {
                        if (imageView.getBoundsInParent().intersects(node.getBoundsInParent())) {
                            destroy();
                            showSmallExplosion(pane, imageView.getX(), imageView.getY());
                            return;
                        }
                    }
                }

                // Enemy hit → big explosion
                if (fromPlayer) {
                    for (EnemyTank enemy : Main.enemies) {
                        if (enemy.getImageView().getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                            destroy();
                            enemy.destroy();
                            Main.onEnemyKilled();
                            showExplosion(pane, enemy.getImageView().getX(), enemy.getImageView().getY());
                            return;
                        }
                    }
                }

                // Player hit → big explosion
                if (!fromPlayer && Main.player != null && Main.player.getImageView() != null) {
                    if (imageView.getBoundsInParent().intersects(Main.player.getImageView().getBoundsInParent())) {
                        destroy();
                        showExplosion(pane, Main.player.getX(), Main.player.getY());
                        Main.onPlayerDeath(pane);
                    }
                }
            }
        };
        timer.start();
    }

    /**
     * Big explosion (tank hit).
     */
    private void showExplosion(Pane pane, double x, double y) {
        ImageView explosion = new ImageView(Assets.explosion);
        explosion.setFitWidth(40);
        explosion.setFitHeight(40);
        explosion.setX(x);
        explosion.setY(y);
        pane.getChildren().add(explosion);

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> pane.getChildren().remove(explosion));
        }).start();
    }

    /**
     * Small explosion (wall hit).
     */
    private void showSmallExplosion(Pane pane, double x, double y) {
        ImageView explosion = new ImageView(Assets.smallExplosion);
        explosion.setFitWidth(20);
        explosion.setFitHeight(20);
        explosion.setX(x);
        explosion.setY(y);
        pane.getChildren().add(explosion);

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> pane.getChildren().remove(explosion));
        }).start();
    }

    /**
     * Marks bullet as dead.
     */
    public void destroy() {
        isAlive = false;
    }

    /**
     * Returns bullet image.
     */
    public ImageView getImageView() {
        return imageView;
    }
}
