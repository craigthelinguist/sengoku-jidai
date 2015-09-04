import java.util.List;


public class Main {

	public static void main (String[] args) throws Exception {
		List<Clan> clans = Loader.LoadClans("data/clans.dat");
		List<Character> chars = Loader.LoadCharacters("data/characters.dat", clans);
	}
	
}
