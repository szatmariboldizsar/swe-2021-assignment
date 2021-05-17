package boardgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class WelcomeController {
    @FXML
    private TextField P1nameField;

    @FXML
    private TextField P2nameField;

    @FXML
    private void initialize() {
        P1nameField.setText("Player1");
        P2nameField.setText("Player2");
    }

    public String getP1name() {
        return P1nameField.getText();
    }

    public String getP2name() {
        return P2nameField.getText();
    }

    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        Logger.info("Player 1 name entered: {}", P1nameField.getText());
        Logger.info("Player 2 name entered: {}", P2nameField.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = fxmlLoader.load();
        BoardGameController controller = fxmlLoader.<BoardGameController>getController();
        controller.setNames(P1nameField.getText(), P2nameField.getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
