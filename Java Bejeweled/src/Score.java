
public class Score {
	private String nickname;
	private int score;
	
	public Score(String line) {
		super();
		
		String[] tokens = line.split(" ");
		
		this.nickname = tokens[0];
		this.score = Integer.parseInt(tokens[1]);
	}
	public String getNickname() {
		return nickname;
	}
	public int getScore() {
		return score;
	}
	

	
	
}
