package matej.game1;

/**
 * Created by matej on 22.12.2014.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.resolutionpolicy.CroppedResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.andengine.util.modifier.IModifier;
import org.xml.sax.Attributes;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends BaseScene
{
    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld physicsWorld;

    //level loading attributes
    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
    private static final String TAG_ENTITY_ATTRIBUTE_DIRECTIONX   = "directionX";
    private static final String TAG_ENTITY_ATTRIBUTE_DIRECTIONY   = "directionY";
    private static final String TAG_ENTITY_ATTRIBUTE_POWER   = "power";
    private static final String BLUEBALL_COUNTER = "counter";
    private static boolean end;
    public static int counter;
    private static int levelNumber;
    private static int lastLevel;
    private int numberOfTries;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences settings;
    SharedPreferences.Editor settingsEditor;

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BLUEBALL = "blueball";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GREENBALL = "greenball";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_REDBALL = "redball";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LOG = "log";

    private Ball ball;
    private BlueBall blueball;
    private RedBall redball;
    private Log log;
    private LevelCompleteWindow levelCompleteWindow;
    private GameCompletedWindow gameCompletedWindow;


    private ArrayList<RedBall> redBallArrayList;
    private ArrayList<BlueBall> blueBallArrayList;
    private ArrayList<Log> logArrayList;
    public static ArrayList<Explosion> explosionArrayList;
    private CroppedResolutionPolicy resPol;

    @Override
    public void createScene() {
        this.activity.onWindowFocusChanged(true);
        resPol = (CroppedResolutionPolicy) ResourcesManager.getInstance().engine.getEngineOptions().getResolutionPolicy();
        createBackground();
        createPhysics();
        createHUD();

        redBallArrayList = new ArrayList();
        blueBallArrayList = new ArrayList();
        logArrayList = new ArrayList();
        explosionArrayList = new ArrayList();

        settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        levelNumber = settings.getInt("level",1);
        lastLevel = 16;
        loadLevel(levelNumber);
        numberOfTries = 0;
        end = false;

        this.sortChildren();

        levelCompleteWindow = new LevelCompleteWindow(vbom);
        gameCompletedWindow = new GameCompletedWindow(vbom);
        if(levelNumber == 1)
            gameToast("Swipe over the ball to get it moving. Destroy all cats to complete the level. To restart the level, swipe while the ball is moving.");
        drawLine(resPol.getLeft(),resPol.getBottom(),resPol.getLeft() , resPol.getTop());
        drawLine(resPol.getLeft(),resPol.getBottom(),resPol.getRight(),resPol.getBottom());
        drawLine(resPol.getRight(),resPol.getBottom(),resPol.getRight(),resPol.getTop());
        drawLine(resPol.getLeft(),resPol.getTop(),resPol.getRight(),resPol.getTop());

        this.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                setupGestureDetection();
            }
        });
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(counter == 0){
                    if(levelNumber < lastLevel) {
                        levelCompleteWindow.setIgnoreUpdate(false);
                        levelCompleteWindow.setVisible(true);
                        levelCompleteWindow.display(GameScene.this, camera);
                        levelCompleteWindow.registerEntityModifier(new DelayModifier(3, new IEntityModifier.IEntityModifierListener() {

                            @Override
                            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}

                            @Override
                            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        if(!end) {
                                            levelCompleteWindow.setIgnoreUpdate(true);
                                            levelCompleteWindow.setVisible(false);
                                            levelCompleteWindow.clearEntityModifiers();
                                            changeLevel();
                                            createHUD();
                                        }
                                    }
                                });
                            }
                        }));
                    }
                    else {
                        gameCompletedWindow.setIgnoreUpdate(false);
                        gameCompletedWindow.setVisible(true);
                        gameCompletedWindow.display(GameScene.this, camera);
                        gameCompletedWindow.registerEntityModifier(new DelayModifier(3, new IEntityModifier.IEntityModifierListener() {

                            @Override
                            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}

                            @Override
                            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        if(!end) {
                                            gameCompletedWindow.setIgnoreUpdate(true);
                                            gameCompletedWindow.setVisible(false);
                                            gameCompletedWindow.clearEntityModifiers();
                                            changeLevel();
                                        }
                                    }
                                });
                            }
                        }));
                    }

                }

                else if (ball.stopped) {
                    restartLevel();
                }
                else {
                    ball.setRotation(90);
                }
            }

            @Override
            public void reset() {}
        });
    }
    @Override
    public void onBackKeyPressed()
    {
        end = true;
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
    }


    private void createBackground()
    {
        setBackground(new SpriteBackground(new Sprite(camera.getCenterX(),camera.getCenterY(),resourcesManager.grass, vbom)));
    }

    private void createHUD()
    {
        gameHUD = new HUD();

        // CREATE SCORE TEXT
        scoreText = new Text(120 + resPol.getLeft() , resPol.getTop() - 50, resourcesManager.font, "Tries: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setText("Tries: " + numberOfTries);
        gameHUD.attachChild(scoreText);

        camera.setHUD(gameHUD);
    }

    private void addTry()
    {
        numberOfTries += 1;
        scoreText.setText("Tries: " + numberOfTries);
    }

    //level loader
    private void loadLevel(int levelID)
    {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                GameScene.counter = SAXUtils.getIntAttributeOrThrow(pAttributes, BLUEBALL_COUNTER);
                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                float x = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                float y = SAXUtils.getFloatAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final int directionX = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_DIRECTIONX);
                final int directionY = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_DIRECTIONY);
                final int power = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_POWER);

                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if(x < 200)
                    x = x + resPol.getLeft();
                else if(x > 520)
                    x = x - resPol.getLeft();
                if(y < 200)
                    y = y + resPol.getBottom();
                else if (y > 1000)
                    y = y -  resPol.getBottom();

                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BLUEBALL))
                {

                    blueball = new BlueBall(x, y, resourcesManager.browncat, vbom, power, directionX, directionY, ball, camera, GameScene.this);
                    levelObject = blueball;
                    blueBallArrayList.add(blueball);
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GREENBALL))
                {
                    ball = new Ball(x, y, resourcesManager.greenball, vbom, physicsWorld);
                    ball.setZIndex(2);
                    ball.setScale(1.5f);
                    levelObject = ball;
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_REDBALL)){
                    redball = new RedBall(x, y, resourcesManager.redball, vbom, power, directionX, directionY, ball, camera, GameScene.this);
                    redball.setZIndex(1);
                    levelObject = redball;
                    redBallArrayList.add(redball);
                }

                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LOG))
                {
                    log = new Log(x, y, power, resourcesManager.log, vbom, physicsWorld);
                    levelObject = log;
                    logArrayList.add(log);
                }

                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }

    private void setupGestureDetection(){


        SurfaceGestureDetector surfaceGestureDetector = new SurfaceGestureDetector(this.activity) {



            @Override
            protected boolean onSwipe(final MotionEvent pMotionEventStart, final float pVelocityX,final float pVelocityY) {
                if(ball.ready) {
                    float[] sceneCoordinates = camera.getSceneCoordinatesFromSurfaceCoordinates(pMotionEventStart.getX(), pMotionEventStart.getY());
                    ball.handleActionDown(sceneCoordinates[0], sceneCoordinates[1]);
                    int directionX = Math.round(pVelocityX / 50);
                    int directionY = Math.round(pVelocityY / 50);
                    ball.setDirectionX(directionX);
                    ball.setDirectionY(-directionY);
                    if (resourcesManager.sound)
                        resourcesManager.slide.play();
                    return true;
                }
                else {
                    restartLevel();
                    return true;
                }
            }



            @Override
            public boolean onManagedTouchEvent(TouchEvent pSceneTouchEvent) {
                return super.onManagedTouchEvent(pSceneTouchEvent);
            }

            @Override
            public boolean onSceneTouchEvent(Scene pScene,
                                             TouchEvent pSceneTouchEvent) {
                return super.onSceneTouchEvent(pScene, pSceneTouchEvent);
            }


        };

        surfaceGestureDetector.setEnabled(true);
        setOnSceneTouchListener(surfaceGestureDetector);
    }

    private void createPhysics()
    {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);
    }

    private void changeLevel() {
        clearSprites();
        if(levelNumber<lastLevel) {
            levelNumber++;
            settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            settingsEditor = settings.edit();
            settingsEditor.putInt("level",levelNumber);
            settingsEditor.commit();
            int completedLevel = settings.getInt("completedLevel",1);
            if(completedLevel<levelNumber){
                settingsEditor.putInt("completedLevel",levelNumber);
                settingsEditor.commit();
            }
            numberOfTries=0;
            loadLevel(levelNumber);
        }
        else {
            disposeScene();
            SceneManager.getInstance().loadMenuScene(engine);
        }
    }

    private void restartLevel() {
        clearSprites();
        addTry();
        loadLevel(levelNumber);
    }

    private void clearSprites() {
        physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(ball));
        physicsWorld.destroyBody(ball.body);
        ball.detachSelf();

        for (RedBall redball : redBallArrayList){
            redball.setIgnoreUpdate(true);
            redball.detachSelf();

        }
        for (BlueBall blueball : blueBallArrayList){
            blueball.setIgnoreUpdate(true);
            blueball.detachSelf();

        }
        for (Explosion explosion : explosionArrayList){
            explosion.detachSelf();
        }
        for (Log log : logArrayList){
            log.removeBody();
            log.setIgnoreUpdate(true);
            log.detachSelf();
        }
        redBallArrayList.clear();
        blueBallArrayList.clear();
        explosionArrayList.clear();
        logArrayList.clear();
    }

    public void gameToast(final String msg) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResourcesManager.getInstance().activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void drawLine(final float startX, final float startY, final float endX, final float endY ) {
        Line bodyLine = new Line(startX, startY, endX, endY, vbom);
        Body lb;
        FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(2, 1, 1);
        lb = PhysicsFactory.createLineBody(physicsWorld, bodyLine, objectFixtureDef);
        bodyLine.setVisible(false);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(bodyLine, lb, true, true));
    }
}

