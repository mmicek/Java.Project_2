package Project.GameObjects;

import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.BotSettings;
import Project.Game.WorldMap;
import Project.Settings.Position;
import Project.Settings.State;

public class MyBot extends Block {
    private BotSettings settings = new BotSettings();
    private WorldMap map = new WorldMap(this);
    private int x;
    private int y;
    private int minesCounter;
    private State state;

    public MyBot(){
        super(BlockTypes.MyBot);
        state = new State();
        this.minesCounter = 1;
    }

    public BotSettings getSettings() {
        return settings;
    }

    public WorldMap getMap() {
        return map;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Position getPosition(){
        return new Position(x,y);
    }

    public State getState(){
        return state;
    }

    public void setMines(int numb){
        this.minesCounter = numb;
    }

    public int getMinesCounter(){
        return minesCounter;
    }
}
