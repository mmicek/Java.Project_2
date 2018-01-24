package Project.Game;

import Project.GameObjects.MyBot;
import Project.NoActionGameObjects.Bomb;
import Project.Settings.BlockTypes;
import Project.Settings.Directions;
import Project.Settings.Position;
import java.util.LinkedList;
import java.util.List;

public class MinesLogic {
    private WorldMap map;
    private MyBot bot;
    private Position enemyPosition;
    private Directions toRunAfterPlant;
    private int range;

    MinesLogic(MyBot bot,WorldMap map){
        this.bot = bot;
        this.map = map;
    }

    public int [][] ifStandingOnBombBlowArea(){
        int [][] result = new int [map.getWidth()][map.getHeight()];
        boolean amIAffected = false;
        range = 40;

        for (Bomb bomb:map.getBombs()) {
            if(bomb.getPosition().equals(bot.getPosition())){
                result[bot.getPosition().getX()][bot.getPosition().getY()] = 1;
                amIAffected = true;
            }
            result[bomb.getPosition().getX()][bomb.getPosition().getY()] = 1;
            for(int i=1;i<5;i++){
                Position position = bomb.getPosition().getNewPosition(i);

                while(map.getObjectAt(position) != null && map.getObjectAt(position).getType() != BlockTypes.InaccesCell){
                    result[position.getX()][position.getY()] = 1;
                    if(bot.getPosition().equals(position)) {
                        amIAffected = true;
                        if(range > bomb.getBlowUpLevel() - map.getRound())
                            range = bomb.getBlowUpLevel() - map.getRound();
                    }
                    position = position.getNewPosition(i);
                }

            }
        }

        if (amIAffected) return result;
        return null;
    }

    public boolean checkIfICanRunAway(Position position,int [][] affectedPositions,int range,int [][] result){
        List<Position> positions = new LinkedList<>();
        List <Position> controlPositions = new LinkedList<>();
        for(int i = 1;i<5;i++)
            if(map.canMoveTo(position.getNewPosition(i))) {
                positions.add(position.getNewPosition(i));
                if(affectedPositions[position.getNewPosition(i).getX()][position.getNewPosition(i).getY()] == 0) {
                    toRunAfterPlant = new BotAlgorytm(bot).findDirectionToGo(position.getNewPosition(i), result, null);
                    return true;
                }
            }
        while(range - 1 > 0) {
            while (positions.size() != 0) {
                Position pos = positions.get(0);
                positions.remove(0);
                for (int i = 1; i < 5; i++) {
                    if (map.canMoveTo(pos.getNewPosition(i))) {
                        controlPositions.add(pos.getNewPosition(i));
                        if(affectedPositions[pos.getNewPosition(i).getX()][pos.getNewPosition(i).getY()] == 0) {
                            toRunAfterPlant = new BotAlgorytm(bot).findDirectionToGo(pos.getNewPosition(i), result, null);
                            return true;
                        }
                    }
                }
            }
            positions = controlPositions;
            range --;
        }

        return false;
    }

    public Directions checkLaserMineNear(int [][] result, List<Position> mine){
        List<Position> mines = mine;
        for (Position minePos:mines) {
            if(result[minePos.getX()][minePos.getY()] < 6) {
                return findDirectionToLaserMine(minePos, result);
            }
        }
        return Directions.Pass;
    }

    public Directions findDirectionToLaserMine(Position mines,int [][] result){
        return new BotAlgorytm(bot).findDirectionToGo(mines,result,null);
    }

    public void setEnemyPosition(Position position){
        this.enemyPosition = position;
    }

    public Directions getToRunAfterPlant() {
        return toRunAfterPlant;
    }

    public int getRange(){
        return range;
    }

}
