import java.awt.image.BufferedImage;
import java.util.Comparator;


public class Officer {

	private BufferedImage portrait;
	public String name;
	public final Stats stats;
	
	public Officer (String name, Stats stats) {
		this.name = name;
		this.stats = stats;
	}
	
	public String toString() {
		return name;
	}
	
	public static Comparator<Officer> NameComparator () {
		return new Comparator<Officer>(){
			@Override
			public int compare(Officer c1, Officer c2) {
				return c1.name.compareTo(c2.name);
			}
		};
	}
	
}
