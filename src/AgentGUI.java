import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AgentGUI extends Application {
    private static Agent agent;
    private static MainScene mainScene;
    private static Stage primaryStage;

    public static void changeScenes() {
        Thread t = new Thread(agent);
        t.start();

        primaryStage.setTitle(agent.getUsername() + "'s Agent");
        primaryStage.setScene(new Scene(mainScene, 400, 800));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        agent = new Agent();

        //Update the GUI
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
               updateGUI();
            }
        }; animationTimer.start();

        //Class to create the starting VBox to take username and userBalance
        TakeUsername takeUsername = new TakeUsername(agent);

        primaryStage.setTitle("Agent Class");
        primaryStage.setScene(new Scene(takeUsername, 340, 250));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {

            //TODO: Close the particular agent only and then exit
            System.exit(0);
        });
        primaryStage.show();
    }

    private void updateGUI() {
        //TODO: GUI initialization and updates


    }
}
