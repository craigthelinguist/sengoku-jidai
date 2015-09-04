import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loader {

	public static List<Clan> LoadClans(String fpath) throws IOException {
		File file = new File(fpath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			List<Clan> clans = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0)
					continue;
				int id = -1;
				String name = null;
				while (line != null && line.length() != 0) {
					String[] stuff = line.split(" ");
					String identifier = stuff[0];
					if (identifier.equals("ID")) {
						id = Integer.parseInt(stuff[1]);
					} else if (identifier.equals("Name")) {
						name = stuff[1];
					}
					line = br.readLine();
				}
				if (id == -1 || name == null)
					throw new IOException("Malformed entry in " + fpath);
				Clan clan = new Clan(name, id);
				clans.add(clan);
			}
			br.close();
			return clans;
		} finally {
			if (br != null)
				br.close();
		}
	}

	public static List<Character> LoadCharacters(String fpath, List<Clan> clans)
			throws IOException {
		File file = new File(fpath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			List<Character> chars = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				
				line = line.trim();
				if (line.length() == 0)
					continue;
				
				int id = -1;
				String name = null;
				int clanID = -1;
				int WAR = -1;
				int INT = -1;
				int POL = -1;
				int CHR = -1;
				int CUL = -1;
				int SPI = -1;

				while (line != null && line.length() != 0) {
					String[] stuff = line.split(" ");
					String identifier = stuff[0];

					switch (identifier) {

					case "ID":
						id = Integer.parseInt(stuff[1]);
						line = br.readLine();
						continue;

					case "Name":
						name = join(stuff, 1);
						line = br.readLine();
						continue;

					case "Clan":
						clanID = Integer.parseInt(stuff[1]);
						line = br.readLine();
						continue;

					}

					// Otherwise it's a STAT.
					int value = Integer.parseInt(stuff[1]);
					if (value < 0 || value > 100)
						throw new IOException("Bad stat value: " + value
								+ " for " + name);

					switch (identifier) {
					case "WAR":
						WAR = value;
						break;
					case "INT":
						INT = value;
						break;
					case "POL":
						POL = value;
						break;
					case "CHR":
						CHR = value;
						break;
					case "CUL":
						CUL = value;
						break;
					case "SPI":
						SPI = value;
						break;
					}

					line = br.readLine();
				}

				if (id == -1 || WAR == -1 || INT == -1 || POL == -1
						|| CHR == -1 || CUL == -1 || CHR == -1 || SPI == -1
						|| clanID == -1 || name == null)
					throw new IOException("Malformed entry in " + fpath);

				Stats stats = new Stats();
				stats.WAR = WAR;
				stats.INT = INT;
				stats.POL = POL;
				stats.CHR = CHR;
				stats.CUL = CUL;
				stats.SPI = SPI;
				stats.clan = clans.get(clanID);

				Character character = new Character(name, stats);
				chars.add(character);

			}
			br.close();
			return chars;

		} finally {
			if (br != null)
				br.close();
		}
	}

	private static String join (String[] arr, int start) {
		StringBuilder sb = new StringBuilder();
		for (int i =  start; i < arr.length; i++) {
			String str = arr[i];
			sb.append(str);
			sb.append(" ");
		}
		return sb.toString();
	}
}
