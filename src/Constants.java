import java.awt.Color;

public class Constants {
    // New Full HD Resolution
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;
    public static final String SCREEN_TITLE = "Pong";

    // Scaled up dimensions for the larger screen
    public static final double PADDLE_HEIGHT = 150; // Increased from 100
    public static final double PADDLE_WIDTH = 20;   // Increased from 10
    public static final Color PADDLE_COLOR = Color.WHITE;
    public static final double BALL_WIDTH = 20;     // Increased from 10

    public static final double HORIZONTAL_PADDING = 40;

    // Increased speed to match the new distance
    public static final double PADDLE_SPEED = 600;

    public static double TOOLBAR_HEIGHT;
    public static double INSETS_BOTTOM;

    public static double MAX_ANGLE = Math.toRadians(45);
    public static double SPEED_INCREASE = 1.03;
}