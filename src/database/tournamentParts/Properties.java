package database.tournamentParts;

public class Properties {
	public boolean DO_KNOCKOUT = true;
	public boolean DO_QUALIFYING = true;

	String name = "";

	public int[][] subMatches = new int[][] { { 1, 2 }, { 2, 1 }, { 1, 1 },
			{ 2, 2 } };
	boolean TYPE_2TEAM = false;
	boolean TYPE_DOUBLE = false;
	boolean TYPE_SINGLE = true;

	public Properties() {

	}
}
