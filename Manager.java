import java.util.ArrayList;

public class Manager {
	
	private InputOutput io;
	private BTree bTree;
	private BTree stopWords;
	
	public Manager(String[] arguments) {
		bTree = new BTree(); //Default max key number is 4
		stopWords = new BTree(6);
		
		io = new InputOutput(arguments);
	}
	
	public void createBTree() {
		ArrayList<String> lines = io.getText();
		stopWords = io.getStopWords();
		String regex = "[\\p{IsPunctuation}\\p{IsWhiteSpace}&&[^']]+";
		
		do {
			for(int i = 1; i < lines.size(); i++) {
				int index = 0;
				
				if(!lines.get(i).equals("")) { //If the line is not empty
					String[] words = lines.get(i).split(regex); //Split the line
					
					for(int j = 0; j < words.length; j++) {		
						index = lines.get(i).indexOf(words[j], index); //Get the position
												
						// If the word is not a stop-word
						if(stopWords.get(words[j]) == null) {
							String position = Integer.valueOf(i) + ":";
							position += Integer.valueOf(index + 1);
							
							bTree.put(words[j], new TextFile(lines.get(0), position));
						}
						
						index += words[j].length(); //To find the position of next word
					}
				}
			}
		
			lines = io.getText(); //Get content of the next text file
			
		}while(lines != null); //Terminate when texts finished
	}
	
	public void executeCommands() {
		String command = io.getCommand();
		String result = null;
		
		while(command != null) {
			String[] tokens = command.split(" ");
			
			if(tokens[0].equals("traverse")) {
				result = bTree.traverse(tokens[1]);
			}
			else if(tokens[0].equals("search")) {
				result = search(tokens);
			}
			else {
				result = "Error: Unknown command type!\n";
			}
			
			// Print results
			io.writeLine(command + "\n" + result);
			
			command = io.getCommand(); //Get next command
		}
		
		io.closeFiles(); //Close the IO files
	}
	
	private String search(String[] tokens) {
		String result = "";
		
		// search str, search str* commands
		if(tokens.length == 2) {
			if(tokens[1].endsWith("*")) {
				ArrayList<Word> list;
				list = bTree.getStartsWith(tokens[1].substring(0, tokens[1].length() - 1));
				
				for(int i = 0; i < list.size(); i++) {
					result += list.get(i).toString();
				}
			}
			else {
				Word found = bTree.get(tokens[1]);
				if(found == null) {
					return "";
				}
				
				for(int i = 0; i < found.getFiles().size(); i++) {
					result += found.getFiles().get(i).toString() + "\n";
				}
			}
		}
		// "search str1 and str2", "search str1 not str2" commands
		else if(tokens.length == 4){
			ArrayList<TextFile> files1, files2;
			String str = "";
			
			Word word1 = bTree.get(tokens[1]);
			Word word2 = bTree.get(tokens[3]);
			
			/* Check if the given key containing by the tree or not
			 * If they do not exists, assign them as empty lists 
			 * to avoid null reference exceptions
			 */
			if(word1 == null) {
				files1 = new ArrayList<TextFile>();
			}
			else {
				files1 = new ArrayList<TextFile>(bTree.get(tokens[1]).getFiles());
			}
			if(word2 == null) {
				files2 = new ArrayList<TextFile>();
			}
			else {
				files2 = new ArrayList<TextFile>(bTree.get(tokens[3]).getFiles());
			}

			if(tokens[2].equals("and")) { //"and" operation
				files1.retainAll(files2);
				files2.retainAll(files1);
				
				if(!tokens[1].equals(tokens[3])) {
					for(int i = 0; i < files2.size(); i++) {
					str += tokens[3] + " " + files2.get(i).toString() + "\n";
					}
				}
			}
			else if(tokens[2].equals("not")) { //"not" operation
				files1.removeAll(files2);
			}
			else {
				return "Error: Unknown operation!\n";
			}
			
			for(int i = 0; i < files1.size(); i++) {
				result += tokens[1] + " " + files1.get(i).toString() + "\n";
			}
			
			result = result + str;
		}
		else {
			return "Error: Unknown command!\n";
		}
		
		return result;
	}
}
