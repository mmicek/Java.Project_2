package Project.Game;

import Project.GameObjects.EnemyBot;
import Project.GameObjects.EnemyBug;
import Project.GameObjects.Gate;
import Project.GameObjects.MyBot;
import Project.NoActionGameObjects.*;
import Project.Settings.BlockTypes;
import Project.Settings.BugType;
import Project.Settings.Directions;
import Project.Settings.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WorldMap {
    private int width = 0;
    private int height = 0;
    private int round;
    private MyBot bot;
    private Block map [][];
    private boolean isMultiple = false;
    private List<Bomb> bombs = new LinkedList<>();

    public WorldMap(MyBot bot){
        this.bot = bot;
        this.round = 0;
    }

    public void setWidth(int width){
        this.width = width;
        if(height != 0)
            initMap();
    }

    public void setHeight(int height){
        this.height = height;
        if(width != 0)
            initMap();
    }

    public void initMap(){
        map = new Block[this.width][this.height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void updateRound(int round){
        this.round = round;
    }

    public int getRound(){
        return round;
    }

    public void updateMap(String mapStr){
        int i=0;
        int coord = 0;
        String object;
        while(i < mapStr.length()){
            object = "";
            while(i < mapStr.length() &&  mapStr.charAt(i) != ',') {
                object += mapStr.charAt(i);
                i++;
            }
            addObjectOnMap(coord,object);
            coord++;
            i++;
        }
    }

    public void addObjectOnMap(int coord,String object){
        int x = coord%width;
        int y = coord/width;
        if(moreThanOne(object))
            object = getMostImportant(object,x,y);

        switch(object){
            case ".":
                map[x][y] = new MovableCell();
                break;
            case "x":
                map [x][y] = new InaccesCell();
                break;
            case "C":
                map[x][y] = new Snippet();
                break;
            case "B":
                map[x][y] = new LaserMine();
        }

        if(object.startsWith("P")){
            addBotToMap(x,y,object);
        }
        else if(object.startsWith("S")){
            map[x][y] = new MovableCell(); //Dorobic spawn potem
        }
        else if(object.startsWith("G")){
            addGateToMap(x,y,object);
        }else if(object.startsWith("E")){
            addBugToMap(x,y,object);
        }else if(object.startsWith("B") && object.length() > 1){
            addBombToMap(x,y,object);
        }
    }

    public String getMostImportant(String objects,int x,int y){
        if(objects.contains("B") && objects.length() > 1) {
            System.out.println(objects);
            addBombToMap(x,y,cutTo(objects,"B"));
        }
        if(objects.contains("P")
                && objects.contains(Integer.toString(bot.getSettings().getBotID()))) {
            bot.setX(x);
            bot.setY(y);
        }
        if(objects.contains("G")) {
            if(objects.contains("C"))  return cutTo(objects,"C");
            return cutTo(objects, "G");
        }
        if(objects.contains("P"))
            return cutTo(objects,"P");
        if(objects.contains("E")) {
            isMultiple = true;
            return cutTo(objects, "E");
        }
        if(objects.contains("B"))
            return cutTo(objects,"B");
        if(objects.contains("C"))
            return cutTo(objects,"C");
        if(objects.contains("S"))
            return cutTo(objects,"S");
        return null;
    }

    public String cutTo(String string,String strToCancel){
        char toCancel = strToCancel.charAt(0);
        int i = 0;
        String result = "";
        while(string.charAt(i) != toCancel)
            i++;
        while(i < string.length() && string.charAt(i) != ';') {
            result += string.charAt(i);
            i++;
        }
        return result;
    }

    public void addInaccesCell(Position position){
        map[position.getX()][position.getY()] = new InaccesCell();
    }

    public void addBombToMap(int x,int y,String object){
        if(object.length() == 1) return;
        int nr = object.charAt(1) - 48;
        bombs.add(new Bomb(nr + round,new Position(x,y)));
        map[x][y] = new MovableCell();
    }

    public void addBugToMap(int x,int y,String object){
        int nr = (int)object.charAt(1) - 47;
        switch(nr){
            case 1:
                map[x][y] = new EnemyBug(BugType.Chase);
                break;
            case 2:
                map[x][y] = new EnemyBug(BugType.Predict);
                break;
            case 3:
                map[x][y] = new EnemyBug(BugType.Level);
                break;
            case 4:
                map[x][y] = new EnemyBug(BugType.FarChase);
        }
        if(isMultiple){
            map[x][y].setIsMultiple();
            isMultiple = false;
        }
    }

    public void addGateToMap(int x,int y,String object){
        if(object.charAt(1) == 'l')
            map[x][y] = new Gate(Directions.Left);
        else
            map[x][y] = new Gate(Directions.Right);
    }

    public void addBotToMap(int x,int y,String object){
        int id;
        String idStr = "";
        for(int i = 1;i<object.length();i++){
            if(object.charAt(i) == ' ') break;
            idStr += object.charAt(i);
        }
        id = Integer.parseInt(idStr);
        if(id == bot.getSettings().getBotID()) {
            bot.setX(x);
            bot.setY(y);
            map[x][y] = bot;
        }
        else
            map[x][y] = new EnemyBot(id);
    }

    public boolean moreThanOne(String s){
        return s.contains(";");
    }

    public boolean canMoveTo(int x,int y){
        if(x < width && x >= 0 && y < height && y >= 0){
            BlockTypes type = map[x][y].getType();
            if(type == BlockTypes.MoveableCell || type == BlockTypes.LaserMine
                    || type == BlockTypes.Snippet || type == BlockTypes.EnemyBot
                    || type == BlockTypes.Gate || type == BlockTypes.BugSpawnPoint
                    || type == BlockTypes.MyBot)
               return true;
        }
        return false;
    }

    public boolean canMoveTo(Position p){
        return canMoveTo(p.getX(),p.getY());
    }

    public Block getObjectAt(int x,int y){
        if(x < width && x >= 0 && y < height && y >= 0)
            return map[x][y];
        return null;
    }

    public Block getObjectAt(Position p){
        return getObjectAt(p.getX(),p.getY());
    }

    public void addMovableCell(Position position){
        map[position.getX()][position.getY()] = new MovableCell();
    }

    public List<Bomb> getBombs() {
        return bombs;
    }
}
