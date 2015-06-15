package matej.game1;

import com.badlogic.gdx.math.Vector2;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by matej on 27.12.2014.
 */
public class RedBall extends Sprite {

    private Ball ball;
    private Camera camera;
    private Scene scene;
    private boolean drowned;

    public RedBall(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbo, int power, int directionX, int directionY, Ball ball, Camera camera, Scene scene) {
        super(pX, pY, texture, vbo);
        this.ball=ball;
        this.camera = camera;
        this.scene = scene;
        this.drowned = false;
    }

    public void checkCollision() {
        if(!drowned) {
            if (in_circle(this.getX(), this.getY(), 75, ball.getX(), ball.getY())) {
                ball.setVisible(false);
                ball.setIgnoreUpdate(true);
                Explosion explosion = new Explosion(ball.getX(), ball.getY(), ResourcesManager.getInstance().mParticleTexture, this.getVertexBufferObjectManager(), camera, scene, 0, 1);
                GameScene.explosionArrayList.add(explosion);
                ball.body.setLinearVelocity(new Vector2(1, 1));
                drowned = true;
                if(ResourcesManager.getInstance().sound)
                    ResourcesManager.getInstance().splash.play();
            }
        }
    }
    //check if ball fell in the pond - used in check collision
    private boolean in_circle(float center_x, float center_y, float radius, float x, float y) {
        double square_dist = Math.pow(center_x - x, 2) + Math.pow(center_y - y, 2);
        return square_dist <= Math.pow(radius, 2);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed)
    {
        super.onManagedUpdate(pSecondsElapsed);
        checkCollision();
    }




}
