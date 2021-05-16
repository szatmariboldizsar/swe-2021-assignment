package boardgame;

import boardgame.model.BlueDirection;
import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import boardgame.model.RedDirection;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class BoardGameController {

    private enum SelectionPhase {
        SELECT_FROM_RED,
        SELECT_TO_RED,
        SELECT_FROM_BLUE,
        SELECT_TO_BLUE;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM_RED -> SELECT_TO_RED;
                case SELECT_TO_RED -> SELECT_FROM_BLUE;
                case SELECT_FROM_BLUE -> SELECT_TO_BLUE;
                case SELECT_TO_BLUE -> SELECT_FROM_RED;
            };
        }
    }

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM_RED;

    private List<Position> selectablePositions = new ArrayList<>();
    private Position[] unselectablePositions;

    private Position selected;

    private BoardGameModel model = new BoardGameModel();

    @FXML
    private GridPane board;

    @FXML
    private void initialize() {
        System.out.println("initialize");
        createBoard();
        createPieces();
        setUnselectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void createBoard() {
        System.out.println("createBoard");
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }

    private StackPane createSquare(int i, int j) {
        System.out.println("createSquare");
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createPieces() {
        for (int i = 0; i < model.getRedPieceCount(); i++) {
            System.out.println("createPieces " + i);

            model.redPositionProperty(i).addListener(this::piecePositionChange);
            var redPiece = createPiece(Color.valueOf(model.getRedPieceType(i).name()));
            getSquare(model.getRedPiecePosition(i)).getChildren().add(redPiece);

            model.bluePositionProperty(i).addListener(this::piecePositionChange);
            var bluePiece = createPiece(Color.valueOf(model.getBluePieceType(i).name()));
            getSquare(model.getBluePiecePosition(i)).getChildren().add(bluePiece);
        }
    }

    private Circle createPiece(Color color) {
        var piece = new Circle(25);
        piece.setFill(color);
        return piece;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.info("Click on square {}", position);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) {
        switch (selectionPhase) {
            case SELECT_FROM_RED -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO_RED -> {
                if (selectablePositions.contains(position)) {
                    var pieceNumber = model.getRedPieceNumber(selected).getAsInt();
                    var direction = RedDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    Logger.info("Moving piece {} {}", pieceNumber, direction);
                    model.redMove(pieceNumber, direction);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
            }
            case SELECT_FROM_BLUE -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO_BLUE -> {
                if (selectablePositions.contains(position)) {
                    var pieceNumber = model.getBluePieceNumber(selected).getAsInt();
                    var direction = BlueDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    Logger.info("Moving piece {} {}", pieceNumber, direction);
                    model.blueMove(pieceNumber, direction);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
            }
        }
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
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
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM_RED -> selectablePositions.addAll(model.getRedPiecePositions());
            case SELECT_TO_RED -> {
                var pieceNumber = model.getRedPieceNumber(selected).getAsInt();
                for (var direction : model.getRedValidMoves(pieceNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
            case SELECT_FROM_BLUE -> selectablePositions.addAll(model.getBluePiecePositions());
            case SELECT_TO_BLUE -> {
                var pieceNumber = model.getBluePieceNumber(selected).getAsInt();
                for (var direction : model.getBlueValidMoves(pieceNumber)) {
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
        System.out.println("getSquare");
        testcase: for (var child : board.getChildren()) {
            System.out.println("getSquare " + child);
            if (GridPane.getRowIndex(child) == null) {
                System.out.println("child rowindex is null ");
                continue testcase;
            }
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
}