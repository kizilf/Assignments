
import java.io.*;
import java.util.*;

public class Assignment2 {

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		File infile = new File(args[0]);
		
		Scanner scin;
		
		try {
			scin = new Scanner(infile);
			FileWriter outfile = new FileWriter("output.txt");
			BufferedWriter bf = new BufferedWriter(outfile);
			
			String[] words = scin.nextLine().split(" ");
			int memSize = Integer.parseInt(words[1]);
			bf.write("Memory "+ memSize +"\n");
			
			words = scin.nextLine().split(" ");
			String policy = words[1];
			bf.write(policy + " Page Replacement\n");
			scin.nextLine();
			bf.write("Binary Search Tree\n\n");
			
			
			VirtualMemory vm = new VirtualMemory(memSize, policy);
			
			
			while(scin.hasNext()){
				String line = scin.nextLine();
				words = line.split(" ");
				if(policy.equals("PriorityQueue")) vm.insertPagePQ(words[1].charAt(0), bf);
				else{
					vm.insertPage(words[1].charAt(0),bf);
				}
			}
			
			long endTime   = System.nanoTime();
			long totalTime = (endTime - startTime)/1000;
			bf.write(String.valueOf(vm.getPageFaultCount()) + "\n");
			bf.write("Total time passed for execution in ms: " + Double.toString(totalTime));
			bf.close();
			scin.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Input.txt cannot be found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

	}

}
