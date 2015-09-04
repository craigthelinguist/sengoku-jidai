import java.awt.image.BufferedImage;
import java.util.Comparator;


public class Character {

	private BufferedImage portrait;
	public final String name;
	public final Stats stats;
	
	public Character (String name, Stats stats) {
		this.name = name;
		this.stats = stats;
	}
	
	public String toString() {
		return name;
	}
	
	public static Comparator<Character> NameComparator () {
		return new Comparator<Character>(){
			@Override
			public int compare(Character c1, Character c2) {
				return c1.name.compareTo(c2.name);
			}
		};
	}
	
}
