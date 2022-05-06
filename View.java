/**
 * Specifies operations for painting the maze and listening to keyboard input.
 */

import java.awt.event.KeyListener;
import java.util.List;

public interface View {
	/**
	 * Paint the maze on the canvas using JPanel.
	 * @param isGameOver true if the game is over, otherwise false.
	 * @param wallsLocation a 2D list of {@code Location}. Each wall is a line between point(i1, j1) and point(i2, j2).
	 * @param goldCoinLocation a list of {@code Location} where stand gold coins.
	 * @param thiefLocation a list of {@code Location} where stand thieves.
	 * @param startLocation {@code Location} of start point in the maze.
	 * @param goalLocation {@code Location} of goal in the maze.
	 * @param playerLocation {@code Location} of player in the maze.
	 * @param nextPossibleMove a string describing the player's next possible move({@code Direction}).
	 * @param playerStatus a string describing the player's current location and his/her collected gold coins.
	 * @param solveLocation a list of {@code Location} which is the solved path of the maze.
	 */
	void paint(boolean isGameOver, List<List<Location>> wallsLocation, List<Location> goldCoinLocation,
	           List<Location> thiefLocation, Location startLocation, Location goalLocation,
	           Location playerLocation, String nextPossibleMove, String playerStatus, List<Location> solveLocation);

	/**
	 * Make the panel listen to keyboard.
	 * @param keyListener a object implements KeyListener.
	 */
	void addKeyListener(KeyListener keyListener);
}
