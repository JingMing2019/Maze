/**
 * Specifies operations for getting the {@code Location} of walls/gold coins/thieves/starting point/goal point/player
 * on the canvas, getting the total gold coins the player collects, showing the possible moves ({@code Direction}) of
 * the player from his/her current location, changing the status of maze when the player making a move(removing the
 * gold coin from the place where the player picks up one).
 */
import java.util.List;

public interface Maze {
    /**
     * Get the goal Location in the maze.
     * @return the {@code Location} of goal point.
     */
    Location getGoalLocation();

    /**
     * Get the player's Location in the maze.
     * @return the current {@code Location} of player.
     */
    Location getPlayerLocation();

    /**
     * Get the player's gold count.
     * @return the current total gold coins player has in the maze.
     */
    int getPlayerGoldCount();

    /**
     * Print the player's status.
     * @return a string describing his/her {@code Location} and gold count.
     */
    String printPlayerStatus();

    /**
     * Print the next possible move of player from his/her current location.
     * @return a string describing the {@code Direction} which player can move to.
     */
    String printNextPossibleMove();

    /**
     * Move the player to the specified `playerDirectionInput` and refresh the maze status.
     * @param playerDirectionInput a {@code Direction} which player can move to.
     */
    void movePlayer(Direction playerDirectionInput);

    /**
     * Get the walls' Location of the maze. Each wall is a line.
     * @return a 2D array, is a list of walls. Each wall represents as a list of 4 Integer [x1, y1, x2, y2]. These 4
     * Integer forms a line between points(x1, y1) and points(x2, y2).
     */
    List<List<Location>> getWallsLocation();

    /**
     * Get the gold coins' Location. Initially, 20% of cells in this maze contains gold coins.
     * @return a list of {@code Location} where contains a gold coin.
     */
    List<Location> getGoldCoinLocation();

    /**
     * Get the thieves' Location. Initially, 10% of cells in this maze contains thieves.
     * @return a list of {@code Location} where contains a thief.
     */
    List<Location> getThiefLocation();

    /**
     * Get the starting point in the maze.
     * @return the {@code Location} of starting point.
     */
    Location getStartLocation();

    /**
     * Whether the game is over or not.
     * @return true if the player's {@code Location} equals to the goal {@code Location};
     */
    boolean isGameOver();


    int countRemainedWalls();

    List<Location> getSolveLocation();

}
