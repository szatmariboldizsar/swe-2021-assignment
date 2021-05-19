package boardgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Piece {
    /**
     * Class of Piece objects
     * A Piece object has a type and a position
     */

    private PieceType type;
    private ObjectProperty<Position> position = new SimpleObjectProperty<Position>();

    /**
     * Constructor of Piece class
     */
    public Piece(PieceType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * Returns piece's type
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Returns piece's position
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * Sets piece's position based on the given <code>direction</code>
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * Returns a piece's Position object
     */
    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     * Returns a String of a piece
     */
    public String toString() {
        return type.toString() + position.get().toString();
    }
}
