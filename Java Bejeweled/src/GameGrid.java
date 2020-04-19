
public class GameGrid {
	private static int rowNumber;
	private static int columnNumber;
	static Jewel[][] grid;

	public GameGrid(int row, int column) {

		super();
		GameGrid.rowNumber = row;
		GameGrid.columnNumber = column;
		grid = new Jewel[row][column];
	}

	//According to type this function creates the jewel and puts it to board
	public void addJewel(int row, int column, String type) {
		if (type.equals("D"))
			grid[row][column] = new Diamond(row, column);
		else if (type.equals("S"))
			grid[row][column] = new Square(row, column);
		else if (type.equals("W"))
			grid[row][column] = new Wildcard(row, column);
		else if (type.equals("T"))
			grid[row][column] = new Triangle(row, column);
		else if (type.equals("+"))
			grid[row][column] = new Plus(row, column);
		else if (type.equals("-"))
			grid[row][column] = new Minus(row, column);
		else if (type.equals("/"))
			grid[row][column] = new Slash(row, column);
		else if (type.equals("\\"))
			grid[row][column] = new BackSlash(row, column);
		else if (type.equals("|"))
			grid[row][column] = new VerticalBar(row, column);
	}
	
	//This function makes jewels fall after a deletion.
	//The way it does that is, it always swims the places with null to top.
	//In the end all empty spaces will be at the top hence jewel will fall.
	public void updateBoard() {
		for (int rowCount = rowNumber - 1; rowCount >= 0; rowCount--) {
			for (int columnCount = columnNumber - 1; columnCount >= 0; columnCount--) {
				Jewel j1 = grid[rowCount][columnCount];
				if (j1 == null)
					checkTheColumn(rowCount, columnCount);
			}
		}
	}

	private static void checkTheColumn(int row, int column) {
		while (isValid(row - 1, column) == true) {
			if (grid[row - 1][column] != null) {
				
				grid[row][column] = grid[row - 1][column];
				grid[row][column].xCoord = row;
				grid[row][column].yCoord = column;
				
				grid[row - 1][column] = null;
				
			}
			row--;
		}
	}

	//Checks if given coordinate is out of bounds
	public static boolean isValid(int x, int y) {
		if (x > -1 && x < columnNumber ) {
			if (y > -1 && y < rowNumber  )
				return true;
		}

		return false;
	}
	
	//Simply displays the board with requested format(spaces)
	public void viewBoard() {
		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < columnNumber; j++) {
				if (grid[i][j] != null)
					System.out.print(grid[i][j].getName() + " ");
				else {
					System.out.print("  ");
				}

			}
			System.out.println();
		}

	}

	public Jewel getJewelAtPosition(int x, int y) {
		return grid[x][y];
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

}
