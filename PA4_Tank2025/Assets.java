import javafx.scene.image.Image;

/**
 * Loads and stores all image assets used in the game.
 */
public class Assets {

    public static Image yellowTank1;
    public static Image yellowTank2;
    public static Image whiteTankUp;
    public static Image whiteTank2;
    public static Image bullet;
    public static Image wall;
    public static Image explosion;
    public static Image smallExplosion;

    /**
     * Call this once during game startup to load all images.
     */
    public static void load() {
        yellowTank1 = new Image(Assets.class.getResource("/yellowTank1.png").toString());
        yellowTank2 = new Image(Assets.class.getResource("/yellowTank2.png").toString());
        whiteTankUp = new Image(Assets.class.getResource("/whiteTank1.png").toString());
        whiteTank2 = new Image(Assets.class.getResource("/whiteTank2.png").toString()); // added
        bullet = new Image(Assets.class.getResource("/bullet.png").toString());
        wall = new Image(Assets.class.getResource("/wall.png").toString());
        explosion = new Image(Assets.class.getResource("/explosion.png").toString());
        smallExplosion = new Image(Assets.class.getResource("/smallExplosion.png").toString());
    }
}
