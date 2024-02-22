import java.awt.event.KeyEvent;

public class PlayerController {
    public Rect rect;
    public KL keyListener;
    
    //Player 1 controller
    public PlayerController(Rect rect, KL keyListener) {
        this.rect = rect;
        this.keyListener = keyListener;
    }

    //AI controller
    public PlayerController(Rect rect) {
        this.rect = rect;
        this.keyListener = null;
    }

    public void update(double dt) {
        if(keyListener != null) {
            if(keyListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                moveDown(dt);
            } else if(keyListener.isKeyPressed(KeyEvent.VK_UP)) {
                moveUp(dt);
            }
        } else {
            
        }
    }

    public void moveUp(double dt) {
        if (this.rect.y - Constants.PADDLE_SPEED * dt > Constants.TOOLBAR_HEIGHT)
            this.rect.y -= Constants.PADDLE_SPEED * dt;
    }
    
    public void moveDown(double dt) {
        if (this.rect.y + Constants.PADDLE_SPEED * dt + rect.height < Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM)
            this.rect.y += Constants.PADDLE_SPEED * dt;
    }
}
