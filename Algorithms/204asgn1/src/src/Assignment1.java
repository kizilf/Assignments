import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Furkan KIZILTAN 21607877
 *
 */
public class Assignment1 {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(new File(args[0]));
        ArrayList<Log> allLogs = new ArrayList<Log>();
        ArrayList<Double> column = new ArrayList<Double>();
        int index = Integer.parseInt(args[1]);

        
        String header = scanner.nextLine(); //consume the first line
        while(scanner.hasNext()){
        	String line = scanner.nextLine();
        	allLogs.add(new Log(line));
        	String[] words = line.split(",");
        	column.add(Double.parseDouble(words[index]));
        }
        
        
        long start,end,timeInMillis;


        
        start = System.nanoTime();
        ArrayList<Log> quickSortedLogs = qSort(allLogs,column);
        end = System.nanoTime();
        timeInMillis = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        
        System.out.println("Time spend for quick sort in ms: " + timeInMillis);
       
        start = System.nanoTime();
        ArrayList<Log> bubbleSortedLogs = bubbleSort(allLogs,allLogs.size(),column);
        end = System.nanoTime();
        timeInMillis = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        System.out.println("Time spend for bubble sort in ms: " + timeInMillis);
        
        start = System.nanoTime();
        ArrayList<Log> selectionSortedLogs = selectionSort(allLogs,column);
        end = System.nanoTime();
        timeInMillis = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        System.out.println("Time spend for selection sort in ms: " + timeInMillis);
        
      
        if(args[2].equals("T")){
        	FileWriter out = new FileWriter("SortedTrafficFlow.csv");
        	out.write(header);
        	out.write("\n");
        	for(Log l: quickSortedLogs){
        		for(String feature: l.getFeatures()){
        			out.write(feature + ",");
        		}
        		out.write("\n");
        	}
        }
       
        
        scanner.close();
	}
	
	
	public static ArrayList<Log> bubbleSort(ArrayList<Log> logs,int size,ArrayList<Double> column){
		for(int i=0; i<size-1;i++){
			for(int j= 0;j<size-i-1;j++){
				double firstVal = column.get(j);
				double secondVal = column.get(j+1);
				if(firstVal>secondVal){
					Collections.swap(logs, j, j+1);
					Collections.swap(column, j, j+1);
				}
			}
		}
		return logs;
	}
	
	
	public static ArrayList<Log> qSort(ArrayList<Log> logs,ArrayList<Double> column){
		if(logs.size() != 0 && column.size() != 0){
			return quickSort(logs,0,logs.size()-1,column);
		}
		
		return logs;
		
	}
	
	public static ArrayList<Log> quickSort(ArrayList<Log> logs,int startIndex,int endIndex,ArrayList<Double> column){
		int i = startIndex;
		int j = endIndex;
		
		double pivot = column.get(startIndex + (endIndex-startIndex)/2);
		while(i<=j){
			while(column.get(i)<pivot){
				i++;
			}
			while(column.get(j)>pivot){
				j--;
			}
			
			if(i<=j){
				Collections.swap(logs, i, j);
				Collections.swap(column, i, j);
				i++;
				j--;
			}
		}
		
		
		if(startIndex<j){
			quickSort(logs,startIndex,j,column);
		}
		if(i<endIndex){
			quickSort(logs,i,endIndex,column);
		}
		return logs;
		
	}
	
	
	public static ArrayList<Log> selectionSort(ArrayList<Log> logs, ArrayList<Double> column){
		
		for(int i = 0; i<logs.size()-1; i++){
			double min = column.get(i);
			int swapIndex = i;
			
			
			for(int j= i+1; j<logs.size();j++){
				double l2Feature = column.get(j);
				if(l2Feature < min)
				{
					min = l2Feature;
					swapIndex = j;
				}
			}
			Collections.swap(logs, i, swapIndex);
			Collections.swap(column, i, swapIndex);
		}
		return logs;		
	}

	public static void merge(ArrayList<Log> logs, int left,int middle,int right,ArrayList<Double> column){
		int size1 = middle - left + 1;
		int size2 = right - middle;
		
		Double[] leftArray = new Double[size1];
		Double[] rightArray = new Double[size2];
		
		for(int i=0; i<size1; ++i){
			leftArray[i] = column.get(left+ i);
		}	
		for(int j=0; j<size2; ++j){
			rightArray[j] = column.get(middle + j + 1);
		}
		
		int i= 0, j=0;
		int k = left;
		
		while(i<size1 && j<size2){
			
			if(leftArray[i]<= rightArray[j]){
				column.set(k, leftArray[i]);
				i++;
			}
			else{
				column.set(k, rightArray[j]);
				j++;
			}
			k++;
		}
		
		while(i < size1){
			column.set(k, leftArray[i]);
			i++;
			k++;
		}
		
		while(j< size2){
			column.set(k, rightArray[j]);
			j++;
			k++;
		}
	}
	

}
