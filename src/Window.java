import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;

public class Window extends JFrame implements Runnable {

    public Graphics2D g2;
    public KL keyListener = new KL();
    public Rect playerOne, ai, ballRect;
    public PlayerController playerController;
    public AIController aiController;
    public Ball ball;
    
    public Window() {
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.addKeyListener(keyListener);
        Constants.TOOLBAR_HEIGHT = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;

        g2 = (Graphics2D)this.getGraphics();

        playerOne = new Rect(Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        playerController = new PlayerController(playerOne, keyListener);
        
        ballRect = new Rect (Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, Constants.BALL_WIDTH, Constants.BALL_WIDTH, Color.WHITE);
        ball = new Ball(ballRect, playerOne, ai);
        
        ai = new Rect (Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH - Constants.HORIZONTAL_PADDING, 40, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Constants.PADDLE_COLOR);
        aiController = new AIController(new PlayerController(ai), ballRect);
    }

    public void update(double dt) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        this.draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);

        playerController.update(dt);
        ball.update(dt);
        aiController.update(dt);

        playerOne.draw(g2);
        ballRect.draw(g2);
        ai.draw(g2);

    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        playerOne.draw(g2);
        ai.draw(g2);
        ballRect.draw(g2);
    }

    public void run() {
        double lastFrameTime = 0.0;
        while(true) {
            double time = Time.getTime();
            double deltaTime =  time - lastFrameTime;
            lastFrameTime = time;

            update(deltaTime);

        
        }
    }

}
