package Project.Game;

import Project.GameObjects.Gate;
import Project.GameObjects.MyBot;
import Project.NoActionGameObjects.Block;
import Project.Settings.BlockTypes;
import Project.Settings.Directions;
import Project.Settings.Position;
import java.util.LinkedList;
import java.util.List;

public class BotAlgorytm {
    private MyBot bot;
    private Position enemyBot = null;
    private WorldMap map;

    BotAlgorytm(MyBot bot){
        this.bot = bot;
        map = bot.getMap();
    }

    public Directions getMove(){
        MinesLogic minesLogic = new MinesLogic(bot,map);
        List<Position> bannedPositions = checkIfBugIsNear(bot.getPosition());
        Package mySnip = getSortedSnipped(bot.getPosition());

        /**
            Patrzy czy trzeba uciekać przed minąmi
         */

        int [][] afectedPositions = minesLogic.ifStandingOnBombBlowArea();
        if(afectedPositions != null
                && minesLogic.checkIfICanRunAway(bot.getPosition(),afectedPositions,minesLogic.getRange(),mySnip.result))
            return minesLogic.getToRunAfterPlant();

        /**
         * Odnajduje scieżkę z BFA wyliczonego powyżej
         */

        Package enemySnip = getSortedSnipped(enemyBot);
        minesLogic.setEnemyPosition(enemyBot);
        Position snipppetToGo;
        if(enemyBot == bot.getPosition())
            snipppetToGo = mySnip.sortedSnipped.get(0);
        else
            snipppetToGo = findClosestSnippet(mySnip,enemySnip);

        Directions moveDirection = findDirectionToGo(snipppetToGo,mySnip.result,mySnip.mines);
        if(moveDirection != null && moveDirection != Directions.Pass)
            return moveDirection;

        restoreBannedCells(bannedPositions);
        Directions forceDirection = forceMove();

        return forceDirection;
    }

    public Directions forceMove(){
        int count = 0;
        Directions bugDirection = Directions.Pass;
        List<Directions> movableDirections = new LinkedList<>();
        for(int i=1;i<5;i++){
            Block block = map.getObjectAt(bot.getPosition().getNewPosition(i));
            if(block == null) continue;
            if(block.getType() == BlockTypes.Bug) {
                count++;
                if(!block.isMultiple())
                    bugDirection = Directions.getDirectionFromNumber(i);
            }
            if(map.canMoveTo(bot.getPosition().getNewPosition(i)))
                movableDirections.add(Directions.getDirectionFromNumber(i));
        }
        if(count != 0 && movableDirections.size() > 0)
            return movableDirections.get(0);
        if(count != 0)
            return bugDirection;
        return Directions.Pass;
    }

    public List<Position>  checkIfBugIsNear(Position myBot){
        List <Position> closePosition = new LinkedList<>();
        List<Position> bannedPositions = new LinkedList<>();
        for(int i=1;i<5;i++)
            if(map.canMoveTo(myBot.getNewPosition(i)))
                closePosition.add(myBot.getNewPosition(i));

        while(closePosition.size() != 0){
            Position position = closePosition.get(0);
            closePosition.remove(0);
            for(int i=1;i<5;i++){
                if(map.getObjectAt(position.getNewPosition(i)) != null &&
                      map.getObjectAt(position.getNewPosition(i)).getType() == BlockTypes.Bug) {
                    bannedPositions.add(position);
                    map.addInaccesCell(position);
                    break;
                }
            }
        }
        return bannedPositions;
    }

    public Position findClosestSnippet(Package mySnip,Package enemySnip){
        List <Position> mySnippets = mySnip.sortedSnipped;
        if(mySnippets.size() == 0) return enemyBot;
        Position result = enemySnip.sortedSnipped.get(enemySnip.sortedSnipped.size()-1);

        while(mySnippets.size() != 0){
            Position position = mySnippets.get(0);
            mySnippets.remove(0);
            if(mySnip.result[position.getX()][position.getY()] <= enemySnip.result[position.getX()][position.getY()])
                return position;
        }
        return result;
    }

    public Directions findDirectionToGo(Position snipPos,int result [][],List<Position> mines){
        if(snipPos.equals(bot.getPosition())) return Directions.Pass;
        Position snippet = snipPos;
        boolean doesPosChanged;
        Position myBotPos = this.bot.getPosition();
        Gate gate;
        int currentNumer = result[snipPos.getX()][snipPos.getY()];

        if(mines != null && mines.size() > 0 && bot.getMinesCounter() == 0) {
            Directions toPickLaserMine = new MinesLogic(bot, map).checkLaserMineNear(result, mines);
            if (toPickLaserMine != Directions.Pass)
                return toPickLaserMine;
        }
        if(map.getObjectAt(myBotPos).getType() == BlockTypes.Gate) {
            gate = (Gate) map.getObjectAt(myBotPos);
            if(abs(snipPos.getX() - myBotPos.getX()) > map.getWidth()/2)
                return gate.getDirection();
        }

        while(currentNumer != 1) {
            doesPosChanged = false;
            for (int i = 1; i < 5; i++) {
                Position newPosition = snippet.getNewPosition(i);
                if (map.canMoveTo(newPosition) && result[newPosition.getX()][newPosition.getY()] + 1 == currentNumer) {
                    snippet = newPosition;
                    doesPosChanged = true;
                    break;
                }
            }
            if (!doesPosChanged) {
                if (isGate(snippet, result) != null)
                    snippet = isGate(snippet, result);
                else
                    return Directions.Pass;
            }
                currentNumer--;
        }

        return myBotPos.getDirectionFromVecto(snippet);
    }

    public Package getSortedSnipped(Position startingPos){
        int  result [][] = new int [map.getWidth()][map.getHeight()];
        List<Position> sortedSnipped = new LinkedList<>();
        List<BFS> BFSList = new LinkedList<>();
        List<Position> mines = new LinkedList<>();
        BFSList.add(new BFS(startingPos,startingPos,0));
        boolean gate = true;

        while(BFSList.size() != 0){
            BFS bfs = BFSList.get(0);
            BFSList.remove(0);

            BlockTypes block = map.getObjectAt(bfs.currentPos).getType();
            if(block == BlockTypes.EnemyBot)
                enemyBot = bfs.currentPos;
            if(block == BlockTypes.Snippet)
                sortedSnipped.add(bfs.currentPos);
            if(block == BlockTypes.LaserMine) {
                mines.add(bfs.currentPos);
            }

            if(isGate(bfs.currentPos,result) != null && gate) {  //tu zle jest
                Position currentPos = isGate(bfs.currentPos, result);
                BFSList.add(new BFS(currentPos, bfs.currentPos, bfs.length + 1));
                if(result[currentPos.getX()][currentPos.getY()] == 0)
                    result[currentPos.getX()][currentPos.getY()] = bfs.length + 1;
                gate = false;
            }

            for(int i=1;i<5;i++) {
                Position newPosition = bfs.currentPos.getNewPosition(i);
                if (map.canMoveTo(newPosition) && result[newPosition.getX()][newPosition.getY()] == 0) {
                    BFSList.add(new BFS(newPosition, bfs.currentPos, bfs.length + 1));
                    result[newPosition.getX()][newPosition.getY()] = bfs.length + 1;
                }
            }
        }
        result[startingPos.getX()][startingPos.getY()] = 0;

        if(enemyBot == null)
           enemyBot = bot.getPosition();
        return new Package(result,sortedSnipped,mines);
    }

    public void restoreBannedCells(List<Position> banned){
        for (Position position: banned) {
            map.addMovableCell(position);
        }
    }

    public Position isGate(Position position,int [][] result){ //ret val == null nie jest brama
        /**
         * Zwraca nowa pozycje po przejsciu przez brame
         * i jesli nie stal juz na niej nikt wczesniej
         */
        if(map.getObjectAt(position).getType() != BlockTypes.Gate)
            return null;

        if(position.getX() == 0)
            return new Position(18,7);
        else
            return new Position(0,7);
    }

    private class Package{
        /**
         * result to tablica intow gdzie 0 to nasz gracz a kazda inna liczba to ilosc ruchow potrzebnych do przejscia
         * na dane pole
         */
        private int [][] result;
        private List<Position> sortedSnipped;
        private List<Position> mines;

        Package(int [][] result,List<Position> sortedSnipped,List<Position> mines){
            this.result = result;
            this.sortedSnipped = sortedSnipped;
            this.mines = mines;
        }
    }

    private class BFS{
        private Position currentPos;
        private int length;

        BFS (Position currentPos,Position prevPos,int length){
            this.currentPos = currentPos;
            this.length = length;
        }
    }

    public int abs(int x){
        if (x > 0) return x;
        return -x;
    }
}
