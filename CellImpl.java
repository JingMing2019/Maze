import java.util.*;
import java.util.List;

public final class CellImpl implements Cell{
  private final int ID;
  private final Location cellLocation;
  private final Map<Direction, Boolean> walls;
  private boolean hasGoldCoin;
  private boolean hasThief;

  /**
   * Constructs a {@code CellImpl} object with unique ID and {@code Location} in the maze. Initially the walls at four
   * {@code Direction} are set to true. Initially the cells contains neither a gold coin nor a thief.
   *
   * @param ID the unique ID of this cell.
   * @param numOfCols number of columns the maze has. Used to calculate the {@code Location} of this cell.
   *
   */
  public CellImpl(int ID, int numOfCols) {
    this.ID = ID;
    this.cellLocation = MazeHelper.IDToLocation(ID, numOfCols);
    this.walls = new HashMap<>();
    initializeWalls();
    this.hasGoldCoin = false;
    this.hasThief = false;
  }

  @Override
  public void tearDownWall(Direction d) {
    // To tear down the wall at Direction `d`, replace the Boolean value at key `d` to false.
    this.walls.replace(d, false);
  }

  @Override
  public void setHasGoldCoin(boolean b) {
    if(b && this.hasThief) {
      throw new IllegalArgumentException("This cell already contains thief. A gold coin cannot be added to this cell.");
    }
    this.hasGoldCoin = b;
  }

  @Override
  public void setHasThiefTrue() {
    if(this.hasGoldCoin) {
      throw new IllegalArgumentException("This cell already contains gold coin. A thief cannot be added to this cell.");
    }

    this.hasThief = true;
  }

  @Override
  public List<Direction> findNoWallDirection() {
    List<Direction> dList = new ArrayList<>();
    for(Direction d: Direction.values()) {
      if(!this.walls.get(d)) {
        dList.add(d);
      }
    }
    return dList;
  }

  @Override
  public boolean getHasGoldCoin() {
    return this.hasGoldCoin;
  }

  @Override
  public boolean getHasThief() {
    return this.hasThief;
  }

  @Override
  public Location getLocation() {
    return this.cellLocation;
  }

  @Override
  public Map<Direction, Boolean> getWalls() {
    return this.walls;
  }

  @Override
  public String toString() {
    return "ID: " + ID + "\nLocation: " + cellLocation + "\nWalls status: " + walls + "\nHas gold coin: " + hasGoldCoin
            + "\nHas Thief: " + hasThief + "\n";
  }

  /**
   * Initialize walls. Initially the walls at all direction are set to true.
   */
  private void initializeWalls() {
    for(Direction d: Direction.values()) {
      this.walls.put(d, true);
    }
  }
}
