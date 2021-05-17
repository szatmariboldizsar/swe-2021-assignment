package boardgame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class EndController {

    @FXML
    private Text text;

    private String name;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            text.setText("The winner is " + name + "!");
        });
    }

    @FXML
    public void setName(String name) {
        this.name = name;
    }

    @FXML
    public void handleNewGameButton(ActionEvent event) throws IOException {
        Logger.info("New Game Started");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }
}
