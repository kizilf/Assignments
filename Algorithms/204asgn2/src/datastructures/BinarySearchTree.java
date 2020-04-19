package datastructures;

public class BinarySearchTree {
	public class Node{
		char data;
		Node rightChild;
		Node leftChild;
		
		public Node(char data) {
			super();
			this.data = data;
			this.rightChild = null;
			this.leftChild = null;
		}

		public char getData() {
			return data;
		}

		public Node getRightChild() {
			return rightChild;
		}

		public Node getLeftChild() {
			return leftChild;
		}
		
		
	}
	
	private Node root;

	
	public BinarySearchTree() {
		super();
		this.root = null;
	}
	
	public void insert(char data) {
	      root = insertRec(root, data);
	}
	
	private Node insertRec(Node root, char data) {
		 
        if (root == null) {
            root = new Node(data);
            return root;
        }
 
        if (data < root.data)
            root.leftChild = insertRec(root.leftChild, data);
        else if (data > root.data)
            root.rightChild = insertRec(root.rightChild, data);
 
        return root;
    }
	
	public void delete(char data)
    {
        root = deleteRec(root, data);
    }
 
    private Node deleteRec(Node root, char data)
    {
        
        if (root == null)  return root;
 
        if (data < root.data)
            root.leftChild = deleteRec(root.leftChild, data);
        else if (data > root.data)
            root.rightChild = deleteRec(root.rightChild, data);
 
        else
        {
            if (root.leftChild == null)
                return root.rightChild;
            else if (root.rightChild == null)
                return root.leftChild;
            
            root.data = getMin(root.rightChild);
 
            root.rightChild = deleteRec(root.rightChild, root.data);
        }
 
        return root;
    }
	
    private char getMin(Node root)
    {
        char min = root.data;
        while (root.leftChild != null)
        {
            min = root.leftChild.data;
            root = root.leftChild;
        }
        return min;
    }
	
    public Node scan(char data)
    {
        return scanRec(root,data);
    }
 
    private Node scanRec(Node root,char data)
    {
        if (root != null)
        {
           
            if(root.data == data) return root;
            else if(data < root.data ) return scanRec(root.leftChild,data);
            else if(data > root.data) return scanRec(root.rightChild,data);
            
        }
		return root;
    }
    
	public Node getRoot() {
		return root;
	}

	
	
}
