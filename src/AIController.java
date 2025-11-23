public class AIController {
    public PlayerController playerController;
    public Ball ball;

    private double targetY = -1;
    private double targetX = -1;
    private double randomError = 0; // Error offset to make AI imperfect

    // Bounds
    private double minX, maxX;

    // Constants to tweak AI difficulty
    private static final double AI_SPEED_FACTOR = 0.75; // AI moves at 75% of max speed
    private static final double MAX_ERROR = 40.0; // Max pixels the AI can be wrong by

    public AIController(PlayerController plrController, Ball ball) {
        this.playerController = plrController;
        this.ball = ball;
    }

    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
        this.targetX = (minX + maxX) / 2.0;
    }

    public void update(double dt) {
        playerController.update(dt);

        if (targetX < minX || targetX > maxX) {
            targetX = (minX + maxX) / 2.0;
        }

        if (ball.vx > 0) {
            // Ball moving towards AI
            calculateTarget();
        } else {
            // Ball moving away: Reset Error and return to center
            randomError = 0;
            targetY = Constants.SCREEN_HEIGHT / 2.0;
            targetX = (minX + maxX) / 2.0;
        }

        moveTowardsTarget(dt);
    }

    private void calculateTarget() {
        // 1. Calculate the perfect prediction
        double timeToReach = Math.abs((playerController.rect.x - ball.rect.x) / ball.vx);
        double predictedY = ball.rect.y + (ball.vy * timeToReach);

        // Handle wall bounces
        while (predictedY < 0 || predictedY > Constants.SCREEN_HEIGHT - Constants.BALL_WIDTH) {
            if (predictedY < 0) predictedY = -predictedY;
            else predictedY = 2 * (Constants.SCREEN_HEIGHT - Constants.BALL_WIDTH) - predictedY;
        }

        // 2. Add Error
        // We only generate a new error if we haven't yet (randomError is 0)
        // This prevents the paddle from shaking violently as it recalculates every frame
        if (randomError == 0) {
            randomError = (Math.random() * MAX_ERROR * 2) - MAX_ERROR;
        }

        targetY = predictedY + randomError;

        // Occasional random X movement
        if (Math.random() < 0.02) {
            targetX = minX + Math.random() * (maxX - minX);
        }
    }

    private void moveTowardsTarget(double dt) {
        // Apply Speed Factor to nerf AI speed
        double aiDt = dt * AI_SPEED_FACTOR;

        // Move Y
        double paddleCenterY = playerController.rect.y + (playerController.rect.height / 2.0);
        if (Math.abs(paddleCenterY - targetY) > 10) {
            if (paddleCenterY < targetY) playerController.moveDown(aiDt);
            else playerController.moveUp(aiDt);
        }

        // Move X
        double paddleCenterX = playerController.rect.x + (playerController.rect.width / 2.0);
        if (Math.abs(paddleCenterX - targetX) > 10) {
            if (paddleCenterX < targetX) playerController.moveRight(aiDt);
            else playerController.moveLeft(aiDt);
        }
    }
}