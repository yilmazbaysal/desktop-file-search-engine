import java.util.ArrayList;

public class Node{

	private ArrayList<Word> entries;
    private ArrayList<Node> children;

    public Node() { 
        entries = new ArrayList<Word>();
        children = new ArrayList<Node>();
    }
    
    //Inserts given value and child with respect to 'key'
    public void insert(Word value) {
    	for(int i = 0; i <= entries.size(); i++) {
    		
    		// If the key already exists, update it's value
    		if(i != entries.size() && entries.get(i).equals(value)) {
    			entries.get(i).update(value);
    			return;
    		}
    		
    		/* If the key does not exists add the new node to it's place
    		 * Then insert the given child
    		 */
    		else if(i == entries.size() || value.compareTo(entries.get(i)) < 0) {
    			entries.add(i, value);
    			return;
    		}
    	}
    }
    
    //Inserts given node to its place as a child
    public void insertChild(Node child) {
    	for(int i = 0; i <= children.size(); i++) {
    		if(i == children.size()) {
    			children.add(i, child);
    			return;
    		}
    		else if(child != null && children.get(i) != null &&
    			child.getEntry(0).compareTo(children.get(i).getEntry(0)) < 0)  {
    			
    			children.add(i, child);
    			return;
    		}
    	}
    }
    
    //Returns the entry at the given index
    public Word getEntry(int index) {
    	return entries.get(index);
    }
    
    //Returns the entry at the given index, then removes it
    public Word popEntry(int index) {
    	return entries.remove(index);
    }
    
    // Returns the child at the given index
    public Node getChild(int index) {
    	if(index >= children.size()) {
    		return null;
    	}
    	return children.get(index);
    }
    
    //Returns the rightmost child, then removes it
    public Node popChild() {
    	return children.remove(children.size() - 1);
    }
    
    //Returns the number of words which are stored in this node
    public int size() {
    	return entries.size();
    }
}
