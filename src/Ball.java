public class Ball {
    public Rect rect;
    public Rect leftPaddle, rightPaddle;

    public double vy = -180.0;
    public double vx = -150.0;
    
    public Ball(Rect rect, Rect leftPaddle, Rect rightPaddle) {
        this.rect = rect;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
    }

    /**
     * Resets the ball to the center of the screen with its default velocity.
     */
    public void reset() {
        this.rect.x = Constants.SCREEN_WIDTH / 2.0;
        this.rect.y = Constants.SCREEN_HEIGHT / 2.0;
        this.vx = -150.0;
        this.vy = -180.0;
    }

    /**
     * Updates the ball's position and handles all collision physics.
     * @param dt The time delta since the last frame.
     */
    public void update(double dt) {

        // --- Paddle Collision Physics ---

        // Check collision with the left paddle (player)
        if (vx < 0) { // Only check if moving left
            if (this.rect.x <= this.leftPaddle.x + this.leftPaddle.width && 
                this.rect.y + this.rect.height >= this.leftPaddle.y && 
                this.rect.y <= this.leftPaddle.y + this.leftPaddle.height) {
                
                calculateBounceAngle(leftPaddle);
            }
        } 
        // Check collision with the right paddle (AI)
        else if (vx > 0) { // Only check if moving right
            if (this.rect.x + this.rect.width >= this.rightPaddle.x &&
                this.rect.y + this.rect.height >= this.rightPaddle.y && 
                this.rect.y <= this.rightPaddle.y + this.rightPaddle.height) {

                calculateBounceAngle(rightPaddle);
            }
        }

        // --- Wall Collision Physics ---
        if (vy > 0) {
            if (this.rect.y + this.rect.height >= Constants.SCREEN_HEIGHT) {
                this.vy *= -1; // Invert vertical velocity
            }
        } else if (vy < 0) {
            if (this.rect.y <= Constants.TOOLBAR_HEIGHT) {
                this.vy *= -1; // Invert vertical velocity
            }
        }
        
        // Update ball's position based on its velocity
        this.rect.x += vx * dt;
        this.rect.y += vy * dt;
    }

    /**
     * Calculates the new velocity of the ball after hitting a paddle.
     * @param paddle The paddle that was hit.
     */
    private void calculateBounceAngle(Rect paddle) {
        // Calculate the center of the paddle and ball
        double paddleCenter = paddle.y + (paddle.height / 2.0);
        double ballCenter = this.rect.y + (this.rect.height / 2.0);

        // Get the relative intersection point (-1 for top, 0 for center, 1 for bottom)
        double relativeIntersectY = (paddleCenter - ballCenter) / (paddle.height / 2.0);

        // Convert the intersection point to a bounce angle
        double bounceAngle = relativeIntersectY * Constants.MAX_ANGLE;
        
        // Get the current speed and increase it
        double currentSpeed = Math.sqrt(vx * vx + vy * vy) * Constants.SPEED_INCREASE;
        
        // The new X velocity is based on the cosine of the angle
        // The direction is inverted based on which paddle was hit
        double direction = (this.vx > 0) ? -1.0 : 1.0;
        this.vx = currentSpeed * Math.cos(bounceAngle) * direction;
        
        // The new Y velocity is based on the sine of the angle
        this.vy = currentSpeed * -Math.sin(bounceAngle);
    }
}