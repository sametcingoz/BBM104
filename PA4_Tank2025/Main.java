import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class that initializes and runs the Tank 2025 game.
 * Handles the game state, player and enemy behavior, UI overlays, and input control.
 */
public class Main extends Application {

    /** List of all enemy tanks currently on screen */
    public static List<EnemyTank> enemies = new ArrayList<>();

    /** Score and life tracking component */
    public static GameStats stats;

    /** The player's tank instance */
    public static PlayerTank player;

    /** Indestructible walls */
    public List<Wall> walls = new ArrayList<>();

    /** Breakable walls */
    public List<BreakableWall> breakableWalls = new ArrayList<>();

    /** The main game pane where all objects are rendered */
    private Pane gameRoot;

    /** Scene for keyboard input and root attachment */
    private Scene scene;

    /** Pause state tracker */
    private boolean isPaused = false;

    /** Game over state tracker */
    private boolean isGameOver = false;

    /** Label for displaying "Game Over" screen */
    private Label gameOverLabel;

    /** Label for displaying "Paused" screen */
    private Label pauseLabel;

    /**
     * JavaFX entry point. Initializes stage, loads assets, and starts splash screen.
     */
    @Override
    public void start(Stage primaryStage) {
        Assets.load(); // load all image assets
        gameRoot = new Pane();
        gameRoot.setPrefSize(800, 600);
        gameRoot.setStyle("-fx-background-color: #2b2b2b;");
        scene = new Scene(gameRoot);

        primaryStage.setTitle("Tank 2025");
        primaryStage.setScene(scene);
        primaryStage.show();

        showSplashScreen(primaryStage);
    }

    /**
     * Dynamically adjusts the number of active enemies based on player's score.
     */
    private void updateEnemySpawn() {
        int desiredCount = Math.min(6, (Main.stats.getScore() / 2) + 1);
        int currentCount = enemies.size();
        int toSpawn = desiredCount - currentCount;

        for (int i = 0; i < toSpawn; i++) {
            spawnEnemy();
        }
    }

    /**
     * Called when an enemy is destroyed by the player.
     * Updates score and attempts to spawn new enemies.
     */
    public static void onEnemyKilled() {
        stats.increaseScore();
        getInstance().updateEnemySpawn();
    }

    /**
     * Returns the current player's tank instance.
     */
    public static PlayerTank getPlayer() {
        return player;
    }

    /**
     * Displays a brief splash screen before the game starts.
     */
    private void showSplashScreen(Stage stage) {
        Pane splashRoot = new Pane();
        splashRoot.setPrefSize(800, 600);
        splashRoot.setStyle("-fx-background-color: black;");

        Label title = new Label(" Tank 2025");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, 48));
        title.setTextFill(Color.LIMEGREEN);
        title.setLayoutX(250);
        title.setLayoutY(250);

        splashRoot.getChildren().add(title);
        Scene splashScene = new Scene(splashRoot);
        stage.setScene(splashScene);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                stage.setScene(scene);
                initGame();
                handleInput();
            });
        }).start();
    }

    /**
     * Creates outer borders and predefined breakable wall tiles.
     */
    private void initWalls() {
        int tileSize = 40;
        int columns = 20, rows = 15;
        Set<String> usedCoords = new HashSet<>();

        // Create outer border walls
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (x == 0 || y == 0 || x == columns - 1 || y == rows - 1) {
                    Wall wall = new Wall(x * tileSize, y * tileSize);
                    wall.addToPane(gameRoot);
                    walls.add(wall);
                    usedCoords.add(x + "," + y);
                }
            }
        }

        // Custom layout for breakable walls
        int[][] breakablePositions = {
                {3, 9}, {4, 9}, {5, 9}, {6, 9},
                {6, 4}, {7, 4}, {14, 8}, {13, 9}, {15, 3}, {15, 4},
                {7, 5}, {8, 5}, {9, 5}, {12, 8}, {13, 8}
        };

        for (int[] pos : breakablePositions) {
            String key = pos[0] + "," + pos[1];
            if (!usedCoords.contains(key)) {
                BreakableWall bw = new BreakableWall(pos[0] * tileSize, pos[1] * tileSize);
                bw.addToPane(gameRoot);
                breakableWalls.add(bw);
            }
        }
    }

    /**
     * Respawns the player in the center of the map.
     */
    public void respawnPlayer() {
        player = new PlayerTank(400, 300);
        player.addToPane(gameRoot);
    }

    /**
     * Resets the game state and initializes all components.
     */
    private void initGame() {
        if (gameOverLabel != null) {
            gameRoot.getChildren().remove(gameOverLabel);
            gameOverLabel = null;
        }

        gameRoot.getChildren().clear();
        enemies.clear();
        walls.clear();
        breakableWalls.clear();
        isPaused = false;
        isGameOver = false;

        stats = new GameStats(gameRoot);
        player = new PlayerTank(400, 300);
        player.addToPane(gameRoot);

        initWalls();
        spawnEnemy();
        setupOverlays();
    }

    /**
     * Spawns an enemy tank at a valid empty tile.
     */
    private void spawnEnemy() {
        if (enemies.size() >= 6) return;

        Random rand = new Random();
        int tileSize = 40;
        int maxAttempts = 100;
        int attempts = 0;

        while (attempts++ < maxAttempts) {
            int col = rand.nextInt(20);
            int row = rand.nextInt(4);
            double x = col * tileSize;
            double y = row * tileSize;

            boolean intersects = false;

            for (Wall wall : walls)
                if (wall.getBounds().intersects(x, y, tileSize, tileSize)) { intersects = true; break; }

            for (BreakableWall bw : breakableWalls)
                if (bw.getBounds().intersects(x, y, tileSize, tileSize)) { intersects = true; break; }

            for (EnemyTank et : enemies)
                if (et.getImageView().getBoundsInParent().intersects(x, y, tileSize, tileSize)) { intersects = true; break; }

            if (!intersects) {
                EnemyTank enemy = new EnemyTank(x, y, gameRoot);
                enemies.add(enemy);
                return;
            }
        }

        System.out.println(" Warning: Couldn't find spot to spawn enemy.");
    }

    /**
     * Sets up keyboard controls for player and system input.
     */
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private void handleInput() {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            if (code == KeyCode.P && !pressedKeys.contains(KeyCode.P)) {
                togglePause();
            }

            pressedKeys.add(code);

            if (code == KeyCode.R) {
                initGame();
                return;
            } else if (code == KeyCode.ESCAPE) {
                System.exit(0);
                return;
            }

            if (isGameOver || isPaused || player == null) return;

            if (code == KeyCode.X) {
                player.fireBullet(gameRoot);
            }
        });


        scene.setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
        });

        // Movement with delay (100ms between moves)
        new javafx.animation.AnimationTimer() {
            private long lastMoveTime = 0;

            @Override
            public void handle(long now) {
                if (isPaused || isGameOver || player == null) return;

                if (now - lastMoveTime < 31_000_000) return; // 100ms delay
                lastMoveTime = now;

                if (pressedKeys.contains(KeyCode.UP)) {
                    player.move(PlayerTank.Direction.UP, walls, breakableWalls, gameRoot);
                } else if (pressedKeys.contains(KeyCode.DOWN)) {
                    player.move(PlayerTank.Direction.DOWN, walls, breakableWalls, gameRoot);
                } else if (pressedKeys.contains(KeyCode.LEFT)) {
                    player.move(PlayerTank.Direction.LEFT, walls, breakableWalls, gameRoot);
                } else if (pressedKeys.contains(KeyCode.RIGHT)) {
                    player.move(PlayerTank.Direction.RIGHT, walls, breakableWalls, gameRoot);
                }
            }
        }.start();
    }



    /**
     * Pauses or resumes the game state and enemy actions.
     */
    private void togglePause() {
        isPaused = !isPaused;
        pauseLabel.setVisible(isPaused);
        for (EnemyTank enemy : enemies) {
            if (isPaused) enemy.pause();
            else enemy.resume();
        }
    }

    /**
     * Triggers the game over screen and disables gameplay.
     */
    public static void triggerGameOver(Pane pane) {
        Main game = getInstance();

        if (game != null && game.gameOverLabel != null) {
            pane.getChildren().remove(game.gameOverLabel);
        }

        int finalScore = stats != null ? stats.getScore() : 0;

        Label label = new Label("         GAME OVER         \n          Score:" + finalScore +"         \n   R:Restart | ESC:Exit");
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 25));
        label.setTextFill(Color.LIGHTGREEN);
        label.setLayoutX(220);
        label.setLayoutY(450);

        pane.getChildren().add(label);

        if (game != null) {
            game.isGameOver = true;
            game.gameOverLabel = label;
            game.isPaused = true;
            game.pauseAllEnemies();
        }
    }

    /**
     * Stops all enemy activity by pausing them.
     */
    private void pauseAllEnemies() {
        for (EnemyTank enemy : enemies) {
            enemy.pause();
        }
    }

    /**
     * Creates overlay UI elements (like pause label).
     */
    private void setupOverlays() {
        pauseLabel = new Label("         PAUSED!         \n R:Restart | ESC:Exit");
        pauseLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 25));
        pauseLabel.setTextFill(Color.GREY);
        pauseLabel.setLayoutX(220);
        pauseLabel.setLayoutY(450);
        pauseLabel.setVisible(false);
        gameRoot.getChildren().add(pauseLabel);
    }

    /**
     * Handles the logic when the player is hit by a bullet.
     * Either respawns the player or ends the game based on remaining lives.
     */
    public static void onPlayerDeath(Pane pane) {
        stats.decreaseLife();
        if (stats.getLives() <= 0) {
            triggerGameOver(pane);
            if (player != null) {
                pane.getChildren().remove(player.getImageView());
                player = null;
            }
        } else {
            Main game = getInstance();
            if (game != null) {
                if (player != null) {
                    pane.getChildren().remove(player.getImageView());
                }
                game.respawnPlayer();
            }
        }
    }

    /**
     * Launches the game.
     */
    public static void main(String[] args) {
        launch(args);
    }

    // Singleton instance
    private static Main instance;

    /**
     * Sets the singleton instance on creation.
     */
    public Main() {
        instance = this;
    }

    /**
     * Returns the current singleton instance of the game.
     */
    public static Main getInstance() {
        return instance;
    }
}
