package Project.Settings;

public enum Directions {
    Left,Right,Up,Down,Pass,PLeft,PRight,PUp,PDown;

    public static Directions getDirectionFromNumber(int i){
        switch (i){
            case 1:
                return Directions.Up;
            case 2:
                return Directions.Right;
            case 3:
                return Directions.Down;
            case 4:
                return Directions.Left;
            default:
                return Directions.Pass;
        }
    }

    public int getNumberFromDirection(){
        switch(this){
            case Left:
                return 4;
            case Right:
                return 2;
            case Up:
                return 1;
            case Down:
                return 3;
            default:
                return 5;
        }
    }

    public String toString(){
        switch(this){
            case Left:
                return "left";
            case Right:
                return "right";
            case Up:
                return "up";
            case Down:
                return "down";
            case PLeft:
                return "Pleft";
            case PRight:
                return "Pright";
            case PUp:
                return "Pup";
            case PDown:
                return "Pdown";
            case Pass:
                return "pass";
            default:
                return "pass";
        }
    }

    public boolean canPlant(){
        if(this.toString().charAt(0) == 'P' && this != Directions.Pass)
            return true;
        return false;
    }

    public Directions toMove(){
        switch(this){
            case PLeft:
                return Directions.Left;
            case PRight:
                return Directions.Right;
            case PUp:
                return Directions.Up;
            case PDown:
                return Directions.Down;
        }
        return Directions.Pass;
    }
}
