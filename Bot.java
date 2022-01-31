import gamingBoard.Board;
import gamingBoard.Board.Color;

public class Bot extends Player {
    private int difficulty, treeHeight, columns, rows;
    private Board board;
    private Color otherColor;
    private boolean firstRound = true;

private int placedToken;
    public Bot(int d, Color c, Board b) {
        super("Bot", c);
        difficulty = d;
        board = b;
        otherColor = (myColor == Color.BLUE ? Color.RED : Color.BLUE);
        columns = b.getNumberOfColumns();
        rows = b.getNumberOfRows();
    }



    public int play() {
int mustPlace = 1_000_000;
if(placedToken != 0)System.out.println("Baumtiefe sollte jetzt sein: " + (
                                        (int) (Math.log(mustPlace - placedToken) / Math.log(7)))
                                        );
placedToken = 0;
        int numberOfPlayableSlots = 7;
        for (int i = 0; i < columns; i++) {
//            if (board.getToken(i, 0) != null)
//                noTokensSet = false;
            if (board.getToken(i, 5) != null)
                numberOfPlayableSlots--;
        }
        treeHeight = (int) (Math.log(Math.pow(difficulty, columns))
                                     / Math.log(numberOfPlayableSlots));
        System.out.println("Baumtiefe : " + treeHeight);
        if (firstRound) {
            firstRound = false;
            return 3;
        }
        MinMaxTree minmax = new MinMaxTree(columns);
        spanTree(myColor, 0, minmax);
        int playSlot = minmax.getBestMove();
System.out.println("placed Tokens: " + placedToken);
        return playSlot;
    }

    public void setDifficulty(int d) {
        difficulty = d;
    }

    private int rateBoard() {
        int result = 0;
        for (int x = 0; x < board.getNumberOfColumns(); x++) {
            for (int y = 0;
                 y < board.getNumberOfRows() && board.getToken(x,y) != null;
                 y++)
            {
                if (board.getToken(x, y) == otherColor) {
                    result += rateHorizontalSequence(x, y);
                    result += rateVerticalSequence(x,y);
                    result += rateDiagonalUpSequence(x,y);
                    result += rateDiagonalDownSequence(x,y);
                }
            }
        }
        return result;
    }

    private int myHeuristic(int length) {
        switch (length) {
            case 1:
                return 1;
            case 2:
                return 4;
            case 3:
                return 16;
            default:
                return 100;
        }
    }

    private int rateHorizontalSequence(int x, int y) {
        if (x != 0 && board.getToken(x - 1, y) == otherColor)
            return 0;
        int possibleSeqLength = 1;
        for (int x_tmp = x - 1;
            possibleSeqLength < 4 && x_tmp >= 0
                    && board.getToken(x_tmp, y) != myColor;
            x_tmp--)
        {
            possibleSeqLength++;
        }
        int possibleRightLength = 1;
        for (int x_tmp = x + 1;
            possibleSeqLength < 4 && x_tmp < columns
                    && board.getToken(x_tmp, y) != myColor;
            x_tmp++)
        {
            possibleSeqLength++;
        }
        if (possibleSeqLength < 4)
            return 0;
        int sequenceLength = board.getHorizontalLength(x, y);
        return myHeuristic(sequenceLength);
    }

	private int rateVerticalSequence(int x, int y) {
        if (y >= rows - 3 || y != 0 && board.getToken(x, y - 1) == otherColor)
            return 0;
        for (int y_tmp = y + 1, possibleSeqLength = 1;
             possibleSeqLength < 4;
             y_tmp++, possibleSeqLength++)
        {
            if (board.getToken(x, y_tmp) == myColor)
                return 0;
        }
        int sequenceLength = board.getVerticalLength(x, y);
        return myHeuristic(sequenceLength);
    }

    private int rateDiagonalUpSequence(int x, int y) {
        if (x != 0 && y != 0 && board.getToken(x - 1, y - 1) == otherColor)
            return 0;
        int possibleSeqLength = 1;
        for (int x_tmp = x - 1, y_tmp = y - 1;
             possibleSeqLength < 4 && x_tmp >= 0 && y_tmp >= 0
                    && board.getToken(x_tmp, y_tmp) != myColor;
            x_tmp--, y_tmp--)
        {
            possibleSeqLength++;
        }
        int possibleRightLength = 1;
        for (int x_tmp = x + 1, y_tmp = y + 1;
             possibleSeqLength < 4 && x_tmp < columns && y_tmp < rows
                    && board.getToken(x_tmp, y_tmp) != myColor;
            x_tmp++, y_tmp++)
        {
            possibleSeqLength++;
        }
        if (possibleSeqLength < 4)
            return 0;
        int sequenceLength = board.getDiagonalUpLength(x, y);
        return myHeuristic(sequenceLength);
    }

    private int rateDiagonalDownSequence(int x, int y) {
        if (x != 0 && y + 1 < rows && board.getToken(x - 1, y + 1) == otherColor)
            return 0;
        int possibleSeqLength = 1;
        for (int x_tmp = x - 1, y_tmp = y + 1;
             possibleSeqLength < 4 && x_tmp >= 0 && y_tmp < rows
                    && board.getToken(x_tmp, y_tmp) != myColor;
            x_tmp--, y_tmp++)
        {
            possibleSeqLength++;
        }
        int possibleRightLength = 1;
        for (int x_tmp = x + 1, y_tmp = y - 1;
            possibleSeqLength < 4 && x_tmp < columns && y_tmp >= 0
                    && board.getToken(x_tmp, y_tmp) != myColor;
            x_tmp++, y_tmp--)
        {
            possibleSeqLength++;
        }
        if (possibleSeqLength < 4)
            return 0;
        int sequenceLength = board.getDiagonalDownLength(x, y);
        return myHeuristic(sequenceLength);
    
    }

    private void spanTree(Color c, int level, MinMaxTree mmTree) {
        if (level == treeHeight) {
            int heuristicValue = rateBoard();
            mmTree.setValue(heuristicValue);
            board.removeLastPlaced();
        }
        else {
            for (int slot = 0; slot < columns; slot++) {
                boolean canPlaceToken = board.canBePlaced(slot);
                if (canPlaceToken) {

placedToken++;
                    board.placeToken(slot, c);
                    mmTree.canBeSet(slot);
                    Color nextColor = (c == myColor) ? otherColor : myColor;
                    spanTree(nextColor, level + 1, mmTree);
                }
            }
            if (level > 0) {
                board.removeLastPlaced();
                mmTree.traverseUp();
            }
        }
    }
}
