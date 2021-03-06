package boardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;

import java.util.*;

public class BoardGameModel {
    /**
     * Model (MVC) of 2.4. board game
     */

    /**
     * Constants, contain the size of the board
     */
    public static final int BOARD_HEIGHT = 6;
    public static final int BOARD_WIDTH = 7;

    /**
     * Contains each players pieces
     */
    private Piece[] redPieces;
    private Piece[] bluePieces;

    /**
     * Contains black tile positions
     */
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

    /**
     * Constructor of BoardGameModel
     * sets base values of <code>redPieces</code>, <code>bluePieces</code> and <code>unselectablePositions</code>
     */
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

    /**
     * Checks if given <code>pieces</code> can exist
     */
    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (!isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    /**
     * Checks if given <code>positions</code> can exist
     */
    private void checkPositions(Position[] positions) {
        ArrayList<Integer> starterRows = new ArrayList<>();
        starterRows.add(0);
        starterRows.add(BOARD_HEIGHT - 1);
        var seen = new HashSet<Position>();
        for (var position : positions) {
            if (!isOnBoard(position) || seen.contains(position) || starterRows.contains(position.row())) {
                throw new IllegalArgumentException();
            }
            seen.add(position);
        }
    }

    /**
     * Returns Player2 piece at given index
     */
    public Piece getRedPiece(int pieceNumber) {
        return redPieces[pieceNumber];
    }

    /**
     * Returns Player1 piece at given index
     */
    public Piece getBluePiece(int pieceNumber) {
        return bluePieces[pieceNumber];
    }

    /**
     * Returns Player2 piece count
     */
    public int getRedPieceCount() {
        return redPieces.length;
    }

    /**
     * Returns Player1 piece count
     */
    public int getBluePieceCount() {
        return bluePieces.length;
    }

    /**
     * Returns Player2 piece type
     */
    public PieceType getRedPieceType() {
        return PieceType.RED;
    }

    /**
     * Returns Player1 piece type
     */
    public PieceType getBluePieceType() {
        return PieceType.BLUE;
    }

    /**
     * Returns Player2 piece type
     */
    public PieceType getRedPieceType(int pieceNumber) {
        return redPieces[pieceNumber].getType();
    }

    /**
     * Returns Player1 piece type
     */
    public PieceType getBluePieceType(int pieceNumber) {
        return bluePieces[pieceNumber].getType();
    }

    /**
     * Returns Player2 piece position at given index
     */
    public Position getRedPiecePosition(int pieceNumber) {
        return redPieces[pieceNumber].getPosition();
    }

    /**
     * Returns Player1 piece position at given index
     */
    public Position getBluePiecePosition(int pieceNumber) {
        return bluePieces[pieceNumber].getPosition();
    }

    /**
     * Returns Player2 piece position property at given index
     */
    public ObjectProperty<Position> redPositionProperty(int pieceNumber) {
        return redPieces[pieceNumber].positionProperty();
    }

    /**
     * Returns Player1 piece position property at given index
     */
    public ObjectProperty<Position> bluePositionProperty(int pieceNumber) {

        return bluePieces[pieceNumber].positionProperty();
    }

    /**
     * Checks if a Player2 pieces <code>direction</code> is a valid move
     */
    public boolean isValidRedMove(int pieceNumber, RedDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= redPieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = redPieces[pieceNumber].getPosition().moveTo(direction);
        if (!isOnBoard(newPosition) || isUnselectable(newPosition)) {
            return false;
        }
        if (direction.equals(RedDirection.DOWN) && getBluePieceNumber(newPosition).isPresent()) {
            return false;
        }
        for (var piece : redPieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a Player1 pieces <code>direction</code> is a valid move
     */
    public boolean isValidBlueMove(int pieceNumber, BlueDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= bluePieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = bluePieces[pieceNumber].getPosition().moveTo(direction);
        if (!isOnBoard(newPosition) || isUnselectable(newPosition)) {
            return false;
        }
        if (direction.equals(BlueDirection.UP) && getRedPieceNumber(newPosition).isPresent()) {
            return false;
        }
        for (var piece : bluePieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a position is unselectable (black tile)
     */
    public boolean isUnselectable(Position position) {
        for (var unselectable : unselectablePositions) {
            if (position.equals(unselectable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all Player2 valid moves
     */
    public Set<RedDirection> getAllRedValidMoves() {
        EnumSet<RedDirection> allValidMoves = EnumSet.noneOf(RedDirection.class);
        for (var pieceNumber = 0; pieceNumber < redPieces.length; pieceNumber++) {
            allValidMoves.addAll(getRedValidMoves(pieceNumber));
            }
        return allValidMoves;
    }

    /**
     * Returns all Player1 valid moves
     */
    public Set<BlueDirection> getAllBlueValidMoves() {
        EnumSet<BlueDirection> allValidMoves = EnumSet.noneOf(BlueDirection.class);
        for (var pieceNumber = 0; pieceNumber < bluePieces.length; pieceNumber++) {
            allValidMoves.addAll(getBlueValidMoves(pieceNumber));
        }
        return allValidMoves;
    }

    /**
     * Returns all of a Player2 piece's valid moves
     */
    public Set<RedDirection> getRedValidMoves(int pieceNumber) {
        EnumSet<RedDirection> validMoves = EnumSet.noneOf(RedDirection.class);
        for (var direction : RedDirection.values()) {
            if (isValidRedMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Returns all of a Player1 piece's valid moves
     */
    public Set<BlueDirection> getBlueValidMoves(int pieceNumber) {
        EnumSet<BlueDirection> validMoves = EnumSet.noneOf(BlueDirection.class);
        for (var direction : BlueDirection.values()) {
            if (isValidBlueMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Moves a Player2 piece in the given <code>direction</code>.
     * If a Player1 piece is in the new position, it is removed from the game
     */
    public void redMove(int pieceNumber, RedDirection direction) {
        redPieces[pieceNumber].moveTo(direction);

        OptionalInt bluePieceIndex = getBluePieceNumber(getRedPiecePosition(pieceNumber));
        if (bluePieceIndex.isPresent()) {
            Piece[] tmpBluePieces = new Piece[getBluePieceCount() - 1];
            int j = 0;
            for (int i = 0; i < tmpBluePieces.length; i++) {
                if (bluePieces[j] == bluePieces[bluePieceIndex.getAsInt()]) j++;
                tmpBluePieces[i] = bluePieces[j];
                j++;
            }
            bluePieces = tmpBluePieces.clone();
        }
    }

    /**
     * Moves a Player1 piece in the given <code>direction</code>.
     * If a Player2 piece is in the new position, it is removed from the game
     */
    public void blueMove(int pieceNumber, BlueDirection direction) {
        bluePieces[pieceNumber].moveTo(direction);

        OptionalInt redPieceIndex = getRedPieceNumber(getBluePiecePosition(pieceNumber));
        if (redPieceIndex.isPresent()) {
            Piece[] tmpRedPieces = new Piece[getRedPieceCount() - 1];
            int j = 0;
            for (int i = 0; i < tmpRedPieces.length; i++) {
                if (redPieces[j] == redPieces[redPieceIndex.getAsInt()]) j++;
                tmpRedPieces[i] = redPieces[j];
                j++;
            }
            redPieces = tmpRedPieces.clone();
        }
    }

    /**
     * Checks if a <code>position</code> is located on the board
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_HEIGHT
                && 0 <= position.col() && position.col() < BOARD_WIDTH;
    }

    /**
     * Returns a list of the positions of ALL pieces
     */
    public List<Position> getAllPiecePositions() {
        List<Position> positions = new ArrayList<>(redPieces.length + bluePieces.length);

        positions.addAll(getRedPiecePositions());
        positions.addAll(getBluePiecePositions());

        return positions;
    }

    /**
     * Returns a list of the positions of Player2 pieces
     */
    public List<Position> getRedPiecePositions() {
        List<Position> positions = new ArrayList<>(redPieces.length);
        for (var piece : redPieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    /**
     * Returns a list of the positions of Player1 pieces
     */
    public List<Position> getBluePiecePositions() {
        List<Position> positions = new ArrayList<>(bluePieces.length);
        for (var piece : bluePieces) {
            positions.add(piece.getPosition());
        }
        return positions;
    }

    /**
     * Returns a list of the positions of not finished Player2 pieces
     */
    public List<Position> getNotFinishedRedPiecePositions() {
        List<Position> allPositions = getRedPiecePositions();
        List<Position> newPositions = new ArrayList<>();
        for (var position : allPositions) {
            if (position.row() != BOARD_HEIGHT - 1) {
                newPositions.add(position);
            }
        }
        return newPositions;
    }

    /**
     * Returns a list of the positions of not finished Player1 pieces
     */
    public List<Position> getNotFinishedBluePiecePositions() {
        List<Position> allPositions = getBluePiecePositions();
        List<Position> newPositions = new ArrayList<>();
        for (var position : allPositions) {
            if (position.row() != 0) {
                newPositions.add(position);
            }
        }
        return newPositions;
    }

    /**
     * Returns a Player2 piece's index at the given <code>position</code>
     */
    public OptionalInt getRedPieceNumber(Position position) {
        for (int i = 0; i < redPieces.length; i++) {
            if (redPieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Returns a Player1 piece's index at the given <code>position</code>
     */
    public OptionalInt getBluePieceNumber(Position position) {
        for (int i = 0; i < bluePieces.length; i++) {
            if (bluePieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Returns a String listing all pieces
     */
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
}