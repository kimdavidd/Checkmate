package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.Color;
import java.util.ArrayList;

import java.util.Random;

public class RandomWorldGenerator implements java.io.Serializable {
    private final int  WIDTH = Game.WIDTH;
    private final int HEIGHT = Game.HEIGHT;
    private final int USAGEPERCENTAGEUPPERBOUND = 60;
    private final int USAGEPERCENTAGELOWERBOUND = 50;
    static Random RANDOM;
    private TETile[][] world;
    int x;
    int y;
    boolean wall1Hit;
    boolean wall2Hit;
    boolean wall3Hit;
    boolean wall4Hit;
    private String orientation;
    int floors;
    private Player p;
    ArrayList<Enemy> enemies;
    private int beginningEnemies;
    private int randX;
    private int randY;

    /**
     * The Constructor for the RandomWorldGenerator.
     * @param world the world which will be generated
     * @param seed the seed for random
     */
    public RandomWorldGenerator(TETile[][] world, int seed) {
        RANDOM = new Random(seed);
        this.world = world;
        this.x = 0;
        this.y = 0;
        wall1Hit = false;
        wall2Hit = false;
        wall3Hit = false;
        wall4Hit = false;
        orientation = "none";
        floors = 0;
        enemies = new ArrayList<Enemy>();
        beginningEnemies = 2;

        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (Game.withKeyboard) {
                    this.world[i][j] = new TETile(' ', Color.red,
                            new Color(245 + randNum(10, 0), 110
                                    + randNum(50, 0), 0), "nothing");
                } else {
                    this.world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    /**
     * Creates the randomly generated world! Generates the hallways,
     * then generates the walls, then generates the rooms.
     */
    public void create() {
        generateHallways(0);
        int usageFactor = (int) ((WIDTH - 2) * (HEIGHT - 2)
                * (randNum(USAGEPERCENTAGEUPPERBOUND, USAGEPERCENTAGELOWERBOUND) / 100.0));
        generateRooms(usageFactor);
        generateWalls();
        spawnPlayer();
        for (int i = 0; i < beginningEnemies; i++) {
            spawnEnemies();
        }
        spawnKing();
    }

    private void spawnKing() {
        randLocation();
        enemies.add(new Enemy("king", world, randX, randY, this));
        randX = 0; // resetting
        randY = 0;
    }

    private void randLocation() {
        while (!world[randX][randY].equals(Tileset.FLOOR)) {
            randX = randNum(WIDTH - 2, 1);
            randY = randNum(HEIGHT - 2, 1);
        }
    }

    private void spawnEnemies() {
        randLocation();
        enemies.add(new Enemy("pawn", world, randX, randY, this));
        randX = 0; // resetting
        randY = 0;
    }

    private void spawnPlayer() {
        randX = randNum(WIDTH - 2, 1);
        randY = randNum(HEIGHT - 2, 1);

        randLocation();

        p = new Player(world, randX, randY, this);
    }

    /**
     * Generates the hallways randomly. West wall is 1, North is 2, East is 3, South is 4.
     * Continues to lay down hallways until every "wall" is hit at least once.
     */
    private void generateHallways(int usageFactor) {
        // int startWall = randNum(4, 1);
        int startOrientation = randNum(2, 1);
        if (startOrientation == 1) {
            orientation = "horizontal";
        } else if (startOrientation == 2) {
            orientation = "vertical";
        }

        x = randNum(WIDTH - 2, 1);
        y = randNum(HEIGHT - 2, 1);

        // replace if you want another method of generating hallways!
/*        if (startWall == 1) { // y is changing, x constant 0
            x = 1;
            y = randNum(HEIGHT - 2, 1);
            wall1Hit = true;
            // System.out.println("Got here1");
        }
        else if (startWall == 2) { // x is changing, y constant HEIGHT - 1
            x = randNum(WIDTH - 2, 1);
            y = HEIGHT - 2;
            wall2Hit = true;
            // System.out.println("Got here2");
        }
        else if (startWall == 3) { // y is changing, x constant WIDTH - 1
            x = WIDTH - 2;
            y = randNum(HEIGHT - 2, 1);
            wall3Hit = true;
            // System.out.println("Got here3");
        }
        else if (startWall == 4) { // x is changing, y constant 0
            x = randNum(WIDTH - 2, 1);
            y = 1;
            wall4Hit = true;
            // System.out.println("Got here4");
        }*/

        while (!wall1Hit || !wall2Hit  || !wall3Hit
                || !wall4Hit || floors < usageFactor) {
            placeSingleHallway();
        }
    }

    /**
     * Method that places a single hallway. RANDOMLY chooses the length of each hallway.
     * RANDOMLY chooses where the hallway will start (like could connect the
     * previous hallway from the middle of new hallway). Also RANDOMLY
     * chooses the x and y coordinates of the next hallway.
     */
    private void placeSingleHallway() {
        int length;
        if (orientation.equals("horizontal")) {
            length = randNum(WIDTH, 0);
        } else {
            length = randNum(HEIGHT, 0);
        }

        int startingAtLength = randNum(length, 0);

        if (orientation.equals("horizontal")) {
            int originalX = x;
            for (int i = startingAtLength; i < length; i++) {
                if (x + 2 >= WIDTH) {
                    wall3Hit = true;
                    break;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    floors++;
                    x++;
                }
            }
            int rightXPos = x;
            x = originalX;
            for (int i = 0; i < startingAtLength; i++) {
                if (x - 2 <= -1) {
                    wall1Hit = true;
                    break;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    floors++;
                    x--;
                }
            }
            int leftXPos = x;
            x = randNum(rightXPos, leftXPos);
        } else if (orientation.equals("vertical")) {
            int originalY = y;
            for (int i = startingAtLength; i < length; i++) {
                if (y + 2 >= HEIGHT) {
                    wall2Hit = true;
                    break;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    floors++;
                    y++;
                }
            }
            int topYPos = y;
            y = originalY;
            for (int i = 0; i < startingAtLength; i++) {
                if (y - 2 <= -1) {
                    wall4Hit = true;
                    break;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    floors++;
                    y--;
                }
            }
            int bottomYPos = y;
            y = randNum(topYPos, bottomYPos);
        }
        if (orientation.equals("horizontal")) {
            orientation = "vertical";
        } else {
            orientation = "horizontal";
        }
    }

    /**
     * Generates the walls! First checks the corners, then checks the border,
     * then checks every tile in the world that's not border or corner.
     */
    private void generateWalls() {
        if (world[0][1].equals(Tileset.FLOOR) || world[1][1].equals(Tileset.FLOOR)
                || world[1][0].equals(Tileset.FLOOR)) {
            world[0][0] = Tileset.WALL;
        }
        if (world[0][HEIGHT - 2].equals(Tileset.FLOOR) || world[1][HEIGHT - 1].equals(Tileset.FLOOR)
                || world[1][HEIGHT - 2].equals(Tileset.FLOOR)) {
            world[0][HEIGHT - 1] = Tileset.WALL;
        }
        if (world[WIDTH - 1][1].equals(Tileset.FLOOR) || world[WIDTH - 2][1].equals(Tileset.FLOOR)
                || world[WIDTH - 2][0].equals(Tileset.FLOOR)) {
            world[WIDTH - 1][0] = Tileset.WALL;
        }
        if (world[WIDTH - 1][HEIGHT - 2].equals(Tileset.FLOOR)
                || world[WIDTH - 2][HEIGHT - 2].equals(Tileset.FLOOR)
                || world[WIDTH - 2][HEIGHT - 1].equals(Tileset.FLOOR)) {
            world[WIDTH - 1][HEIGHT - 1] = Tileset.WALL;
        }

        for (int i = 1; i < WIDTH - 1; i += 1) {
            if (world[i - 1][0].equals(Tileset.FLOOR) || world[i - 1][1].equals(Tileset.FLOOR)
                    || world[i][1].equals(Tileset.FLOOR)
                    || world[i + 1][0].equals(Tileset.FLOOR)
                    || world[i + 1][1].equals(Tileset.FLOOR)) {
                world[i][0] = Tileset.WALL;
            }
            if (world[i - 1][HEIGHT - 1].equals(Tileset.FLOOR)
                    || world[i - 1][HEIGHT - 2].equals(Tileset.FLOOR)
                    || world[i][HEIGHT - 2].equals(Tileset.FLOOR)
                    || world[i + 1][HEIGHT - 1].equals(Tileset.FLOOR)
                    || world[i + 1][HEIGHT - 2].equals(Tileset.FLOOR)) {
                world[i][HEIGHT - 1] = Tileset.WALL;
            }
        }

        for (int i = 1; i < HEIGHT - 1; i += 1) {
            if (world[0][i - 1].equals(Tileset.FLOOR)
                    || world[1][i - 1].equals(Tileset.FLOOR) || world[1][i].equals(Tileset.FLOOR)
                    || world[1][i + 1].equals(Tileset.FLOOR)
                    || world[0][i + 1].equals(Tileset.FLOOR)) {
                world[0][i] = Tileset.WALL;
            }
            if (world[WIDTH - 1][i - 1].equals(Tileset.FLOOR)
                    || world[WIDTH - 2][i - 1].equals(Tileset.FLOOR)
                    || world[WIDTH - 2][i].equals(Tileset.FLOOR)
                    || world[WIDTH - 1][i + 1].equals(Tileset.FLOOR)
                    || world[WIDTH - 2][i + 1].equals(Tileset.FLOOR)) {
                world[WIDTH - 1][i] = Tileset.WALL;
            }
        }

        for (int i = 1; i < WIDTH - 1; i += 1) {
            for (int j = 1; j < HEIGHT - 1; j += 1) {
                if ((world[i - 1][j + 1].equals(Tileset.FLOOR)
                        || world[i - 1][j].equals(Tileset.FLOOR)
                        || world[i - 1][j - 1].equals(Tileset.FLOOR)
                        || world[i][j + 1].equals(Tileset.FLOOR)
                        || world[i][j - 1].equals(Tileset.FLOOR)
                        || world[i + 1][j + 1].equals(Tileset.FLOOR)
                        || world[i + 1][j].equals(Tileset.FLOOR)
                        || world[i + 1][j - 1].equals(Tileset.FLOOR))
                        && !world[i][j].equals(Tileset.FLOOR)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * Generates rooms aka more hallways that will become rooms when joined with other hallways
     * with a random usage factor.
     * @param usageFactor Number of tiles must be used
     */
    private void generateRooms(double usageFactor) {

        while (floors < usageFactor) {
            int roomWidth = randNum(5, 2);
            int roomHeight = randNum(5, 2);
            placeSingleRoom(roomWidth, roomHeight);
        }
    }

    /**
     * Method to place a single room. Makes sure all rooms are RANDOM and CONNECTED.
     * @param roomWidth width of room
     * @param roomHeight height of room
     */
    private void placeSingleRoom(int roomWidth, int roomHeight) {
        int randomX = randNum(WIDTH - 2, 1);
        int randomY = randNum(HEIGHT - 2, 1);

        while (randomX + roomWidth >= WIDTH - 1) {
            roomWidth -= 1;
        }
        while (randomY + roomHeight >= HEIGHT - 1) {
            roomHeight -= 1;
        }

        boolean connected = checkIfConnected(randomX - 1, randomY - 1,
                randomX + roomWidth, randomY + roomHeight);

        if (!connected) {
            boolean up = false;
            boolean down = false;
            boolean left = false;
            boolean right = false;
            for (int i = randomX; i < randomX + roomWidth; i++) {
                for (int j = randomY; j < randomY + roomHeight; j++) {
                    up = checkIfPathUp(i, j);
                    if (up) {
                        break;
                    }
                    down = checkIfPathDown(i, j);
                    if (down) {
                        break;
                    }
                    left = checkIfPathLeft(i, j);
                    if (left) {
                        break;
                    }
                    right = checkIfPathRight(i, j);
                    if (right) {
                        break;
                    }
                }
                if (up || down || right || left) {
                    break;
                }
            }
        }

        for (int i = randomX; i < randomX + roomWidth; i++) {
            // actually adding the room to the world
            if (i < WIDTH - 1) { // just checking again
                for (int j = randomY; j < randomY + roomHeight; j++) {
                    if (j < HEIGHT - 1) { // just checking again
                        world[i][j] = Tileset.FLOOR;
                        floors++;
                    }
                }
            }
        }
    }

    /**
     * If the added room is not connected (isolated),
     * checks to see if it can add a path up to connect room.
     * If yes, then adds path.
     * @param i x coord
     * @param j y coord
     * @return
     */
    private boolean checkIfPathUp(int i, int j) {
        int originalY = j;
        while (j != HEIGHT - 1 && world[i][j].description().equals("nothing")) {
            j++;
        }

        if (world[i][j].equals(Tileset.FLOOR)) {
            addPath(i, originalY, i, j);
            return true;
        }
        return false;
    }

    /**
     * If the added room is not connected (isolated),
     * checks to see if it can add a path down to connect room.
     * If yes, then adds path.
     * @param i x coord
     * @param j y coord
     * @return
     */
    private boolean checkIfPathDown(int i, int j) {
        int originalY = j;
        while (j != 0 && world[i][j].description().equals("nothing")) {
            j--;
        }

        if (world[i][j].equals(Tileset.FLOOR)) {
            addPath(i, originalY, i, j);
            return true;
        }
        return false;
    }

    /**
     * If the added room is not connected (isolated),
     * checks to see if it can add a path left to connect room.
     * If yes, then adds path.
     * @param i x coord
     * @param j y coord
     * @return
     */
    private boolean checkIfPathLeft(int i, int j) {
        int originalX = i;
        while (i != 0 && world[i][j].description().equals("nothing")) {
            i--;
        }

        if (world[i][j].equals(Tileset.FLOOR)) {
            addPath(originalX, j, i, j);
            return true;
        }
        return false;
    }

    /**
     * If the added room is not connected (isolated),
     * checks to see if it can add a path right to connect room.
     * If yes, then adds path.
     * @param i x coord
     * @param j y coord
     * @return
     */
    private boolean checkIfPathRight(int i, int j) {
        int originalX = i;
        while (i != WIDTH - 1 && world[i][j].description().equals("nothing")) {
            i++;
        }

        if (world[i][j].equals(Tileset.FLOOR)) {
            addPath(originalX, j, i, j);
            return true;
        }
        return false;
    }

    /**
     * Method to addPath to connect room with rest of world
     * @param xStart starting x coord (from room)
     * @param yStart starting y coord (from room)
     * @param xEnd ending x coord (either from room or rest of world)
     * @param yEnd ending y coord (either from room or rest of world)
     */
    private void addPath(int xStart, int yStart, int xEnd, int yEnd) {
        if (xStart != xEnd) { // then y is constant
            if (xStart > xEnd) {
                for (int i = xStart; i > xEnd; i--) {
                    world[i][yStart] = Tileset.FLOOR;
                    floors++;
                }
            } else {
                for (int i = xStart; i < xEnd; i++) {
                    world[i][yStart] = Tileset.FLOOR;
                    floors++;
                }
            }
        } else { // then x is constant
            if (yStart > yEnd) {
                for (int i = yStart; i > yEnd; i--) {
                    world[xStart][i] = Tileset.FLOOR;
                    floors++;
                }
            } else {
                for (int i = yStart; i < yEnd; i++) {
                    world[xStart][i] = Tileset.FLOOR;
                    floors++;
                }
            }
        }
    }

    /**
     * Method to check if room is connected to the rest of world by checking its boundaries
     * @param xStart bottom left corner x
     * @param yStart bottom left corner y
     * @param xEnd top right corner x
     * @param yEnd top right corner y
     * @return
     */
    private boolean checkIfConnected(int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (!((i == xStart && j == yStart) || (i == xEnd && j == yStart)
                        || (i == xEnd && j == yEnd) || (i == xStart && j == yEnd))) {
                    if (world[i][j].equals(Tileset.FLOOR)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gives a random number from upperBound to lowerBound inclusive
     * @param upperBound Highest random number can be
     * @param lowerBound lowest random number can be
     * @return r some random number
     */
    public static int randNum(int upperBound, int lowerBound) {
        // Inclusive aka random number can be upper or lower bound
        int r = RANDOM.nextInt((upperBound - lowerBound) + 1) + lowerBound;
        return r;
    }
}
