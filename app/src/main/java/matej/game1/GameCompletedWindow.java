package matej.game1;

/**
 * Created by matej on 23.1.2015.
 */
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GameCompletedWindow extends Sprite
{

    public GameCompletedWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().game_complete_window_region, pSpriteVertexBufferObject);
    }

    public void display(Scene scene, Camera camera)
    {
        // Hide HUD
        camera.getHUD().setVisible(false);

        // Attach our game complete panel in the middle of camera
        setPosition(camera.getCenterX(), camera.getCenterY());
        this.detachSelf();
        scene.attachChild(this);
    }
}
