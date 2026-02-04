import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Represents a semi-transparent overlay for displaying messages (e.g., pause or game over).
 */
public class GameOverlay extends StackPane {

    private Label label;

    /**
     * Constructs the overlay with the given message.
     */
    public GameOverlay(String message) {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        label = new Label(message);
        label.setFont(Font.font("Consolas", 40));
        label.setTextFill(Color.RED);
        label.setTextAlignment(TextAlignment.CENTER);

        getChildren().add(label);
    }

    /**
     * Updates the message displayed on the overlay.
     */
    public void setMessage(String text) {
        label.setText(text);
    }
}
