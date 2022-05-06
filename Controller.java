import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_RIGHT;


public class Controller implements KeyListener{
	private final Maze maze;
	private final View view;
	private final boolean isSolve;

	public Controller(Maze maze, View view, boolean isSolve) {
		this.maze = maze;
		this.view = view;
		this.view.addKeyListener(this);
		this.isSolve = isSolve;
	}

	public void startGame() {
		paintMaze();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if (key == VK_UP) {
			maze.movePlayer(Direction.NORTH);
		} else if (key == VK_DOWN) {
			maze.movePlayer(Direction.SOUTH);
		} else if (key == VK_LEFT) {
			maze.movePlayer(Direction.WEST);
		} else if (key == VK_RIGHT) {
			maze.movePlayer(Direction.EAST);
		}
		paintMaze();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private void paintMaze() {
		// get the latest game status
		boolean isGameOver = maze.isGameOver();
		List<List<Location>> wallsLocation = maze.getWallsLocation();
		List<Location> goldCoinLocation = maze.getGoldCoinLocation();
		List<Location> thiefLocation = maze.getThiefLocation();
		Location startLocation = maze.getStartLocation();
		Location goalLocation = maze.getGoalLocation();
		Location playerLocation = maze.getPlayerLocation();
		String nextPossibleMove = maze.printNextPossibleMove();
		String playerStatus = maze.printPlayerStatus();
		List<Location> solveLocation = isSolve? maze.getSolveLocation() : new ArrayList<>();

		// ask the view to visualize the game status
		view.paint(isGameOver, wallsLocation, goldCoinLocation, thiefLocation, startLocation, goalLocation,
				playerLocation, nextPossibleMove, playerStatus, solveLocation);
	}
}
