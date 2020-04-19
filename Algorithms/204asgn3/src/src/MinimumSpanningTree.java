import java.util.*;

public class MinimumSpanningTree {

	private EdgeWeightedGraph G;
	private HashMap<String, Boolean> marked; // MST vertices
	private Queue<Edge> mst; // MST edges
	private PriorityQueue<Edge> pq; // PQ of edges

	public MinimumSpanningTree(EdgeWeightedGraph G) {
		this.G = G;
		Comparator<Edge> comparator = new DoubleWeightComperator();
		pq = new PriorityQueue<Edge>(G.getV().size(), comparator);
		mst = new PriorityQueue<Edge>(G.getV().size(), comparator);
		marked = new HashMap<String, Boolean>();

		
		
		for(String vertex: G.getV()){
			marked.put(vertex, false);
		}
		
		visit(G, G.getV().get(0));

		while (!pq.isEmpty()) {
			Edge e = pq.poll();
			String v = e.either();
			String w = e.other(v);
			if (marked.get(v) && marked.get(w))
				continue;
			mst.add(e);
			if (!marked.get(v))
				visit(G, v);
			if (!marked.get(w))
				visit(G, w);
		}
	}

	

	public Queue<Edge> getMst() {
		return mst;
	}

	public EdgeWeightedGraph getG() {
		return G;
	}



	private void visit(EdgeWeightedGraph G, String v) {
		marked.put(v,true);
		for (Edge e : G.adj(v))
			if(!marked.get(e.other(v)))
				pq.add(e);
	}

}


