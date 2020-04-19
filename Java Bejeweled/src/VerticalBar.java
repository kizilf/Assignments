
public class VerticalBar extends Jewel {

	public VerticalBar(int xCoord, int yCoord) {
		super(xCoord, yCoord);
		this.name = '|';
		this.point = 20;
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
		boolean name;

		// Check 2
		if (GameGrid.isValid(this.xCoord - 1, this.yCoord)) {

			name = GameGrid.grid[this.xCoord - 1][this.yCoord].name == '+'
					|| GameGrid.grid[this.xCoord - 1][this.yCoord].name == '-'
					|| GameGrid.grid[this.xCoord - 1][this.yCoord].name == '/'
					|| GameGrid.grid[this.xCoord - 1][this.yCoord].name == '\\'
					|| GameGrid.grid[this.xCoord - 1][this.yCoord].name == '|';

			if (GameGrid.grid[this.xCoord - 1][this.yCoord] != from && name && (direction == 0 || direction == 2)
					&& GameGrid.grid[this.xCoord - 1][this.yCoord].check(from, this, found, 2))
				return true;
		}

		// Check 8
		if (GameGrid.isValid(this.xCoord + 1, this.yCoord)) {
			name = GameGrid.grid[this.xCoord + 1][this.yCoord].name == '+'
					|| GameGrid.grid[this.xCoord + 1][this.yCoord].name == '-'
					|| GameGrid.grid[this.xCoord + 1][this.yCoord].name == '/'
					|| GameGrid.grid[this.xCoord + 1][this.yCoord].name == '\\'
					|| GameGrid.grid[this.xCoord + 1][this.yCoord].name == '|';

			if (GameGrid.grid[this.xCoord + 1][this.yCoord] != from && name && (direction == 0 || direction == 8)
					&& GameGrid.grid[this.xCoord + 1][this.yCoord].check(from, this, found, 8))
				return true;
		}
		return false;
	}

}
