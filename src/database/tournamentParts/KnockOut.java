package database.tournamentParts;

import java.util.ArrayList;
import java.util.List;

import database.Calculator;
import database.match.Commandable;
import database.match.Match;
import database.players.Player;

/**
 * Represents a knock out
 * 
 * @author Tobias Denkinger
 * 
 */
public class KnockOut extends Commandable {
	private List<Match> matches;
	private List<List<Player>> survivors;
	private boolean unsaved = false;

	public KnockOut() {
		matches = new ArrayList<Match>();
		survivors = new ArrayList<List<Player>>();
		unsaved = true;
	}

	@Override
	public boolean addMatch(Match g) {
		for (List<Player> plrs : survivors)
			for (int i = 0; i < plrs.size(); i = i + 2)
				if (plrs.size() > i + 1)
					if (plrs.get(i) != null & plrs.get(i + 1) != null)
						if ((plrs.get(i).equals(g.getLeftPlayer()))
								& (plrs.get(i + 1).equals(g.getRightPlayer()))) {
							int idx = survivors.indexOf(plrs);
							survivors.get(idx + 1).set(i / 2, g.getWinner());
							return true;
						}
		return false;
	}

	@Override
	public void delMatch(Match g) {
		for (List<Player> plrs : survivors)
			for (int i = 0; i < plrs.size(); i = i + 2)
				if (plrs.size() > i + 1)
					if (plrs.get(i) != null & plrs.get(i + 1) != null)
						if ((plrs.get(i).equals(g.getLeftPlayer()))
								& (plrs.get(i + 1).equals(g.getRightPlayer()))) {
							int idx = survivors.indexOf(plrs);

							if (survivors.size() > idx + 2) {
								// calculate affected games
								int idxn1, idxn2;
								if ((i / 2) % 2 == 0) {
									idxn1 = i / 2;
									idxn2 = i / 2 + 1;
								} else {
									idxn1 = i / 2 - 1;
									idxn2 = i / 2;
								}
								Player pLeft = survivors.get(idx + 1)
										.get(idxn1);
								Player pRight = survivors.get(idx + 1).get(
										idxn2);
								Match m = new Match(pLeft, pRight);
								delMatch(m);
							}
							// delete affected players
							survivors.get(idx + 1).set(i / 2, null);
						}
	}

	@Override
	public int getFinishedGamesCount() {
		return Calculator.getGamesByState(getMatches(), 2).size();
	}

	@Override
	public List<Match> getGamesByState(int state) {
		List<Match> result = new ArrayList<Match>();
		for (Match g : getMatches())
			if (g.getState() == state)
				result.add(g);
		return result;
	}

	@Override
	public List<Match> getMatches() {
		List<Match> newGames = new ArrayList<Match>();
		for (List<Player> plrs : survivors)
			for (int i = 0; i < plrs.size(); i = i + 2)
				if (plrs.size() > i + 1)
					if ((plrs.get(i) != null) & (plrs.get(i + 1) != null)) {
						Match g = new Match(plrs.get(i), plrs.get(i + 1));
						if (!matches.contains(g)) {
							newGames.add(g);
							setUnsaved(true);
						} else
							newGames.add(matches.get(matches.indexOf(g)));
					}
		matches = newGames;
		return matches;
	}

	@Override
	public int getTotalGamesCount() {
		int result = 0;
		for (List<Player> plrs : survivors)
			for (int i = 0; i < plrs.size(); i = i + 2)
				if (plrs.size() > i + 1)
					if ((survivors.indexOf(plrs) > 0)
							| ((plrs.get(i) != null) & (plrs.get(i + 1) != null)))
						result++;
		return result;
	}

	/**
	 * Returns the
	 * 
	 * @return tree of survivors
	 */
	public List<List<Player>> getTree() {
		return survivors;
	}

	/**
	 * Returns if the KnockOut is over
	 * 
	 * @return true if KnockOut is over, false otherwise
	 */
	public boolean isDone() {
		return (survivors.get(survivors.size() - 1).get(0) != null);
	}

	/**
	 * Indicates unsaved changes
	 * 
	 * @return true if there are unsaved changes, false otherwise
	 */
	public boolean isUnsaved() {
		boolean result = unsaved;
		for (Match g : matches)
			result = result | g.isUnsaved();
		for (List<Player> plrs : survivors)
			for (Player p : plrs)
				if (p != null)
					result = result | p.isUnsaved();
		return result;
	}

	public void setPlayers(List<Player> plrs) {
		boolean doIt = survivors.isEmpty();
		if (!doIt)
			doIt = !survivors.get(0).equals(plrs);
		if (doIt) {
			survivors.clear();
			int i = plrs.size();
			List<Player> filtered = new ArrayList<Player>();
			for (Player p : plrs)
				if (p == null)
					filtered.add(null);
				else if (p.isNobody())
					filtered.add(null);
				else
					filtered.add(p);
			survivors.add(filtered);
			while (i > 1) {
				List<Player> lst = new ArrayList<Player>();
				i = i / 2;
				for (int j = 0; j < i; j++)
					lst.add(null);
				survivors.add(lst);
			}
			for (i = 0; i < plrs.size(); i += 2) {
				if (filtered.get(i) == null)
					survivors.get(1).set(i / 2, plrs.get(i + 1));
				if (filtered.get(i + 1) == null)
					survivors.get(1).set(i / 2, plrs.get(i));
			}
			
			//add already played games here
			List<Match> oldMatches = new ArrayList<Match>();
			oldMatches.addAll(matches);
			matches.clear();
			boolean addedSomething = true;
			while (addedSomething & !oldMatches.isEmpty()) {
				addedSomething = false;
				for (Match g: oldMatches) {
					if (addMatch(g)) {
						matches.add(g);
						addedSomething = true;
					}
				}
				oldMatches.removeAll(matches);
			}
			
			unsaved = true;
		}

	}

	/**
	 * Sets state to saved/unsaved
	 * 
	 * @param unsaved
	 *            new saved state
	 */
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
		if (unsaved == false) {
			for (Match g : matches)
				g.setUnsaved(false);
			for (List<Player> plrs : survivors)
				for (Player p : plrs)
					if (p != null)
						p.setUnsaved(false);
		}
	}

}
