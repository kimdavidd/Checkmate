package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.awt.Color;
import java.awt.Font;
import edu.princeton.cs.introcs.StdAudio;



public class Game implements java.io.Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 105;
    public static final int HEIGHT = 55;
    private final int canvasWidth = 40;
    private final int canvasHeight = 40;
    private Character[] nums = new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private TETile[][] finalWorldFrame;
    RandomWorldGenerator world;
    int step = 1;
    String seed = "";
    static boolean withKeyboard = false;
    static boolean lost = false;
    static boolean win = false;
    private int randX = 0;
    private int randY = 0;


    /**
     * For testing purposes
     * @param args
     */
    /*public static void main(String[] args) {
        Game game = new Game();
        game.playWithInputString("n5197880843569031643s");
        StdDraw.pause(10000);
        game.playWithInputString("n5197880843569031643s");
    }*/

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        if (step == 1) {
            startGame();
        }
        int offset = 0;

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char keyTyped = StdDraw.nextKeyTyped();
                keyTyped = Character.toLowerCase(keyTyped);
                if (keyTyped == 'n' && step == 1) {
                    step++; // become step 2
                    seed += Character.toString(keyTyped);
                    promptSeed();
                } else if (keyTyped == 'l' && step == 1) {
                    load();
                } else if (keyTyped == 'q' && step == 1) {
                    seed += Character.toString(keyTyped);
                    System.exit(0);
                } else if (keyTyped == 'h' && step == 1) {
                    helpScreen();
                } else if (keyTyped == 's' && step == 2) {
                    step++; // become step 3
                    seed += Character.toString(keyTyped);
                    playWithInputString(seed);
                } else if (step == 2) {
                    for (char n : nums) {
                        if (n == keyTyped) {
                            seed += Character.toString(keyTyped);
                            if (offset <= canvasWidth - (canvasWidth / 2.3)) {
                                StdDraw.textLeft(canvasWidth / 5 + offset,
                                        canvasHeight / 2, Character.toString(keyTyped));
                                offset += 2;
                                StdDraw.show();
                            }
                        }
                    }
                } else if (step == 3) {
                    withKeyboardMove(keyTyped);
                    if (keyTyped == ':') {
                        step++;
                    }
                    if (lost) {
                        save(seed.substring(0, seed.length() - 1));
                        lost = false;
                        Game.withKeyboard = false;
                        // resets in case runs with keyboard first then without
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                System.exit(0);
                            }
                        }
                    }
                    if (win) {
                        save(seed.substring(0, seed.length() - 1));
                        win = false;
                        Game.withKeyboard = false;
                        // resets in case runs with keyboard first then without
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                System.exit(0);
                            }
                        }
                    }
                    ter.renderFrame(finalWorldFrame);
                } else if (step == 4) { // only activated if : is pressed
                    if (keyTyped == 'q') {
                        save(seed);
                        System.exit(0);
                    } else {
                        step--;
                    }
                }
            }
            if (step == 3) {
                hud();
            }
        }
    }

    private void startGame() {
        loadStartScreen();
        StdAudio.loop("/bg.wav");
        withKeyboard = true;
    }

    private void helpScreen() {
        loadHelpScreen();
        boolean inHelp = true;
        while (inHelp) {
            if (StdDraw.hasNextKeyTyped()) {
                char keyTyped = StdDraw.nextKeyTyped();
                keyTyped = Character.toLowerCase(keyTyped);
                if (keyTyped == 'n') {
                    loadCharacterScreen();
                } else if (keyTyped == 'b') {
                    inHelp = false;
                    loadStartScreen();
                }
            }
        }
    }

    private void loadCharacterScreen() {
        StdDraw.clear(new Color(51, 51, 51));
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(canvasWidth / 2, canvasHeight * .90, "Chracters:");
        font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.textLeft(canvasWidth * .25, canvasHeight * .83, "This is you: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .83,
                "/Users/davidkim/Documents/cs61bImg/playerfront.png");
        StdDraw.text(canvasWidth * .5, canvasHeight * .75,
                "Enemies (They move like Chess pieces!):");

        StdDraw.text(canvasWidth * .5, canvasHeight * .70, "Enemy King (Hostile on right): ");
        StdDraw.picture(canvasWidth * .40, canvasHeight * .65,
                "/Users/davidkim/Documents/cs61bImg/enemyking.png");
        StdDraw.picture(canvasWidth * .60, canvasHeight * .65,
                "/Users/davidkim/Documents/cs61bImg/hostileking.png");

        StdDraw.text(canvasWidth * .50, canvasHeight * .60, "Pawn: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .55,
                "/Users/davidkim/Documents/cs61bImg/enemypawn.png");

        StdDraw.text(canvasWidth * .50, canvasHeight * .50, "Rook: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .45,
                "/Users/davidkim/Documents/cs61bImg/enemyrook.png");

        StdDraw.text(canvasWidth * .50, canvasHeight * .40, "Knight: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .35,
                "/Users/davidkim/Documents/cs61bImg/enemyknight.png");

        StdDraw.text(canvasWidth * .50, canvasHeight * .30, "Bishop: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .25,
                "/Users/davidkim/Documents/cs61bImg/enemybishop.png");

        StdDraw.text(canvasWidth * .50, canvasHeight * .20, "Queen: ");
        StdDraw.picture(canvasWidth / 2, canvasHeight * .15,
                "/Users/davidkim/Documents/cs61bImg/enemyqueen.png");

        StdDraw.text(canvasWidth * .5, canvasHeight * .10, "Back to Menu (B)");
        StdDraw.show();
    }

    private void withKeyboardMove(char keyTyped) {
        boolean playerMoved;
        if (keyTyped == 'w') {
            playerMoved = Player.up();
            if (playerMoved) {
                enemyTurn();
            }
            seed += Character.toString(keyTyped);
        } else if (keyTyped == 's') {
            playerMoved = Player.down();
            if (playerMoved) {
                enemyTurn();
            }
            seed += Character.toString(keyTyped);
        } else if (keyTyped == 'a') {
            playerMoved = Player.left();
            if (playerMoved) {
                enemyTurn();
            }
            seed += Character.toString(keyTyped);
        } else if (keyTyped == 'd') {
            playerMoved = Player.right();
            if (playerMoved) {
                enemyTurn();
            }
            seed += Character.toString(keyTyped);
        }
    }

    private void enemyTurn() {
        for (Enemy e : world.enemies) {
            e.takeTurn();
        }
        spawnEnemies();
    }

    private void spawnEnemies() {
        int randN = RandomWorldGenerator.randNum(3, 1);
        if (randN == 1) {
            generateRandomLocation();
            world.enemies.add(new Enemy("pawn", finalWorldFrame, randX, randY, world));
        }
        randN = RandomWorldGenerator.randNum(12, 1);
        if (randN == 1) {
            generateRandomLocation();
            world.enemies.add(new Enemy("knight", finalWorldFrame, randX, randY, world));
        }
        randN = RandomWorldGenerator.randNum(15, 1);
        if (randN == 1) {
            generateRandomLocation();
            world.enemies.add(new Enemy("bishop", finalWorldFrame, randX, randY, world));
        }
        randN = RandomWorldGenerator.randNum(17, 1);
        if (randN == 1) {
            generateRandomLocation();
            world.enemies.add(new Enemy("rook", finalWorldFrame, randX, randY, world));
        }
        if (world.enemies.size() == 16) {
            generateRandomLocation();
            world.enemies.add(new Enemy("queen", finalWorldFrame, randX, randY, world));
        }
        randX = 0; // resetting
        randY = 0;
    }

    private void generateRandomLocation() {
        while (!finalWorldFrame[randX][randY].equals(Tileset.FLOOR)) {
            randX = RandomWorldGenerator.randNum(WIDTH - 2, 1);
            randY = RandomWorldGenerator.randNum(HEIGHT - 2, 1);
        }
    }

    /**
     * Method to load saved game
     */
    private void load() {
        Seed s;
        try {
            FileInputStream fileIn = new FileInputStream("seed.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            s = (Seed) in.readObject();
            s.load(this);
            playWithInputString(s.getSeed());
            if (!lost) {
                step = 3;
            }
            /*if (withKeyboard) {
                playWithKeyboard();
            }*/
            in.close();
            fileIn.close();
        } catch (IOException i) {
            if (withKeyboard) {
                System.exit(0);
            }
            return;
            //i.printStackTrace();
        } catch (ClassNotFoundException c) {
            if (withKeyboard) {
                System.exit(0);
            }
            return;
            //System.out.println("Seed class not found");
            //c.printStackTrace();
        }
    }

    /**
     * @source https://www.tutorialspoint.com/java/java_serialization.htm
     */
    private TETile[][] save(String se) {
        try {
            FileOutputStream fileOut = new FileOutputStream("seed.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            Seed s = new Seed(se, withKeyboard);
            out.writeObject(s);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        // System.out.println(seed);
        /*System.out.println("Saved x: " + Player.getXPosition());
        System.out.println("Saved y: " + Player.getYPosition());
        System.out.println("Save Step: " + step);*/
        return finalWorldFrame;
    }

    /**
     * Method that updates the hud
     */
    private void hud() {
        double xMouse = StdDraw.mouseX();
        double yMouse = StdDraw.mouseY();
        String tile = "";
        StdDraw.setPenColor(Color.WHITE);
        if (xMouse < WIDTH && yMouse < HEIGHT) {
            if (finalWorldFrame[(int) xMouse][(int) yMouse].equals(Tileset.WALL)) {
                tile = "Wall";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].equals(Tileset.FLOOR)) {
                tile = "Floor";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].description().equals("player")) {
                tile = "Player";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].description().equals("pawn")) {
                tile = "Enemy Pawn";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].
                    description().equals("bishop")) {
                tile = "Enemy Bishop";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].
                    description().equals("knight")) {
                tile = "Enemy Knight";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].
                    description().equals("rook")) {
                tile = "Enemy Rook";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].
                    description().equals("queen")) {
                tile = "Enemy Queen";
            } else if (finalWorldFrame[(int) xMouse][(int) yMouse].
                    description().equals("king")) {
                tile = "Enemy King";
            }
        }
        StdDraw.textLeft(WIDTH / 80, HEIGHT + 1.15, tile);
        StdDraw.show();
        ter.renderFrame(finalWorldFrame);
    }

    /**
     * Method that opens the enter seed menu
     */
    private void promptSeed() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(canvasWidth / 2, canvasHeight * .75, "Enter a Seed");
        StdDraw.show();
    }

    private void loadHelpScreen() {
        StdDraw.clear(new Color(51, 51, 51));
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(canvasWidth / 2, canvasHeight * .90, "Welcome to a RPG inspired by Chess!");
        font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(canvasWidth / 2, canvasHeight * .80,
                "You are a lone hero at war against the evil kingdom of Stanfordalot.");
        StdDraw.text(canvasWidth / 2, canvasHeight * .75,
                "You have successfully infiltrated their castle");
        StdDraw.text(canvasWidth / 2, canvasHeight * .70,
                "and on the hunt for their king!");
        StdDraw.text(canvasWidth / 2, canvasHeight * .65,
                "However, the guards know that you are here,");
        StdDraw.text(canvasWidth / 2, canvasHeight * .60,
                "and every step you take, more guards appear.");
        StdDraw.text(canvasWidth / 2, canvasHeight * .55,
                "If you bump into any enemy or they bump into you, "
                        + "they will alert the");
        StdDraw.text(canvasWidth / 2, canvasHeight * .50,
                "rest of their army and overpower you!");
        StdDraw.text(canvasWidth / 2, canvasHeight * .45,
                "Your goal is to avoid all enemies and eliminate the evil king.");
        StdDraw.text(canvasWidth / 2, canvasHeight * .40,
                "Watch out, while the king is most of the time vulnerable,");
        StdDraw.text(canvasWidth / 2, canvasHeight * .35,
                "sometimes he becomes hostile and can slay you instead.");
        StdDraw.text(canvasWidth / 2, canvasHeight * .20, "Next Page (N)");
        StdDraw.text(canvasWidth / 2, canvasHeight * .15, "Back to Menu (B)");
        StdDraw.show();
    }

    /**
     * Method that opens the main menu
     */
    private void loadStartScreen() {
        StdDraw.setCanvasSize(canvasWidth * 16, canvasHeight * 16);
        Font font = new Font("Impact", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, canvasWidth);
        StdDraw.setYscale(0, canvasHeight);
        StdDraw.clear(new Color(51, 51, 51));
        StdDraw.picture(canvasWidth / 2, canvasHeight / 2,
                "/Users/davidkim/Documents/cs61bImg/bgmain.png");
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(canvasWidth / 2, canvasHeight * .75, "♚ Checkmate ♛");
        font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(canvasWidth / 2, canvasHeight * .45, "New Game (N)");
        StdDraw.text(canvasWidth / 2, canvasHeight * .40, "Load Game (L)");
        StdDraw.text(canvasWidth / 2, canvasHeight * .35, "How to Play (H)");
        StdDraw.text(canvasWidth / 2, canvasHeight * .30, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        // Although it seems like redundant code as other play method,
        // there are EXTREMELY important little details that
        // can't be simplified.
        String seedNumber = "";
        String inputSeed = "";
        if (withKeyboard) {
            step = 1;
        }
        for (int i = 0; i < input.length(); i++) {
            char keyTyped = input.charAt(i);
            keyTyped = Character.toLowerCase(keyTyped);

            if (keyTyped == 'n' && step == 1) {
                step++; // become step 2
                inputSeed += Character.toString(keyTyped);
            } else if (keyTyped == 'l' && step == 1) {
                load();
            } else if (keyTyped == 'q' && step == 1) {
                System.exit(0);
            } else if (keyTyped == 's' && step == 2) {
                step++; // become step 3
                inputSeed += Character.toString(keyTyped);
                createWorld(seedNumber);
            } else if (step == 2) {
                for (char n : nums) {
                    if (n == keyTyped) {
                        inputSeed += Character.toString(keyTyped);
                        seedNumber += Character.toString(keyTyped);
                    }
                }
            } else if (step == 3) {
                inputSeed = inputWithStringMove(keyTyped, inputSeed);
                if (keyTyped == ':') {
                    step++;
                }
                if (lost) {
                    step = 1;
                    lost = false;
                    Game.withKeyboard = false;
                    // resets in case runs with keyboard first then without
                    return save(seed + inputSeed.substring(0, inputSeed.length() - 1));
                }
                if (win) {
                    step = 1;
                    win = false;
                    Game.withKeyboard = false;
                    // resets in case runs with keyboard first then without
                    return save(seed + inputSeed.substring(0, inputSeed.length() - 1));
                }
                if (withKeyboard) {
                    render();
                }
            } else if (step == 4) { // only activated if : is pressed
                if (keyTyped == 'q') {
                    step = 1; // was step--;
                    return save(seed + inputSeed);
                } else {
                    step--;
                }
            }
        }
        // System.out.println(finalWorldFrame);
        if (!withKeyboard) {
            step = 1; // reset step to use playWithInputString again
        }
        return finalWorldFrame;
    }

    private String inputWithStringMove(char keyTyped, String inputSeed) {
        boolean playerMoved;
        if (keyTyped == 'w') {
            playerMoved = Player.up();
            if (playerMoved) {
                enemyTurn();
            }
            inputSeed += Character.toString(keyTyped);
        } else if (keyTyped == 's') {
            playerMoved = Player.down();
            if (playerMoved) {
                enemyTurn();
            }
            inputSeed += Character.toString(keyTyped);
        } else if (keyTyped == 'a') {
            playerMoved = Player.left();
            if (playerMoved) {
                enemyTurn();
            }
            inputSeed += Character.toString(keyTyped);
        } else if (keyTyped == 'd') {
            playerMoved = Player.right();
            if (playerMoved) {
                enemyTurn();
            }
            inputSeed += Character.toString(keyTyped);
        }
        return inputSeed;
    }

    private void render() {
        ter.renderFrame(finalWorldFrame);
    }

    /**
     * Method that creates the world and opens the frame if playing with keyboard
     * @param seedString
     */
    private void createWorld(String seedString) {
        int s = (int) Long.parseLong(seedString);

        // System.out.println(seed);
        if (withKeyboard) {
            ter.initialize(WIDTH, HEIGHT + 2);
        }

        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        world = new RandomWorldGenerator(finalWorldFrame, s);
        world.create();

        if (withKeyboard) {
            ter.renderFrame(finalWorldFrame);
        }
    }
}
