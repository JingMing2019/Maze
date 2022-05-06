import java.util.List;
import java.util.Random;

public final class PerfectMaze extends GeneralMaze{
	public PerfectMaze(int numOfRows,
	                int numOfCols,
	                Location startLocation,
	                Location goalLocation) {
		super(numOfRows, numOfCols, MazeHelper.calculateMaxRemainingWalls(numOfRows, numOfCols), startLocation, goalLocation);
	}

	@Override
	protected boolean isNumOfRemainingWallsValid(int numOfRows, int numOfCols, int numOfRemainingWalls) {
		return true;
	}

	@Override
	protected void continueRemoveWalls(int remainedWalls, List<Edge> innerEdgeList, List<List<Cell>> grid) {
	}
}
