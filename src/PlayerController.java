public class PlayerController {
    public Rect rect;
    
    // This constructor is now for both the Player and the AI,
    // as the player is no longer controlled by the keyboard.
    public PlayerController(Rect rect) {
        this.rect = rect;
    }

    // The update method is now only needed for the AI's logic,
    // but we'll leave it for potential future use.
    // Player movement is now handled directly by the mouse in GamePanel.
    public void update(double dt) {
        // This space can be used for other power-ups or effects in the future.
    }

    public void moveUp(double dt) {
        if (this.rect.y - Constants.PADDLE_SPEED * dt > Constants.TOOLBAR_HEIGHT)
            this.rect.y -= Constants.PADDLE_SPEED * dt;
    }
    
    public void moveDown(double dt) {
        if (this.rect.y + Constants.PADDLE_SPEED * dt + rect.height < Constants.SCREEN_HEIGHT)
            this.rect.y += Constants.PADDLE_SPEED * dt;
    }
}