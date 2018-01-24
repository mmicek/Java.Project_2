package Project.Settings;

public enum BlockTypes {
    MyBot,EnemyBot,Bug,MoveableCell,InaccesCell,BugSpawnPoint,Gate,LaserMine,Bomb,Snippet;


    @Override
    public String toString() {
        if(this == MyBot)
            return "P";
        if(this == Bug)
            return "&";
        return super.toString();
    }
}
