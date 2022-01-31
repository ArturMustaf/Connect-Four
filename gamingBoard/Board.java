package gamingBoard;

import java.util.ArrayList;

public class Board {
    private Color[][] board; 
    private final int X, Y;
//    private int lastPlacedTokenPosX, lastPlacedTokenPosY;
    private ArrayList<PosOfToken> placedTokens = new ArrayList<>();

    class PosOfToken {
        int posX;
        int posY;

        PosOfToken(int x, int y) {
            posX = x;
            posY = y;
        }
    }

    public Board(int sizeX, int sizeY) {
        X = sizeX;
        Y = sizeY;
        board = new Color[X][Y];
   }

    public int getNumberOfColumns() {
        return X;
    }

    public int getNumberOfRows() {
        return Y;
    }

    public boolean canBePlaced(int slot) {
        return board[slot][Y - 1] == null;
    }

    public boolean canBePlaced(int slot, int height) {
        return board[slot][height] == null;
    }

    public void removeLastPlaced() {
        int lastInList = placedTokens.size() - 1;
        board[placedTokens.get(lastInList).posX][placedTokens.get(lastInList).posY] = null;
        placedTokens.remove(lastInList);
    }

    public int placeToken(int slot, Color c) {
        boolean tokenPlaced = false;
        int y = 0;
        while (tokenPlaced == false) {
            if (board[slot][y] == null) {
                board[slot][y] = c;
                tokenPlaced = true;
            }
            else
                y++;
        }
        placedTokens.add(new PosOfToken(slot, y));
        return y;
    }

    public int getLastPlacedX() {
        return placedTokens.get(placedTokens.size() - 1).posX;
    }

    public int getLastPlacedY() {
        return placedTokens.get(placedTokens.size() - 1).posY;
    }

    public Color getToken(int x, int y) {
        return board[x][y];
    }

    public int getHorizontalLength(int x, int y) {
        Color c = board[x][y];
        int sequenceLength = 1, x_tmp;

        x_tmp = x - 1;
        while (x_tmp >= 0 && board[x_tmp][y] == c) {
            sequenceLength++;
            x_tmp--;
        }

        x_tmp = x + 1;
        while (x_tmp < X && board[x_tmp][y] == c) {
            sequenceLength++;
            x_tmp++;
        }
        return sequenceLength;
    }

    public int getVerticalLength(int x, int y) {
        Color c = board[x][y];
        int sequenceLength = 1, y_tmp;

        y_tmp = y + 1;
        while (y_tmp < Y && board[x][y_tmp] == c) {
            sequenceLength++;
            y_tmp++;
        }

        y_tmp = y - 1;
        while (y_tmp >= 0 && board[x][y_tmp] == c) {
            sequenceLength++;
            y_tmp--;
        }
        return sequenceLength;
    }

    public int getDiagonalUpLength(int x, int y) {
        Color c = board[x][y];
        int sequenceLength = 1, x_tmp, y_tmp;

        x_tmp = x - 1;
        y_tmp = y - 1;
        while (x_tmp >= 0 && y_tmp >= 0 && board[x_tmp][y_tmp] == c) {
            sequenceLength++;
            x_tmp--;
            y_tmp--;
        }

        x_tmp = x + 1;
        y_tmp = y + 1;
        while (x_tmp < X && y_tmp < Y && board[x_tmp][y_tmp] == c) {
            sequenceLength++;
            x_tmp++;
            y_tmp++;
        }
        return sequenceLength;       
    }

    public int getDiagonalDownLength(int x, int y) {
        Color c = board[x][y];
        int sequenceLength = 1, x_tmp, y_tmp;

        x_tmp = x - 1;
        y_tmp = y + 1;
        while (x_tmp >= 0 && y_tmp < Y && board[x_tmp][y_tmp] == c) {
            sequenceLength++;
            x_tmp--;
            y_tmp++;
        }

        x_tmp = x + 1;
        y_tmp = y - 1;
        while (x_tmp < X && y_tmp >= 0 && board[x_tmp][y_tmp] == c) {
            sequenceLength++;
            x_tmp++;
            y_tmp--;
        }
        return sequenceLength;       
    }

    public enum Color {
        RED, BLUE;
    }
}
