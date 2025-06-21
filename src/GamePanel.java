import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private enum GameState {
        MENU, PLAYING, PAUSED
    }
    private GameState gameState = GameState.MENU;

    // --- Button Definitions ---
    // Main Menu
    private Rectangle2D.Float startButton = new Rectangle2D.Float(300, 250, 230, 50);
    private Rectangle2D.Float loadButton = new Rectangle2D.Float(300, 320, 230, 50);

    // Pause Menu
    private Rectangle2D.Float resumeButton = new Rectangle2D.Float(300, 250, 230, 50);
    private Rectangle2D.Float saveButton = new Rectangle2D.Float(300, 320, 230, 50);
    private Rectangle2D.Float exitButton = new Rectangle2D.Float(300, 390, 230, 50);

    private Thread gameThread;
    public Rect playerOne, ai;
    public PlayerController playerController;
    public AIController aiController;
    public Ball ball;

    private int playerOneScore = 0;
    private int aiScore = 0;
    private Font mainFont = new Font("Arial", Font.BOLD, 32);

    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);

        // Object Initialization
        playerOne = new Rect(Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR); //
        playerController = new PlayerController(playerOne); //
        Rect ballRect = new Rect(Constants.SCREEN_WIDTH / 2.0, Constants.SCREEN_HEIGHT / 2.0, Constants.BALL_WIDTH, Constants.BALL_WIDTH, Color.WHITE); //
        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR); //
        aiController = new AIController(new PlayerController(ai), ballRect); //
        ball = new Ball(ballRect, playerOne, ai); //

        // Mouse Click Handler
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState == GameState.MENU) {
                    handleMenuClicks(e);
                } else if (gameState == GameState.PAUSED) {
                    handlePauseMenuClicks(e);
                }
            }
        });

        // Mouse Motion Handler
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (gameState == GameState.PLAYING) {
                    playerOne.y = e.getY() - (playerOne.height / 2.0);
                    if (playerOne.y < Constants.TOOLBAR_HEIGHT) playerOne.y = Constants.TOOLBAR_HEIGHT;
                    if (playerOne.y + playerOne.height > Constants.SCREEN_HEIGHT) playerOne.y = Constants.SCREEN_HEIGHT - playerOne.height;
                }
            }
        });
    }

    // --- Mouse Click Logic ---
    private void handleMenuClicks(MouseEvent e) {
        if (startButton.contains(e.getPoint())) {
            gameState = GameState.PLAYING;
            resetGame();
        } else if (loadButton.contains(e.getPoint())) {
            loadGame();
        }
    }

    private void handlePauseMenuClicks(MouseEvent e) {
        if (resumeButton.contains(e.getPoint())) {
            gameState = GameState.PLAYING;
        } else if (saveButton.contains(e.getPoint())) {
            saveGame();
        } else if (exitButton.contains(e.getPoint())) {
            gameState = GameState.MENU;
        }
    }

    // --- Drawing Methods ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == GameState.MENU) {
            drawMenu(g2);
        } else if (gameState == GameState.PLAYING) {
            drawGame(g2);
        } else if (gameState == GameState.PAUSED) {
            drawGame(g2); // Game is visible in the background
            drawPauseMenu(g2);
        }
    }

    public void drawMenu(Graphics2D g2) {
        g2.setFont(mainFont.deriveFont(50f));
        g2.setColor(Color.WHITE);
        String title = "PONG";
        g2.drawString(title, (getWidth() - g2.getFontMetrics().stringWidth(title)) / 2, 150);
        g2.setFont(mainFont);

        // Draw Start Button
        g2.setColor(Color.GREEN);
        g2.fill(startButton);
        g2.setColor(Color.BLACK);
        g2.drawString("Start Game", (int)(startButton.x + 25), (int)(startButton.y + 35));
        
        // Draw Load Button
        g2.setColor(Color.CYAN);
        g2.fill(loadButton);
        g2.setColor(Color.BLACK);
        g2.drawString("Load Game", (int)(loadButton.x + 30), (int)(loadButton.y + 35));

        // Display loaded score
        String scoreText = "Loaded Score: " + playerOneScore + " : " + aiScore;
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, (getWidth() - g2.getFontMetrics().stringWidth(scoreText)) / 2, 500);
    }

    public void drawGame(Graphics2D g2) {
        g2.setFont(mainFont);
        g2.setColor(Color.WHITE);
        String scoreText = playerOneScore + " : " + aiScore;
        int stringWidth = g2.getFontMetrics().stringWidth(scoreText);
        g2.drawString(scoreText, (Constants.SCREEN_WIDTH / 2) - (stringWidth / 2), 50);
        playerOne.draw(g2);
        ai.draw(g2);
        ball.rect.draw(g2);
    }

    public void drawPauseMenu(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(mainFont.deriveFont(50f));
        g2.setColor(Color.WHITE);
        String title = "PAUSED";
        g2.drawString(title, (getWidth() - g2.getFontMetrics().stringWidth(title)) / 2, 150);
        g2.setFont(mainFont);

        g2.setColor(Color.GREEN);
        g2.fill(resumeButton);
        g2.setColor(Color.BLACK);
        g2.drawString("Resume", (int)(resumeButton.x + 50), (int)(resumeButton.y + 35));

        g2.setColor(Color.ORANGE);
        g2.fill(saveButton);
        g2.setColor(Color.BLACK);
        g2.drawString("Save Game", (int)(saveButton.x + 32), (int)(saveButton.y + 35));

        g2.setColor(Color.RED);
        g2.fill(exitButton);
        g2.setColor(Color.BLACK);
        g2.drawString("Exit to Menu", (int)(exitButton.x + 20), (int)(exitButton.y + 35));
    }

    // --- Game Logic Methods ---
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(double dt) {
        playerController.update(dt);
        aiController.update(dt);
        ball.update(dt);
        if (ball.rect.x + ball.rect.width < 0) {
            aiScore++;
            ball.reset();
        } else if (ball.rect.x > Constants.SCREEN_WIDTH) {
            playerOneScore++;
            ball.reset();
        }
    }

    private void resetGame() {
        playerOneScore = 0;
        aiScore = 0;
        ball.reset();
    }

    private void saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("pong_save.txt"))) {
            writer.println(playerOneScore + ":" + aiScore);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }
    
    private void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader("pong_save.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] scores = line.split(":");
                if (scores.length == 2) {
                    playerOneScore = Integer.parseInt(scores[0]);
                    aiScore = Integer.parseInt(scores[1]);
                    System.out.println("Game loaded successfully!");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No saved game found.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        double lastFrameTime = System.nanoTime();
        while (true) {
            double time = System.nanoTime();
            double deltaTime = (time - lastFrameTime) / 1_000_000_000.0;
            lastFrameTime = time;

            if (gameState == GameState.PLAYING) {
                update(deltaTime);
            }
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // --- KeyListener Methods ---
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}