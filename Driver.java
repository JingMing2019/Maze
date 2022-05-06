import java.util.Scanner;

class Driver {
	public static void main(String[] args) {
		// arg 0: print -> print maze; draw -> draw image; solve -> automatic solve maze and draw
		// arg 1: non_wrap -> no wrap; wrap -> wrap.
		// arg 2: perfect -> perfect maze; room -> room maze.
		// arg 3: number of rows in the maze.
		// arg 4: number of columns in the maze.
		// arg 5: index i of starting point. [0, numOfRows - 1]
		// arg 6: index j of starting point. [0, numOfCols - 1]
		// arg 7: index i of goal location. [0, numOfRows - 1]
		// arg 8: index j of goal location. [0, numOfCols - 1]
		// arg 9: number of remaining walls. for perfect maze, this value can be any value.
		String howToDealWithMaze = args[0];
		String isWrap = args[1];
		String type = args[2];
		int numOfRows = Integer.parseInt(args[3]);
		int numOfCols = Integer.parseInt(args[4]);
		int startIdxI = Integer.parseInt(args[5]); // Top-Left -> 0
		int startIdxJ = Integer.parseInt(args[6]); // Top-Left -> 0
		int goalIdxI = Integer.parseInt(args[7]); // Bottom-Right -> numOfRows - 1
		int goalIdxJ = Integer.parseInt(args[8]); // Bottom-Right -> numOfCols - 1
		int numOfRemainingWalls = Integer.parseInt(args[9]);

		// Translate starting point and goal location's index to its location on the canvas.
		Location startLocation = new Location(startIdxI, startIdxJ);
		Location goalLocation = new Location(goalIdxI, goalIdxJ);
		// Generate specific maze.
		Maze maze;
		if (type.equals("perfect") && isWrap.equals("non_wrap")) {
			maze = new PerfectMaze(numOfRows, numOfCols, startLocation, goalLocation);
		} else if (type.equals("room") && isWrap.equals("non_wrap")) {
			maze = new RoomMaze(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
		} else if (type.equals("room") && isWrap.equals("wrap")) {
			maze = new WrapRoomMaze(numOfRows, numOfCols, numOfRemainingWalls, startLocation, goalLocation);
		} else {
			throw new IllegalArgumentException("Unsupported maze type.");
		}
		System.out.println("The total remained walls in the maze: " + maze.countRemainedWalls());

		// Print maze, move the player in the terminal using scanner.
		// Copy below to the Edit Configuration:
		// Case 1: perfect maze     ->  print non_wrap perfect 4 6 0 0 3 5 0    //last one: any number
		// Case 2: room maze        ->  print non_wrap room 4 6 0 0 3 5 34      //last one: 20 - 34
		// Case 3: wrap room maze   ->  print wrap room 4 6 0 0 3 5 18          //last one: 0 - 33
		if (howToDealWithMaze.equals("print")) {
			System.out.println(maze);
			Scanner s = new Scanner(System.in);
			while (!maze.isGameOver()) {
				System.out.println(maze.printNextPossibleMove());
				System.out.println(maze.printPlayerStatus());
				System.out.println("Please choose a direction: ");
				String line = s.nextLine();
				Direction d = Direction.valueOf(line);
				System.out.println("Player moves to: " + d);
				try {
					maze.movePlayer(d);
				}catch(Exception e) {
					System.out.println(e);
				}
				System.out.println(maze); // this is optional
			}
			System.out.println(maze.printPlayerStatus());
			System.out.println("Game is over.");
		}

		// Draw maze and play the maze game with keyboard.
		// Case 1: perfect maze     ->  draw non_wrap perfect 40 40 0 0 39 39 0
		// Case 2: room maze        ->  draw non_wrap room 10 12 0 0 9 11 110
		// Case 3: wrap room maze   ->  draw wrap room 10 12 0 0 9 11 110
		if (howToDealWithMaze.equals("draw")) {
			int frameWidth = (numOfCols + 8) * Parameters.CELL_SIZE;
			int frameHeight = (numOfRows + 6) * Parameters.CELL_SIZE;

			View view = new SwingView(frameWidth, frameHeight);
			Controller c = new Controller(maze, view, false);
			c.startGame();
		}

		// solve and draw the maze. player can still play this maze game.
		// Case 1: perfect maze         ->  solve non_wrap perfect 40 40 0 0 39 39 0
		// Case 2: wrap room maze       ->  solve wrap room 10 12 0 0 9 11 110
		if (howToDealWithMaze.equals("solve")) {
			int frameWidth = (numOfCols + 8) * Parameters.CELL_SIZE;
			int frameHeight = (numOfRows + 6) * Parameters.CELL_SIZE;

			View view = new SwingView(frameWidth, frameHeight);
			Controller c = new Controller(maze, view, true);
			c.startGame();
		}

	}
}