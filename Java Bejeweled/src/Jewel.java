
public abstract class Jewel {
	protected int xCoord;
	protected int yCoord;
	protected char name;
	protected int point;

	public Jewel(int xCoord, int yCoord) {
		super();
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public int getxCoord() {
		return xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public char getName() {
		return name;
	}

	public int getPoint() {
		return point;
	}

	//All jewel classes will have a check function but each will does a different job
	public abstract boolean check(Jewel fromfrom,Jewel from,int found,int direction);


}
