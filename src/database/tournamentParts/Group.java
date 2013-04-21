package database.tournamentParts;

import gui.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.Calculator;
import database.match.Commandable;
import database.match.Match;
import database.players.Player;

/**
 * Represents a qualifying group
 * 
 * @author Tobias Denkinger
 * 
 */
public class Group extends Commandable{
	private List<Match> games;
	private int num = 0;
	private List<Player> players;

	private boolean unsaved = false;

	/**
	 * Constructs and saves the group number
	 * 
	 * @param groupNum
	 */
	public Group(int groupNum) {
		players = new ArrayList<Player>();
		games = new ArrayList<Match>();
		num = groupNum;
	}

	/**
	 * Add a player to the group
	 * 
	 * @param player
	 *            player to add
	 */
	void addPlayer(Player player) {
		players.add(player);
		unsaved = true;
	}

	/**
	 * removes a player from group
	 * 
	 * @param p
	 *            player to remove
	 */
	void delPlayer(Player p) {
		players.remove(p);
		unsaved = true;
	}

	/**
	 * Returns the game result as a string
	 * 
	 * @param p
	 *            player1
	 * @param q
	 *            player2
	 * @return game result
	 */
	public String getGameResultString(Player p, Player q) {
		if (p.equals(q))
			return "X";
		for (Match g : p.getMatches()) {
			if (g.getPriority() != -1) {
				if (g.getRightPlayer().equals(q))
					return g.getLeftSentences() + ":" + g.getRightSentences();
				if (g.getLeftPlayer().equals(q))
					return g.getRightSentences() + ":" + g.getLeftSentences();
			}
		}
		return "";
	}

	/**
	 * List of containing games with specified state
	 * 
	 * @param state
	 *            specified state
	 * @return List of games with specified state
	 */
	@Override
	public List<Match> getMatchByState(int state) {
		List<Match> result = new ArrayList<Match>();
		for (Match g : games)
			if (g.getState() == state)
				result.add(g);
		return result;
	}

	/**
	 * Returns the group number
	 * 
	 * @return group number
	 */
	public int getNum() {
		return num;
	}

	/**
	 * contained players
	 * 
	 * @return contained players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Sorted list of players from best to worst rank
	 * 
	 * @param best
	 *            best rank
	 * @param worst
	 *            worst rank
	 * @return Sorted list of players
	 */
	public List<Player> getPlayersByPlace(int best, int worst) {
		List<Player> help = new ArrayList<Player>();
		help.addAll(players);
		Collections.sort(help);
		return help.subList(best - 1, worst - 1);
	}

	/**
	 * Place of specified player
	 * 
	 * @param p
	 *            specified player
	 * @return place of player
	 */
	public int getPlayersPlace(Player p) {
		List<Player> help = new ArrayList<Player>();
		help.addAll(players);
		Collections.sort(help);
		return help.indexOf(p) + 1;
	}

	/**
	 * Player statistics for tables
	 * 
	 * @return player statistics
	 */
	public Object[][] getPlayerStats() {
		Object[][] objArr = new Object[players.size()][4];
		List<Player> help = new ArrayList<Player>();
		help.addAll(players);
		Collections.sort(help);
		for (int i = 0; i < help.size(); i++)
			for (int j = 0; j < 4; j++)
				objArr[i][j] = help.get(i).toArray()[j];
		return objArr;
	}

	/**
	 * Returns number of players
	 * 
	 * @return number of players
	 */
	public int getSize() {
		return players.size();
	}

	/**
	 * Returns the existence of changes
	 * 
	 * @return existence of changes
	 */
	public boolean isUnsaved() {
		boolean result = unsaved;
		for (Match g : games)
			result = result | g.isUnsaved();
		for (Player p : players)
			result = result | p.isUnsaved();
		return result;
	}

	/**
	 * sets the group number
	 * 
	 * @param num
	 *            new group number
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * Notifies if changes have been made
	 * 
	 * @param unsaved
	 *            if changes have been made
	 */
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
		if (unsaved == false) {
			for (Match g : games)
				g.setUnsaved(false);
			for (Player p : players)
				p.setUnsaved(false);
		}
	}

	/**
	 * starts the group
	 * 
	 * @return number of generated games
	 */
	int startGroup() {
		games = Calculator.calculateGames(this);
		unsaved = true;
		return games.size();
	}

	/**
	 * swap 2 players initial positions
	 * 
	 * @param i
	 *            position of player1
	 * @param j
	 *            position of player2
	 */
	public void swapPlayers(int i, int j) {
		Player p = players.get(i);
		players.set(i, players.get(j));
		players.set(j, p);
		unsaved = true;
	}

	@Override
	public String toString() {
		return Language.get("group") + " " + num;
	}

	@Override
	public boolean addMatch(Match game) {
		return false;
	}

	@Override
	public void delMatch(Match game) {
		
	}

	@Override
	public int getFinishedGamesCount() {
		int finished = 0;
		for (Match g: games) {
			if (g.getState() == 2)
				finished++;
		}
		return finished;
	}

	@Override
	public List<Match> getMatches() {
		return games;
	}

	@Override
	public int getTotalGamesCount() {
		return games.size();
	}

}
