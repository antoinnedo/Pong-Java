import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Window extends JFrame {

    public Window() {
        this.setTitle(Constants.SCREEN_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // --- Force Full Screen ---
        this.setUndecorated(true); // Remove title bar and borders

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this); // Exclusive Full Screen
        } else {
            // Fallback for systems that don't support exclusive mode
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        // Create and add the panel that contains the game
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);

        // If not using exclusive full screen, we pack()
        if (gd.getFullScreenWindow() != this) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        // Note: In exclusive mode, visibility is handled by setFullScreenWindow

        // Get the height of the toolbar after the frame is visible
        Constants.TOOLBAR_HEIGHT = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;

        // Start the game
        gamePanel.startGame();
    }
}