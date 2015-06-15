package matej.game1;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by matej on 27.12.2014.
 */
public class BlueBall extends AnimatedSprite {


    private int power;   // power of fling
    private int directionX;   // direction of fling
    private int directionY;   // direction of fling
    private Ball ball;
    private ResourcesManager resourcesManager;
    private Camera camera;
    private Scene scene;

    public BlueBall(float pX, float pY, ITiledTextureRegion texture, VertexBufferObjectManager vbo, int power, int directionX, int directionY, Ball ball, Camera camera, Scene scene) {
        super(pX, pY, texture, vbo);
        this.power = power;
        this.directionX = directionX;    // direction of fling
        this.directionY = directionY;   // direction of fling
        this.ball = ball;
        this.resourcesManager = ResourcesManager.getInstance();
        final long[] BALL_ANIMATE = new long[] {100, 100, 100, 100, 100, 100, 100, 100, 100};
        animate(BALL_ANIMATE, 0, 8, true);
        this.camera = camera;
        this.scene = scene;
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    public void checkCollision() {

        if(this.isVisible()) {
            if(ball.collidesWith(this)){
                this.setVisible(false);
                this.setIgnoreUpdate(true);
                GameScene.counter = GameScene.counter - 1;
                Explosion explosion = new Explosion(this.getX(),this.getY(), resourcesManager.mParticleTexture, this.getVertexBufferObjectManager(), camera, scene, 1, 0);
                GameScene.explosionArrayList.add(explosion);
                if(resourcesManager.sound)
                    resourcesManager.impact.play();
            }
        }
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed)
    {
        super.onManagedUpdate(pSecondsElapsed);
        this.setX(this.getX() + (directionX * power));
        this.setY(this.getY() + (directionY * power));
        checkCollision();


        //check collision with game borders
        if (this.getX() > 720) {
            this.setDirectionX(-this.getDirectionX());
        }
        if (this.getX() < 0) {
            this.setDirectionX(-this.getDirectionX());
        }
        if (this.getY() > 1200) {
            this.setDirectionY(-this.getDirectionY());
        }
        if (this.getY() < 0) {
            this.setDirectionY(-this.getDirectionY());
        }
    }


}