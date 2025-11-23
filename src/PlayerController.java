public class PlayerController {
    public Rect rect;

    // Boundaries for 2D movement
    private double minX, maxX;

    // Velocity tracking (for transferring momentum to the ball)
    public double vx, vy;
    private double lastX, lastY;

    public PlayerController(Rect rect) {
        this.rect = rect;
        this.lastX = rect.x;
        this.lastY = rect.y;
    }

    // Set the allowed movement zone (called from GamePanel)
    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }

    public void update(double dt) {
        // 1. Calculate Velocity based on how much we moved since last frame
        if (dt > 0) {
            this.vx = (rect.x - lastX) / dt;
            this.vy = (rect.y - lastY) / dt;
        }

        // 2. Enforce Boundaries
        if (rect.x < minX) rect.x = minX;
        if (rect.x > maxX) rect.x = maxX;
        if (rect.y < Constants.TOOLBAR_HEIGHT) rect.y = Constants.TOOLBAR_HEIGHT;
        if (rect.y + rect.height > Constants.SCREEN_HEIGHT) rect.y = Constants.SCREEN_HEIGHT - rect.height;

        // 3. Update last positions for next frame
        this.lastX = rect.x;
        this.lastY = rect.y;
    }

    // AI Helper methods
    public void moveUp(double dt) {
        if (rect.y - Constants.PADDLE_SPEED * dt > Constants.TOOLBAR_HEIGHT)
            rect.y -= Constants.PADDLE_SPEED * dt;
    }

    public void moveDown(double dt) {
        if (rect.y + Constants.PADDLE_SPEED * dt + rect.height < Constants.SCREEN_HEIGHT)
            rect.y += Constants.PADDLE_SPEED * dt;
    }

    public void moveLeft(double dt) {
        if (rect.x - Constants.PADDLE_SPEED * dt > minX)
            rect.x -= Constants.PADDLE_SPEED * dt;
    }

    public void moveRight(double dt) {
        if (rect.x + Constants.PADDLE_SPEED * dt < maxX)
            rect.x += Constants.PADDLE_SPEED * dt;
    }
}