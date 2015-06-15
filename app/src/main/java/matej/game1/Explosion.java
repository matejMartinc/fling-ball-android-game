package matej.game1;

import android.opengl.GLES20;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


/**
 * Created by matej on 26.1.2015.
 */
public class Explosion extends Sprite{

    private float timer = 0;

    public Explosion(final float pX, final float pY, TextureRegion region,
                     final VertexBufferObjectManager pVertexBufferObjectManager, Camera camera, Scene scene, float red, float blue)
    {

        super(pX, pY, region, pVertexBufferObjectManager);
        final CircleOutlineParticleEmitter particleEmitter = new CircleOutlineParticleEmitter(0, 0, 10);
        final SpriteParticleSystem particleSystem = new SpriteParticleSystem(particleEmitter, 10, 20, 25, region, this.getVertexBufferObjectManager());

        particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1, 0, 0));
        particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
        particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-2, 2, -10, -10));
        particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));

        particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 0.5f, 1.0f, 4.0f));
        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0, 0.2f, red, red, 0, 0.5f, blue, blue));
        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.3f, 0.5f, 1, 1, 0.5f, 1, 0, 1));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 0.2f, 0, 1));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.45f, 0.5f, 1, 0));
        particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<Sprite>(camera));

        this.attachChild(particleSystem);
        scene.attachChild(this);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        timer += pSecondsElapsed;

        if(timer > 0.5){
            this.setIgnoreUpdate(true);
            this.setVisible(false);

        }
        else{
            super.onManagedUpdate(pSecondsElapsed);
        }
    }
}

