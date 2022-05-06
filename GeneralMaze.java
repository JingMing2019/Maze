/**
 * The {@code GeneralMaze} represents a general maze. Its size(number of rows, number of columns, number of remaining
 * walls) and the starting point and goal location are user defined arguments. The generation of this maze is based on
 * Kruskal Algorithm. It is guaranteed that there exists a path from the starting point to the goal location. A player
 * is generated within this maze. Player can move from one `room` to its adjacent `room` if there is no wall stands
 * between two `room`. During the walk, player picks up gold coin if the entered room contains one and this gold coin
 * is removed after the pickup. Player loses 10% of his gold coins if he/she encounters a thief. When player moves to
 * the goal location, the maze game is over.
 */

import java.util.*;
import java.util.List;

abstract class GeneralMaze implements Maze {
	protected final int numOfRows;
	protected final int numOfCols;
	protected final int numOfRemainingWalls;
	protected final int numOfCells;
	private final List<List<Cell>> grid;
	protected final Player player;
	private final Location startLocation;
	private final Location goalLocation;
	private final List<Location> thiefLocation;
	private final List<List<Location>> wallsLocation;
	private final List<Location> solveLocation;

	/**
	 * Initialize the abstract GeneralMaze. Create the grid and a player in the maze.
	 *
	 * @param numOfRows           number of rows in the maze. Must be positive.
	 * @param numOfCols           number of columns in the maze. Must be positive.
	 * @param numOfRemainingWalls number of remaining walls in the maze.
	 * @param startLocation       starting point in the maze.
	 * @param goalLocation        goal point in the maze.
	 * @throws IllegalArgumentException {@code numOfRows} and {@code numOfCols} is 0 or negative.
	 * @throws IllegalArgumentException {@code numOfRemainingWalls} is not in the bound.
	 * @throws IllegalArgumentException {@code startLocation} and {@code startLocation} is not inside the maze grid.
	 */
	protected GeneralMaze(int numOfRows,
	                      int numOfCols,
	                      int numOfRemainingWalls,
	                      Location startLocation,
	                      Location goalLocation) {
		if (numOfRows <= 0 || numOfCols <= 0) {
			throw new IllegalArgumentException("NumOfRows " + numOfRows + " and NumOfCols " + numOfCols +
					" must be positive!\n");
		}

		if (!isNumOfRemainingWallsValid(numOfRows, numOfCols, numOfRemainingWalls)) {
			throw new IllegalArgumentException("NumOfRemainingWalls " + numOfRemainingWalls + " is out of range!\n");
		}

		if (!isStartAndGoalInsideGrid(startLocation, numOfRows, numOfCols)) {
			throw new IllegalArgumentException("Start location " + startLocation + " is not inside the maze grid.\n");
		}

		if (!isStartAndGoalInsideGrid(goalLocation, numOfRows, numOfCols)) {
			throw new IllegalArgumentException("Goal location " + goalLocation + " is not inside the maze grid.\n");
		}

		this.numOfRows = numOfRows;
		this.numOfCols = numOfCols;
		this.numOfRemainingWalls = numOfRemainingWalls;
		this.numOfCells = numOfRows * numOfCols;
		this.startLocation = startLocation;
		this.goalLocation = goalLocation;
		this.grid = generateGrid();
		this.player = new PlayerImpl(new Location(startLocation.getI(), startLocation.getJ()));
		this.thiefLocation = generateThiefLocation();
		this.wallsLocation = generateWallsLocation();
//		this.solvePlayer = new PlayerImpl(new Location(startLocation.getI(), startLocation.getJ()));
		this.solveLocation = solveByDfs();
	}

	@Override
	public Location getGoalLocation() {
		return this.goalLocation;
	}

	@Override
	public Location getPlayerLocation() {
		return this.player.getLocation();
	}

	@Override
	public int getPlayerGoldCount() {
		return this.player.getGold();
	}

	@Override
	public String printPlayerStatus() {
		return this.player.toString();
	}

	@Override
	public String printNextPossibleMove() {
		return "Player's next possible move is: " + produceNextPossibleMove();
	}

	@Override
	public void movePlayer(Direction playerDirectionInput) {
		if (isGameOver()) {
			return;
		}
		if (!produceNextPossibleMove().contains(playerDirectionInput)) {
			throw new IllegalArgumentException("Player can not move to this direction " + playerDirectionInput +
					". A wall stands at this direction.");
		}
		movePlayerToNextCell(playerDirectionInput);
		updatePlayerAndGridAfterGoldCollection();
	}

	@Override
	public List<List<Location>> getWallsLocation() {
		return this.wallsLocation;
	}

	@Override
	public List<Location> getGoldCoinLocation() {
		List<Location> goldCoinLocation = new ArrayList<>();
		for (List<Cell> cells : grid) {
			for (Cell c : cells) {
				if (c.getHasGoldCoin()) {
					goldCoinLocation.add(c.getLocation());
				}
			}
		}

		return goldCoinLocation;
	}

	@Override
	public List<Location> getThiefLocation() {
		return this.thiefLocation;
	}

	@Override
	public Location getStartLocation() {
		return this.startLocation;
	}

	@Override
	public boolean isGameOver() {
		return player.getLocation().equals(goalLocation);
	}

	@Override
	public int countRemainedWalls() {
		int count = 0;
		for(int i = 0; i < numOfRows; i++) {
			for(int j = 0; j < numOfCols; j++) {
				Cell c = grid.get(i).get(j);
				if (c.getWalls().get(Direction.NORTH)) {
					count++;
				}
				if (c.getWalls().get(Direction.WEST)) {
					count++;
				}
				if (i == numOfRows - 1 && c.getWalls().get(Direction.SOUTH)) {
					count++;
				}
				if (j == numOfCols - 1 && c.getWalls().get(Direction.EAST)) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public List<Location> getSolveLocation() {
		return this.solveLocation;
	}

	private List<Location> solveByDfs() {
		List<Location> pathFromStartToGoal = new ArrayList<>();

		Set<Location> seen = new HashSet<>();

		dfs(startLocation.getI(), startLocation.getJ(), seen, pathFromStartToGoal);

		return pathFromStartToGoal;
	}

	private boolean dfs(int i, int j, Set<Location> seen, List<Location> path) {
		Location l = new Location(i, j);
		if (l.equals(goalLocation)) {
			path.add(l);
			return true;
		}
		if(seen.contains(l)) {
			return false;
		}
		seen.add(l);
		boolean flag = false;
		List<Direction> validDirection = grid.get(i).get(j).findNoWallDirection();

		for(Direction d: validDirection) {
			int nextI = moveInSolveMaze(i, j, d)[0];
			int nextJ = moveInSolveMaze(i, j, d)[1];
			boolean innerFlag = dfs(nextI, nextJ, seen, path);
			flag = flag || innerFlag;
			if (innerFlag) {
				path.add(l);
			}
		}

		return flag;
	}

	protected int[] moveInSolveMaze(int i, int j, Direction d) {
		return moveToNextCell(i, j, d);
	}

	protected int[] moveToNextCell(int i, int j, Direction d){
		int[] res = {i, j};
		switch (d) {
			case NORTH -> res[0] -= 1;
			case SOUTH -> res[0] += 1;
			case EAST -> res[1] += 1;
			case WEST -> res[1] -= 1;
		}
		return res;
	}

	/**
	 * Print the maze in String.
	 *
	 * @return a string describing the maze's walls/starting point/goal/gold coin/thief/player.
	 * String format:
	 * +-----+-----+-----+
	 * |  p           $  |
	 * +     +-----+-----+
	 * |     |        $  |
	 * +     +-----+     +
	 * |  &          end |
	 * +-----+-----+-----+
	 */
	@Override
	public String toString() {
		return printMaze();
	}

	/**
	 * Check the validation of {@code numOfRemainingWalls}. For Non-Wrap perfect maze, always return true. For Non-Wrap
	 * room Maze, the border walls cannot be removed. For Wrap Maze, the border can be removed. When the border is
	 * removed, decreases the walls by 2.
	 *
	 * @return true if {@code numOfRemainingWalls} is inside the bound, otherwise false. The bound for Non-Wrap Room
	 * Maze is [borderWalls, maxRemainingWalls - 1]. The bound for Wrap Room Maze is [0, maxRemainingWalls - 2].
	 * @param numOfRows           number of rows in the maze. Must be positive.
	 * @param numOfCols           number of columns in the maze. Must be positive.
	 * @param numOfRemainingWalls number of remaining walls in the maze.
	 */
	protected abstract boolean isNumOfRemainingWallsValid(int numOfRows, int numOfCols, int numOfRemainingWalls);

	/**
	 * Randomly select an edge in the edgeList. Get the sourceID and destID of the selected {@code edge}, store them in
	 * an array. And remove this edge from the edgeList.
	 * @param edgeList a list of {@code Edge}.
	 * @return a 2-elements array, index 0 refers to the sourceID, index 1 refers to the destID.
	 */
	protected int[] randomSelectAndRemoveEdges(List<Edge> edgeList) {
		Random randNum = new Random();
		int[] IDList = new int[2];
		int randEdgeIdx = randNum.nextInt(edgeList.size());
		IDList[0] = edgeList.get(randEdgeIdx).getSource();
		IDList[1] = edgeList.get(randEdgeIdx).getDest();
		edgeList.remove(randEdgeIdx);

		return IDList;
	}

	/**
	 * Check if the input startLocation and goalLocation is inside the grid size or not.
	 *
	 * @param l         start {@code Location} or goal {@code Location}.
	 * @param numOfRows number of rows in the maze.
	 * @param numOfCols number of columns in the maze.
	 * @return true if the input location is inside the grid, otherwise false.
	 */
	private boolean isStartAndGoalInsideGrid(Location l, int numOfRows, int numOfCols) {
		int i = l.getI();
		int j = l.getI();

		return i >= 0 && i < numOfRows && j >= 0 && j < numOfCols;
	}

	/**
	 * Generate a connected maze grid.
	 *
	 * @return a well-generated 2D-Array maze grid.
	 */
	private List<List<Cell>> generateGrid() {
		List<List<Cell>> grid = initializeGrid();
		// Using `Kruskal Algorithm` to generate connected maze grid.
		makeGridConnectedUsingKruskalAlgorithm(grid);
		// Randomly select `Cell` to place gold coins and thieves.
		decorateMazeGridWithItems(grid);

		return grid;
	}

	/**
	 * Initialize the grid. Each cell is isolated by 4 surrounded walls.
	 * @return a 2D-Array grid, maze grid generated in progress.
	 */
	private List<List<Cell>> initializeGrid() {
		List<List<Cell>> grid = new ArrayList<>();
		for (int i = 0; i < numOfRows; i++) {
			List<Cell> gridRow = new ArrayList<>();
			for (int j = 0; j < numOfCols; j++) {
				int ID = MazeHelper.idxToID(i, j, numOfCols);
				Cell c = new CellImpl(ID, numOfCols);
				gridRow.add(c);
			}
			grid.add(gridRow);
		}

		return grid;
	}

	/**
	 * Using Kruskal Algorithm to make the maze grid becomes connected. At least one path from any {@code Cell} to any
	 * other {@code Cell} is available in this maze.
	 * @param grid a 2D-Array, maze grid generated in progress.
	 */
	private void makeGridConnectedUsingKruskalAlgorithm(List<List<Cell>> grid) {
		List<Edge> innerEdgeList = generateInnerEdgeList();
		int[] parent = makeSet();
		int remainedWalls = createPerfectMazeGrid(calculateTotalWalls(), innerEdgeList, grid, parent);
		// For Room Maze, need to remove more walls.
		continueRemoveWalls(remainedWalls, innerEdgeList, grid);
	}

	/**
	 * For Room Maze, continue remove walls until count equals to {@code numOfRemainingWalls}.
	 * @param remainedWalls the current remaining walls.
	 * @param innerEdgeList the inside edge list of the maze.
	 * @param grid a 2D-array, maze grid generated in progress.
	 */
	protected abstract void continueRemoveWalls(int remainedWalls, List<Edge> innerEdgeList, List<List<Cell>> grid);

	/**
	 * Create a Perfect Maze grid. Randomly select walls to be torn down and remove the corresponding edge from the
	 * inner edge list. When the `remainedWalls` reaches the `maxRemainingWalls`, the maze grid becomes connected.
	 * @param remainedWalls the current remained walls in the maze, including the inner walls and border walls.
	 * @param innerEdgeList a list of inner {@code Edge} in the maze.
	 * @param grid a 2D-Array, maze grid generated in progress.
	 * @param parent the disjoint sets of maze cells.
	 * @return the current remained walls in the maze.
	 */
	private int createPerfectMazeGrid(int remainedWalls, List<Edge> innerEdgeList, List<List<Cell>> grid, int[] parent) {
		Random randNum = new Random();
		int maxRemainingWalls = MazeHelper.calculateMaxRemainingWalls(numOfRows, numOfCols);
		List<Edge> edgeList = new ArrayList<>(innerEdgeList); // copy the innerEdgeList's element.
		while (remainedWalls > maxRemainingWalls) {
			int randEdgeIdx = randNum.nextInt(edgeList.size());
			int sourceID = edgeList.get(randEdgeIdx).getSource();
			int destID = edgeList.get(randEdgeIdx).getDest();
			// If the `sourceID` cell and `destID` cell can be joint, tear down the wall between these two cells and
			// decrement the count.
			if (findAndUnion(sourceID, destID, parent)) {
				tearDownInnerWall(sourceID, destID, grid);
				remainedWalls--;
				Edge removedEdge = edgeList.get(randEdgeIdx);
				// only remove the randomly selected edge when the corresponding wall is torn down.
				innerEdgeList.remove(removedEdge);
			}
			// once the randomly selected edge is processed, no need to process again.
			edgeList.remove(randEdgeIdx);
		}
		return remainedWalls;
	}

	/**
	 * Tear down the wall inside the maze which is between `source` ID cell and `dest` ID cell.
	 * @param sourceID the source ID of an edge.
	 * @param destID the destination ID of the same edge.
	 * @param grid a 2D-Array, maze grid generated in progress.
	 */
	protected void tearDownInnerWall(int sourceID, int destID, List<List<Cell>> grid) {
		int sourceI = MazeHelper.IDToLocation(sourceID, numOfCols).getI();
		int sourceJ = MazeHelper.IDToLocation(sourceID, numOfCols).getJ();
		int destI = MazeHelper.IDToLocation(destID, numOfCols).getI();
		int destJ = MazeHelper.IDToLocation(destID, numOfCols).getJ();
		if (Math.abs(sourceJ - destJ) == 1) {
			grid.get(sourceI).get(sourceJ).tearDownWall(Direction.EAST);
			grid.get(destI).get(destJ).tearDownWall(Direction.WEST);
		} else if (Math.abs(sourceI - destI) == 1) {
			grid.get(sourceI).get(sourceJ).tearDownWall(Direction.SOUTH);
			grid.get(destI).get(destJ).tearDownWall(Direction.NORTH);
		}
	}

	/**
	 * Generate the inner Edge List of the grid. Inner edge is the edge between one cell and its adjacent cell in the
	 * maze.
	 * @return a list of {@code Edge}.
	 */
	private List<Edge> generateInnerEdgeList() {
		List<Edge> innerEdgeList = new ArrayList<>();
		for(int i = 0; i < numOfRows; i++) {
			for(int j = 0; j < numOfCols; j++) {
				int ID = MazeHelper.idxToID(i, j, numOfCols);
				if(j != numOfCols - 1) {
					int IDEast = MazeHelper.idxToID(i, j + 1, numOfCols);
					Edge e = new Edge(ID, IDEast);
					innerEdgeList.add(e);
				}
				if(i != numOfRows - 1) {
					int IDSouth = MazeHelper.idxToID(i + 1, j, numOfCols);
					Edge e = new Edge(ID, IDSouth);
					innerEdgeList.add(e);
				}
			}
		}
		return innerEdgeList;
	}

	private int calculateTotalWalls() {
		return 2 * numOfRows * numOfCols + numOfRows + numOfCols;
	}

	/**
	 * Make disjoint sets of vertices. Vertex is each cell in the maze grid.
	 *
	 * @return an int array. Each element is initialized as -1, means every vertex is the parent of its-self.
	 */
	private int[] makeSet() {
		int[] parent = new int[numOfCells];
		for (int k = 0; k < numOfCells; k++) {
			parent[k] = -1;
		}
		return parent;
	}

	/**
	 * Check if the `sourceID` cell and `destID` cell can be joint or not. If they are in the disjoint cells, union
	 * these two sets. Otherwise, do nothing.
	 * @param sourceID the source ID of an edge.
	 * @param destID the destination ID of the same edge.
	 * @param parent the disjoint sets of maze cells.
	 * @return true if they can be joint, otherwise false.
	 */
	protected boolean findAndUnion(int sourceID, int destID, int[] parent) {
		boolean success = false;
		if (findSet(sourceID, parent) != findSet(destID, parent)) {
			success = true;
			unionSet(sourceID, destID, parent);
		}
		return success;
	}

	/**
	 * Find the parent of the `ID` cell.
	 * @param ID ID of each cell in the maze.
	 * @param parent the disjoint sets of maze cells.
	 * @return the parent of the `ID` cell.
	 */
	private int findSet(int ID, int[] parent) {
		while (parent[ID] >= 0) {
			ID = parent[ID];
		}
		return ID;
	}

	/**
	 * Make the union of the sets contains the `sourceID` cell and the `destID` cell.
	 * @param sourceID the source ID of an edge.
	 * @param destID the destination ID of the same edge.
	 * @param parent the disjoint sets of maze cells.
	 */
	private void unionSet(int sourceID, int destID, int[] parent) {
		int root1 = parent[sourceID] < 0 ? sourceID : parent[sourceID];
		int root2 = parent[destID] < 0 ? destID : parent[destID];

		if (parent[root1] <= parent[root2]) {
			updateSet(root1, root2, parent);
		} else {
			updateSet(root2, root1, parent);
		}
	}

	/**
	 * Update the parent set during the union.
	 * @param largerSetRoot the root ID of the set which has more elements.
	 * @param smallerSetRoot the root ID of the set which has fewer elements.
	 * @param parent the disjoint sets of maze cells.
	 */
	private void updateSet(int largerSetRoot, int smallerSetRoot, int[] parent) {
		parent[largerSetRoot] += parent[smallerSetRoot];
		// Set the parent of child and its child to root.
		parent[smallerSetRoot] = largerSetRoot;
		for (int k = 0; k < parent.length; k++) {
			if (parent[k] == smallerSetRoot) {
				parent[k] = largerSetRoot;
			}
		}
	}

	/**
	 * Decorate maze grid with gold coins and thieves.
	 * @param grid a 2D-Array grid, maze grid ready to place items.
	 */
	private void decorateMazeGridWithItems(List<List<Cell>> grid) {
		Set<Integer> itemsIDSet = new HashSet<>();
		// Make sure gold coin and thief do not stand on the start location or goal location.
		itemsIDSet.add(MazeHelper.LocationToID(startLocation, numOfCols));
		itemsIDSet.add(MazeHelper.LocationToID(goalLocation, numOfCols));
		// Randomly select Cell to place gold coin
		int numOfGoldCoin = MazeHelper.getXPercentageInInt(numOfCells, Parameters.GOLD_COIN_PERCENT);
		placeItems(TypeOfItem.GOLD_COIN, numOfGoldCoin, itemsIDSet, grid);

		// Randomly select Cell to place thief
		int numOfThief = MazeHelper.getXPercentageInInt(numOfCells, Parameters.THIEF_PERCENT);
		placeItems(TypeOfItem.THIEF, numOfThief, itemsIDSet, grid);
	}

	/**
	 * Place gold coin or thief on the randomly selected Cell. Each cell can place only one item.
	 * @param type the type of item. See details in {@code TypeOfItem}.
	 * @param numOfItems number of the items.
	 * @param itemsIDSet a set of items' ID.
	 * @param grid a 2D-Array grid, maze grid ready to place items.
	 */
	private void placeItems(TypeOfItem type, int numOfItems, Set<Integer> itemsIDSet, List<List<Cell>> grid) {
		Random randNum = new Random();
		int count = 0;
		while(count != numOfItems) {
			int ID = randNum.nextInt(numOfCells);
			if (!itemsIDSet.contains(ID)) {
				int i = MazeHelper.IDToLocation(ID, numOfCols).getI();
				int j = MazeHelper.IDToLocation(ID, numOfCols).getJ();
				if (type == TypeOfItem.GOLD_COIN) {
					grid.get(i).get(j).setHasGoldCoin(true);
				} else if (type == TypeOfItem.THIEF) {
					grid.get(i).get(j).setHasThiefTrue();
				}
				itemsIDSet.add(ID);
				count++;
			}
		}
	}

	/**
	 * Generate the {@code Location} of the {@code Cell} which contains a thief. Used in the print of Maze.
	 * @return a list of {@code Location}.
	 */
	private List<Location> generateThiefLocation() {
		List<Location> thiefLocation = new ArrayList<>();
		for (List<Cell> cells : grid) {
			for (Cell c : cells) {
				if (c.getHasThief()) {
					thiefLocation.add(c.getLocation());
				}
			}
		}
		return thiefLocation;
	}

	/**
	 * Generate the location of the walls in each {@code Cell}. Used in the draw of Maze.
	 * @return a list of walls. Each wall is a line between start point(i1, j1) and end point(i2, j2).
	 */
	private List<List<Location>> generateWallsLocation() {
		List<List<Location>> walls = new ArrayList<>();
		for(int i = 0; i < numOfRows; i++) {
			for(int j = 0; j < numOfCols; j++) {
				Cell c = grid.get(i).get(j);
				if (c.getWalls().get(Direction.NORTH)) {
					walls.add(findWallStartAndEndPoint(Direction.NORTH, c));
				}
				if (c.getWalls().get(Direction.WEST)) {
					walls.add(findWallStartAndEndPoint(Direction.WEST, c));
				}
				if (i == numOfRows - 1 && c.getWalls().get(Direction.SOUTH)) {
					walls.add(findWallStartAndEndPoint(Direction.SOUTH, c));
				}
				if (j == numOfCols - 1 && c.getWalls().get(Direction.EAST)) {
					walls.add(findWallStartAndEndPoint(Direction.EAST, c));
				}
			}
		}
		return walls;
	}

	/**
	 * Represent the wall by 2 {@code Location}.
	 * @param d a {@code Direction} of a {@code Cell}.
	 * @param c a {@code Cell} in the maze.
	 * @return a list of 2 {@code Location}.
	 */
	private List<Location> findWallStartAndEndPoint(Direction d, Cell c) {
		List<Location> wall;
		int i = c.getLocation().getI();
		int j = c.getLocation().getJ();
		switch (d) {
			case NORTH -> wall = new ArrayList<>(List.of(new Location(i, j), new Location(i, j + 1)));
			case WEST -> wall = new ArrayList<>(List.of(new Location(i, j), new Location(i + 1, j)));
			case SOUTH -> wall = new ArrayList<>(List.of(new Location(i + 1, j), new Location(i + 1, j + 1)));
			case EAST -> wall = new ArrayList<>(List.of(new Location(i, j + 1), new Location(i + 1, j + 1)));
			default -> throw new IllegalArgumentException("Unsupported Direction!");
		}
		return wall;
	}

	/**
	 * Produce the next possible move {@code Direction} of the player from his/her current location.
	 * @return a list of {@code Direction} where stand no wall.
	 */
	private List<Direction> produceNextPossibleMove(){
		Location l = getPlayerLocation();
		if (isGameOver()) {
			return new ArrayList<>(); // If it is over, make sure the player cannot move to any direction.
		}
		int i = l.getI();
		int j = l.getJ();

		return grid.get(i).get(j).findNoWallDirection();
	}

	/**
	 * Move the player to next {@code Cell}. For Non-Wrap Maze, simply move the player to the adjacent cell. For
	 * Wrap-Maze, have to deal with the situation that player can be moves from one side to the `wrap` side.
	 * @param playerDirectionInput the input {@code Direction} player moves to.
	 */
	protected void movePlayerToNextCell(Direction playerDirectionInput) {
		player.move(playerDirectionInput);
	}

	/**
	 * When the player enter a {@code Cell} with a gold coin, player collects this gold coin, and it will be removed
	 * from the grid. When the player enter a {@code Cell} with a thief, player loses 10% of his/her total gold coins.
	 */
	private void updatePlayerAndGridAfterGoldCollection() {
		Location l = player.getLocation();
		int i = l.getI();
		int j = l.getJ();

		if (grid.get(i).get(j).getHasGoldCoin()){
			player.collectGold();
			grid.get(i).get(j).setHasGoldCoin(false); // Remove the gold coin from this grid.
		} else if (grid.get(i).get(j).getHasThief()) {
			player.loseGold();
		}
	}

	/**
	 * Print this maze in string type.
	 * @return a string including the basic information of the maze. The location of walls/start/goal/goldCoin/thief/
	 * player.
	 */
	private String printMaze() {
		StringBuilder message = new StringBuilder();

		String startLogo = "start";
		String goalLogo = " end ";
		String playerLogo = "  p  ";
		String goldLogo = "  $  ";
		String thiefLogo = "  &  ";
		message.append(startLogo).append(" means the start location.\n");
		message.append(goalLogo).append(" means the goal location.\n");
		message.append(playerLogo).append(" means the location of player.\n");
		message.append(goldLogo).append(" means the location of gold coin.\n");
		message.append(thiefLogo).append(" means the location of thief.\n");

		String[][] mazeInfo = new String[numOfRows * 2 + 1][numOfCols * 2 + 1];
		String horizonLine = "-----";
		String verticalLine = "|";
		String horizonNoLine = "     ";
		String verticalNoLine = " ";
		String noItem = "     ";
		String corner = "+";

		for(int i = 0; i < numOfRows; i++) {
			for(int j = 0; j < numOfCols; j++) {
				Cell c = grid.get(i).get(j);
				mazeInfo[i * 2][j * 2] = corner;
				mazeInfo[i * 2][j * 2 + 1] = c.getWalls().get(Direction.NORTH) ? horizonLine : horizonNoLine;
				mazeInfo[i * 2 + 1][j * 2] = c.getWalls().get(Direction.WEST) ? verticalLine : verticalNoLine;
				if (i == numOfRows - 1) {
					mazeInfo[i * 2 + 2][j * 2] = corner;
					mazeInfo[i * 2 + 2][j * 2 + 1] = c.getWalls().get(Direction.SOUTH) ? horizonLine : horizonNoLine;
				}
				if (j == numOfCols - 1) {
					mazeInfo[i * 2][j * 2 + 2] = corner;
					mazeInfo[i * 2 + 1][j * 2 + 2] = c.getWalls().get(Direction.EAST) ? verticalLine : verticalNoLine;
				}
				if (c.getLocation().equals(getPlayerLocation())) {
					mazeInfo[i * 2 + 1][j * 2 + 1] = playerLogo;
				} else if (c.getLocation().equals(goalLocation)) {
					mazeInfo[i * 2+ 1][j * 2 + 1] = goalLogo;
				} else if (c.getLocation().equals(startLocation)) {
					mazeInfo[i * 2 + 1][j * 2+ 1] = startLogo;
				} else if (c.getHasGoldCoin()) {
					mazeInfo[i * 2 + 1][j * 2 + 1] = goldLogo;
				} else if (c.getHasThief()) {
					mazeInfo[i * 2 + 1][j * 2+ 1] = thiefLogo;
				} else {
					mazeInfo[i * 2 + 1][j * 2 + 1] = noItem;
				}
			}
		}
		mazeInfo[numOfRows * 2][numOfCols * 2] = corner;

		for (String[] strings : mazeInfo) {
			StringBuilder line = new StringBuilder();
			for(String s: strings) {
				line.append(s);
			}
			message.append(line).append("\n");
		}

		return String.valueOf(message);
	}
}
