import java.util.List;

public final class RoomMaze extends GeneralMaze {
	public RoomMaze(int numOfRows,
	                    int numOfCols,
	                    int numOfRemainingWalls,
	                    Location startLocation,
	                    Location goalLocation) {
		super(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
	}

	@Override
	protected boolean isNumOfRemainingWallsValid(int numOfRows, int numOfCols, int numOfRemainingWalls) {
		return numOfRemainingWalls >= calculateBorderWalls(numOfRows, numOfCols)
				&& numOfRemainingWalls <= MazeHelper.calculateMaxRemainingWalls(numOfRows, numOfCols) - 1;
	}

	@Override
	protected void continueRemoveWalls(int remainedWalls, List<Edge> innerEdgeList, List<List<Cell>> grid) {
		while (remainedWalls > this.numOfRemainingWalls) {
			int[] IDList = randomSelectAndRemoveEdges(innerEdgeList);
			tearDownInnerWall(IDList[0], IDList[1], grid);
			remainedWalls--;
		}
	}

	private int calculateBorderWalls(int numOfRows, int numOfCols) {
		return numOfRows * 2 + numOfCols * 2;
	}
}

