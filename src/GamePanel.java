import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public enum GameState { MENU, PLAYING, PAUSED, SAVE_MENU, LOAD_MENU }
    private GameState gameState = GameState.MENU;

    private Thread gameThread;

    public Rect playerOne, ai;
    public PlayerController playerController;
    public AIController aiController;
    public Ball ball;

    private int playerOneScore = 0;
    private int aiScore = 0;

    private SaveManager saveManager;
    private MenuOverlay menuOverlay;
    private GameInput gameInput;

    public GamePanel() {
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setFocusable(true);

        saveManager = new SaveManager();
        menuOverlay = new MenuOverlay();
        gameInput = new GameInput(this);

        this.addMouseListener(gameInput);
        this.addMouseMotionListener(gameInput);
        this.addKeyListener(gameInput);

        // 1. Create Controllers & Paddles
        playerOne = new Rect(Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        playerController = new PlayerController(playerOne);
        playerController.setBounds(0, Constants.SCREEN_WIDTH * 0.25);

        ai = new Rect(Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        PlayerController aiPaddleController = new PlayerController(ai);
        aiPaddleController.setBounds(Constants.SCREEN_WIDTH * 0.75, Constants.SCREEN_WIDTH);

        // 2. Create Ball
        Rect ballRect = new Rect(Constants.SCREEN_WIDTH / 2.0, Constants.SCREEN_HEIGHT / 2.0, Constants.BALL_WIDTH, Constants.BALL_WIDTH, Color.WHITE);
        ball = new Ball(ballRect, playerOne, ai, playerController, aiPaddleController);

        // 3. Create AI Logic
        aiController = new AIController(aiPaddleController, ball);
        aiController.setBounds(Constants.SCREEN_WIDTH * 0.75, Constants.SCREEN_WIDTH - 40);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(double dt) {
        int subSteps = 4;
        double stepDt = dt / subSteps;

        for (int i = 0; i < subSteps; i++) {
            playerController.update(stepDt);
            aiController.update(stepDt);
            ball.update(stepDt);

            if (ball.rect.x + ball.rect.width < 0) {
                aiScore++;
                ball.reset();
                break;
            } else if (ball.rect.x > Constants.SCREEN_WIDTH) {
                playerOneScore++;
                ball.reset();
                break;
            }
        }
    }

    public void resetGame() {
        playerOneScore = 0;
        aiScore = 0;
        ball.reset();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == GameState.MENU) {
            menuOverlay.drawMainMenu(g2, playerOneScore, aiScore, getWidth());
        } else if (gameState == GameState.PLAYING) {
            drawGame(g2);
        } else if (gameState == GameState.PAUSED) {
            drawGame(g2);
            menuOverlay.drawPauseMenu(g2, getWidth(), getHeight());
        } else if (gameState == GameState.SAVE_MENU) {
            menuOverlay.drawFileMenu(g2, true, saveManager, getWidth(), getHeight());
        } else if (gameState == GameState.LOAD_MENU) {
            menuOverlay.drawFileMenu(g2, false, saveManager, getWidth(), getHeight());
        }
    }

    private void drawGame(Graphics2D g2) {
        g2.setColor(new Color(128, 0, 128, 50));
        g2.fillRect(0, 0, (int)(Constants.SCREEN_WIDTH * 0.25), Constants.SCREEN_HEIGHT);
        g2.fillRect((int)(Constants.SCREEN_WIDTH * 0.75), 0, (int)(Constants.SCREEN_WIDTH * 0.25), Constants.SCREEN_HEIGHT);

        g2.setFont(menuOverlay.mainFont);
        g2.setColor(Color.WHITE);
        String scoreText = playerOneScore + " : " + aiScore;
        g2.drawString(scoreText, (Constants.SCREEN_WIDTH / 2) - (g2.getFontMetrics().stringWidth(scoreText) / 2), 50);

        playerOne.draw(g2);
        ai.draw(g2);
        ball.draw(g2); // <-- Changed to use circle draw method
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
            try { Thread.sleep(8); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState state) { this.gameState = state; }
    public MenuOverlay getMenuOverlay() { return menuOverlay; }
    public SaveManager getSaveManager() { return saveManager; }
    public int getPlayerScore() { return playerOneScore; }
    public int getAiScore() { return aiScore; }
    public void setScores(int p1, int ai) { this.playerOneScore = p1; this.aiScore = ai; }
}