package boardgame;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EndController {
    /**
     * Controller class of end.fxml
     */

    @FXML
    private Text text;

    /**
     * They contain the player names
     */
    private String winner;
    private String other;

    /**
     * When the scene window is set, the program serializes players.json database
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            try {
                updatePlayers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Setters for the names, also sets the FXML text
     */
    @FXML
    public void setNames(String name1, String name2) {
        this.winner = name1;
        this.other = name2;
        this.text.setText("The winner is " + winner + "!");
    }

    /**
     * Gets called when New Game button is pressed, launches a new game
     */
    @FXML
    private void handleNewGameButton(ActionEvent event) throws IOException {
        Logger.info("New Game Started");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Gets called when Exit button is pressed, terminates the program
     */
    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Serializes players.json database
     */
    private void updatePlayers() throws IOException {
        var mapper = new ObjectMapper();

        Player.players = new ArrayList<Player>(Arrays.asList(mapper.readValue(new FileReader("players.json"), Player[].class)));

        if (Player.getNames().contains(this.winner)) {
            for (var player : Player.players) {
                if (player.getName().equals(this.winner)) {
                    player.setWins(player.getWins() + 1);
                }
            }
        }
        else {
            new Player(this.winner, 1);
        }

        if (!Player.getNames().contains(this.other)) {
            new Player(this.other, 0);
        }

        try (var writer = new FileWriter("players.json")) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, Player.players);
        }
    }
}