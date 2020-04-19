
public class Diamond extends Jewel {

	public Diamond(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		this.point = 30;
		this.name = 'D';
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

		// Check 1
		if (GameGrid.isValid(this.xCoord - 1, this.yCoord - 1)
				&& GameGrid.grid[this.xCoord - 1][this.yCoord - 1] != from
				&& GameGrid.grid[this.xCoord - 1][this.yCoord - 1].name == 'D' && (direction == 0 || direction == 1)
				&& GameGrid.grid[this.xCoord - 1][this.yCoord - 1].check(from, this, found, 1))
			return true;

		// Check 9
		if (GameGrid.isValid(this.xCoord + 1, this.yCoord + 1)
				&& GameGrid.grid[this.xCoord + 1][this.yCoord + 1] != from
				&& GameGrid.grid[this.xCoord + 1][this.yCoord + 1].name == 'D' && (direction == 0 || direction == 9)
				&& GameGrid.grid[this.xCoord + 1][this.yCoord + 1].check(from, this, found, 9))
			return true;
		// Check 3
		if (GameGrid.isValid(this.xCoord - 1, this.yCoord + 1)
				&& GameGrid.grid[this.xCoord - 1][this.yCoord + 1] != from
				&& GameGrid.grid[this.xCoord - 1][this.yCoord + 1].name == 'D' && (direction == 0 || direction == 3)
				&& GameGrid.grid[this.xCoord - 1][this.yCoord + 1].check(from, this, found, 3))
			return true;

		// Check 7
		if (GameGrid.isValid(this.xCoord + 1, this.yCoord - 1)
				&& GameGrid.grid[this.xCoord + 1][this.yCoord - 1] != from
				&& GameGrid.grid[this.xCoord + 1][this.yCoord - 1].name == 'D' && (direction == 0 || direction == 7)
				&& GameGrid.grid[this.xCoord + 1][this.yCoord - 1].check(from, this, found, 7))
			return true;

		found--;
		return false;
	}

}
