package matej.game1;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

/**
 * Created by matej on 23.1.2015.
 */
public class Log extends Sprite {


    private int power;   // power of fling
    private int directionX;   // direction of fling
    private int directionY;   // direction of fling
    private Body body;
    PhysicsWorld physicsWorld;

    public Log(float pX, float pY, int power, ITextureRegion texture, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld) {
        super(pX, pY, texture, vbo);
        this.power = power;
        this.directionX = 0;    // direction of fling
        this.directionY = 0;   // direction of fling
        this.setRotation(power);
        this.physicsWorld = physicsWorld;
        createPhysics();


    }

    private void createPhysics() {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(2, 1, 1));

        body.setUserData("log");
        body.setTransform(body.getWorldCenter(), MathUtils.degToRad(360-power));

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);

            }
        });
    }

    public void removeBody() {
        physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this));
        physicsWorld.destroyBody(body);
    }
}



