import gamingBoard.Board;
import gamingBoard.Board.Color;
import java.util.concurrent.Semaphore;
import javafx.concurrent.Task;
import javafx.application.Platform;

class ConnectFour extends Thread {
    private Board board;
    private Player playerOne, playerTwo, currentPlayer;
    private Bot bot;
    private boolean gameOver = false;
    private static final int X = 7, Y = 6;
    private int numberOfPlacedTokens = 0;
    private BoardGUI gui;
    Semaphore sem = new Semaphore(0);

    ConnectFour(String player) {
        board = new Board(X, Y);
        if (Math.random() > 1) {
            playerOne = new Player(player, Color.BLUE);
            bot = new Bot(6, Color.RED, board);
            playerTwo = bot;
        }
        else {
            bot = new Bot(6, Color.BLUE, board);
            playerTwo = new Player(player, Color.RED);
            playerOne = bot;
        }
        currentPlayer = playerOne;
    }

    ConnectFour(String pOne, String pTwo) {
        board = new Board(X, Y);
        if (Math.random() < 0.5) {
            playerOne = new Player(pOne, Color.BLUE);
            playerTwo = new Player(pTwo, Color.RED);
        }
        else {
            playerOne = new Player(pTwo, Color.BLUE);
            playerTwo = new Player(pOne, Color.RED);
        }
        currentPlayer = playerOne;
    }

    public void run() {
        while (gameOver == false) {
            System.out.println(currentPlayer.getName() + " am Zug");
            if (currentPlayer instanceof Bot) {
                System.out.println("bot spielt");
                
                int botToken = bot.play();
                placeToken(botToken);
                gui.botPlacedToken(botToken);
                System.out.println("bot gespielt");
            }
            else
                letHumanPlay();

            printBoard();  
            currentPlayer = (currentPlayer == playerOne)
                            ? playerTwo
                            : playerOne;
        }
    }

    public void letHumanPlay() {
        gui.humanPlacing(true);
        try {
            sem.acquire();
            sleep(1000);
        }
        catch (InterruptedException e) {
        }
        System.out.println("releast!");
    }

    public void setGUI(BoardGUI gui) {
        this.gui = gui;
    }

    public boolean placeToken(int slot) {
        if (board.canBePlaced(slot) == false)
            return false;
        Color tokenColor = currentPlayer.getColor();
        int tokenPosY = board.placeToken(slot, tokenColor);
        numberOfPlacedTokens++;
        return true;
    }

    static boolean correctInput(String s) {
        try {
            int input = Integer.parseInt(s);
            if (input < 0 || input > 6)
                return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean gameIsOver(int x, int y) {
        if (board.getHorizontalLength(x, y) >= 4
                || board.getVerticalLength(x, y) >= 4
                || board.getDiagonalUpLength(x, y) >= 4
                || board.getDiagonalDownLength(x, y) >= 4)
        {
            //current Player won
            return true;
        }
        else {
            if (numberOfPlacedTokens == X * Y)
                //game tied
                return true;
        }
        return false;
    }

    private void printBoard() {
		for (int k = 0; k < X; k++)
			System.out.print("   \u25bc");
		System.out.println();

		for (int i = Y-1; i >= 0; i--) {
			for (int j = 0; j < X; j++) {
                Color c = board.getToken(j, i);
				System.out.print(" | "+ (c == Color.RED ? "*" : (c == Color.BLUE ? "o" : " ")));
			}
			if (i > 0) {
				System.out.println(" |");
				for (int k = 0; k < X; k++)
					System.out.print(" |  ");
				System.out.println(" |");
			}
			else
				System.out.println(" |");
		}
		System.out.print(" ");
		for (int k = 0; k < X; k++)
			System.out.print(" \u2594\u2594\u2594");
		System.out.println("\ni: 0   1   2   3   4   5   6");
	}
}   
