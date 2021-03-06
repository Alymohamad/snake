package application;

import game.GameObject;
import game.Gameboard;
import game.Score;
import game.Snake;
import controller.Control;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;

public class GameLoop extends Application {

    static File splashFile = new File("src/media/splash.mp4");
    static Media splashMedia = new Media(splashFile.toURI().toString());
    static MediaPlayer splashPlayer = new MediaPlayer(splashMedia);
    static MediaView splashView = new MediaView(splashPlayer);
    static File ingamemusicFile = new File("src/media/sound/music/ingame2.mp3");
    static Media ingamemusicMedia = new Media(ingamemusicFile.toURI().toString());
    static MediaPlayer ingamemusicPlayer = new MediaPlayer(ingamemusicMedia);
    static File gameovermusicFile = new File("src/media/sound/music/gameover1.mp3");
    static Media gameovermusicMedia = new Media(gameovermusicFile.toURI().toString());
    static MediaPlayer gameovermusicPlayer = new MediaPlayer(gameovermusicMedia);
    static File eatsoundFile = new File("src/media/sound/eat2.mp3");
    static Media eatsoundMedia = new Media(eatsoundFile.toURI().toString());
    static MediaPlayer eatsoundPlayer = new MediaPlayer(eatsoundMedia);
    static File deathsoundFile = new File("src/media/sound/death1.mp3");
    static Media deathsoundMedia = new Media(deathsoundFile.toURI().toString());
    static MediaPlayer deathsoundPlayer = new MediaPlayer(deathsoundMedia);
    Group root = new Group();
    Pane backgroundPane = new Pane();
    Group splashscreen = new Group();
    //Background stuff
    Image imgSource;
    BackgroundImage backgroundImage;
    Background backgroundView;
    private long lastUpdate = 0; //für Geschwindigkeitssteuerung

    private static final int WIDTH = 1500;
    private static final int HEIGHT = 700;
    private static final int MIN_HEIGHT = 50;
    private static final int MIN_WIDTH = 50;
    private static final String URL = "media/grassTile.png";



    public static void restartIngamemusic() { //Startet Ingame Musik von vorne
        ingamemusicPlayer.seek(Duration.ZERO);
        ingamemusicPlayer.play();
    }

    public static void stopIngamemusic() {
        ingamemusicPlayer.stop();
    }

    public static void restartGameovermusic() {
        gameovermusicPlayer.seek(Duration.ZERO);
        gameovermusicPlayer.play();
    }

    public static void stopGameovermusic() {
        gameovermusicPlayer.stop();
    }

    public static void playEatsound() {
        eatsoundPlayer.seek(Duration.ZERO);
        eatsoundPlayer.play();
    }

    public static void playDeathsound() {
        deathsoundPlayer.seek(Duration.ZERO);
        deathsoundPlayer.play();
    }
    //END Background

    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        AnimationTimer timer;

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        //Background stuff
        imgSource = new Image(URL);
        backgroundImage = new BackgroundImage(imgSource, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        backgroundView = new Background(backgroundImage);
        backgroundPane.setBackground(backgroundView);
        //END Background


        int offset = 21;
        Gameboard gameboard = new Gameboard();
        Control control = new Control();
        Snake snake = new Snake(root, primaryStage); //erstellt neues Snake Listen Objekt und getChilded es
        GameObject food = new GameObject();
        Score score = new Score(root);
        food.setFood(root, primaryStage);//setzt ein neues Food random ab
        Scene scene = new Scene(backgroundPane, primaryStage.getWidth(), primaryStage.getHeight(), Color.DARKGREEN);
        backgroundPane.getChildren().add(root);

        Rectangle blackrect = new Rectangle();  //Schwarzer Block der für eine Szenentransition missbraucht wird
        blackrect.setFill(Color.BLACK);
        blackrect.setHeight(primaryStage.getHeight());
        blackrect.setWidth(primaryStage.getWidth());
        FadeTransition fadeblacktotransparent = new FadeTransition(Duration.millis(700), blackrect);
        fadeblacktotransparent.setFromValue(1.0);
        fadeblacktotransparent.setToValue(0.0);
        root.getChildren().add(blackrect);

        Scene intro = new Scene(splashscreen, primaryStage.getWidth(), primaryStage.getHeight());
        splashscreen.getChildren().add(splashView);
        splashView.setFitHeight(500);
        splashView.setFitWidth(1000);
        intro.setFill(Color.BLACK);
        splashView.setX(400);
        splashView.setY(100);
        primaryStage.setScene(intro);
        primaryStage.setTitle("Rainbow Snake");
        primaryStage.show();
        splashPlayer.play();

        ingamemusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        //Keyeventhandler fragt ab obs ein Keyevent gibt
        scene.setOnKeyPressed(keyEvent ->
            control.keyHandler(keyEvent, snake, root, food, score, primaryStage));//control nimmt Keyevent und schaut speziell nach WASD



        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (now - lastUpdate >= snake.getframeDelay()) {

                    int dx = 0;
                    int dy = 0;

                    snake.collision(food, root, food.getBound(), score, control, primaryStage, gameboard);

                    if (control.getgoUp()) dy += -offset; //offset="speed"
                    else if (control.getgoDown()) dy += offset;
                    else if (control.getgoRight()) dx += offset;
                    else if (control.getgoLeft()) dx += -offset;
                    snake.moveSnake(dx, dy);

                    lastUpdate = now;

                }
            }
        };
        splashPlayer.setOnEndOfMedia(() -> {
            primaryStage.setScene(scene);
            fadeblacktotransparent.play();
            timer.start(); //Animationtimer startet nun erst nach dem Fade out des Hundevideos
            restartIngamemusic();
        });

    }


}
