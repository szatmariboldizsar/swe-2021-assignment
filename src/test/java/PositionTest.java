import boardgame.model.BlueDirection;
import boardgame.model.Position;
import boardgame.model.RedDirection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testMoveTo() {
        var position = new Position(2, 3);
        assertEquals(position, new Position(1, 3).moveTo(RedDirection.DOWN));
        assertEquals(position, new Position(1, 4).moveTo(RedDirection.DOWN_LEFT));
        assertEquals(position, new Position(1, 2).moveTo(RedDirection.DOWN_RIGHT));
        assertEquals(position, new Position(3, 3).moveTo(BlueDirection.UP));
        assertEquals(position, new Position(3, 4).moveTo(BlueDirection.UP_LEFT));
        assertEquals(position, new Position(3, 2).moveTo(BlueDirection.UP_RIGHT));
    }
}
