package boardgame.model;

public enum BlueDirection implements Direction {
    /**
     * Contains directions of moves of Player1
     */

    UP_LEFT(-1, -1),
    UP(-1, 0),
    UP_RIGHT(-1, 1);

    private int rowChange;
    private int colChange;

    private BlueDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * Returns how many rows a direction changes
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * Returns how many columns a direction changes
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * Returns direction of given <code>rowChange</code> and <code>colChange</code>
     */
    public static BlueDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}