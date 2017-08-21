package com.company;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXInit extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GUI g = new GUI();

    }
}
