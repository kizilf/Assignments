
public class Edge implements Comparable<Edge> {
	
	private final String v, w;
	private final double weight;

	public Edge(String v, String w, double weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	public String either() {
		return v;
	}

	public String other(String vertex) {
		if (vertex == v)
			return w;
		else
			return v;
	}

	public int compareTo(Edge that) {
		if (this.weight < that.weight)
			return -1;
		else if (this.weight > that.weight)
			return +1;
		else
			return 0;
	}

	public double getWeight() {
		return weight;
	}
	
	
}