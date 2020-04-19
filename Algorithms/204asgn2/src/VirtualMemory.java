import datastructures.*;
import datastructures.BinarySearchTree.Node;

import java.io.*;
import java.util.ArrayList;


public class VirtualMemory {
	
	class Cell {
		char data;
		Cell nextCell;
		int secondChanceBit;
		public Cell() {
			super();
			this.data = '\0';
			this.nextCell = null;
			this.secondChanceBit = 0;
		}
		
		
	}

	private String policy;
	private int memSize;
	private BinarySearchTree bst;
	private int pageFaultCount = 0;
	
	/*FIFO and Second Chance Related variables*/
	private ArrayList<Character> secondChances = new ArrayList<Character>();
	private Cell start = null;
	private Cell current;
	
	/* Heap related variables*/
	private char[] heap;
	private int last = 0;
	
	
	
	/*** Constructor which takes memSize,policy and search structure and creates them individually.
	 *  Upon creation of page buffer it allocates memSize number of cells whose data values are empty.***/
	public VirtualMemory(int memSize, String policy) {
		this.memSize=memSize;
		this.policy = policy;
		this.bst = new BinarySearchTree();

		for(int i=0;i<this.memSize;i++){
			Cell nc = new Cell();
			if(start == null) start = nc;
			else{
				Cell temp = start;
				while(temp.nextCell !=null) temp = temp.nextCell;
				temp.nextCell = nc;
			}
		}
		
		current = start;
		if(policy.equals("PriorityQueue")) heap = new char[memSize+1];
		
	}
	
	public void insertPagePQ(char d,BufferedWriter bf) throws IOException{
		if(bst.scan(d) == null){
			if(isHeapFull() == false){
				heap[++last] = d;
				swim(last);
				bf.write("Page Fault\t");
				printVmPQ(bf);
				
			}
			else{
				bst.delete(delMax());
				heap[++last] = d;
				swim(last);
				bf.write("Page Fault\t");
				printVmPQ(bf);
			}
			
			bst.insert(d);
			this.pageFaultCount++;
		}
		else{
			bf.write("\t\t\t");
			printVmPQ(bf);
		}
	}
	
	
	private void printVmPQ(BufferedWriter bf) throws IOException {
		for(char c: heap){
			if(c != '\0') bf.write(c + " ");
		}
		bf.write("\n");
	}

	private boolean isHeapFull() {
		
		return last >= memSize;
	}

	private char delMax(){
        if (isEmpty()) return '\0';
        char c= heap[1];
        exch(1,last--);
        heap[last+1] = '\0';
        sink(1);

        return c;
    }
	
	private void swim(int k) {
	 while(k > 1 && (heap[k/2]<heap[k])){
	          exch(k/2,k);
	          k = k/2;
	 }
	}
	
	private void sink(int k) {
		while (2*k < last){
            int j = 2 * k;
            if(j < last && (heap[j]<heap[j+1])) j = j + 1;
            if((heap[j]<heap[k])) break;
            exch(k,j);
            k = j;
        }
	}
	
	private void exch(int i, int j){
        char temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
	
	private boolean isEmpty() {
		return last == 0;		
	}

	

	/* Insertion function for FIFO and SecondChance Replacement Algorithms*/
	public void insertPage(char d,BufferedWriter bf) throws IOException{
		Node search = bst.scan(d);
		if(search == null) {
			if(isFull() == 0){
				/* there is space*/
				current.data = d;
				bf.write("Page Fault\t");
				printVm(bf);
				bf.write("\n");
			}
			else{
				/* replacement time */
				if(this.policy.equals("FIFO")){
					bst.delete(current.data);
					current.data = d;
					bf.write("Page Fault\t");
					printVm(bf);
					bf.write("\n");
					
				}
				else if(this.policy.equals("SecondChance") ){
					while(current.secondChanceBit != 0){
						current.secondChanceBit = 0;
						secondChances.add(current.data);
						if(current.nextCell != null) current = current.nextCell;
						else { current = start;}
					}
					
					bst.delete(current.data);
					current.data = d;
					bf.write("Page Fault\t");
					printVm(bf);
					if(secondChances.isEmpty() != true) {
						bf.write("Second Chance ");
						for(char c: secondChances){
							bf.write(c + " ");
						}
						secondChances.removeAll(secondChances);
					}
					bf.write("\n");
					
				}

				
			}
			
			if(current.nextCell != null) current = current.nextCell;
			else { current = start;}
			bst.insert(d);
			pageFaultCount++;
		}
		else {
			if(policy.equals("SecondChance")) {
				Cell temp = start;
				while(temp != null){
					if(temp.data == search.getData()) temp.secondChanceBit = 1;
					temp = temp.nextCell;
				}
			}
			bf.write("\t\t\t");
			printVm(bf);
			bf.write("\n");
		}
		
		
	}


	public int getPageFaultCount() {
		return pageFaultCount;
	}


	private int isFull() {
		Cell temp = start;
		while(temp != null){
			if(temp.data == '\0') return 0;
			temp = temp.nextCell;
		}
		return 1;
	}


	private void printVm(BufferedWriter bf) throws IOException {
		Cell temp= start;
		while(temp != null){
			try {
				if(temp.data != '\0') bf.write(temp.data + " ");
				temp = temp.nextCell;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
