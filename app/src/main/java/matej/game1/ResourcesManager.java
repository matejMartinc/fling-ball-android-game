package matej.game1;

/**
 * Created by matej on 21.12.2014.
 */
import android.graphics.Color;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import java.io.IOException;


public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public MainActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;

    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion sound_on_region;
    public ITextureRegion sound_off_region;
    public ITextureRegion levels_region;
    public ITextureRegion locked_level_region;
    public ITextureRegion level_region;
    public Font font;

    private BuildableBitmapTextureAtlas menuTextureAtlas;
    private BuildableBitmapTextureAtlas levelsTextureAtlas;

    public boolean sound;
    public Music music;
    public Sound slide;
    public Sound impact;
    public Sound splash;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------

    // Game Texture
    public BuildableBitmapTextureAtlas gameTextureAtlas;

    // Game Texture Regions
    public ITiledTextureRegion browncat;
    public ITextureRegion greenball;
    public ITextureRegion redball;
    public ITextureRegion log;
    public ITextureRegion grass;

    // Explosion particles
    public BitmapTextureAtlas mParticleAtlas;
    public TextureRegion mParticleTexture;



    // Level Complete Window
    public ITextureRegion complete_window_region;

    // Game Complete Window
    public ITextureRegion game_complete_window_region;

    // Game over Window
    public ITextureRegion game_over_window_region;

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }

    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadMenuGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        sound_off_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "sound_off.png");
        sound_on_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "sound_on.png");
        levels_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "levels.png");


        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    public void loadMenuAudio()
    {
        try
        {
            music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), activity, "gfx/menu/music.ogg");
            music.setLooping(true);
            if(sound)
                music.play();
        }
        catch (IOException e)
        {
            music.release();
            music = null;
        }
    }
    public void unloadMenuAudio(){
        if(music.isPlaying())
            music.stop();
        music.release();
        music=null;
    }

    public void loadLevelsResources()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        levelsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        locked_level_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelsTextureAtlas, activity, "levellocked.png");
        level_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelsTextureAtlas, activity, "level.png");

        try
        {
            this.levelsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.levelsTextureAtlas.load();
        }
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
        loadMenuFonts();

    }

    private void loadGameGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);

        browncat = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "browncat.png",3,3);
        greenball = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "greenball.png");
        redball = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pond.png");
        log = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "log.png");
        grass = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "grass.jpg");
        complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
        game_complete_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "gameCompletedWindow.png");
        game_over_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "gameOverWindow.png");
        mParticleAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 32,32);
        mParticleTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParticleAtlas, this.activity, "fire.png",0,0);

        try
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
            mParticleAtlas.load();
        }
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadGameFonts()
    {

    }

    private void loadGameAudio()
    {

        try {
            slide = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "gfx/menu/slide.ogg");
            impact = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "gfx/menu/explosion.ogg");
            splash = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "gfx/menu/splash.ogg");
        } catch (IOException e) {
            slide.release();
            slide = null;
            impact.release();
            impact = null;
            splash.release();
            splash = null;
        }

    }

    public void unloadGameAudio()
    {
        slide.stop();
        slide.release();
        slide = null;
        impact.stop();
        impact.release();
        impact = null;
        splash.stop();
        splash.release();
        splash = null;
    }

    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png",0,0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen()
    {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, MainActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        getInstance().sound = true;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }

    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }

    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }

    public void unloadLevelsTextures()
    {
        levelsTextureAtlas.unload();
    }

    public void unloadGameTextures()
    {
        gameTextureAtlas.unload();
    }



}