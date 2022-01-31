import static gamingBoard.Board.Color;

class Player {
    private String name;
    protected Color myColor;

    Player (String n, Color c) {
        name = n;
        myColor = c;
    }

    public String getName() { return name; }

    public Color getColor() { return myColor; } 
}
