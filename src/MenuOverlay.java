import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class MenuOverlay {

    public Font mainFont = new Font("Arial", Font.BOLD, 32);

    // --- Buttons ---
    public Rectangle2D.Float startButton = new Rectangle2D.Float(300, 250, 230, 50);
    public Rectangle2D.Float loadButton = new Rectangle2D.Float(300, 320, 230, 50);
    public Rectangle2D.Float resumeButton = new Rectangle2D.Float(300, 220, 230, 50);
    public Rectangle2D.Float resetButton = new Rectangle2D.Float(300, 290, 230, 50);
    public Rectangle2D.Float saveButton = new Rectangle2D.Float(300, 360, 230, 50);
    public Rectangle2D.Float exitButton = new Rectangle2D.Float(300, 430, 230, 50);

    // File Menu
    public Rectangle2D.Float newSaveButton = new Rectangle2D.Float(300, 100, 230, 50);
    public Rectangle2D.Float backButton = new Rectangle2D.Float(300, 500, 230, 50);

    public void drawMainMenu(Graphics2D g2, int p1Score, int aiScore, int width) {
        g2.setFont(mainFont.deriveFont(50f));
        g2.setColor(Color.WHITE);
        String title = "PONG";
        g2.drawString(title, (width - g2.getFontMetrics().stringWidth(title)) / 2, 150);
        g2.setFont(mainFont);

        drawButton(g2, startButton, Color.GREEN, "Start Game", 25, 35);
        drawButton(g2, loadButton, Color.CYAN, "Load Game", 30, 35);

        String scoreText = "Current: " + p1Score + " : " + aiScore;
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, (width - g2.getFontMetrics().stringWidth(scoreText)) / 2, 500);
    }

    public void drawPauseMenu(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, width, height);

        drawButton(g2, resumeButton, Color.GREEN, "Resume", 50, 35);
        drawButton(g2, resetButton, Color.YELLOW, "Reset Game", 25, 35);
        drawButton(g2, saveButton, Color.ORANGE, "Save Game", 32, 35);
        drawButton(g2, exitButton, Color.RED, "Exit to Menu", 20, 35);
    }

    public void drawFileMenu(Graphics2D g2, boolean isSaveMode, SaveManager saveManager, int width, int height) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        g2.setFont(mainFont);
        g2.setColor(Color.WHITE);
        String title = isSaveMode ? "Save Game" : "Load Game";
        g2.drawString(title, (width - g2.getFontMetrics().stringWidth(title)) / 2, 50);

        int startY = 100;
        if (isSaveMode) {
            drawButton(g2, newSaveButton, Color.BLUE, "New Save...", 25, 35);
            startY = 170;
        }

        File[] files = saveManager.getSaveFiles();
        for (int i = 0; i < files.length; i++) {
            // File Button
            Rectangle2D.Float fileBtn = new Rectangle2D.Float(250, startY + (i * 60), 280, 50);
            g2.setColor(Color.DARK_GRAY);
            g2.fill(fileBtn);
            g2.setColor(Color.WHITE);
            String fname = files[i].getName();
            if (fname.length() > 15) fname = fname.substring(0, 12) + "...";
            g2.drawString(fname, (int) (fileBtn.x + 10), (int) (fileBtn.y + 35));

            // Delete Button (Red X)
            Rectangle2D.Float delBtn = new Rectangle2D.Float(540, startY + (i * 60), 50, 50);
            g2.setColor(Color.RED);
            g2.fill(delBtn);
            g2.setColor(Color.WHITE);
            g2.drawString("X", (int)(delBtn.x + 15), (int)(delBtn.y + 35));
        }

        drawButton(g2, backButton, Color.GRAY, "Back", 80, 35);
    }

    private void drawButton(Graphics2D g2, Rectangle2D.Float rect, Color color, String text, int xOff, int yOff) {
        g2.setColor(color);
        g2.fill(rect);
        g2.setColor(Color.BLACK);
        g2.drawString(text, (int) (rect.x + xOff), (int) (rect.y + yOff));
    }
}