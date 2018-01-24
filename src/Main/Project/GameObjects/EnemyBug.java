package Project.GameObjects;

import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.BugType;

public class EnemyBug extends Block {
    private BugType bugType;

    public EnemyBug(BugType type){
        super(BlockTypes.Bug);
        this.bugType = type;
    }

    private BugType getBugType(){
        return bugType;
    }
}
