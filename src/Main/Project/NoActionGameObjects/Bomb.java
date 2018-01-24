package Project.NoActionGameObjects;

import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.Position;

public class Bomb extends Block {
    private int blowUpLevel;
    private Position position;

    public Bomb(int blowUpLevel,Position position){
        super(BlockTypes.Bomb);
        this.position = position;
        this.blowUpLevel = blowUpLevel;
    }

    public int getBlowUpLevel() {
        return blowUpLevel;
    }

    public Position getPosition() {
        return position;
    }
}
