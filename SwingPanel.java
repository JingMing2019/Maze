import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SwingPanel extends JPanel {
	private final int frameHeight;
	private boolean isGameOver;
	private List<List<Location>> wallsLocation;
	private List<Location> goldCoinLocation;
	private List<Location> thiefLocation;
	private Location startLocation;
	private Location goalLocation;
	private Location playerLocation;
	private List<Location> solveLocation;
	private String nextPossibleMove;
	private String playerStatus;
	private Image startLocationImage;
	private Image goalLocationImage;
	private Image playerImage;
	private Image goldImage;
	private Image thiefImage;
	private Image solveLocationImage;
	private boolean initialized = false;

	/**
	 * Constructs a {@code SwingPanel} object with the loaded image. This panel is set to receive focus.
	 * @param frameHeight the height of the frame. Used to define the location of painted string.
	 */
	public SwingPanel(int frameHeight) {
		this.frameHeight = frameHeight;
		// load images
		try {
			playerImage = ImageIO.read(new File("./img/player.png"));
			goldImage = ImageIO.read(new File("./img/gold.png"));
			thiefImage = ImageIO.read(new File("./img/thief.png"));
			startLocationImage = ImageIO.read(new File("./img/start.png"));
			goalLocationImage = ImageIO.read(new File("./img/goal.png"));
			solveLocationImage = ImageIO.read(new File("./img/solve.png"));
		} catch (IOException e) {
			System.out.println("File doesn't exist");
		}
		// Make this panel receive the focus.
		this.setFocusable(true);
	}

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
	public void paint(boolean isGameOver, List<List<Location>> wallsLocation, List<Location> goldCoinLocation,
	                  List<Location> thiefLocation, Location startLocation, Location goalLocation,
	                  Location playerLocation, String nextPossibleMove, String playerStatus, List<Location> solveLocation) {
		this.isGameOver = isGameOver;
		this.wallsLocation = wallsLocation;
		this.goldCoinLocation = goldCoinLocation;
		this.thiefLocation = thiefLocation;
		this.startLocation = startLocation;
		this.goalLocation = goalLocation;
		this.playerLocation = playerLocation;
		this.nextPossibleMove = nextPossibleMove;
		this.playerStatus = playerStatus;
		this.solveLocation = solveLocation;
		this.initialized = true;

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!initialized) {
			return;
		}
		if (isGameOver) {
			// draw game over string
			drawMaze(g);
			g.drawString("You Win!", Parameters.CELL_SIZE , frameHeight - Parameters.CELL_SIZE  * 4);
			g.drawString(playerStatus, Parameters.CELL_SIZE , frameHeight - Parameters.CELL_SIZE  * 2);
			return;
		}

		drawMaze(g);
		g.drawString(nextPossibleMove, Parameters.CELL_SIZE , frameHeight - Parameters.CELL_SIZE  * 4);
		g.drawString(playerStatus, Parameters.CELL_SIZE , frameHeight - Parameters.CELL_SIZE  * 2);
	}

	/**
	 * Draw the maze walls/gold coins/thieves/starting point/goal/player.
	 * @param g the graphic drawn on the canvas.
	 */
	private void drawMaze(Graphics g) {
		for(List<Location> points: wallsLocation) {
			Location startPoint = points.get(0);
			Location endPoint = points.get(1);
			g.drawLine(LocationJToCoordinateX(startPoint.getJ()), LocationIToCoordinateY(startPoint.getI()),
					LocationJToCoordinateX(endPoint.getJ()), LocationIToCoordinateY(endPoint.getI()));
		}
		for(Location l: solveLocation) {
			g.drawImage(solveLocationImage, LocationJToCoordinateX(l.getJ()), LocationIToCoordinateY(l.getI()), this);
		}
		for(Location l: goldCoinLocation) {
			g.drawImage(goldImage, LocationJToCoordinateX(l.getJ()), LocationIToCoordinateY(l.getI()), this);
		}
		for(Location l: thiefLocation) {
			g.drawImage(thiefImage, LocationJToCoordinateX(l.getJ()), LocationIToCoordinateY(l.getI()), this);
		}

		g.drawImage(startLocationImage, LocationJToCoordinateX(startLocation.getJ()),
				LocationIToCoordinateY(startLocation.getI()),this);
		g.drawImage(goalLocationImage, LocationJToCoordinateX(goalLocation.getJ()),
				LocationIToCoordinateY(goalLocation.getI()), this);
		g.drawImage(playerImage, LocationJToCoordinateX(playerLocation.getJ()),
				LocationIToCoordinateY(playerLocation.getI()),this);
	}

	/**
	 * Convert the {@code Location} `j` of each item to its corresponding X coordinate on the canvas.
	 * @param j the field i in the {@code Location} of the item.
	 * @return the X coordinate of this item on the canvas.
	 */
	private int LocationJToCoordinateX(int j) {
		return j * Parameters.CELL_SIZE + Parameters.TOP_LEFT_X;
	}

	/**
	 * Convert the {@code Location} `i` of each item to its corresponding Y coordinate on the canvas.
	 * @param i the field i in the {@code Location} of the item.
	 * @return the Y coordinate of this item on the canvas.
	 */
	private int LocationIToCoordinateY(int i) {
		return i * Parameters.CELL_SIZE + Parameters.TOP_LEFT_Y;
	}
}
