package Project.GameObjects;

import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.EnemyBotSettings;

public class EnemyBot extends Block {
    private EnemyBotSettings settings;
    private int ID;

    public EnemyBot(int ID){
        super(BlockTypes.EnemyBot);
        settings = null;
        this.ID = ID;
    }

    public EnemyBot(String name){
        super(BlockTypes.EnemyBot);
        settings = new EnemyBotSettings();
        settings.setName(name);
    }

    public EnemyBotSettings getSettings() {
        return settings;
    }
}
