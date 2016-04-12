import java.util.ArrayList;
import java.util.Collections;

public class Word implements Comparable<Word>{
	
	private String value;
	private ArrayList<TextFile> files;
	
	public Word(String value, TextFile file) {
		this.value = value;
		files = new ArrayList<TextFile>();
		
		files.add(file);
	}
	
	//When a word is already exists, updates its files
	public void update(Word that) {
		//temp contains only 1 TextFile
		TextFile temp = that.popFile();
		int index = files.indexOf(temp);

		if(index < 0) {
			files.add(temp);
		}
		else {
			files.get(index).addPlace(temp.getPlace());
		}
	}
	
	public String getValue() {
		return value;
	}
	
	//Returns list of the files in sorted order
	public ArrayList<TextFile> getFiles() {
		Collections.sort(files);
		
		return files;
	}

	public TextFile popFile() {
		return files.remove(0);
	}
	
	//Checks whether this word starts with given string
	public int startsWith(String str) {
		if(value.startsWith(str)) {
			return 0;
		}
		
		return this.value.compareTo(str);
	}
	
	@Override
	public int compareTo(Word that) {
		return this.value.compareTo(that.value);
	}
	
	@Override
	public boolean equals(Object that) {
		return this.value.equals(((Word)that).value);
	}
	
	@Override
	public String toString() {
		Collections.sort(files);
		String str = "";
		
		for(int i = 0; i < files.size(); i++) {
			str += value + " " + files.get(i).toString() + "\n";
		}
		
		return str;
	}
}
