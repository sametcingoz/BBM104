import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.Random;

public class EnemyTank {

    private ImageView imageView;
    private PlayerTank.Direction direction;
    private double x, y;
    private static final double MOVE_SPEED = 2;

    private boolean isAlive = true;
    private boolean isPaused = false;
    private Random random = new Random();
    private AnimationTimer timer;
    private Pane pane;
    private boolean frameToggle = false; // for animation effect

    // Constructor to initialize the enemy tank with position and add it to the game pane
    public EnemyTank(double startX, double startY, Pane pane) {
        this.x = startX;
        this.y = startY;
        this.direction = PlayerTank.Direction.UP;
        this.pane = pane;

        imageView = new ImageView(Assets.whiteTankUp);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        imageView.setRotate(270);

        pane.getChildren().add(imageView);

        startBehaviorLoop();
    }

    // Starts the animation loop for moving and firing behavior
    private void startBehaviorLoop() {
        timer = new AnimationTimer() {
            long lastMoveChange = 0;
            long lastFire = 0;

            @Override
            public void handle(long now) {
                if (!isAlive || isPaused) return;

                // Randomly change movement direction every second
                if (now - lastMoveChange > 1_000_000_000L) {
                    direction = PlayerTank.Direction.values()[random.nextInt(4)];
                    rotateTank();
                    lastMoveChange = now;
                }

                // Fire a bullet at random intervals
                if (now - lastFire > 1_500_000_000L + random.nextInt(1_000_000_000)) {
                    fireBullet(pane);
                    lastFire = now;
                }

                move(Main.getInstance().walls, Main.getInstance().breakableWalls);
                imageView.setX(x);
                imageView.setY(y);
            }
        };
        timer.start();
    }

    // Moves the tank unless blocked by wall, breakable wall, another enemy tank, or the player tank
    public void move(List<Wall> walls, List<BreakableWall> breakables) {
        double nextX = x, nextY = y;
        switch (direction) {
            case UP: nextY -= MOVE_SPEED; break;
            case DOWN: nextY += MOVE_SPEED; break;
            case LEFT: nextX -= MOVE_SPEED; break;
            case RIGHT: nextX += MOVE_SPEED; break;
        }

        Bounds nextBounds = new BoundingBox(nextX, nextY, imageView.getFitWidth(), imageView.getFitHeight());

        // Block movement if collides with wall
        for (Wall wall : walls)
            if (wall.getBounds().intersects(nextBounds)) return;

        // Block movement if collides with breakable wall
        for (BreakableWall bw : breakables)
            if (bw.getBounds().intersects(nextBounds)) return;

        // Block movement if collides with another enemy tank
        for (EnemyTank other : Main.enemies) {
            if (other != this && other.isAlive) {
                Bounds otherBounds = other.getImageView().getBoundsInParent();
                if (otherBounds.intersects(nextBounds)) {
                    return;
                }
            }
        }

        // Block movement if collides with the player tank
        PlayerTank player = Main.getInstance().getPlayer();
        if (player != null && player.getImageView().getBoundsInParent().intersects(nextBounds)) {
            return;
        }

        // Update position if no collisions
        x = nextX;
        y = nextY;

        // toggle tank image to simulate animation
        frameToggle = !frameToggle;
        imageView.setImage(frameToggle ? Assets.whiteTankUp : Assets.whiteTank2);

    }

    // Pauses the tank behavior
    public void pause() {
        if (!isPaused) {
            isPaused = true;
            timer.stop();
        }
    }

    // Resumes the tank behavior
    public void resume() {
        if (isPaused) {
            isPaused = false;
            timer.start();
        }
    }

    // Rotates the tank image to match current direction
    private void rotateTank() {
        switch (direction) {
            case UP: imageView.setRotate(270); break;
            case RIGHT: imageView.setRotate(0); break;
            case DOWN: imageView.setRotate(90); break;
            case LEFT: imageView.setRotate(180); break;
        }
    }

    // Spawns a bullet in the current direction
    private void fireBullet(Pane pane) {
        if (!isAlive) return;

        double bulletX = x + imageView.getFitWidth() / 2 - 4;
        double bulletY = y + imageView.getFitHeight() / 2 - 4;

        switch (direction) {
            case UP: bulletY -= 28; break;
            case DOWN: bulletY += 28; break;
            case LEFT: bulletX -= 28; break;
            case RIGHT: bulletX += 28; break;
        }

        new Bullet(bulletX, bulletY, direction, pane, false);
    }

    // Destroys the tank and removes it from the pane
    public void destroy() {
        isAlive = false;

        if (timer != null) {
            timer.stop();
            timer = null;
        }

        if (pane != null) {
            pane.getChildren().remove(imageView);
        }

        Main.enemies.remove(this);

        if (Main.enemies.size() < 1) {
            spawnNewEnemy();
        }
    }

    // Spawns a new enemy tank at a valid position
    private void spawnNewEnemy() {
        int tileSize = 40;
        int attempts = 0;
        int maxAttempts = 100;

        while (attempts++ < maxAttempts) {
            int col = random.nextInt(20);
            int row = random.nextInt(4);

            double newX = col * tileSize;
            double newY = row * tileSize;

            BoundingBox testBounds = new BoundingBox(newX, newY, tileSize, tileSize);

            boolean blocked = false;

            for (Wall w : Main.getInstance().walls)
                if (w.getBounds().intersects(testBounds)) { blocked = true; break; }

            for (BreakableWall bw : Main.getInstance().breakableWalls)
                if (bw.getBounds().intersects(testBounds)) { blocked = true; break; }

            for (EnemyTank e : Main.enemies)
                if (e != this && e.getImageView().getBoundsInParent().intersects(testBounds)) { blocked = true; break; }

            PlayerTank player = Main.getInstance().getPlayer();
            if (player != null && player.getImageView().getBoundsInParent().intersects(testBounds)) {
                blocked = true;
            }

            if (!blocked) {
                EnemyTank newEnemy = new EnemyTank(newX, newY, pane);
                Main.enemies.add(newEnemy);
                break;
            }
        }
    }

    // Returns the image view of the tank for rendering and collision
    public ImageView getImageView() {
        return imageView;
    }
}
