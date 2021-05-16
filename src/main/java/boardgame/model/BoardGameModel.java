package boardgame.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class BoardGameModel {

    public static int BOARD_HEIGHT = 6;
    public static int BOARD_WIDTH = 7;

    private Piece[] redPieces;
    private Piece[] bluePieces;
    public Position[] unselectablePositions;

    public BoardGameModel() {
        this(new Position[]{new Position(3, 2),
                        new Position(2, 4)},

                new Piece(PieceType.RED, new Position(0, 0)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 6)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 5)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 4)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 3)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 2)),
                new Piece(PieceType.RED, new Position(0, BOARD_WIDTH - 1)),

                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, 0)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 6)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 5)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 4)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 3)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 2)),
                new Piece(PieceType.BLUE, new Position(BOARD_HEIGHT - 1, BOARD_WIDTH - 1)));


    }

    public BoardGameModel(Position[] positions, Piece... pieces) {
        checkPieces(pieces);
        checkPositions(positions);
        this.unselectablePositions = positions.clone();
        Piece[] redPieces = new Piece[BOARD_WIDTH];
        Piece[] bluePieces = new Piece[BOARD_WIDTH];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            redPieces[i] = pieces[i];
            bluePieces[i] = pieces[i + BOARD_WIDTH];
        }
        this.redPieces = redPieces.clone();
        this.bluePieces = bluePieces.clone();
    }

    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (! isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    private void checkPositions(Position[] positions) {
        ArrayList<Integer> starterRows = new ArrayList<>();
        starterRows.add(0);
        starterRows.add(BOARD_HEIGHT - 1);
        var seen = new HashSet<Position>();
        for (var position : positions) {
            if (! isOnBoard(position) || seen.contains(position) || starterRows.contains(position.row())) {
                throw new IllegalArgumentException();
            }
            seen.add(position);
        }
    }

    public int getRedPieceCount() {
        return redPieces.length;
    }
    public int getBluePieceCount() {
        return bluePieces.length;
    }

    public PieceType getRedPieceType(int pieceNumber) {
        return redPieces[pieceNumber].getType();
    }
    public PieceType getBluePieceType(int pieceNumber) {
        return bluePieces[pieceNumber].getType();
    }

    public Position getRedPiecePosition(int pieceNumber) {
        return redPieces[pieceNumber].getPosition();
    }
    public Position getBluePiecePosition(int pieceNumber) {
        return bluePieces[pieceNumber].getPosition();
    }

    public ObjectProperty<Position> redPositionProperty(int pieceNumber) {
        return redPieces[pieceNumber].positionProperty();
    }
    public ObjectProperty<Position> bluePositionProperty(int pieceNumber) {
        return bluePieces[pieceNumber].positionProperty();
    }

    public boolean isValidRedMove(int pieceNumber, RedDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= redPieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = redPieces[pieceNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition) || isUnselectable(newPosition)) {
            return false;
        }
        for (var piece : redPieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidBlueMove(int pieceNumber, BlueDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= bluePieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = bluePieces[pieceNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition) || isUnselectable(newPosition)) {
            return false;
        }
        for (var piece : bluePieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUnselectable(Position position) {
        for (var unselectable : unselectablePositions) {
            if (position.equals(unselectable)) {
                return true;
            }
        }
        return false;
    }

    public Set<RedDirection> getRedValidMoves(int pieceNumber) {
        EnumSet<RedDirection> validMoves = EnumSet.noneOf(RedDirection.class);
        for (var direction : RedDirection.values()) {
            if (isValidRedMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    public Set<BlueDirection> getBlueValidMoves(int pieceNumber) {
        EnumSet<BlueDirection> validMoves = EnumSet.noneOf(BlueDirection.class);
        for (var direction : BlueDirection.values()) {
            if (isValidBlueMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    public void redMove(int pieceNumber, RedDirection direction) {
        redPieces[pieceNumber].moveTo(direction);
    }
    public void blueMove(int pieceNumber, BlueDirection direction) {
        bluePieces[pieceNumber].moveTo(direction);
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_HEIGHT
                && 0 <= position.col() && position.col() < BOARD_WIDTH;
    }

    public List<Position> getAllPiecePositions() {
        List<Position> positions = new ArrayList<>(redPieces.length + bluePieces.length);
        for (var piece : redPieces) {
            positions.add(piece.getPosition());
        }
        for (var piece : bluePieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public List<Position> getRedPiecePositions() {
        List<Position> positions = new ArrayList<>(redPieces.length);
        for (var piece : redPieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public List<Position> getBluePiecePositions() {
        List<Position> positions = new ArrayList<>(bluePieces.length);
        for (var piece : bluePieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    public OptionalInt getRedPieceNumber(Position position) {
        for (int i = 0; i < redPieces.length; i++) {
            if (redPieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public OptionalInt getBluePieceNumber(Position position) {
        for (int i = 0; i < bluePieces.length; i++) {
            if (bluePieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : redPieces) {
            joiner.add(piece.toString());
        }
        for (var piece : bluePieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        BoardGameModel model = new BoardGameModel();
        System.out.println(model);
    }
}