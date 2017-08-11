package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    private Stage primaryStage;

    public FileController(Stage primaryStage){
        this.primaryStage = primaryStage;
        init();
    }

    private void init(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File: ");
        Button button1 = new Button();
        button1.setText("Hello World!");

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Witam!!!");
                File file = fileChooser.showOpenDialog(primaryStage);
                try {
                    LoadFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(button1);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public void LoadFile(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> list = new ArrayList<>();
        while (br.readLine() != null){
            list.add(br.readLine());
        }

        for (String line:list) {
            System.out.println(line);
        }
    }
}
