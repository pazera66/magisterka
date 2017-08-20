package simulator;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class GUI {

    private Stage stage;
    private Scene scene;
    private HBox hbox;
    private GridPane gridPane;
    private VBox vBox;

    GUI(){
        hbox = new HBox();

        gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));


        vBox = new VBox();

        hbox.getChildren().addAll(gridPane,vBox);

        scene = new Scene(hbox, 900, 900);
        stage = new Stage();
        stage.setScene(scene);
        //stage.show();
    }
}
