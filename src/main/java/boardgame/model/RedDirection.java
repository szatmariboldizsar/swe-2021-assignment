package boardgame.model;

public enum RedDirection implements Direction {
    /**
     * Contains directions of moves of Player2
     */

    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1);

    private int rowChange;
    private int colChange;

    private RedDirection(int rowChange, int colChange) {
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
    public static RedDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}