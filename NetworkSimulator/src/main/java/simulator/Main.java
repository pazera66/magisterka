package simulator;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final int NUMBER_OF_NODES = Settings.getNumber_Of_Clients();


    @Override
    public void start(Stage primaryStage) throws Exception{
        GUI gui = new GUI();
        Thread mainSimulationThread = new Thread(new MainSimulatorThread(gui));
        mainSimulationThread.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
