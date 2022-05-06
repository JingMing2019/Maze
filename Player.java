/**
 * Specifies operations of player moving one step to the specified direction, collecting one gold coin and losing 10
 * percent gold coins when encountering thief in the maze. Also get the player's location and collected coins.
 */

public interface Player{
  /**
   * Player moves one step to the specified direction in the maze.
   * @param d direction which player can move to. see {@code Direction} enum class
   */
  void move(Direction d);

  /**
   * Player collects one gold coin.
   */
  void collectGold();

  /**
   * Player loses 10 percent of his/her gold coins. Assume coin is the integer type, so when doing the division, round
   * up the result to the nearest integer.
   */
  void loseGold();

  /**
   * Get the player's collected gold coins.
   * @return the total gold player has.
   */
  int getGold();

  /**
   * Get the player's location.
   * @return the Location of player.
   */
  Location getLocation();
}