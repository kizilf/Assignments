import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class EdgeWeightedGraph {
	private final ArrayList<String> V;
	private final HashMap<String, ArrayList<Edge>> adjmap;

	public EdgeWeightedGraph(ArrayList<String> allWords) {
		this.V = allWords;
		adjmap = new HashMap<String, ArrayList<Edge>>();
		for (String s : V) {
			adjmap.put(s, new ArrayList<Edge>());
		}
	}

	public void addEdge(Edge e) {
		String v = e.either();
		String w = e.other(v);

		ArrayList<Edge> edgesV = this.adjmap.get(v);
		ArrayList<Edge> edgesW = this.adjmap.get(w);

		if (!edgesW.contains(e)) {
			edgesV.add(e);
			edgesW.add(e);
		}

	}

	public void delEdge(Edge e) {

		String v = e.either();
		String w = e.other(v);

		ArrayList<Edge> edgesV = this.adjmap.get(v);
		ArrayList<Edge> edgesW = this.adjmap.get(w);

		if (edgesW.contains(e)) {
			edgesV.remove(e);
			edgesW.remove(e);
		}

	}

	public Iterable<Edge> adj(String v) {
		return this.adjmap.get(v);
	}

	public void printEdges(FileWriter fw) throws IOException {
		int edgeCount = 0;
		Set<Edge> edges = new HashSet<Edge>();
		for (Entry<String, ArrayList<Edge>> entry : this.adjmap.entrySet()) {
			edges.addAll(entry.getValue());
			
		}
		
		for(Edge e: edges){
			fw.write(e.either() + " - " + e.other(e.either()) + " : " + e.getWeight() + "\n");
			edgeCount++;
		}

		fw.write("Number of Edges: " + edgeCount + "\n");
		fw.write("\n\n");
	}

	public ArrayList<String> getV() {
		return V;
	}

	public HashMap<String, ArrayList<Edge>> getAdjMap() {
		return adjmap;
	}

}