package database.players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import database.tournamentParts.Tournament;
import exceptions.InputFormatException;
import gui.Language;

public class Single extends Player implements Serializable{

	private Person person;

	public Single() {
		super();
	}

	public Single(Integer id, Player p) {
		this(id, p.tournament, p.getPersons().get(0));
	}

	public Single(Integer id, Tournament t, Person p) {
		super(id, t);
		person = p;
	}

	public Single(Integer id, Tournament t, String[] name) throws InputFormatException {
		super(id, t);
		switch (name.length) {
		case 1:
			person = new Person(name[0]);
			break;
		case 2:
			person = new Person(name[0], name[1]);
			break;
		case 3:
			person = new Person(name[0], name[1], name[2]);
			break;
		default:
			throw (new InputFormatException());
		}
	}
	
	public static String[] getDataNames() {
		return new String[] {Language.get("surname"), Language.get("prename"), Language.get("club")};
	}

	public static boolean[] mandatoryVec() {
		return new boolean[] {true, false, false};
	}

	public boolean equals(Single p) {
		return person.equals(p.person);
	}

	@Override
	public String getFullName() {
		return person.getFullName();
	}

	@Override
	public List<Person> getPersons() {
		List<Person> result = new ArrayList<Person>();
		result.add(person);
		return result;
	}

	@Override
	public String toString() {
		if (isNobody())
			return Language.get("nobody");
		return person.toString();
	}

	@Override
	public String getClub() {
		return person.getClub();
	}

}
