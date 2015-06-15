package matej.game1;

/**
 * Created by matej on 22.12.2014.
 */
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(200, 240, resourcesManager.font, "Loading...", vbom));
    }



    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneManager.SceneType getSceneType()
    {
        return SceneManager.SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }


}
