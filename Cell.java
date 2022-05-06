/**
 * Specifies operations for tearing down wall in the cell by specifying a {@code Direction}, setting and getting the
 * condition of cell(whether this cell has one gold coin or not, whether this cell has a thief or not), finding all the
 * {@code Direction} which do not stand a wall, getting the condition of walls(Whether a wall is standing on each
 * {@code Direction} or not).
 */
import java.util.*;

public interface Cell {
  /**
   * Tear down the wall in this cell at direction d.
   * @param d one value in {@code Direction} enum class.
   */
  void tearDownWall(Direction d);

  /**
   * Set whether this cell has gold coin or not.
   * @param b true if this cell contains a gold coin, false if the gold coin is removed from this cell.
   * @throws IllegalArgumentException if b is true and hasThief is true, because one cell can only contain one gold coin
   * or one thief or no item.
   */
  void setHasGoldCoin(boolean b);

  /**
   * Set this cell to have a thief.
   * @throws IllegalArgumentException if b is true and hasGoldCoin is true, because one cell can only contain one gold
   * coin or one thief or no item.
   *
   */
  void setHasThiefTrue();

  /**
   * Find the directions which do not have walls.
   * @return a list of {@code Direction}.
   */
  List<Direction> findNoWallDirection();

  /**
   * Get whether this cell contains a goal coin or not.
   * @return true if it has one, otherwise false.
   */
  boolean getHasGoldCoin();

  /**
   * Get whether this cell contains a thief or not.
   * @return true if it has one, otherwise false.
   */
  boolean getHasThief();

  /**
   * Get the location of this cell.
   * @return {@code Location}.
   */
  Location getLocation();

  /**
   * Get the walls map of the cell.
   * @return a map mapping {@code Direction} to Boolean. If a wall stands at direction d, the Boolean value is set to
   * true. If there is no wall stands at direction d, the Boolean value is set false.
   */
  Map<Direction, Boolean> getWalls();

}
