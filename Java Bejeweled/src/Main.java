import java.io.*;
import java.util.*;

public class Main {

	public static int score = 0;

	public static void main(String[] args) {
		int numberOfRows = 0;
		int numberOfItems = 0;
		int numberOfColumns = 0;

		try {
			//Since game grid can be of any size first counting the number of rows from file
			File inFile = new File("gameGrid.txt");
			Scanner rowCounter = new Scanner(inFile);
			while (rowCounter.hasNextLine()) {
				numberOfRows++;
				rowCounter.nextLine();
			}
			rowCounter.close();
			//Then counting the number of total items
			Scanner itemCounter = new Scanner(inFile);
			while (itemCounter.hasNext()) {
				numberOfItems++;
				itemCounter.next();
			}
			itemCounter.close();
			
			//Now that we know our grid size we can create a new game grid object
			numberOfColumns = numberOfItems / numberOfRows;
			GameGrid gg = new GameGrid(numberOfRows, numberOfColumns);
			
			//After creating the game grid we can add the jewels from the file one by one
			Scanner jewelSc = new Scanner(inFile);
			int r = 0;
			int c = 0;
			while (jewelSc.hasNext()) {
				if (c != 0 && c % numberOfColumns == 0) {
					c = 0;
					r++;
				}
				//Add jewels to game grid
				gg.addJewel(r, c, jewelSc.next());
				c++;
			}
			jewelSc.close();
			
			//We have initialized the board so now we can start taking commands
			Scanner inputSc = new Scanner(System.in);

			while (true) {
				/*Loop should be braked when user enters an E otherwise,
				,take a coordinate
				,check it
				,update the board
				,and display it.
				*/
				System.out.println("Game Grid:\n");
				gg.viewBoard();
				System.out.print("Select coordinate or enter E to end the game: ");
				String[] words = inputSc.nextLine().split(" ");
				if (words[0].equals("E"))
					break;
				//Takes the coordinate
				int xCoord = Integer.parseInt(words[0]);
				int yCoord = Integer.parseInt(words[1]);
				if (gg.getJewelAtPosition(xCoord, yCoord) != null) {
					Jewel j = gg.getJewelAtPosition(xCoord, yCoord);
					//Checks the jewel in given coordinates
					j.check(null, j, 0, 0);
					//Updates the board
					gg.updateBoard();
					gg.updateBoard();
				} else {
					System.out.println("\nSelected Jewel is already deleted, please select another one.\n");
				}

			} 
			
			
			System.out.println("\nTotal Score: " + Main.score + "\n");
			System.out.print("\nEnter name: ");
			String name = inputSc.nextLine();
			
			
			//If the user enters E now it's time to check the leaderboard.txt
			File lb = new File("leaderboard.txt");
			//It may be the first time user plays the game so we have to check it's existence first
			if (!lb.exists()) {
				//If it is not located we create it and instantiate this player as first one
				lb.createNewFile();

				FileWriter fw = new FileWriter(lb, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(name + " " + score + "\n");
				System.out.println("\nYour rank is 1/1, congratulations\n");

				bw.close();
			}

			else {
				//If the file already exists we will shape a list of all scores in it
				FileWriter fw = new FileWriter(lb, true);
				BufferedWriter bw = new BufferedWriter(fw);

				ArrayList<Score> entries = new ArrayList<Score>();
				BufferedReader br = new BufferedReader(new FileReader(lb));
				String line;
				while ((line = br.readLine()) != null) {
					entries.add(new Score(line));
				}
				SortByScore sbs = new SortByScore();
				String newEntryString = name + " " + score;
				Score newScore = new Score(newEntryString);
				
				//If the current player scored the same points then there is no need for a duplicate we can simply pass
				int existFlag = 0;
				for (Score s: entries){
					if(s.getNickname().equals(newScore.getNickname()) && s.getScore() == newScore.getScore()) existFlag = 1;
				}
				if(existFlag == 0){
				bw.write(newEntryString + "\n");
				entries.add(newScore);
				}
				
				//Collections and Comparable interface is used as requested
				Collections.sort(entries, sbs);
				int index = Collections.binarySearch(entries, newScore,sbs);
				
				System.out.print("\nYour rank is: " + (index + 1) + "/" + entries.size() + ",");
				
				if(index + 1 == entries.size()) {
					//Player Finished in Last place
					int difference = entries.get(index-1).getScore() -score;
					
					System.out.print("your score is "+difference + " points lower than " + entries.get(index-1).getNickname() + "\n");
				}
				else if(index == 0){
					//Player Finished in First place
					int difference = score - entries.get(index + 1).getScore();
					System.out.print("your score is "+ difference + " points higher than " + entries.get(index+1).getNickname()+ "\n");
				}
				else{
					//Player Finished in Middle place
					int dif1 = score - entries.get(index + 1).getScore();
					int dif2 = entries.get(index-1).getScore() - score;
					
					System.out.print("your score is " + dif1 + " points higher than " + entries.get(index+1).getNickname() + " and " + dif2 + " points lower than "+ entries.get(index-1).getNickname()+"\n");
					
				}
				
				bw.close();
				br.close();
			}

			System.out.println("\nGood bye!");

			inputSc.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
