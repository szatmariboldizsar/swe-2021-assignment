package boardgame.model;

public record Position(int row, int col) {
    /**
     * Record class
     * Gives positions (rows and columns) to everything on the board
     */

    /**
     * Returns new Position in the given <code>direction</code>
     */
    public Position moveTo(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Returns a String of the position
     */
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}