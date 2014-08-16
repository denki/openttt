package database.players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import database.tournamentParts.Tournament;
import exceptions.InputFormatException;
import gui.Language;

/**
 * Representation of a pair of Players
 * @author Tobias Denkinger
 *
 */
public class Double extends Player implements Serializable{

	private Person pLeft, pRight;

	/**
	 * Empty constructor
	 */
	public Double() {
		super();
	}

	/**
	 * Constructor generating a Double of only one Player
	 * @param s Player to construct of
	 * @throws InputFormatException thrown if the Player has not pre- and surname
	 */
	public Double(Integer id, Player s) throws InputFormatException {
		this(id, s.tournament, new String[] { s.getPersons().get(0).getSurname(),
				s.getPersons().get(0).getPrename() });
	}

	/**
	 * Constructor generating a double of a List of Strings
	 * @param t associated Tournament
	 * @param name List of Strings
	 * @throws InputFormatException if name has the wrong format
	 */
	public Double(Integer id, Tournament t, String[] name) throws InputFormatException {
		super(id, t);
		switch (name.length) {
		case 2:
			pLeft = new Person(name[0]);
			pRight = new Person(name[1]);
			break;
		case 3:
			pLeft = new Person(name[0], "", name[2]);
			pRight = new Person(name[1], "", name[2]);
			break;
		case 4:
			pLeft = new Person(name[0], name[1]);
			pRight = new Person(name[2], name[3]);
			break;
		case 5:
			pLeft = new Person(name[0], name[1], name[4]);
			pRight = new Person(name[2], name[3], name[4]);
			break;
		case 6:
			pLeft = new Person(name[0], name[1], name[2]);
			pRight = new Person(name[3], name[4], name[5]);
			break;
		default:
			throw (new InputFormatException());
		}
	}
	
	public static String[] getDataNames() {
		return new String[] {Language.get("surname") + " 1", Language.get("prename") + " 1", Language.get("club") + " 1",
				Language.get("surname") + " 2", Language.get("prename") + " 2", Language.get("club") + " 2" };
	}
	
	public static boolean[] mandatoryVec() {
		return new boolean[] {true, false, false, true, false, false};
	}

	/**
	 * Compares two Doubles
	 * @param arg0 Double to compare with
	 * @return true if equal, false otherwise
	 */
	public boolean equals(Double arg0) {
		return pLeft.equals(arg0.pLeft) & pRight.equals(arg0.pRight);
	}

	@Override
	public String getFullName() {
		if (pLeft.getClub().equals(pRight.getClub()))
			return pLeft.getFullName().substring(0, pLeft.getFullName().indexOf(" (")) + " / " + pRight.getFullName();
		return pLeft.getFullName() + " / " + pRight.getFullName();
	}

	@Override
	public List<Person> getPersons() {
		List<Person> result = new ArrayList<Person>();
		result.add(pLeft);
		result.add(pRight);
		return result;
	}

	@Override
	public String toString() {
		if (isNobody())
			return "<nobody>";
		return pLeft.toString() + " / " + pRight.toString();
	}

	@Override
	public String getClub() {
		return pLeft.getClub();
	}

}
