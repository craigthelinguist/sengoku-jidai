import java.awt.image.BufferedImage;
import java.util.Comparator;


public class Clan {

	private String name;
	private BufferedImage emblem;
	public final int ID;

	public Clan (String name_, int ID) {
		this.name = name_;
		this.ID = ID;
	}
	
	public String toString () {
		return name;
	}
	
	@Override
	public boolean equals (Object o) {
		return o == this;
	}
	
	@Override
	public int hashCode () {
		return name.hashCode();
	}
	
	public static Comparator<Clan> NameComparator () {
		return new Comparator<Clan>() {
			@Override
			public int compare(Clan c1, Clan c2) {
				return c1.name.compareTo(c2.name);
			}
		};
	}
	
}
