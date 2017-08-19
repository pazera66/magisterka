package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main extends Application {

    private boolean isServer = false;

    private NetworkConnection connection = isServer ? createServer() : createClient();

    private TextArea messages = new TextArea();

    String inputFile = "C:\\mag\\Input\\test.pdf";
    String outputFile = "C:\\mag\\Output\\test2.pdf";
    byte[] fileArray;

    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        Button button = new Button();

        button.setText("Send file");
        button.setOnAction(event -> {
           Path file = Paths.get(inputFile);
            try {
                fileArray = Files.readAllBytes(file);
                connection.send(fileArray);
                wait(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        VBox root = new VBox(20, messages, input, button);
        root.setPrefSize(600,600);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Server createServer(){
        return new Server(55555, data -> {
            Path path = Paths.get(outputFile);
            try {
                Files.write(path, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {

        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
