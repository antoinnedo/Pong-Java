import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import javax.swing.JOptionPane;

public class GameInput extends MouseAdapter implements KeyListener {

    private GamePanel panel;

    public GameInput(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GamePanel.GameState state = panel.getGameState();
        MenuOverlay menu = panel.getMenuOverlay();

        if (state == GamePanel.GameState.MENU) {
            if (menu.startButton.contains(e.getPoint())) {
                panel.setGameState(GamePanel.GameState.PLAYING);
                panel.resetGame();
            } else if (menu.loadButton.contains(e.getPoint())) {
                panel.setGameState(GamePanel.GameState.LOAD_MENU);
            }
        }
        else if (state == GamePanel.GameState.PAUSED) {
            if (menu.resumeButton.contains(e.getPoint())) {
                panel.setGameState(GamePanel.GameState.PLAYING);
            } else if (menu.resetButton.contains(e.getPoint())) {
                panel.resetGame();
                panel.setGameState(GamePanel.GameState.PLAYING);
            } else if (menu.saveButton.contains(e.getPoint())) {
                panel.setGameState(GamePanel.GameState.SAVE_MENU);
            } else if (menu.exitButton.contains(e.getPoint())) {
                panel.setGameState(GamePanel.GameState.MENU);
                panel.resetGame();
            }
        }
        else if (state == GamePanel.GameState.SAVE_MENU || state == GamePanel.GameState.LOAD_MENU) {
            handleFileMenuClicks(e, state == GamePanel.GameState.SAVE_MENU);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (panel.getGameState() == GamePanel.GameState.PLAYING) {
            Rect p1 = panel.playerOne;
            p1.y = e.getY() - (p1.height / 2.0);

            // X Movement mapped to the first 1/4 of the screen
            double maxPlayerX = Constants.SCREEN_WIDTH * 0.25;
            p1.x = e.getX() - (p1.width / 2.0);

            if (p1.x < 0) p1.x = 0;
            if (p1.x > maxPlayerX) p1.x = maxPlayerX;
        }
    }

    private void handleFileMenuClicks(MouseEvent e, boolean isSaveMode) {
        MenuOverlay menu = panel.getMenuOverlay();

        if (menu.backButton.contains(e.getPoint())) {
            panel.setGameState(isSaveMode ? GamePanel.GameState.PAUSED : GamePanel.GameState.MENU);
            return;
        }

        if (isSaveMode && menu.newSaveButton.contains(e.getPoint())) {
            String fileName = JOptionPane.showInputDialog(panel, "Enter save name:");
            if (fileName != null && !fileName.trim().isEmpty()) {
                panel.getSaveManager().saveScore(fileName, panel.getPlayerScore(), panel.getAiScore());
                panel.setGameState(GamePanel.GameState.PAUSED);
            }
            return;
        }

        // Handle File List Clicks
        File[] files = panel.getSaveManager().getSaveFiles();
        int startY = isSaveMode ? 170 : 100;

        for (int i = 0; i < files.length; i++) {
            // Main File Button
            Rectangle2D.Float fileBtn = new Rectangle2D.Float(250, startY + (i * 60), 280, 50);
            // Delete Button
            Rectangle2D.Float delBtn = new Rectangle2D.Float(540, startY + (i * 60), 50, 50);

            if (delBtn.contains(e.getPoint())) {
                int confirm = JOptionPane.showConfirmDialog(panel, "Delete " + files[i].getName() + "?", "Delete Save", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    panel.getSaveManager().deleteSave(files[i].getName());
                    panel.repaint(); // Refresh list
                }
                return;
            }

            if (fileBtn.contains(e.getPoint())) {
                if (isSaveMode) {
                    int resp = JOptionPane.showConfirmDialog(panel, "Overwrite " + files[i].getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        panel.getSaveManager().saveScore(files[i].getName(), panel.getPlayerScore(), panel.getAiScore());
                        panel.setGameState(GamePanel.GameState.PAUSED);
                    }
                } else {
                    int[] loaded = panel.getSaveManager().loadScore(files[i].getName());
                    if (loaded != null) {
                        panel.setScores(loaded[0], loaded[1]);
                        panel.setGameState(GamePanel.GameState.PAUSED);
                    }
                }
                return;
            }
        }
    }

    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GamePanel.GameState state = panel.getGameState();
            if (state == GamePanel.GameState.PLAYING) panel.setGameState(GamePanel.GameState.PAUSED);
            else if (state == GamePanel.GameState.PAUSED) panel.setGameState(GamePanel.GameState.PLAYING);
            else if (state == GamePanel.GameState.SAVE_MENU) panel.setGameState(GamePanel.GameState.PAUSED);
            else if (state == GamePanel.GameState.LOAD_MENU) panel.setGameState(GamePanel.GameState.MENU);
        }
    }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}