import boardgame.model.*;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameModelTest {

    @Test
    public void testGetPieceCount() {
        BoardGameModel bgm = new BoardGameModel();
        assertEquals(7, bgm.getBluePieceCount());
        assertEquals(7, bgm.getRedPieceCount());
    }

    @Test
    public void testGetPieceType() {
        BoardGameModel bgm = new BoardGameModel();
        assertEquals(PieceType.RED, bgm.getRedPieceType(0));
        assertEquals(PieceType.BLUE, bgm.getBluePieceType(0));
    }

    @Test
    public void testValidMove() {
        BoardGameModel bgm = new BoardGameModel();
        assertTrue(bgm.isValidRedMove(0, RedDirection.DOWN));
        assertFalse(bgm.isValidRedMove(0, RedDirection.DOWN_LEFT));
        assertTrue(bgm.isValidBlueMove(0, BlueDirection.UP));
        assertFalse(bgm.isValidBlueMove(0, BlueDirection.UP_LEFT));
    }

    @Test
    public void testGetPieceNumber() {
        BoardGameModel bgm = new BoardGameModel();
        assertEquals(3, bgm.getRedPieceNumber(new Position(0, 3)).getAsInt());
        assertEquals(3, bgm.getBluePieceNumber(new Position(bgm.BOARD_HEIGHT - 1, 3)).getAsInt());
    }

    @Test
    public void testGetAllValidMoves() {
        BoardGameModel bgm = new BoardGameModel();
        EnumSet<RedDirection> redMoves = EnumSet.allOf(RedDirection.class);
        EnumSet<BlueDirection> blueMoves = EnumSet.allOf(BlueDirection.class);

        assertEquals(redMoves, bgm.getAllRedValidMoves());
        assertEquals(blueMoves, bgm.getAllBlueValidMoves());
    }

    @Test
    public void testIsOnBoard() {
        BoardGameModel bgm = new BoardGameModel();
        for (int i = 0; i < bgm.BOARD_WIDTH; i++) {
            for (int j = 0; j < bgm.BOARD_HEIGHT; j++) {
                assertTrue(bgm.isOnBoard(new Position(j, i)));
            }
        }
        for (int i = 0; i < BoardGameModel.BOARD_WIDTH; i++) {
            assertFalse(BoardGameModel.isOnBoard(new Position(-1, i)));
            assertFalse(BoardGameModel.isOnBoard(new Position(BoardGameModel.BOARD_HEIGHT, i)));
        }
        for (int i = 0; i < BoardGameModel.BOARD_HEIGHT; i++) {
            assertFalse(BoardGameModel.isOnBoard(new Position(i, -1)));
            assertFalse(BoardGameModel.isOnBoard(new Position(i, BoardGameModel.BOARD_WIDTH)));
        }
    }
}