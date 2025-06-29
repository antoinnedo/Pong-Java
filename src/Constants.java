import java.awt.Color;

public class Constants {
    public static final int SCREEN_HEIGHT = 600;
    public static final int SCREEN_WIDTH = 800;
    public static final String SCREEN_TITLE = "Pong";

    public static final double PADDLE_HEIGHT = 100;
    public static final double PADDLE_WIDTH = 10;
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final double BALL_WIDTH = 10;
    public static final double HORIZONTAL_PADDING = 40;
    public static final double PADDLE_SPEED = 300;

    public static double TOOLBAR_HEIGHT;
    public static double INSETS_BOTTOM;


    public static double MAX_ANGLE = Math.toRadians(45); // Max bounce angle in radians (45 degrees)
    public static double SPEED_INCREASE = 1.03; // Increase ball speed by 3% on each hit
}