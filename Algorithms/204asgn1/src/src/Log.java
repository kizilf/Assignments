import java.util.*;

public class Log{
	private ArrayList<String> features = new ArrayList<String>();

	public Log(String line) {
		String[]fts = line.split(",");
		for(String s:fts){
			this.features.add(s);
		}
	}

	public ArrayList<String> getFeatures() {
		return features;
	}

	public int compareTo(Log other,int index) {
		double thisFeature = Double.parseDouble(this.getFeatures().get(index));
		double otherFeature = Double.parseDouble(other.getFeatures().get(index));
		return Double.compare(thisFeature, otherFeature);
		
	}

	
	
}
