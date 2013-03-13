package database.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.match.Match;
import database.match.Node;
import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;
import database.tournamentParts.Tournament;
import exceptions.InputFormatException;

public abstract class Player extends Node implements Comparable<Player> {
	private List<Match> matches;
	protected Tournament tournament;
	private boolean unsaved = false, there = false, paid = false;

	public Player() {

	}

	public Player(Tournament tournament) {
		this.tournament = tournament;
		matches = new ArrayList<Match>();
	}

	public static Player getNobody() {
		try {
			return new Single(null, new String[] { "nobody", "nobody" });
		} catch (InputFormatException e) {
			System.out
					.println("ERROR: Unexpected behaviour in database.Player.getNobody()");
		}
		return null;
	}

	public void addMatch(Match m) {
		matches.add(m);
	}

	@Override
	public int compareTo(Player p) {
		if (getPoints() > p.getPoints())
			return -1;
		if (getPoints() < p.getPoints())
			return 1;
		if (getSentences() > p.getSentences())
			return -1;
		if (getSentences() < p.getSentences())
			return 1;
		if (getBalls() > p.getBalls())
			return -1;
		if (getBalls() < p.getBalls())
			return 1;
		return 0;
	}

	public void delMatch(Match m) {
		matches.remove(m);
	}

	public int getBalls() {
		int result = 0;
		if (matches.isEmpty())
			return 0;
		for (Match m : matches) {
			if (m.getGroup() != -1) {
				if (m.getLeftPlayer().equals(this)) {
					result += m.getLeftBalls();
					result -= m.getRightBalls();
				} else {
					result += m.getRightBalls();
					result -= m.getLeftBalls();
				}
			}
		}
		return result;
	}

	public abstract String getFullName();

	public List<Match> getMatches() {
		return matches;
	}

	public abstract List<Person> getPersons();

	public String getPlayerPlaceGroup() {
		if (this.equals(Player.getNobody()))
			return toString();
		if (this.tournament == null)
			return toString();
		if (!tournament.getDoQualifying())
			return toString();
		String result = "";
		for (Group g : tournament.getQualifying().getGroups())
			if (g.getPlayers().contains(this)) {
				List<Player> help = new ArrayList<Player>();
				help.addAll(g.getPlayers());
				Collections.sort(help);
				result += (help.indexOf(this) + 1)
						+ ".Gr"
						+ (tournament.getQualifying().getGroups().indexOf(g) + 1);
			}
		result += ": " + toString();
		return result;
	}

	public int getPoints() {
		int result = 0;
		if (matches.isEmpty())
			return 0;
		for (Match m : matches) {
			if (m.getGroup() != -1) {
				if (m.getWinner() != null) {
					if (m.getWinner().equals(this))
						result++;
					else
						result--;
				}
			}
		}
		return result;
	}

	public int getRank() {
		for (Group g : tournament.getQualifying().getGroups())
			if (g.getPlayers().contains(this)) {
				List<Player> help = new ArrayList<Player>();
				help.addAll(g.getPlayers());
				Collections.sort(help);
				return (help.indexOf(this) + 1);
			}
		return 0;
	}

	public int getSentences() {
		int result = 0;
		if (matches.isEmpty())
			return 0;
		for (Match m : matches) {
			if (m.getGroup() != -1) {
				if (m.getLeftPlayer().equals(this)) {
					result += m.getLeftSentences();
					result -= m.getRightSentences();
				} else {
					result += m.getRightSentences();
					result -= m.getLeftSentences();
				}
			}
		}
		return result;
	}

	public Tournament getTournament() {
		return tournament;
	}

	@Override
	public boolean isNobody() {
		return (tournament == null)
				& getPersons().get(0).getSurname().equals("Nobody");
	}

	public boolean isThere() {
		return there;
	}

	public boolean isUnsaved() {
		return unsaved;
	}

	public void setThere(boolean newThere) {
		there = newThere;
	}

	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
	}

	public String[] toArray() {
		String[] result = new String[5];
		result[0] = toString();
		int rBalls = 0, lBalls = 0, rSentences = 0, lSentences = 0, rPoints = 0, lPoints = 0;
		Group group = null;
		Qualifying q = tournament.getQualifying();
		for (Group g : q.getGroups())
			if (g.getPlayers().contains(this))
				group = g;

		for (Match g : group.getMatches()) {
			if (g.getWinner() != null
					& (g.getLeftPlayer().equals(this) | g.getRightPlayer()
							.equals(this)))
				if (g.getWinner().equals(this))
					lPoints++;
				else
					rPoints++;
			if (g.getLeftPlayer().equals(this)) {
				lBalls += g.getLeftBalls();
				rBalls += g.getRightBalls();
				lSentences += g.getLeftSentences();
				rSentences += g.getRightSentences();
			}
			if (g.getRightPlayer().equals(this)) {
				lBalls += g.getRightBalls();
				rBalls += g.getLeftBalls();
				lSentences += g.getRightSentences();
				rSentences += g.getLeftSentences();
			}
		}
		result[1] = lBalls + ":" + rBalls;
		result[2] = lSentences + ":" + rSentences;
		result[3] = lPoints + ":" + rPoints;
		result[4] = "" + getRank();
		return result;
	}

	@Override
	public abstract String toString();

	public boolean isPaid() {
		return paid;
	}
	
	public void setPaid(boolean isPaid) {
		paid = isPaid;
	}

	public abstract String getClub();
}
