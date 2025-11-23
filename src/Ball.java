import java.awt.Graphics2D;
import java.awt.Color;

public class Ball {
    public Rect rect;
    public Rect leftPaddle, rightPaddle;
    private PlayerController leftController, rightController;

    public double vy = -180.0;
    public double vx = -150.0;

    private static final double FRICTION_Y = 0.4;
    private static final double POWER_X = 0.3;

    public Ball(Rect rect, Rect leftPaddle, Rect rightPaddle, PlayerController leftPc, PlayerController rightPc) {
        this.rect = rect;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.leftController = leftPc;
        this.rightController = rightPc;
    }

    // --- NEW: Custom Draw Method for Circle ---
    public void draw(Graphics2D g2) {
        g2.setColor(rect.color);
        // Draw an oval using the rect's dimensions
        g2.fillOval((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
    }

    public void reset() {
        this.rect.x = Constants.SCREEN_WIDTH / 2.0;
        this.rect.y = Constants.SCREEN_HEIGHT / 2.0;
        this.vx = -150.0;
        this.vy = -180.0;
    }

    public void update(double dt) {
        // 1. Move First
        this.rect.x += vx * dt;
        this.rect.y += vy * dt;

        // 2. Collision Checks (Standard AABB)
        if (vx < 0) {
            if (rect.x <= leftPaddle.x + leftPaddle.width && rect.x + rect.width >= leftPaddle.x &&
                    rect.y + rect.height >= leftPaddle.y && rect.y <= leftPaddle.y + leftPaddle.height) {

                this.rect.x = leftPaddle.x + leftPaddle.width;
                calculateBounce(leftPaddle, leftController);
            }
        } else if (vx > 0) {
            if (rect.x + rect.width >= rightPaddle.x && rect.x <= rightPaddle.x + rightPaddle.width &&
                    rect.y + rect.height >= rightPaddle.y && rect.y <= rightPaddle.y + rightPaddle.height) {

                this.rect.x = rightPaddle.x - rect.width;
                calculateBounce(rightPaddle, rightController);
            }
        }

        // Wall Collision
        if (vy > 0 && rect.y + rect.height >= Constants.SCREEN_HEIGHT) {
            vy *= -1;
            rect.y = Constants.SCREEN_HEIGHT - rect.height;
        } else if (vy < 0 && rect.y <= Constants.TOOLBAR_HEIGHT) {
            vy *= -1;
            rect.y = Constants.TOOLBAR_HEIGHT;
        }
    }

    private void calculateBounce(Rect paddle, PlayerController controller) {
        double paddleCenter = paddle.y + (paddle.height / 2.0);
        double ballCenter = rect.y + (rect.height / 2.0);
        double relativeIntersectY = (paddleCenter - ballCenter) / (paddle.height / 2.0);
        double bounceAngle = relativeIntersectY * Constants.MAX_ANGLE;

        double currentSpeed = Math.sqrt(vx * vx + vy * vy) * Constants.SPEED_INCREASE;
        double direction = (vx > 0) ? -1.0 : 1.0;

        vx = currentSpeed * Math.cos(bounceAngle) * direction;
        vy = currentSpeed * -Math.sin(bounceAngle);

        vy += controller.vy * FRICTION_Y;
        if (direction < 0 && controller.vx < 0) vx += controller.vx * POWER_X;
        if (direction > 0 && controller.vx > 0) vx += controller.vx * POWER_X;

        double maxSpeed = 3000.0;
        if (Math.abs(vx) > maxSpeed) vx = Math.signum(vx) * maxSpeed;
    }
}