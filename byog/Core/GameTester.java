package byog.Core;

import byog.TileEngine.TETile;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTester {

    @Test
    public void testSameWorld() {
        Game game = new Game();
        TETile[][] x = game.playWithInputString("n5197880843569031643s");
        TETile[][] y = TETile.copyOf(x);
        Game game2 = new Game();

        TETile[][] z = game.playWithInputString("n5197880843569031643s");

        assertArrayEquals(z, y);

    }

    @Test
    public void testDiff() {
        Game game = new Game();
        TETile[][] x = game.playWithInputString("n5197880843569031643s");
        TETile[][] y = TETile.copyOf(x);
        assertNotEquals(game.playWithInputString("n1234567890123s"), y);

        assertNotEquals(game.playWithInputString("n9056129480306637831s"),
                game.playWithInputString("n6364148889459864464s"));
    }

    @Test
    public void testSaveAndLoad() {
        Game game = new Game();
        TETile[][] x = game.playWithInputString("n123saaa:q");

        assertEquals(game.playWithInputString("l"), x);
        assertNotEquals(game.playWithInputString("la"), x);
    }

    @Test
    public void saveLoadAndCommands() {
        Game game = new Game();
        TETile[][] x = game.playWithInputString("n2838278388919144292ssaasawdwsdss");
        TETile[][] y = game.playWithInputString("n2838278388919144292ssaasawdwsd:q");
        y = game.playWithInputString("lss");

        assertEquals(x, y);
    }

}
