
public class Stats {
	
	public int WAR;
	public int CHR;
	public int INT;
	public int POL;
	public int CUL;
	public int SPI;
	public Clan clan;
	
	public int overall () {
		return (int) (Math.round(1.0*(WAR + CHR + INT + POL + CUL + SPI) / 6));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + CHR;
		result = prime * result + CUL;
		result = prime * result + INT;
		result = prime * result + POL;
		result = prime * result + SPI;
		result = prime * result + WAR;
		result = prime * result + ((clan == null) ? 0 : clan.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stats other = (Stats) obj;
		if (CHR != other.CHR)
			return false;
		if (CUL != other.CUL)
			return false;
		if (INT != other.INT)
			return false;
		if (POL != other.POL)
			return false;
		if (SPI != other.SPI)
			return false;
		if (WAR != other.WAR)
			return false;
		else if (!clan.equals(other.clan))
			return false;
		return true;
	}
	
}
