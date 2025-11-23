import java.awt.geom.Rectangle2D;

public class AIController {
    public PlayerController playerController;
    public Ball ball; // We now reference the Ball object, not just the Rect

    private double targetY = -1;
    private double reactionTimer = 0;

    // Tweak these to change difficulty
    private static final double REACTION_DELAY = 0.2; // AI waits 0.2s before calculating path
    private static final double RANDOM_ERROR = 20.0;  // +/- 20 pixels error margin

    public AIController(PlayerController plrController, Ball ball) {
        this.playerController = plrController;
        this.ball = ball;
    }

    public void update(double dt) {
        playerController.update(dt);

        // Only move if the ball is coming towards the AI (vx > 0)
        if (ball.vx > 0) {

            // If we haven't picked a target yet (or ball changed direction), calculate one
            if (targetY == -1) {
                // Add a small delay to simulate human reaction time
                reactionTimer += dt;
                if (reactionTimer > REACTION_DELAY) {
                    calculateTarget();
                }
            }

            // Move towards the target
            moveTowardsTarget(dt);

        } else {
            // Ball is moving away: Reset target and center paddle (idle behavior)
            targetY = -1;
            reactionTimer = 0;
            centerPaddle(dt);
        }
    }

    private void calculateTarget() {
        // 1. Calculate time until ball hits the paddle x-plane
        double distanceX = playerController.rect.x - (ball.rect.x + ball.rect.width);
        double timeToReach = Math.abs(distanceX / ball.vx);

        // 2. Predict Y position based on current velocity
        double predictedY = ball.rect.y + (ball.vy * timeToReach);

        // 3. Account for bounces off top/bottom walls
        // We simulate the bounces mathematically
        while (predictedY < 0 || predictedY > Constants.SCREEN_HEIGHT - Constants.BALL_WIDTH) {
            if (predictedY < 0) {
                predictedY = -predictedY;
            } else if (predictedY > Constants.SCREEN_HEIGHT - Constants.BALL_WIDTH) {
                predictedY = 2 * (Constants.SCREEN_HEIGHT - Constants.BALL_WIDTH) - predictedY;
            }
        }

        // 4. Add randomness (human error)
        double error = (Math.random() * RANDOM_ERROR * 2) - RANDOM_ERROR;
        targetY = predictedY + error;
    }

    private void moveTowardsTarget(double dt) {
        // Determine the center of our paddle
        double paddleCenter = playerController.rect.y + (playerController.rect.height / 2.0);

        // Tolerance ensures the paddle doesn't "jitter" when it reaches the spot
        if (Math.abs(paddleCenter - targetY) > 10) {
            if (paddleCenter < targetY) {
                playerController.moveDown(dt);
            } else {
                playerController.moveUp(dt);
            }
        }
    }

    private void centerPaddle(double dt) {
        double screenCenter = Constants.SCREEN_HEIGHT / 2.0;
        double paddleCenter = playerController.rect.y + (playerController.rect.height / 2.0);

        if (Math.abs(paddleCenter - screenCenter) > 10) {
            if (paddleCenter < screenCenter) {
                playerController.moveDown(dt);
            } else {
                playerController.moveUp(dt);
            }
        }
    }
}