package Project.Settings;

public class BotSettings {
    private int maxTimeBank;
    private int timeBank;
    private int timePerMove;
    private String playerName = null;
    private int botID = 0;
    private int round;

    public BotSettings(){
        ;
    }

    public void setTimePerMove(int value){
        this.timePerMove = value;
    }

    public void setTimeBank(int value) {
        this.timeBank = value;
        this.maxTimeBank = timeBank;
    }

    public void addToTimeBank(){
        this.timeBank += timePerMove;
        if(timeBank > maxTimeBank)
            timeBank = maxTimeBank;
    }

    public int getTimeBank() {
        return timeBank;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setBotID(int ID){
        this.botID = ID;
    }

    public int getBotID(){
        return this.botID;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
