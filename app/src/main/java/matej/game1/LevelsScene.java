package matej.game1;

/**
 * Created by matej on 29.1.2015.
 */

import android.content.Context;
import android.content.SharedPreferences;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import org.andengine.opengl.texture.region.ITextureRegion;

import org.andengine.util.adt.color.Color;


public class LevelsScene extends BaseScene implements ScrollDetector.IScrollDetectorListener, IOnSceneTouchListener, ClickDetector.IClickDetectorListener
{


        // ===========================================================
        // Constants
        // ===========================================================
        protected static int CAMERA_WIDTH = 570;
        protected static int CAMERA_HEIGHT = 1000;

        protected static int LEVELS = 16;
        protected static int LEVEL_COLUMNS_PER_SCREEN = 4;
        protected static int LEVEL_ROWS_PER_SCREEN = 4;
        protected static int LEVEL_PADDING = 60;

        public static final String MyPREFERENCES = "MyPrefs" ;
        SharedPreferences settings;
        SharedPreferences.Editor settingsEditor;

        // Scrolling
        private SurfaceScrollDetector mScrollDetector;
        private ClickDetector mClickDetector;

        private float mMinY;
        private float mMaxY;
        private float mCurrentY;
        private int iLevelClicked;

        //This value will be loaded from whatever method used to store data.
        private int mMaxLevelReached;


    @Override
    public void createScene()
    {

        this.mMinY=0;
        this.mMaxY=0;
        this.mCurrentY=0;
        this.iLevelClicked=-1;
        settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        this.mMaxLevelReached = settings.getInt("completedLevel",1);
        this.mScrollDetector = new SurfaceScrollDetector(this);
        this.mClickDetector = new ClickDetector(this);
        this.setBackground(new Background(new Color(65/255f,33/255f,22/255f)));
        this.setOnSceneTouchListener(this);
        this.setTouchAreaBindingOnActionDownEnabled(true);
        this.setTouchAreaBindingOnActionMoveEnabled(true);
        this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);



        CreateLevelBoxes();

    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        this.mClickDetector.onTouchEvent(pSceneTouchEvent);
        this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
        return true;
    }


    private void CreateLevelBoxes() {

        // calculate the amount of required columns for the level count
        int totalRows = (LEVELS / LEVEL_COLUMNS_PER_SCREEN) + 1;

        // Calculate space between each level square
        int spaceBetweenRows = (CAMERA_HEIGHT / LEVEL_ROWS_PER_SCREEN) - LEVEL_PADDING;
        int spaceBetweenColumns = (CAMERA_WIDTH / LEVEL_COLUMNS_PER_SCREEN) - LEVEL_PADDING;


        //Current Level Counter
        int iLevel = 1;

        //Create the Level selectors, one row at a time.
        int boxX = LEVEL_PADDING + 90, boxY = LEVEL_PADDING + 170;
        for (int y = 0; y < totalRows; y++) {
            for (int x = 0; x < LEVEL_COLUMNS_PER_SCREEN; x++) {

                //On Touch, save the clicked level in case it's a click and not a scroll.
                final int levelToLoad = iLevel;

                // Create the rectangle. If the level selected
                // has not been unlocked yet, don't allow loading.

                ITextureRegion boxTexture;
                if (iLevel > mMaxLevelReached) {
                    boxTexture = resourcesManager.locked_level_region;
                }
                else {
                    boxTexture = resourcesManager.level_region;
                }
                Sprite box = new Sprite(boxX, boxY, boxTexture, vbom) {
                    @Override
                    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                        if (levelToLoad > mMaxLevelReached)
                            iLevelClicked = -1;
                        else
                            iLevelClicked = levelToLoad;

                        return false;
                    }
                };

                this.attachChild(box);

                //Center for different font size
                if (levelToLoad <= mMaxLevelReached) {
                    Text text = new Text(boxX, boxY - 7, resourcesManager.font, String.valueOf(iLevel), vbom);
                    text.setColor(Color.BLACK);
                    text.setScale(1.2f);
                    this.attachChild(text);
                }

                this.registerTouchArea(box);

                iLevel++;
                boxX += spaceBetweenColumns + LEVEL_PADDING;

                if (iLevel > LEVELS)
                    break;
            }

            if (iLevel > LEVELS)
                break;

            boxY += spaceBetweenRows + LEVEL_PADDING;
            boxX = LEVEL_PADDING + 90;
        }

        //Set the max scroll possible, so it does not go over the boundaries.
        mMaxY = boxY - CAMERA_HEIGHT + 200;
    }



    //Here is where you call the level load.
    private void loadLevel(final int iLevel) {
        settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        settingsEditor = settings.edit();


        if (iLevel != -1) {
            settingsEditor.putInt("level", iLevel);
            settingsEditor.commit();
            camera.setCenter(360,600);
            SceneManager.getInstance().loadGameFromLevelsScene(engine);
        }
    }



    @Override
    public void onClick(ClickDetector pClickDetector, int pPointerID,
                        float pSceneX, float pSceneY) {
        // TODO Auto-generated method stub
        loadLevel(iLevelClicked);
     }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector,
                                int pPointerID, float pDistanceX, float pDistanceY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScroll(ScrollDetector pScollDetector, int pPointerID,
                         float pDistanceX, float pDistanceY) {
        // TODO Auto-generated method stub
        if ( ((mCurrentY - pDistanceY) < mMinY) || ((mCurrentY - pDistanceY) > mMaxY) )
            return;

        this.camera.offsetCenter(0, -pDistanceY);

        mCurrentY -= pDistanceY;
    }

    @Override
    public void onScrollFinished(ScrollDetector pScollDetector,
                                 int pPointerID, float pDistanceX, float pDistanceY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackKeyPressed()
    {
        camera.setCenter(360,600);
        SceneManager.getInstance().loadMenuFromLevelsScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_MENU;
    }


    @Override
    public void disposeScene()
    {
        // TODO Auto-generated method stub
    }


}