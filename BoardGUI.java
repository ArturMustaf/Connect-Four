import javafx.application.*;  
import javafx.scene.*;  
import javafx.stage.*;  
import javafx.scene.layout.*;  
import javafx.geometry.*; 
import javafx.scene.image.*; 
import javafx.scene.shape.*; 
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.concurrent.Task;  

public class BoardGUI extends Application {    

    StackPane rootNode;
    static ConnectFour c4;
    boolean humanPlacing;
    int botToken;

    Image imgBlue = new Image("TokenBLUE.png", 40.5, 40.5, true, true);
    Image imgRed = new Image("TokenRED.png", 40.5, 40.5, true, true);
    Image imgGrey = new Image("TokenGREY.png", 40.5, 40.5, true, true);
    Image imgYellow = new Image("TokenYELLOW.png", 40.5, 40.5, true, true);
    Image currentImage = imgYellow;

    double hgap = 27.75, leftMargin = 40.5, lowestPos = 352;
 
    public static void main(String[] args) {
        c4 = new ConnectFour("Artur");

        launch(args);
    }

    public void start(Stage myStage) {  
        myStage.setTitle("Connect 4");  
  
        rootNode = new StackPane();  

        Image imgBackground = new Image("C4Background.png", 530, 533, true, true);
        ImageView ivBackground = new ImageView(imgBackground);

        Image imgBoard = new Image("C4Board.png", 530, 465, true, true);
        ImageView ivBoard = new ImageView(imgBoard);
        StackPane.setAlignment(ivBoard, Pos.BOTTOM_CENTER);

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.millis(500));

        HBox hbox = new HBox(27);
        StackPane.setAlignment(hbox, Pos.TOP_CENTER);

        for (int i = 0; i < 7; i++) {
            ImageView iv = new ImageView(imgGrey);
            iv.setId("" + i);
            iv.setOnMouseEntered(e -> {
                if (humanPlacing && tt.getStatus() == Animation.Status.STOPPED)
                    iv.setImage(currentImage);
            });
            iv.setOnMouseExited(e -> {
                if (humanPlacing && tt.getStatus() == Animation.Status.STOPPED)
                    iv.setImage(imgGrey);
            });
            iv.setOnMousePressed(e -> {
                if (humanPlacing && tt.getStatus() == Animation.Status.STOPPED) {
                    humanPlacing = false;
                    ImageView ivTmp = new ImageView(currentImage);
                    currentImage = (currentImage == imgYellow) ? imgRed : imgYellow;
                    iv.setImage(imgGrey);
                    ((StackPane) iv.getParent()).getChildren().add(ivTmp);
                    tt.setNode(ivTmp);
                    int tokenPos = (int) (-1.5 + (8 - ((Pane) iv.getParent()).getChildren().size()) * (hgap + leftMargin));
                    tt.setByY(tokenPos);
                    tt.play();
                    c4.placeToken(Integer.parseInt(iv.getId()));
                    c4.sem.release();
                }
            });

            StackPane spTmp = new StackPane();
            spTmp.getChildren().add(iv);
            hbox.getChildren().add(spTmp);
            spTmp.setAlignment(Pos.TOP_CENTER);
        }
   
        rootNode.getChildren().addAll(ivBackground, hbox, ivBoard);
        Insets ins = new Insets(25, 0, 0, leftMargin);

        rootNode.setMargin(hbox, ins);
        Scene myScene = new Scene(rootNode);

        myStage.setScene(myScene);
        myStage.show();

        c4.setGUI(this);
        c4.start();
    }

    public void botPlacedToken(int pos) {
        botToken = pos;
        Thread th = new Thread(new Task<Void>() {
            protected Void call() throws Exception {
System.out.println("yes");
                Platform.runLater(new Runnable() {
                    public void run() {
                        place(botToken);
                    }
                });
            return null;
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public void place(int pos) {
System.out.println("pos auf " +pos);
        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.millis(500));

        StackPane spTmp = (StackPane) (((HBox) (rootNode.getChildren().get(1))).getChildren()).get(pos);
        ImageView iv = (ImageView) spTmp.getChildren().get(0);
        if (tt.getStatus() == Animation.Status.STOPPED) {
            ImageView ivTmp = new ImageView(currentImage);
            currentImage = (currentImage == imgYellow) ? imgRed : imgYellow;
            iv.setImage(imgGrey);
            ((StackPane) iv.getParent()).getChildren().add(ivTmp);
            tt.setNode(ivTmp);
            int tokenPos = (int) (-1.5 + (8 - ((Pane) iv.getParent()).getChildren().size()) * (hgap + leftMargin));
            tt.setByY(tokenPos);
            tt.play();
        }
    }

    public void humanPlacing(boolean b) {
        humanPlacing = b;
    }

    Task<Void> task = new Task<>() {
        protected Void call() throws Exception {
            Platform.runLater(new Runnable() {
                public void run() {
                    place(botToken);
//                    c4.notify();
//                    int botSlot = c4.letBotPlay();
  //                  placeToken(botSlot);
                }
            });
        return null;
        }
    };
}
