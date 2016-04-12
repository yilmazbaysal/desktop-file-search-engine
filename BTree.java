import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class BTree {

	private int heightOfTree;
	private  int max; //Maximum key number of each node
	
	private Node root;
	
	//Default case
	public BTree() {
		this.max = 4;
		heightOfTree = 0;
		
		root = new Node();
	}
	
	//If the maximum key number is specified
	public BTree(int max) {
		this.max = max;
		heightOfTree = 0;
		
		root = new Node();
	}
	
	/* 
	 * Returns all the elements in the tree as a string
	 * with respect to given traverse type in or level order
	 */
	public String traverse(String type) {
		if(type.equals("in-order")) {
			return inOrder(root, heightOfTree) + "\n";
		}
		else if(type.equals("level-order")){
			return levelOrder() + "\n";
		}
		else {
			return "Error: undefined traverse type!\n";
		}
	}
	
	// Returns a line which contains all elements of the BTree in in-order
	public String inOrder(Node node, int height) {
		String str = "";

		for(int i = 0; i <= node.size(); i++) {
			
			if(height != 0) { //If the node is not a leaf 
				str += inOrder(node.getChild(i), height - 1); //Go left
			}
			
			if(i != node.size()) { 
				str += node.getEntry(i).getValue() + " "; //Traverse
			}
		}
		
		return str;
	}
	
	//Returns a line which contains all elements of the tree in level-order
	public String levelOrder(){
 		Queue<Node> queue = new LinkedList<Node>();
 		String str = "";
 		int height = heightOfTree;
 		
 		queue.add(root); //Start from the root
 		
 		while(!queue.isEmpty()){

 			int levelNodes = queue.size();
 			
 			// When the level ends, decrease the height
 			while(levelNodes > 0){
				Node n = queue.remove(); //pop the first node from the queue
				
				// Get values of node
				for(int i = 0; i < n.size(); i++) {
					str += n.getEntry(i).getValue() + " ";
				}
				
				// Add children of the node to the queue
				if(height != 0) {
					for(int i = 0; i <= n.size(); i++) {
						queue.add(n.getChild(i));
					}
				}
				levelNodes--; 
			}
 			height--; //Decrement the height
		}
 		
 		return str;
	}
	
	// Returns the sorted result of startsWith method
	public ArrayList<Word> getStartsWith(String str) {
		ArrayList<Word> founds = startsWith(root, new ArrayList<Word>(), str);
		Collections.sort(founds);
		
		return founds;
	}
	
	/*  Finds all words which starts with the given string
	 *  Returns the list of found words
	 */
	private ArrayList<Word> startsWith(Node node, ArrayList<Word> found, String searched) {
		if(node == null) {
			return null;
		}
		
		for(int i = 0; i <= node.size(); i++) {
			if(i == node.size() || node.getEntry(i).startsWith(searched) > 0) {
				startsWith(node.getChild(i), found, searched);
			}
			else if(node.getEntry(i).startsWith(searched) == 0) {
				found.add(node.getEntry(i));
				startsWith(node.getChild(i), found, searched);
			}
		}
		
		return found;
	}
	
	// Returns the result of private search method
	public Word get(String key) {
		return search(root, new Word(key, new TextFile("", "")));
	}
	
	// Finds the item with the given key recursively
	private Word search(Node node, Word searched) {
		if(node == null) {
			return null;
		}
		
		for(int i = 0; i <= node.size(); i++) {
			if(i == node.size() || searched.compareTo(node.getEntry(i)) < 0) {
				return search(node.getChild(i), searched);
			}
			else if(searched.compareTo(node.getEntry(i)) == 0) {
				return node.getEntry(i);
			}
		}
		
		return null;
	}
	
	public void put(String key, TextFile file) {
        Node returned = insert(root, key, file, heightOfTree); 
        
        // Split the root
        if (returned != null) {
        	
	        Node temp = new Node();
	        temp.insert(returned.popEntry(0));
	        temp.insertChild(root);
	        temp.insertChild(returned);
	     
	        root = temp;
	        heightOfTree++; //If the root split, height will be increase
        }
    }
	
	//Inserts given key and file recursively, from starting to given node
    private Node insert(Node node, String key, TextFile file, int height) {
        Word tempWord = new Word(key, file);
        Node returned = null;
        
        // If the node is not leaf
        if (height != 0) {
            for (int i = 0; i < node.size(); i++) {
                if (tempWord.compareTo(node.getEntry(i)) < 0) {
                    returned = insert(node.getChild(i), key, file, height - 1);
                    break;
                }
                else if(tempWord.compareTo(node.getEntry(i)) == 0) {
                	node.insert(tempWord);
                	returned = null;
                	break;
                }
                else if(i == node.size() - 1) {
                	returned = insert(node.getChild(++i), key, file, height - 1);
                	break;
                }
            }
            
            if (returned == null) {
            	return null;
            }
            
            node.insert(returned.popEntry(0));
            node.insertChild(returned);
        }
        //If the node is leaf insert the values
        else {
        	node.insert(tempWord);
        	node.insertChild(null);
        }
        
        if (node.size() <= max){ //If split needed
        	return null;
        }
        else {
        	return split(node);
        }
    }
    
    /* Splits given node by half. 
     * Returns a node contains the left part of the node
     */
    private Node split(Node node) {
        Node temp = new Node();
      
        for (int i = 0; i < max/2 + 1; i++) {
        	temp.insert(node.popEntry(max/2));
        	temp.insertChild(node.popChild());
        }
             
        return temp;    
    }
}
