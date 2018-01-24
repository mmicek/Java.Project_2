package Project.GameObjects;

import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.Directions;

public class Gate extends Block {
    private Directions direction;

    public Gate(Directions direction){
        super(BlockTypes.Gate);
        this.direction = direction;
    }

    public Directions getDirection() {
        return direction;
    }
}
