import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
// using swing library
public class SwingView implements View{
	private final SwingPanel panel;
	protected final int frameWidth;
	protected final int frameHeight;

	/**
	 * Constructs a {@code SwingView} object with a {@code JFrame} and {@code SwingPanel}.
	 *
	 * @param frameWidth the width of the frame. Associated with maze size.
	 * @param frameHeight the height of the frame. Associated with maze size.
	 */
	public SwingView(int frameWidth, int frameHeight) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		JFrame frame = new JFrame("My Maze Game");
		panel = new SwingPanel(frameHeight);
		panel.setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void addKeyListener(KeyListener keyListener) {
		panel.addKeyListener(keyListener);
	}

	@Override
	public void paint(boolean isGameOver, List<List<Location>> wallsLocation, List<Location> goldCoinLocation,
	                  List<Location> thiefLocation, Location startLocation, Location goalLocation,
	                  Location playerLocation, String nextPossibleMove, String playerStatus, List<Location> solveLocation) {
		panel.paint(isGameOver, wallsLocation, goldCoinLocation, thiefLocation, startLocation, goalLocation,
				playerLocation, nextPossibleMove, playerStatus, solveLocation);
	}
}
