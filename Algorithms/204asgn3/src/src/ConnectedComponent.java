import java.util.HashMap;

public class ConnectedComponent {
	private HashMap<String, Boolean> marked;
	private HashMap<String, Integer> id;
	private int count;

	public ConnectedComponent(EdgeWeightedGraph G) {

		marked = new HashMap<String, Boolean>();
		id = new HashMap<String, Integer>();

		for (String vertex : G.getV()) {
			marked.put(vertex, false);
		}

		for (String vertex : G.getV()) {
			if (!marked.get(vertex)) {
				dfs(G, vertex);
				count++;
			}
		}

	}

	public int count() {
		return count;
	}

	public int id(String v) {
		return id.get(v);
	}

	private void dfs(EdgeWeightedGraph G, String v) {
		marked.put(v, true);
		id.put(v, count);
		for (Edge w : G.adj(v)) {
			String vertex1 = w.either();
			String vertex2 = w.other(vertex1);
			if (!marked.get(vertex2)){
				dfs(G, vertex2);
			}
			else if(!marked.get(vertex1)){
				dfs(G,vertex1);
			}
		}
	}

	public int getCount() {
		return count;
	}


	
	
	
}
