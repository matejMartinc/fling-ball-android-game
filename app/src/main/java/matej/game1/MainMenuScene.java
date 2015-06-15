package matej.game1;

/**
 * Created by matej on 22.12.2014.
 */



import android.content.Context;
import android.content.SharedPreferences;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;


public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private MenuScene menuChildScene;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences settings;
    SharedPreferences.Editor settingsEditor;
    private boolean sound;

    private final int MENU_PLAY = 0;
    private final int MENU_SOUND = 1;
    private final int MENU_LEVELS = 2;


    //---------------------------------------------
    // METHODS FROM SUPERCLASS
    //---------------------------------------------

    @Override
    public void createScene()
    {
        createBackground();
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed()
    {
        System.exit(0);
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



    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    private void createBackground()
    {
        attachChild(new Sprite(camera.getCenterX(), camera.getCenterY(), resourcesManager.menu_background_region, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        });
    }

    private void createMenuChildScene()
    {
        settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        menuChildScene = new MenuScene(camera);
        menuChildScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
        menuChildScene.addMenuItem(playMenuItem);
        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY());
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);
        menuChildScene.setOnMenuItemClickListener(this);
        setChildScene(menuChildScene);

        if(settings.getInt("audio",0) == 0) {
            IMenuItem soundOffMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND, resourcesManager.sound_off_region, vbom), 1.2f, 1);
            menuChildScene.addMenuItem(soundOffMenuItem);
            soundOffMenuItem.setPosition(menuChildScene.getMenuItem(0).getX(), menuChildScene.getMenuItem(0).getY()-130);
            sound = true;
        }
        else {
            IMenuItem soundOnMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND, resourcesManager.sound_on_region, vbom), 1.2f, 1);
            menuChildScene.addMenuItem(soundOnMenuItem);
            soundOnMenuItem.setPosition(menuChildScene.getMenuItem(0).getX(), menuChildScene.getMenuItem(0).getY()-130);
            sound = false;
            resourcesManager.sound = false;
            resourcesManager.music.pause();
        }

        final IMenuItem levelsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEVELS, resourcesManager.levels_region, vbom), 1.2f, 1);
        menuChildScene.addMenuItem(levelsMenuItem);
        levelsMenuItem.setPosition(menuChildScene.getMenuItem(0).getX(), menuChildScene.getMenuItem(0).getY()-260);




    }

    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
    {
        settings = this.activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        settingsEditor = settings.edit();

        switch(pMenuItem.getID())
        {
            case MENU_PLAY:
                //Load Game Scene!
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_SOUND:
                //disable sound
                if(sound) {
                    settingsEditor.putInt("audio", 1);
                    settingsEditor.commit();
                    menuChildScene.detachChild(menuChildScene.getMenuItem(1));
                    IMenuItem soundOnMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND, resourcesManager.sound_on_region, vbom), 1.2f, 1);
                    menuChildScene.addMenuItem(soundOnMenuItem);
                    soundOnMenuItem.setPosition(menuChildScene.getMenuItem(0).getX(), menuChildScene.getMenuItem(0).getY()-130);
                    sound = false;
                    resourcesManager.sound = false;
                    resourcesManager.music.pause();
                }
                else {
                    //enable sound
                    settingsEditor.putInt("audio", 0);
                    settingsEditor.commit();
                    menuChildScene.detachChild(menuChildScene.getMenuItem(1));
                    IMenuItem soundOffMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUND, resourcesManager.sound_off_region, vbom), 1.2f, 1);
                    menuChildScene.addMenuItem(soundOffMenuItem);
                    soundOffMenuItem.setPosition(menuChildScene.getMenuItem(0).getX(), menuChildScene.getMenuItem(0).getY()-130);
                    sound = true;
                    resourcesManager.sound = true;
                    resourcesManager.music.play();
                }
                return true;
            case MENU_LEVELS:
                //Load levels Scene!
                SceneManager.getInstance().loadLevelsScene(engine);
                return true;

            default:
                return false;
        }
    }
}