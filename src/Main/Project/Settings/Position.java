package Project.Settings;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position getNewPosition(Directions directions){
        return getNewPosition(directions.getNumberFromDirection());
    }

    public Position getNewPosition(int direct) {
        switch(direct){
            case 1:
                return new Position(x,y-1);
            case 2:
                return new Position(x+1,y);
            case 3:
                return new Position(x,y+1);
            case 4:
                return new Position(x-1,y);

        }
        return this;
    }

    public Directions getDirectionFromVecto(Position to){
        for(int i=1;i<5;i++){
            if(this.getNewPosition(i).equals(to))
                return Directions.getDirectionFromNumber(i);
        }
        return Directions.Pass;
    }

    public boolean equals(Position other){
        if(!(other instanceof Position))
            return false;
        return x == other.getX() && y == other.getY();
    }

    public String toString(){
        return "( " + x + " , " + y + " )";
    }
}

