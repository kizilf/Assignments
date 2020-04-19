import java.util.Comparator;

public class SortByScore implements Comparator<Score> {

	@Override
	//A single comparator class for comparing two different scores
	public int compare(Score p1, Score p2) {
		int score1 = p1.getScore();
        int score2 = p2.getScore();

        if (score1 == score2)
            return 0;
        else if (score2 > score1)
            return 1;
        else
            return -1;
 	}

}
