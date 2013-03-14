package database.players;

import gui.Language;

/**
 * Represents a Person
 * @author Tobias Denkinger
 *
 */
public class Person {

	private boolean present = false, free = true;
	private String surname, prename, club;

	/**
	 * Construct from surname
	 * @param surname surname of the Person
	 */
	public Person(String surname) {
		this(surname, "");
	}

	/**
	 * Construct from full name
	 * @param surname surname of the Person
	 * @param prename prename of the Person
	 */
	public Person(String surname, String prename) {
		this(surname, prename, "");
	}

	/**
	 * Construct  from full name and club
	 * @param surname surname of the Person
	 * @param prename prename of the Person
	 * @param club club the Person is in
	 */
	public Person(String surname, String prename, String club) {
		this.surname = surname.substring(0, 1).toUpperCase()
				+ surname.substring(1);
		this.prename = (prename.equals("")) ? "" : prename.substring(0, 1)
				.toUpperCase() + prename.substring(1);
		this.club = club;
	}

	/**
	 * Compares two Persons
	 * @param arg0 Person to compare with
	 * @return true if equal, false otherwise
	 */
	public boolean equals(Person arg0) {
		return ((surname.equals(arg0.surname)) & (prename.equals(arg0.prename)));
	}

	/**
	 * returns the club
	 * @return club
	 */
	public String getClub() {
		return club;
	}

	/**
	 * returns the full name
	 * @return full name
	 */
	public String getFullName() {
		if (surname.equals("noboby"))
			return "<nobody>";
		String result = "";
		result += surname;
		if (!prename.equals(""))
			result += ", " + prename;
		if (!club.equals(""))
			result += " (" + club + ")";
		return result;
	}

	/**
	 * returns the prename
	 * @return prename
	 */
	public String getPrename() {
		return prename;
	}

	/**
	 * returns the surname
	 * @return surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * returns if Person currently plays
	 * @return true if not playing, false otherwise
	 */
	public boolean isFree() {
		return free;
	}

	/**
	 * returns if Person if present
	 * @return true if present, false otherwise
	 */
	public boolean isPresent() {
		return present;
	}

	/**
	 * sets the club
	 * @param club club to set to
	 */
	public void setClub(String club) {
		this.club = club;
	}

	/**
	 * sets the playing/not playing state
	 * @param free state to set to
	 */
	public void setFree(boolean free) {
		this.free = free;
	}

	/**
	 * sets the prename
	 * @param prename prename to set to
	 */
	public void setPrename(String prename) {
		this.prename = prename;
	}

	/**
	 * sets if present
	 * @param present state to set to
	 */
	public void setPresent(boolean present) {
		this.present = present;
	}

	/**
	 * sets the surname
	 * @param surname surname to set to
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		if (surname.equals("noboby"))
			return Language.get("nobody");
		String result = "";
		result += surname;
		if (!prename.equals(""))
			result += ", " + prename.substring(0, 1) + ".";
		return result;
	}

}
