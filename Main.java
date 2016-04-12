
public class Main {

	public static void main(String[] args) {
		
		Manager manager = new Manager(args);
		
		//Read all files then add their content to the B-Tree
		manager.createBTree();
		
		//Read commands file line-by-line than print the results of the commands
		manager.executeCommands();
	}
}
