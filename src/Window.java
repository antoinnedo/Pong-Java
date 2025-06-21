import javax.swing.JFrame;

public class Window extends JFrame {

    public Window() {
        this.setTitle(Constants.SCREEN_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // Create and add the panel that contains the game
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        
        // Size the window to fit the panel
        this.pack();
        
        // Set location and visibility
        this.setLocationRelativeTo(null); // Center the window
        this.setVisible(true);

        // Get the height of the toolbar after the frame is visible
        Constants.TOOLBAR_HEIGHT = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;
        
        // Start the game
        gamePanel.startGame();
    }
}