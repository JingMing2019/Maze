/**
 * The {@code PlayerImpl} class represents a player in the maze. Player can make a move and collect or lose his/her gold
 * coins when walking in the maze.
 */

public final class PlayerImpl implements Player{
  private final Location playerLocation;
  private int gold;

  /**
   * Constructs a {@code PlayerImpl} object with a start location and 0 gold coins.
   *
   * @param playerStartLocation {@code Location} the starting point of the player.
   */
  public PlayerImpl(Location playerStartLocation) {
    this.playerLocation = playerStartLocation;
    this.gold = 0;
  }

  @Override
  public void move(Direction d){
    switch (d) {
      case NORTH -> updatePlayerLocationI(-1);
      case SOUTH -> updatePlayerLocationI(1);
      case EAST -> updatePlayerLocationJ(1);
      case WEST -> updatePlayerLocationJ(-1);
    }
  }

  @Override
  public void collectGold() {
    this.gold += 1;
  }

  @Override
  public void loseGold() {
    this.gold -= MazeHelper.getXPercentageInInt(this.getGold(), Parameters.THIEF_PERCENT);
    // Make sure the gold is non-negative.
    if (this.gold < 0) {
      this.gold = 0;
    }
  }

  @Override
  public int getGold() {
    return this.gold;
  }

  @Override
  public Location getLocation() {
    return this.playerLocation;
  }

  @Override
  public String toString() {
    return "Currently player is at " + playerLocation + " Collecting: " + gold + " gold coins.\n";
  }

  /**
   * Update the player's location at I index with adding a delta to its former location at I index.
   * @param delta the distance between one cell and another cell at I index.
   */
  private void updatePlayerLocationI(int delta) {
    this.playerLocation.setI(this.playerLocation.getI() + delta);
  }

  /**
   * Update the player's location at J index with adding a delta to its former location at J index.
   * @param delta the distance between one cell and another cell at J index.
   */
  private void updatePlayerLocationJ(int delta) {
    this.playerLocation.setJ(this.playerLocation.getJ() + delta);
  }


}