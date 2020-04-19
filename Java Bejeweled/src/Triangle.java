
public class Triangle extends Jewel {

	public Triangle(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		this.name = 'T';
		this.point = 15;
	}

	@Override
	public boolean check(Jewel fromfrom, Jewel from, int found, int direction) {

		found++;
		if (found == 3) {
			int score = this.getPoint() + from.getPoint() + fromfrom.getPoint();
			Main.score = Main.score + score;
			System.out.println("\nScore: " + score);
			GameGrid.grid[this.xCoord][this.yCoord] = null;
			GameGrid.grid[from.xCoord][from.yCoord] = null;
			GameGrid.grid[fromfrom.xCoord][fromfrom.yCoord] = null;
			return true;
		}

		// Check 2
		if (GameGrid.isValid(this.xCoord - 1, this.yCoord) && GameGrid.grid[this.xCoord - 1][this.yCoord] != from
				&& GameGrid.grid[this.xCoord - 1][this.yCoord].name == 'T' && (direction == 0 || direction == 2)
				&& GameGrid.grid[this.xCoord - 1][this.yCoord].check(from, this, found, 2))
			return true;

		// Check 8
		if (GameGrid.isValid(this.xCoord + 1, this.yCoord) && GameGrid.grid[this.xCoord + 1][this.yCoord] != from
				&& GameGrid.grid[this.xCoord + 1][this.yCoord].name == 'T' && (direction == 0 || direction == 8)
				&& GameGrid.grid[this.xCoord + 1][this.yCoord].check(from, this, found, 8))
			return true;
		found--;

		// // Check for triplet
		// if (GameGrid.isValid(this.xCoord - 1, this.yCoord) &&
		// GameGrid.grid[this.xCoord - 1][this.yCoord].name == 'T'
		// && GameGrid.isValid(this.xCoord + 1, this.yCoord)
		// && GameGrid.grid[this.xCoord + 1][this.yCoord].name == 'T') {
		//
		// Main.score += 3 * this.point;
		// GameGrid.grid[this.xCoord][this.yCoord] = null;
		// GameGrid.grid[this.xCoord - 1][this.yCoord] = null;
		// GameGrid.grid[this.xCoord + 1][this.yCoord] = null;
		// GameGrid.updateBoard();
		// GameGrid.updateBoard();
		// return true;
		// }
		//
		// // Check 2
		// if (GameGrid.isValid(this.xCoord - 1, this.yCoord) &&
		// GameGrid.grid[this.xCoord - 1][this.yCoord] != from
		// && GameGrid.grid[this.xCoord - 1][this.yCoord].name == 'T'
		// && GameGrid.grid[this.xCoord - 1][this.yCoord].check(this))
		// return true;
		// // Check 8
		// if (GameGrid.isValid(this.xCoord + 1, this.yCoord) &&
		// GameGrid.grid[this.xCoord + 1][this.yCoord] != from
		// && GameGrid.grid[this.xCoord + 1][this.yCoord].name == 'T'
		// && GameGrid.grid[this.xCoord + 1][this.yCoord].check(this))
		// return true;
		//
		return false;

	}

}
