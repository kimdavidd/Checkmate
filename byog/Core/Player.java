package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import edu.princeton.cs.introcs.StdAudio;

public class Player implements java.io.Serializable {
    private static int x;
    private static int y;
    private static TETile[][] world;
    private static RandomWorldGenerator rwg;

    public Player(TETile[][] world, int xStart, int yStart, RandomWorldGenerator rwg) {
        this.world = world;
        x = xStart;
        y = yStart;
        world[x][y] = Tileset.PLAYER;
        this.rwg = rwg;
    }

    public static int getXPosition() {
        return x;
    }

    public static int getYPosition() {
        return y;
    }

    public static void playMoveSound() {
        if (Game.withKeyboard) {
            StdAudio.play("/boop.wav");
        }
    }

    public static boolean up() {
        if (!checkIfWall(x, y + 1)) {
            world[x][y] = Tileset.FLOOR;
            y = y + 1;
            checkLost();
            world[x][y] = Tileset.PLAYERBACK;
            playMoveSound();
            return true;
        }
        return false;
    }

    public static boolean down() {
        if (!checkIfWall(x, y - 1)) {
            world[x][y] = Tileset.FLOOR;
            y = y - 1;
            checkLost();
            world[x][y] = Tileset.PLAYER;
            playMoveSound();
            return true;
        }
        return false;
    }

    public static boolean right() {
        if (!checkIfWall(x + 1, y)) {
            world[x][y] = Tileset.FLOOR;
            x = x + 1;
            checkLost();
            world[x][y] = Tileset.PLAYERRIGHT;
            playMoveSound();
            return true;
        }
        return false;
    }

    public static boolean left() {
        if (!checkIfWall(x - 1, y)) {
            world[x][y] = Tileset.FLOOR;
            x = x - 1;
            checkLost();
            world[x][y] = Tileset.PLAYERLEFT;
            playMoveSound();
            return true;
        }
        return false;
    }

    public static void checkLost() {
        boolean isEnemy = false;
        String enemy = "";
        if (world[x][y].description().equals("king")) {
            for (Enemy e : rwg.enemies) {
                if (!e.hostile) { // should only be false if King is vulnerable
                    win();
                    return;
                }
            }
        }
        for (Enemy e : rwg.enemies) {
            if (world[x][y].equals(e.getType())) {
                isEnemy = true;
                enemy = e.getDescription();
            }
        }
        if (isEnemy && Game.withKeyboard) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .75, "YOU'VE BEEN SLAIN BY " + enemy.toUpperCase());
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .5, "Press any key to quit.");
            StdDraw.show();
            Game.lost = true;
        } else if (isEnemy) {
            Game.lost = true;
        }
    }

    public static void win() {
        if (Game.withKeyboard) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .75, "YOU CHECKMATED THE ENEMY KING!");
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .5, "Press any key to quit.");
            StdDraw.show();
            Game.win = true;
        } else {
            Game.win = true;
        }
    }

    private static boolean checkIfWall(int X, int Y) {
        if (world[X][Y].equals(Tileset.WALL)) {
            return true;
        }
        return false;
    }
}
