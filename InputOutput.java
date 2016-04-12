import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class InputOutput {
	
	private PrintWriter outputFile;
	private Scanner commandsFile;
	private ArrayList<File> paths;
	private Scanner stopWordsFile;
	private Scanner textFile;
	private File path;

	public InputOutput(String[] arguments) {
		paths = new ArrayList<File>();
		
		//Open the files
		try {
			path = new File(arguments[0]);
			stopWordsFile = new Scanner(new FileInputStream(arguments[1]));
			commandsFile = new Scanner(new FileInputStream(arguments[2]));
			outputFile = new PrintWriter(new FileOutputStream(arguments[3]));
		} 
		catch(FileNotFoundException fileNotFoundException) {
			System.err.println("Error opening file.");
			System.exit(1);
		}
		
		listFiles(path);
	}
	
	//List all the readable files under the given path
	private void listFiles(File path) {
		for(int i = 0; i < path.listFiles().length; i++) {
			if(path.listFiles()[i].isDirectory()) {
				listFiles(new File(path.listFiles()[i].toString()));
			}
			else {
				paths.add(path.listFiles()[i]);
			}
		}
	}
	
	/*
	 * Returns a list of lines starting from index '1'
	 * Also it contains file name at the index '0'
	 */
	public ArrayList<String> getText() {
		ArrayList<String> lines = new ArrayList<String>();
		
		if(paths.size() != 0) {
			lines.add(paths.get(0).getName()); //Add the name of the file
			
			try {
				textFile = new Scanner(new FileInputStream(paths.remove(0)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			while(textFile.hasNextLine()) {
				lines.add(textFile.nextLine().toLowerCase(Locale.US));
			}
			
			textFile.close();
		}
		else {
			return null;
		}
		
		return lines;
	}
	
	// Returns the next command as a string
	public String getCommand() {
		if(commandsFile.hasNext()) {
			return commandsFile.nextLine().toLowerCase(Locale.US);
		}
		
		return null;
	}
	
	// Returns a B-Tree of stop words
	public BTree getStopWords() {
		BTree stopWords = new BTree(4);
		stopWords.put("", null);
		
		while(stopWordsFile.hasNext()) {
			stopWords.put(stopWordsFile.nextLine().toLowerCase(Locale.US), null);
		}	
		
		return stopWords;
	}
	
	// Writes a line to output file
	public void writeLine(String line) {
		outputFile.println(line);
	}
	
	// Closes all the files
	public void closeFiles() {
		stopWordsFile.close();
		commandsFile.close();
		outputFile.close();
	}
}
