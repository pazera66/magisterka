package simulator;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class GUI {

    private Stage stage;
    private Scene scene;
    private HBox hbox;
    private GridPane gridPane;
    private VBox vBox;
    private Label label;

    GUI(){
        hbox = new HBox();

        gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));


        vBox = new VBox();

        label = new Label();
        label.setText("AAAAAAAAAAAAAAAAAAAAA");

        hbox.getChildren().addAll(gridPane,vBox,label);

        scene = new Scene(hbox, 900, 900);
        stage = new Stage();
        stage.setScene(scene);
        //stage.show();
    }
}
