import java.util.ArrayList;

public class TextFile implements Comparable<TextFile>{
	
	private String name;
	private ArrayList<String> places;

	public TextFile(String name, String placeOfWord) {
		places = new ArrayList<String>();
		
		this.name = name;
		places.add(placeOfWord);
	}
	
	//Returns name of the file
	public String getName() {
		return name;
	}
	
	//Adds new place to already existed word
	public void addPlace(String place) {
		places.add(place);
	}
	
	//Returns the first place
	public String getPlace() {
		return places.get(0);
	}
	
	//Compares two TextFile object with respect to their occurence count and name
	@Override
	public int compareTo(TextFile that) {
		if(this.places.size() == that.places.size()) {
			return this.name.compareToIgnoreCase(that.name);
		}
		else if(this.places.size() < that.places.size()) {
			return +1;
		}
		else {
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object that) {
		return this.name.equals(((TextFile)that).name);
	}
	
	@Override
	public String toString() {
		String str = name;
		
		for(int i = 0; i < places.size(); i++) {
			str += " " + places.get(i);
			
			if(i != places.size() - 1) {
				str += ",";
			}
		}
		
		return str;
	}
}
