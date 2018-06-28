package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class Enemy {
    private String description;
    TETile[][] world;
    int X;
    int Y;
    RandomWorldGenerator rwg;
    TETile type;
    boolean hostile;
    int hostileTurnsLeft;

    public Enemy(String description, TETile[][] world,
                 int randX, int randY, RandomWorldGenerator rwg) {
        this.description = description;
        this.world = world;
        hostile = true;
        hostileTurnsLeft = 0;
        X = randX;
        Y = randY;
        if (description.equals("pawn")) {
            type = Tileset.PAWN;
        } else if (description.equals("bishop")) {
            type = Tileset.BISHOP;
        } else if (description.equals("king")) {
            type = Tileset.KING;
            hostile = false;
        } else if (description.equals("queen")) {
            type = Tileset.QUEEN;
        } else if (description.equals("knight")) {
            type = Tileset.KNIGHT;
        } else if (description.equals("rook")) {
            type = Tileset.ROOK;
        }
        world[X][Y] = type;
        this.rwg = rwg;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void takeTurn() {
        if (getDescription().equals("pawn")) {
            pawnMove();
        } else if (getDescription().equals("knight")) {
            knightMove();
        } else if (getDescription().equals("bishop")) {
            bishopMove();
        } else if (getDescription().equals("rook")) {
            rookMove();
        } else if (getDescription().equals("queen")) {
            queenMove();
        } else if (getDescription().equals("king")) {
            kingMove();
        }
    }

    private void knightMove() {
        boolean madeMove = false;
        ArrayList<Integer> triedMoves = new ArrayList<>();
        while (!madeMove) {
            if (triedMoves.size() >= 8) {
                return;
            }
            int direction = RandomWorldGenerator.randNum(8, 1);
            triedMoves.add(direction);

            if (direction == 1 && makeSureLegitMove(X - 1, Y + 2)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y + 2;
                X = X - 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 2 && makeSureLegitMove(X + 1, Y + 2)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y + 2;
                X = X + 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 3 && makeSureLegitMove(X + 2, Y + 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y + 1;
                X = X + 2;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 4 && makeSureLegitMove(X + 2, Y - 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y - 1;
                X = X + 2;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 5 && makeSureLegitMove(X + 1, Y - 2)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y - 2;
                X = X + 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 6 && makeSureLegitMove(X - 1, Y - 2)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y - 2;
                X = X - 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 7 && makeSureLegitMove(X - 2, Y - 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y - 1;
                X = X - 2;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (direction == 8 && makeSureLegitMove(X - 2, Y + 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y + 1;
                X = X - 2;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            }
        }
    }

    private void bishopMove() {
        boolean madeMove = false;
        ArrayList<Integer> triedMoves = new ArrayList<>();
        while (!madeMove) {
            if (triedMoves.size() >= 4) {
                return;
            }

            int direction = RandomWorldGenerator.randNum(4, 1);
            triedMoves.add(direction);

            if (direction == 1 && makeSureLegitMove(X - 1, Y + 1)) {
                while (makeSureLegitMove(X - 1, Y + 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y + 1;
                    X = X - 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 2 && makeSureLegitMove(X + 1, Y + 1)) {
                while (makeSureLegitMove(X + 1, Y + 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y + 1;
                    X = X + 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 3 && makeSureLegitMove(X + 1, Y - 1)) {
                while (makeSureLegitMove(X + 1, Y - 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y - 1;
                    X = X + 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 4 && makeSureLegitMove(X - 1, Y - 1)) {
                while (makeSureLegitMove(X - 1, Y - 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y - 1;
                    X = X - 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            }
        }
    }

    private void rookMove() {
        boolean madeMove = false;
        ArrayList<Integer> triedMoves = new ArrayList<>();
        while (!madeMove) {
            if (triedMoves.size() >= 4) {
                return;
            }

            int direction = RandomWorldGenerator.randNum(4, 1);
            triedMoves.add(direction);

            if (direction == 1 && makeSureLegitMove(X - 1, Y)) {
                while (makeSureLegitMove(X - 1, Y)) {
                    world[X][Y] = Tileset.FLOOR;
                    X = X - 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 2 && makeSureLegitMove(X, Y + 1)) {
                while (makeSureLegitMove(X, Y + 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y + 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 3 && makeSureLegitMove(X + 1, Y)) {
                while (makeSureLegitMove(X + 1, Y)) {
                    world[X][Y] = Tileset.FLOOR;
                    X = X + 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            } else if (direction == 4 && makeSureLegitMove(X, Y - 1)) {
                while (makeSureLegitMove(X, Y - 1)) {
                    world[X][Y] = Tileset.FLOOR;
                    Y = Y - 1;
                    checkLose();
                    world[X][Y] = type;
                }
                madeMove = true;
            }
        }
    }

    private void queenMove() {
        int bishOrRook = RandomWorldGenerator.randNum(2, 1);
        if (bishOrRook == 1) {
            bishopMove();
        } else {
            rookMove();
        }
    }

    private void kingMove() {
        double longestDistance = -1;
        int xOriginal = X;
        int yOriginal = Y;
        boolean madeMove = false;
        boolean check = false;

        if (makeSureLegitMove(xOriginal - 1, yOriginal + 1) && !madeMove) {
            double distance = Math.sqrt((Math.abs((xOriginal - 1)
                    - Player.getXPosition()) * Math.abs((xOriginal - 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal + 1) - Player.getYPosition())
                    * Math.abs((yOriginal + 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal - 1, yOriginal + 1)) {
                longestDistance = distance;
                X = xOriginal - 1;
                Y = yOriginal + 1;
            } else if (isPlayer(xOriginal - 1, yOriginal + 1)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal, yOriginal + 1)) {
            double distance = Math.sqrt((Math.abs((xOriginal)
                    - Player.getXPosition()) * Math.abs((xOriginal)
                    - Player.getXPosition())) + (Math.abs((yOriginal + 1) - Player.getYPosition())
                    * Math.abs((yOriginal + 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal, yOriginal + 1)) {
                longestDistance = distance;
                X = xOriginal;
                Y = yOriginal + 1;
            } else if (isPlayer(xOriginal, yOriginal + 1)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal + 1, yOriginal + 1)) {
            double distance = Math.sqrt((Math.abs((xOriginal + 1)
                    - Player.getXPosition()) * Math.abs((xOriginal + 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal + 1) - Player.getYPosition())
                    * Math.abs((yOriginal + 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal + 1, yOriginal + 1)) {
                longestDistance = distance;
                X = xOriginal + 1;
                Y = yOriginal + 1;
            } else if (isPlayer(xOriginal + 1, yOriginal + 1)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal + 1, yOriginal)) {
            double distance = Math.sqrt((Math.abs((xOriginal + 1)
                    - Player.getXPosition()) * Math.abs((xOriginal + 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal) - Player.getYPosition())
                    * Math.abs((yOriginal) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal + 1, yOriginal)) {
                longestDistance = distance;
                X = xOriginal + 1;
                Y = yOriginal;
            } else if (isPlayer(xOriginal + 1, yOriginal)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal + 1, yOriginal - 1)) {
            double distance = Math.sqrt((Math.abs((xOriginal + 1)
                    - Player.getXPosition()) * Math.abs((xOriginal + 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal - 1) - Player.getYPosition())
                    * Math.abs((yOriginal - 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal + 1, yOriginal - 1)) {
                longestDistance = distance;
                X = xOriginal + 1;
                Y = yOriginal - 1;
            } else if (isPlayer(xOriginal + 1, yOriginal - 1)) {
                check = true;
            }
        }
        continueKingMove(longestDistance, xOriginal, yOriginal, check);

    }

    private void continueKingMove(double longestDistance,
                                  int xOriginal, int yOriginal, boolean check) {
        if (makeSureLegitMove(xOriginal, yOriginal - 1)) {
            double distance = Math.sqrt((Math.abs((xOriginal)
                    - Player.getXPosition()) * Math.abs((xOriginal)
                    - Player.getXPosition())) + (Math.abs((yOriginal - 1) - Player.getYPosition())
                    * Math.abs((yOriginal - 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal, yOriginal - 1)) {
                longestDistance = distance;
                X = xOriginal;
                Y = yOriginal - 1;
            } else if (isPlayer(xOriginal, yOriginal - 1)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal - 1, yOriginal - 1)) {
            double distance = Math.sqrt((Math.abs((xOriginal - 1)
                    - Player.getXPosition()) * Math.abs((xOriginal - 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal - 1) - Player.getYPosition())
                    * Math.abs((yOriginal - 1) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal - 1, yOriginal - 1)) {
                longestDistance = distance;
                X = xOriginal - 1;
                Y = yOriginal - 1;
            } else if (isPlayer(xOriginal - 1, yOriginal - 1)) {
                check = true;
            }
        }
        if (makeSureLegitMove(xOriginal - 1, yOriginal)) {
            double distance = Math.sqrt((Math.abs((xOriginal - 1)
                    - Player.getXPosition()) * Math.abs((xOriginal - 1)
                    - Player.getXPosition())) + (Math.abs((yOriginal) - Player.getYPosition())
                    * Math.abs((yOriginal) - Player.getYPosition())));
            if (longestDistance < distance
                    && !isPlayer(xOriginal - 1, yOriginal)) {
                longestDistance = distance;
                X = xOriginal - 1;
                Y = yOriginal;
            } else if (isPlayer(xOriginal - 1, yOriginal)) {
                check = true;
            }
        }
        if (longestDistance != -1) {
            world[xOriginal][yOriginal] = Tileset.FLOOR;
            world[X][Y] = type;
        } else if (check && longestDistance == -1) {
            Player.win();
        }
        if (!hostile) {
            int r = RandomWorldGenerator.randNum(4, 1);
            if (r == 1) {
                hostile = true;
                type = Tileset.KINGHOSTILE;
                world[X][Y] = type;
                hostileTurnsLeft = RandomWorldGenerator.randNum(5, 1);
            }
        } else {
            if (hostileTurnsLeft == 1) {
                hostile = false;
                type = Tileset.KING;
                world[X][Y] = type;
            } else {
                hostileTurnsLeft -= 1;
            }
        }
    }

    private void pawnMove() {
        boolean madeMove = false;
        ArrayList<Integer> triedMoves = new ArrayList<>();

        while (!madeMove) {
            if (triedMoves.size() >= 8) {
                return;
            }
            int turn = RandomWorldGenerator.randNum(4, 1);
            triedMoves.add(turn);
            if (turn == 1 && makeSureLegitMove(X, Y + 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y + 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (turn == 2 && makeSureLegitMove(X + 1, Y)) {
                world[X][Y] = Tileset.FLOOR;
                X = X + 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (turn == 3 && makeSureLegitMove(X, Y - 1)) {
                world[X][Y] = Tileset.FLOOR;
                Y = Y - 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            } else if (turn == 4 && makeSureLegitMove(X - 1, Y)) {
                world[X][Y] = Tileset.FLOOR;
                X = X - 1;
                checkLose();
                world[X][Y] = type;
                madeMove = true;
            }
        }
    }

    private void checkLose() {
        if (isPlayer(X, Y) && Game.withKeyboard && hostile) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .75, "YOU'VE BEEN SLAIN BY "
                    + this.getDescription().toUpperCase());
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(Game.WIDTH / 2, Game.HEIGHT * .5, "Press any key to quit.");
            StdDraw.show();
            Game.lost = true;
        } else if (isPlayer(X, Y) && hostile) {
            Game.lost = true;
        }

    }

    private boolean isPlayer(int xPos, int yPos) {
        return world[xPos][yPos].equals(Tileset.PLAYER)
                || world[xPos][yPos].equals(Tileset.PLAYERBACK)
                || world[xPos][yPos].equals(Tileset.PLAYERLEFT)
                || world[xPos][yPos].equals(Tileset.PLAYERRIGHT);
    }

    private boolean makeSureLegitMove(int x, int y) {
        if (x < Game.WIDTH && x > 0 && y < Game.HEIGHT && y > 0
                && (world[x][y].equals(Tileset.FLOOR) || isPlayer(x, y))) {
            return true;
        }
        return false;
    }

    public TETile getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
