package boardgame;

import boardgame.model.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class BoardGameController {
    /**
     * Controller class of ui.fxml
     */

    private enum SelectionPhase {
        /**
         * Enum of the game stages
         * (1 turn for each player, 2 stages in both, selection of piece, then selection of direction)
         */
        SELECT_FROM_BLUE,
        SELECT_TO_BLUE,
        SELECT_FROM_RED,
        SELECT_TO_RED;


        public SelectionPhase alter() {
            /**
             * Returns the next game stage
             */
            return switch (this) {
                case SELECT_FROM_BLUE -> SELECT_TO_BLUE;
                case SELECT_TO_BLUE -> SELECT_FROM_RED;
                case SELECT_FROM_RED -> SELECT_TO_RED;
                case SELECT_TO_RED -> SELECT_FROM_BLUE;
            };
        }

        public SelectionPhase alterBack() {
            /**
             * Returns from direction selection to piece selection
             * Can't return from piece selection, so it throws RuntimeException
             */
            return switch (this) {
                case SELECT_TO_BLUE -> SELECT_FROM_BLUE;
                case SELECT_TO_RED -> SELECT_FROM_RED;
                case SELECT_FROM_BLUE -> throw new RuntimeException();
                case SELECT_FROM_RED -> throw new RuntimeException();
            };
        }
    }

    /**
     * Stores the current stage of the game
     */
    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM_BLUE;

    /**
     * Stores the selectable positions
     */
    private List<Position> selectablePositions = new ArrayList<>();

    /**
     * Stores the unselectable positions (black tiles)
     */
    private Position[] unselectablePositions;

    /**
     * Stores the currently selected piece's position
     */
    private Position selected;

    /**
     * Instance of BoardGameModel
     */
    private BoardGameModel model = new BoardGameModel();

    /**
     * Stores the name of the players,
     * the winner's name is assigned to winnerName at the end of the game
     */
    private String P1name;
    private String P2name;
    private String winnerName;

    @FXML
    private GridPane board;

    @FXML
    private void initialize() {
        /**
         * Sets up the base of the game on the application window
         */
        createBoard();
        createPieces();
        setUnselectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void createBoard() {
        /**
         * Creates the board using <code>createSquare()</code>
         */
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare(int i, int j) {
        /**
         * Creates and returns a square
         */
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createPieces() {
        /**
         * Creates each piece on the board using <code>createPiece(Color color)</code>
         */
        for (int i = 0; i < model.getRedPieceCount(); i++) {

            model.redPositionProperty(i).addListener(this::piecePositionChange);
            var redPiece = createPiece(Color.valueOf(model.getRedPieceType(i).name()));
            getSquare(model.getRedPiecePosition(i)).getChildren().add(redPiece);
        }

        for (int i = 0; i < model.getBluePieceCount(); i++) {
            model.bluePositionProperty(i).addListener(this::piecePositionChange);
            var bluePiece = createPiece(Color.valueOf(model.getBluePieceType(i).name()));
            getSquare(model.getBluePiecePosition(i)).getChildren().add(bluePiece);
        }
    }

    private Circle createPiece(Color color) {
        /**
         * Creates and returns a piece (Circle)
         * of given <code>color</code>
         */
        var piece = new Circle(35);
        piece.setFill(color);
        return piece;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        /**
         * Handles a given <code>MouseEvent</code>
         */
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.info("Click on square {}", position);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) {
        /**
         * Handles mouse click on squares for different selectionPhases
         */
        switch (selectionPhase) {
            case SELECT_FROM_BLUE:
            case SELECT_FROM_RED: {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                    Logger.info("Piece {} selected", model.getBluePieceNumber(selected).getAsInt());
                    /*TODO selection cancellation only works with wait, but throws IllegalMonitoringStateException on every click
                    try {
                        wait(0,1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }*/
                }
            }
            case SELECT_TO_BLUE: {
                if (model.getBluePieceNumber(selected).isPresent()) {
                    if (selectablePositions.contains(position)) {
                        var pieceNumber = model.getBluePieceNumber(selected).getAsInt();
                        var direction = BlueDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.info("Moving BLUE piece {} {}", pieceNumber, direction);
                        model.blueMove(pieceNumber, direction);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                    }/*else if (position.equals(model.getBluePiecePosition(model.getBluePieceNumber(selected).getAsInt()))) {
                            Logger.info("Back to piece selection");
                            deselectSelectedPosition();
                            backToPieceSelection();
                    }*/
                }
            }
            case SELECT_TO_RED: {
                if (model.getRedPieceNumber(selected).isPresent()) {
                    if (selectablePositions.contains(position) && model.getRedPieceNumber(selected).isPresent()) {
                        var pieceNumber = model.getRedPieceNumber(selected).getAsInt();
                        var direction = RedDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.info("Moving RED piece {} {}", pieceNumber, direction);
                        model.redMove(pieceNumber, direction);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                    }/*else if (position.equals(model.getRedPiecePosition(model.getRedPieceNumber(selected).getAsInt()))) {
                            Logger.info("Back to piece selection");
                            deselectSelectedPosition();
                            backToPieceSelection();
                    }*/
                }
            }
        }
    }

    private void alterSelectionPhase() {
        /**
         * Calls <code>alter()</code> to set the game up
         * for the next stage. Hides the old (<code>hideSelectablePositions()</code>),
         * selects the new (<code>setSelectablePositions()</code>) selectable positions
         * and displays them (<code>showSelectablePositions()</code>).
         * Calls <code>endGame()</code> if a player wins.
         */
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
        if (model.getAllBlueValidMoves().isEmpty()) {
            winnerName = P2name;
            endGame();
        }
        if (model.getAllRedValidMoves().isEmpty()) {
            winnerName = P1name;
            endGame();
        }
    }

    private void backToPieceSelection() {
        /**
         * Cancels move selection
         */
        selectionPhase = selectionPhase.alterBack();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }


    private void selectPosition(Position position) {
        /**
         * Sets <code>selected</code> as given <code>position</code>,
         * then displays it using <code>showSelectedPosition()</code>
         */
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    private void setUnselectablePositions() {
        unselectablePositions = model.unselectablePositions;
        for (var unselectable : unselectablePositions) {
            var square = getSquare(unselectable);
            square.getStyleClass().add("unselectable");
        }
    }

    private void setSelectablePositions() {
        /**
         * Selects all selectable positions for each stage,
         * stores them in <code>selectablePositions</code>
         */
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM_BLUE -> selectablePositions.addAll(model.getBluePiecePositions());
            case SELECT_TO_BLUE -> {
                var pieceNumber = model.getBluePieceNumber(selected).getAsInt();
                for (var direction : model.getBlueValidMoves(pieceNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
            case SELECT_FROM_RED -> selectablePositions.addAll(model.getRedPiecePositions());
            case SELECT_TO_RED -> {
                var pieceNumber = model.getRedPieceNumber(selected).getAsInt();
                for (var direction : model.getRedValidMoves(pieceNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    private StackPane getSquare(Position position) {
        /**
         * Returns a square at a given <code>position</code>
         */
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == null) continue;
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.info("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

    public void setNames(String name1, String name2) {
        /**
         * Setter of <code>P1name</code> and <code>P2name</code>
         */
        Logger.info("Setting names to {} & {}", name1, name2);
        this.P1name = name1;
        this.P2name = name2;
    }

    public void endGame() {
        /**
         * Sets up the application end window
         */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/end.fxml"));
        try {
            Parent root = fxmlLoader.load();
            EndController controller = fxmlLoader.<EndController>getController();
            controller.setNames(winnerName);
            Stage stage = (Stage) board.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}