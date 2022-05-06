import java.util.*;

public final class WrapRoomMaze extends GeneralMaze {
	public WrapRoomMaze(int numOfRows,
	                     int numOfCols,
	                     int numOfRemainingWalls,
	                     Location startLocation,
	                     Location goalLocation) {
		super(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
	}

	@Override
	protected boolean isNumOfRemainingWallsValid(int numOfRows, int numOfCols, int numOfRemainingWalls) {
		return numOfRemainingWalls >= 0 &&
				numOfRemainingWalls <= MazeHelper.calculateMaxRemainingWalls(numOfRows, numOfCols) - 2;
	}

	@Override
	protected void continueRemoveWalls(int remainedWalls, List<Edge> innerEdgeList, List<List<Cell>> grid) {
		List<Edge> borderEdgeList = generateBorderEdgeList();
		// First continue remove border edges. Each time decrease the count by 2.
		while (remainedWalls > (this.numOfRemainingWalls + 2) && !borderEdgeList.isEmpty()) {
			int[] IDList = randomSelectAndRemoveEdges(borderEdgeList);
			tearDownBorderWall(IDList[0], IDList[1], grid);
			remainedWalls -= 2;
		}
		// Second: if there is no border edges remained, continue remove inner edges until count == numOfRemainingWalls.
		// Each time decrease the count by 1.
		if(borderEdgeList.isEmpty()) {
			while (remainedWalls > this.numOfRemainingWalls) {
				int[] IDList = randomSelectAndRemoveEdges(innerEdgeList);
				tearDownInnerWall(IDList[0], IDList[1], grid);
				remainedWalls--;
			}
		} else { // The number of need-to-remove edges is less than or equal to 2.
			// If there is only one edge needed to be removed, remove inner edges.
			if (Math.abs(this.numOfRemainingWalls - remainedWalls) == 1) {
				int[] IDList = randomSelectAndRemoveEdges(innerEdgeList);
				tearDownInnerWall(IDList[0], IDList[1], grid);
			} else { //Otherwise, there is two edges needed to be removed, remove border edges.
				int[] IDList = randomSelectAndRemoveEdges(borderEdgeList);
				tearDownBorderWall(IDList[0], IDList[1], grid);
			}
		}
	}

	@Override
	protected void movePlayerToNextCell(Direction playerDirectionInput) {
		if (playerDirectionInput == Direction.NORTH && getPlayerLocation().getI() == 0){
			movePlayerToOppositeCell(this.numOfRows, Direction.SOUTH);
		} else if (playerDirectionInput == Direction.SOUTH && getPlayerLocation().getI() == numOfRows - 1) {
			movePlayerToOppositeCell(this.numOfRows, Direction.NORTH);
		} else if (playerDirectionInput == Direction.WEST && getPlayerLocation().getJ() == 0){
			movePlayerToOppositeCell(this.numOfCols, Direction.EAST);
		} else if (playerDirectionInput == Direction.EAST && getPlayerLocation().getJ() == numOfCols - 1) {
			movePlayerToOppositeCell(this.numOfCols, Direction.WEST);
		} else {
			this.player.move(playerDirectionInput);
		}
	}

	@Override
	protected int[] moveInSolveMaze(int i, int j, Direction d){
		if (d == Direction.NORTH && i == 0){
			return moveToOppositeCell(this.numOfRows, i, j, Direction.SOUTH);
		} else if (d == Direction.SOUTH && i == numOfRows - 1) {
			return moveToOppositeCell(this.numOfRows, i, j, Direction.NORTH);
		} else if (d == Direction.WEST && j == 0){
			return moveToOppositeCell(this.numOfCols, i, j, Direction.EAST);
		} else if (d == Direction.EAST && j == numOfCols - 1) {
			return moveToOppositeCell(this.numOfCols, i, j, Direction.WEST);
		} else {
			return moveToNextCell(i, j, d);
		}
	}

	private int[] moveToOppositeCell(int times, int i, int j,  Direction d) {
		int[] res = {i, j};
		for(int k = 0; k < times - 1; k++) {
			res = moveToNextCell(res[0], res[1], d);
		}
		return res;
	}

	/**
	 * Generate the border Edge List of the grid. Border edge is the edge between the cell on one side and the cell on
	 * the `wrap` side. Each cell is on top/bottom/left/right of the maze.
	 * @return a list of {@code Edge}.
	 */
	private List<Edge> generateBorderEdgeList() {
		List<Edge> borderEdgeList = new ArrayList<>();
		for (int i = 0; i < this.numOfRows; i++) {
			int sourceID = MazeHelper.idxToID(i, 0, this.numOfCols);
			int destID = MazeHelper.idxToID(i, this.numOfCols - 1, this.numOfCols);
			Edge e = new Edge(sourceID, destID);
			borderEdgeList.add(e);
		}
		for (int j = 0; j < this.numOfCols; j++) {
			int sourceID = MazeHelper.idxToID(0, j, this.numOfCols);
			int destID = MazeHelper.idxToID(this.numOfRows - 1, j, this.numOfCols);
			Edge e = new Edge(sourceID, destID);
			borderEdgeList.add(e);
		}

		return borderEdgeList;
	}

	/**
	 * Tear down the wall on the border. `source` cell and `dest` cell are on the opposite side.
	 * @param sourceID the source ID of an edge.
	 * @param destID the destination ID of the same edge.
	 * @param grid a 2D-Array, maze grid generated in progress.
	 */
	private void tearDownBorderWall(int sourceID, int destID, List<List<Cell>> grid) {
		int sourceI = MazeHelper.IDToLocation(sourceID, numOfCols).getI();
		int sourceJ = MazeHelper.IDToLocation(sourceID, numOfCols).getJ();
		int destI = MazeHelper.IDToLocation(destID, numOfCols).getI();
		int destJ = MazeHelper.IDToLocation(destID, numOfCols).getJ();
		if (sourceI == destI) {
			grid.get(sourceI).get(sourceJ).tearDownWall(Direction.WEST);
			grid.get(destI).get(destJ).tearDownWall(Direction.EAST);
		} else if (sourceJ == destJ) {
			grid.get(sourceI).get(sourceJ).tearDownWall(Direction.NORTH);
			grid.get(destI).get(destJ).tearDownWall(Direction.SOUTH);
		}
	}

	/**
	 * Move the player to the opposite side of the maze.
	 * @param times moving times.
	 * @param playerDirectionInput the direction the player moves to.
	 */
	private void movePlayerToOppositeCell(int times, Direction playerDirectionInput) {
		for(int i = 0; i < times - 1; i++) {
			this.player.move(playerDirectionInput);
		}
	}
}

