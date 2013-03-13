package database.tournamentParts;


public class Properties {
	public boolean DO_KNOCKOUT = true;
	public boolean DO_QUALIFYING = true;

	public String name = "";

	public int[][] subMatches = new int[][] { { 1, 2 }, { 2, 1 }, { 1, 1 },
			{ 2, 2 } };
	public boolean TYPE_2TEAM = false;
	public boolean TYPE_DOUBLE = false;

	public boolean TYPE_SINGLE = true;

	public Properties() {

	}
}
