package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class communicatorWindowGUI {

    public Stage stage;
    public Scene scene;
    public Button button;
    public Button ExitButton;
    public TextField msg;
    TextArea receivedMsg;

    public communicatorWindowGUI(String windowName){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 300, 275);

        Text sceneTitle = new Text(windowName);
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2 ,1);

        Label userName = new Label("Received messages:");
        grid.add(userName, 0, 1, 2,1);

        receivedMsg = new TextArea();
        receivedMsg.setWrapText(true);
        GridPane.setFillWidth(receivedMsg, true);
        receivedMsg.setMaxWidth(Double.MAX_VALUE);
        grid.add(receivedMsg, 0, 2, 4, 4);

        Label pw = new Label("Write message:");
        grid.add(pw, 0, 6);


        msg = new TextField();
        grid.add(msg, 0, 7, 2,3);

        button = new Button();
        button.setText("Send message");
        grid.add(button, 1,10);

        ExitButton = new Button();
        ExitButton.setText("Close connection");
        grid.add(ExitButton, 1,12);

        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void sendMessage(String msg){

    }
}
