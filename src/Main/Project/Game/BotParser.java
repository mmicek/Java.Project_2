package Project.Game;

import Project.GameObjects.EnemyBot;
import Project.GameObjects.MyBot;
import Project.Settings.Directions;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class BotParser {
    private Scanner scanner;
    private MyBot bot = new MyBot();
    private ArrayList<EnemyBot> enemyBots = new ArrayList<>();

    public BotParser(){
        scanner = new Scanner(System.in);
    }

    public void run(){
        while(scanner.hasNextLine()){
            String [] commands = scanner.nextLine().split(" ");
            if (commands.length == 0) continue;
            if (commands.length > 3 && commands[1].contains("game")) commands[1] += commands[2];
            else if(commands.length > 3 && commands[2].contains("bomb") &&
                    commands[1].charAt(6) - 48 == bot.getSettings().getBotID())
                commands[1] = "bomb";
            else if(commands.length > 3 && commands[2].contains("round")) commands[1] = "round";

            switch(commands[0]){
                case "update":
                   updateSettings(commands[1],commands[3]);
                    break;
                case "action":
                    returnAction(commands[1]);
                    break;
                case "settings":
                    parseSettings(commands[1],commands[2]);
                    break;
                default:
            }
        }
    }

    private void parseSettings(String type,String value){
        try{
            switch(type){
                case "timebank":
                    this.bot.getSettings().setTimeBank(Integer.parseInt(value));
                    break;
                case "time_per_move":
                    this.bot.getSettings().setTimePerMove(Integer.parseInt(value));
                    break;
                case "player_names":
                    String [] playerNames = value.split(",");
                    for(int i = 0;i < playerNames.length;i++){
                        if(bot.getSettings().getPlayerName() != null && playerNames[i] == bot.getSettings().getPlayerName())
                            continue;
                        enemyBots.add(new EnemyBot(playerNames[i]));
                    }
                    break;
                case "your_bot":
                    for(EnemyBot enemyBot : enemyBots)
                        if(enemyBot.getSettings().getName() == value)
                            enemyBots.remove(enemyBot);
                    bot.getSettings().setPlayerName(value);
                    break;
                case "your_botid":
                    bot.getSettings().setBotID(Integer.parseInt(value));
                    break;
                case "field_width":
                    bot.getMap().setWidth(Integer.parseInt(value));
                    break;
                case "field_height":
                    bot.getMap().setHeight(Integer.parseInt(value));
                    break;
                case "max_rounds":
                    break;
                default:
                    throw new Exception("Nie mozna ustawic ustawien z 'settings' dla " + type);
            }
        }catch(Exception e){
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void returnAction(String type){
        try{
            switch(type) {
                case "character":
                    System.out.println("bixiette");
                    break;
                case "move":
                    try {
                        Directions directions = new BotAlgorytm(bot).getMove();
                        String toPrint = directions.toString();
                        if (directions.canPlant())
                            toPrint = directions.toMove().toString() + ";drop_bomb 2";
                        System.out.println(toPrint);
                    }catch(Exception e){
                        System.err.println(e);
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new Exception("Wrong action for " + type);
            }

        }catch(Exception e){
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void updateSettings(String type,String value){
        try{
            switch(type){
                case "gameround":
                    bot.getMap().updateRound(Integer.parseInt(value));
                    break;
                case "gamefield":
                    bot.getMap().updateMap(value);
                    break;
                case "bomb":
                    bot.setMines(Integer.parseInt(value));
                    break;
                case "round":
                    bot.getSettings().setRound(Integer.parseInt(value));
                default:
                    throw new Exception("Nie mozna ustawic ustawien z 'update' dla " + type);
            }
        }catch(Exception e){
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
