import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Handles the display and tracking of player's score and lives.
 */
public class GameStats {

    private int score;
    private int lives;

    private Label scoreLabel;
    private Label livesLabel;

    /**
     * Initializes labels and adds them to the game pane.
     */
    public GameStats(Pane pane) {
        scoreLabel = new Label();
        scoreLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        scoreLabel.setTextFill(Color.TURQUOISE);
        scoreLabel.setLayoutX(60);
        scoreLabel.setLayoutY(50);

        livesLabel = new Label();
        livesLabel.setFont(Font.font("Consolas", FontWeight.BOLD,  22));
        livesLabel.setTextFill(Color.ORANGE);
        livesLabel.setLayoutX(60);
        livesLabel.setLayoutY(75);

        pane.getChildren().addAll(scoreLabel, livesLabel);
        reset();
    }

    /**
     * Resets the score and lives to default values.
     */
    public void reset() {
        score = 0;
        lives = 3;
        updateLabels();
    }

    /**
     * Increments the player's score by 1.
     */
    public void increaseScore() {
        score++;
        updateLabels();
    }

    /**
     * Decreases the player's lives by 1 if above 0.
     */
    public void decreaseLife() {
        if (lives > 0) { // Prevent negative lives
            lives--;
        }
        updateLabels();
    }

    /**
     * Returns current number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the score and lives text labels.
     */
    private void updateLabels() {
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
    }
}
