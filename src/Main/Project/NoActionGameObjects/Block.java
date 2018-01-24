package Project.NoActionGameObjects;

import Project.Settings.BlockTypes;

public abstract class Block {
    private BlockTypes type;
    private boolean isMultiple = false;

    public Block(BlockTypes type){
        this.type = type;
    }

    public BlockTypes getType() {
        return type;
    }

    public boolean isMultiple(){
        return isMultiple;
    }

    public void setIsMultiple(){
        isMultiple = true;
    }
}
