
public class Square extends Jewel {

	public Square(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		this.name = 'S';
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

		// Check 4
		if (GameGrid.isValid(this.xCoord, this.yCoord - 1) && GameGrid.grid[this.xCoord][this.yCoord - 1] != from
				&& GameGrid.grid[this.xCoord][this.yCoord - 1].name == 'S' && (direction == 0 || direction == 4)
				&& GameGrid.grid[this.xCoord][this.yCoord - 1].check(from, this, found, 4))
			return true;

		// Check 6
		if (GameGrid.isValid(this.xCoord, this.yCoord + 1) && GameGrid.grid[this.xCoord][this.yCoord + 1] != from
				&& GameGrid.grid[this.xCoord][this.yCoord + 1].name == 'S' && (direction == 0 || direction == 6)
				&& GameGrid.grid[this.xCoord][this.yCoord + 1].check(from, this, found, 6))
			return true;
		found--;

		return false;

	}

}
