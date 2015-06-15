package matej.game1;

/**
 * Created by matej on 16.12.2014.
 */

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Ball extends Sprite {


    private int power;   // power of fling
    private int directionX;   // direction of fling
    private int directionY;   // direction of fling
    private int counter;
    public boolean ready;
    public boolean stopped;
    private boolean touched; // if droid is touched/picked up
    public Body body;

    public Ball(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld) {
        super(pX, pY, texture, vbo);
        this.power = 1;
        this.directionX = 0;    // direction of fling
        this.directionY = 0;   // direction of fling
        this.counter = 0;
        this.ready = true;
        this.stopped = false;
        createPhysics(physicsWorld);
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void handleActionDown(float eventX, float eventY) {

        if (eventX >= (this.getX() - this.getTextureRegion().getWidth()*2) && (eventX <= (this.getX() + this.getTextureRegion().getWidth()*2))) {
            if (eventY >= (this.getY() - this.getTextureRegion().getHeight()*2) && (eventY <= (this.getY() + this.getTextureRegion().getHeight()*2))) {
                // droid touched
                setTouched(true);
            }
            else {
                setTouched(false);
            }
        }
        else {
            setTouched(false);
        }
    }

    private void createPhysics(PhysicsWorld physicsWorld)
    {
        body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

        body.setUserData("ball");
        body.setFixedRotation(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                if (touched && ready) {
                    body.setLinearVelocity(new Vector2(directionX * power, directionY * power));
                    body.setLinearDamping(body.getLinearDamping() + 0.5f);
                    touched=false;
                    ready=false;
                }

                if(!ready){
                    if(Math.round(body.getLinearVelocity().x) == 0 && Math.round(body.getLinearVelocity().y) == 0) {
                        ready=true;
                        stopped=true;
                    }
                }
            }
        });
    }



}