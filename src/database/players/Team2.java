package database.players;

import java.util.ArrayList;
import java.util.List;

import database.tournamentParts.Tournament;
import exceptions.InputFormatException;

public class Team2 extends Player {

	private Person pFirst, pSecound;

	public Team2() {
		super();
	}

	public Team2(Integer id, Tournament t, String[] name) throws InputFormatException {
		super(id, t);
		for (int i = 0; i < name.length; i++) {
			name[i] = name[i].substring(0, 1).toUpperCase()
					+ name[i].substring(1);
		}
		switch (name.length) {
		case 2:
			pFirst = new Person(name[0]);
			pSecound = new Person(name[1]);
			break;
		case 3:
			pFirst = new Person(name[0], "", name[2]);
			pSecound = new Person(name[1], "", name[2]);
			break;
		case 4:
			pFirst = new Person(name[0], name[1]);
			pSecound = new Person(name[2], name[3]);
			break;
		case 5:
			pFirst = new Person(name[0], name[1], name[4]);
			pSecound = new Person(name[2], name[3], name[4]);
			break;
		case 6:
			pFirst = new Person(name[0], name[1], name[2]);
			pSecound = new Person(name[3], name[4], name[5]);
			break;
		default:
			throw (new InputFormatException());
		}
	}
	
	public static String[] getDataNames() {
		return Double.getDataNames();
	}
	
	public static boolean[] mandatoryVec() {
		return Double.mandatoryVec();
	}

	@Override
	public String getFullName() {
		return pFirst.getFullName() + " / " + pSecound.getFullName();
	}

	@Override
	public List<Person> getPersons() {
		List<Person> result = new ArrayList<Person>();
		result.add(pFirst);
		result.add(pSecound);
		return result;
	}

	@Override
	public String toString() {
		if (isNobody())
			return "<nobody>";
		return pFirst.toString() + " / " + pSecound.toString();
	}

	@Override
	public String getClub() {
		return pFirst.getClub();
	}

}
