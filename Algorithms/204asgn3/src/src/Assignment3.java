import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Assignment3 {

	public static void main(String[] args) throws IOException {
		Scanner wordVec;
		Scanner wordPairs;
		try {
			
			wordVec = new Scanner(new File(args[0]));
			wordPairs = new Scanner(new File(args[1]));
			File cluster = new File(args[2]);
			FileWriter outputFileWriter = new FileWriter(cluster);
			int numberOfClusters = Integer.parseInt(args[3]);

			// Get all words from words pair
			ArrayList<String> allWords = new ArrayList<String>();
			while (wordPairs.hasNext()) {
				String line = wordPairs.nextLine();
				String[] words = line.split("-");
				if (!allWords.contains(words[0])) {
					allWords.add(words[0]);
				}
				if (!allWords.contains(words[1])) {
					allWords.add(words[1]);
				}
			}

			HashMap<String, ArrayList<Double>> hmap = new HashMap<String, ArrayList<Double>>();
			while (wordVec.hasNextLine()) {
				String line = wordVec.nextLine();
				String[] words = line.split(" ");
				words[0] = words[0].replaceAll("\"", "");
				if (allWords.contains(words[0])) {
					ArrayList<Double> vals = new ArrayList<Double>();
					for (String s : words) {
						if (!s.equals(words[0])) {
							vals.add(Double.parseDouble(s));
						}
					}
					hmap.put(words[0], vals);
				}
			}

			/*Create An EdgeWeigtedGraph*/
			EdgeWeightedGraph ewg = new EdgeWeightedGraph(allWords);
			

			for (Entry<String, ArrayList<Double>> entry : hmap.entrySet()) {
				ArrayList<Double> val1 = (ArrayList<Double>) entry.getValue();
				
			    for (Entry<String, ArrayList<Double>> otherEntry : hmap.entrySet()) {
			    	ArrayList<Double> val2 = (ArrayList<Double>) otherEntry.getValue();
			    	if(entry.getKey().equals(otherEntry.getKey())) continue;
					/* Calculate Cosine Similarity Of Those Two Vectors */
					double dotProduct = 0.0;
					double normA = 0.0;
					double normB = 0.0;
					for (int i = 0; i < val1.size(); i++) {
						dotProduct += val1.get(i) * val2.get(i);
						normA += Math.pow(val1.get(i), 2);
						normB += Math.pow(val2.get(i), 2);
					}
					
					double similarity = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
					if(similarity < 0) similarity = (1-similarity)/2;
					Edge e = new Edge((String)entry.getKey(),(String)otherEntry.getKey(),similarity);
					ewg.addEdge(e);
			    }
			}

			
			MinimumSpanningTree mst = new MinimumSpanningTree(ewg);
			ArrayList<String> mstverticies = new ArrayList<String>();
			for(Edge e: mst.getMst()){
				if(!mstverticies.contains(e.either())) mstverticies.add(e.either());
				if(!mstverticies.contains(e.other(e.either()))) mstverticies.add(e.other(e.either()));
			}
			EdgeWeightedGraph ewgAsMst = new EdgeWeightedGraph(mstverticies);
			for(Edge e:mst.getMst()){
				ewgAsMst.addEdge(e);
			}
			
			outputFileWriter.write("Edge Weighted Graph:\n");
			ewgAsMst.printEdges(outputFileWriter);
			
			ArrayList<Edge> mstEdgesAsList = new ArrayList<Edge>(mst.getMst());
			ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
			for(int i = 0; i<numberOfClusters -1; i++){
				
				
				int removeIndex = mstEdgesAsList.size()/2 ;
				if(removeIndex == 0) continue;
				Edge edgeToRemove = mstEdgesAsList.get(removeIndex);
				
				for(Edge e: ewg.getAdjMap().get(edgeToRemove.either())){
					if(!mstEdgesAsList.contains(e)) edgesToRemove.add(e);
				}
				mstEdgesAsList.remove(edgeToRemove);
				ewgAsMst.delEdge(edgeToRemove);
			}
			
			for(Edge e: edgesToRemove){
				ewgAsMst.delEdge(e);
			}
			
			outputFileWriter.write("Minimum Spanning Tree:\n");
			ewgAsMst.printEdges(outputFileWriter);
			ConnectedComponent cc = new ConnectedComponent(ewgAsMst);
			
			
			ArrayList<ArrayList<String>> clusters = new ArrayList<ArrayList<String>>(cc.getCount());
			
			outputFileWriter.write(numberOfClusters + " cluster are made:\n");
			for(int i=0; i<cc.getCount();i++){
				clusters.add(i, new ArrayList<String>());
				Iterator<String> itr = ewg.getV().iterator();
				while(itr.hasNext()){
					
					String e = itr.next();
					if(cc.id(e) == i && !clusters.contains(e)){
						clusters.get(i).add(e);
						outputFileWriter.write(e + " ");
					}
				}
				outputFileWriter.write("\n");
			}
			
			outputFileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
