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

    public Piece(PieceType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    public PieceType getType() {
        return type;
    }

    public Position getPosition() {
        return position.get();
    }

    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    public String toString() {
        return type.toString() + position.get().toString();
    }
}
