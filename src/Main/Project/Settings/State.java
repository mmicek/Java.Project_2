package Project.Settings;


import java.util.LinkedList;
import java.util.List;

public class State {
    private int lastDistanceToEnemy = 0;
    private Position positionToEscapeFromBomb;
    private int bombExplode;
    List<Position> runAway = new LinkedList<>();

    public State(){;}

    public void setLastDistanceToEnemy(int dist){
        this.lastDistanceToEnemy = dist;
    }

    public int getLastDistanceToEnemy() {
        return lastDistanceToEnemy;
    }

    public void setPositionToEscapeFromBomb(Position position){
        this.positionToEscapeFromBomb = position;
    }

    public Position getPositionToEscapeFromBomb() {
        return positionToEscapeFromBomb;
    }

    public void setBombExplode(int round){
        this.bombExplode = round;
    }

    public int getBombExplode() {
        return bombExplode;
    }
}
