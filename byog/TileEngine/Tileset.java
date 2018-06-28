package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile PLAYER = new TETile('♔', Color.white, new Color(64, 64, 64), "player",
            "/Users/davidkim/Documents/cs61bImg/playerfront.png");
    public static final TETile PLAYERBACK = new TETile('♔', Color.white, new Color(64, 64, 64), "player",
            "/Users/davidkim/Documents/cs61bImg/playerback.png");
    public static final TETile PLAYERRIGHT = new TETile('♔', Color.white, new Color(64, 64, 64), "player",
            "/Users/davidkim/Documents/cs61bImg/playerright.png");
    public static final TETile PLAYERLEFT = new TETile('♔', Color.white, new Color(64, 64, 64), "player",
            "/Users/davidkim/Documents/cs61bImg/playerleft.png");
    public static final TETile WALL = new TETile('▩', new Color(183, 183, 183),
            new Color(51, 51, 51),
            "wall"); // originally new Color(216, 128, 128)
    public static final TETile FLOOR = new TETile('·', new Color(110, 110, 110),
            new Color(64, 64, 64),
            "floor");
    // was originally new Color(128, 192, 128)
    public static final TETile NOTHING = new TETile(' ', Color.red, new Color(230, 149, 0),
            "nothing", "/Users/davidkim/Documents/cs61bImg/lava.png");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile PAWN = new TETile('♟', Color.black, new Color(64, 64, 64), "pawn",
            "/Users/davidkim/Documents/cs61bImg/enemypawn.png");
    public static final TETile KING = new TETile('♚', Color.yellow, new Color(64, 64, 64), "king",
            "/Users/davidkim/Documents/cs61bImg/enemyking.png");
    public static final TETile KINGHOSTILE = new TETile('♚', Color.red, new Color(64, 64, 64), "king",
            "/Users/davidkim/Documents/cs61bImg/hostileking.png");
    public static final TETile QUEEN = new TETile('♛', Color.pink, new Color(64, 64, 64), "queen",
            "/Users/davidkim/Documents/cs61bImg/enemyqueen.png");
    public static final TETile BISHOP = new TETile('♝', Color.black, new Color(64, 64, 64), "bishop",
            "/Users/davidkim/Documents/cs61bImg/enemybishop.png");
    public static final TETile ROOK = new TETile('♜', Color.black, new Color(64, 64, 64), "rook",
            "/Users/davidkim/Documents/cs61bImg/enemyrook.png");
    public static final TETile KNIGHT = new TETile('♞', Color.black, new Color(64, 64, 64), "knight",
            "/Users/davidkim/Documents/cs61bImg/enemyknight.png");

}

