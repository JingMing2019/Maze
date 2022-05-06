/**
 * The {@code MazeHelper} class contains helper methods used in the `Maze` project. Including the conversion between
 * the index(i, j), the {@code Location} and the ID, the calculation of x percent of total value in integer and the
 * calculation of maximum remaining walls.
 */

public class MazeHelper {
	static int idxToID(int i, int j, int numOfCols) {
		return i * numOfCols + j;
	}

	static int LocationToID(Location l, int numOfCols) {
		return l.getI() * numOfCols + l.getJ();
	}

	static Location IDToLocation(int ID, int numOfCols) {
		return new Location(ID / numOfCols,ID % numOfCols);
	}

	static int getXPercentageInInt(int totalVal, double percentage) {
		return (int) Math.ceil(totalVal * percentage);
	}

	static int calculateMaxRemainingWalls(int numOfRows, int numOfCols) {
		// numOfCols: C
		// numOfRemainingWalls: RW
		// numOfTotalWalls(Border + walls): TW = 2R*C + R + C
		// According to the Maze Document, it says perfect Maze has maximum remaining walls
		// max(RW) = TW - R*C + 1
		// RW = R*C + R + C + 1
		int numOfTotalWalls = 2 * numOfRows * numOfCols + numOfRows + numOfCols;
		return numOfTotalWalls - numOfRows * numOfCols + 1;
	}
}
