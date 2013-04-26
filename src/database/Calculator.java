package database;

import gui.Language;

import java.util.ArrayList;
import java.util.List;

import database.match.Match;
import database.players.Player;
import database.tournamentParts.Group;

/**
 * Performs Calculations needed by several Classes from several Packages.
 * 
 * @author Tobias Denkinger
 * 
 */
public class Calculator {
	/**
	 * Returns the size of the index array
	 * 
	 * @param players
	 *            number of players
	 * @return size of the index array
	 */
	private static int arraySize(int players) {
		if (players == 2)
			return 1;
		return players - 1 + arraySize(players - 1);
	}

	/**
	 * Generates a list of games depending on the players in the group
	 * 
	 * @param g
	 *            Group
	 * @return list of games
	 */
	public static List<Match> calculateGames(Group g) {
		List<Player> players = g.getPlayers();
		if (players.size() < 2) {
			System.err.println("Not enough players for games: "
					+ g.toString());
			return new ArrayList<Match>();
		}
		int[] arrayJGJ = getArrayJGJ(players.size());
		double length = arrayJGJ.length;
		List<Match> games = new ArrayList<Match>();
		// we want to sort the games by priority, therefore we define a
		// priorityInverse for the group,
		// it is the lowest common multiple of all lengths. By dividing by the
		// length, we get the minimum
		// priorityInverse for a group of this length, which corresponds to the
		// maximum priority. The Games
		// will be constructed with multiples of this. Which causes a uniform
		// distribution for the Games of
		// each group in the field of of all games.
		int i;
		double priorityInverse = 1 / length;
		for (i = 0; i < length; i = i + 2) {
			Player p1 = players.get(arrayJGJ[i] - 1);
			Player p2 = players.get(arrayJGJ[i + 1] - 1);
			Match game = new Match(0.5 * i * priorityInverse, g.getNum(), p1,
					p2);
			boolean found = false;
			for (Match gameOld : g.getMatches())
				if (gameOld.equals(game)) {
					gameOld.setPriority(game.getPriority());
					gameOld.setGroup(game.getGroup());
					games.add(gameOld);
					found = true;
				}
			if (!found) {
				games.add(game);
				p1.addMatch(game);
				p2.addMatch(game);
			}

		}
		return games;
	}

	/**
	 * Checks if a number is even
	 * 
	 * @param number
	 *            number to check
	 * @return true if number is even, false otherwise
	 */
	private static boolean even(int number) {
		if (number % 2 == 0)
			return true;
		return false;
	}

	/**
	 * Calculates the array of indexes for games in groups of the specific
	 * number of players
	 * 
	 * @param players
	 *            number of players
	 * @return array of indexes
	 */
	private static int[] getArrayJGJ(int players) {
		int length = 2 * arraySize(players);
		int[] result = new int[length];
		int idx = -1;
		for (int i = 1; i < nextEven(players); i++) {
			if (even(players)) {
				idx++;
				result[idx] = i;
				idx++;
				result[idx] = players;
			}
			for (int j = 1; j < (nextEven(players)) / 2; j++) {
				idx++;
				result[idx] = mod(i + j, nextEven(players) - 1);
				idx++;
				result[idx] = mod(i - j + nextEven(players) - 1,
						nextEven(players) - 1);
			}
		}
		return result;
	}

	/**
	 * Returns the List of games having the given state
	 * 
	 * @param games
	 *            List of games
	 * @param state
	 *            state to look for
	 * @return games with given state
	 */
	public static List<Match> getGamesByState(List<Match> games, int state) {
		List<Match> result = new ArrayList<Match>();
		for (Match g : games)
			if (g.getState() == state)
				result.add(g);
		return result;
	}

	/**
	 * HTML code for a tabular for a qualifying with groups
	 * 
	 * @param filled
	 *            if results should be inserted
	 * @param list
	 *            list of groups
	 * @return HTML code
	 */
	public static String htmlTabular(boolean filled, List<Group> list) {
		String result = "<h2>" + Language.get("qualifying") + "</h2>";
		if (filled)
			for (Group g : list) {
				result += "<table border=1 width=100% style=\"border-collapse:collapse;\">";
				// colgroup stuff
				result += "<colgroup><col width=40><col width=25%></colgroup><colgroup span="
						+ g.getSize() + "><colgroup width=10% span=3>";
				result += "<tr>";
				int number = g.getSize();
				result += "<td></td><td><b>" + Language.get("group") + " "
						+ (list.indexOf(g) + 1) + "</b></td>";

				for (int i = 0; i < number; i++)
					result += "<td>" + (i + 1) + "</td>";
				
				result += "<td>" + Language.get("balls") + "</td>";
				result += "<td>" + Language.get("sentences") + "</td>";
				result += "<td>" + Language.get("games") + "</td>";
				result += "<td>" + Language.get("rank") + "</td></tr>";

				for (Player p : g.getPlayers()) {
					result += "<tr><td>" + (g.getPlayers().indexOf(p) + 1)
							+ "</td><td>" + p.toString() + "</td>";
					for (int i = 0; i < number; i++) {
						String sentence = "&nbsp";
						if (g.getPlayers().get(i).equals(p))
							sentence = "X";
						else
							for (Match game : p.getMatches())
								if (game.getState() == 2
										& game.getPriority() != -1) {
									if (game.getRightPlayer().equals(
											g.getPlayers().get(i)))
										sentence = game.getLeftSentences()
												+ ":"
												+ game.getRightSentences();
									if (game.getLeftPlayer().equals(
											g.getPlayers().get(i)))
										sentence = game.getRightSentences()
												+ ":" + game.getLeftSentences();
								}
						result += "<td>" + sentence + "</td>";
					}
					String[] help = p.toArray();
					result += "<td>" + help[1] + "</td>";
					result += "<td>" + help[2] + "</td>";
					result += "<td>" + help[3] + "</td>";
					result += "<td>" + g.getPlayersPlace(p) + "</td>";
					result += "</tr>";
				}

				result += "</table>";
			}
		else
			for (Group g : list) {
				result += "<table border=1 width=100% style=\"border-collapse:collapse;\"><tr>";
				int number = g.getSize();
				result += "<td></td><td><b>" + Language.get("group") + " "
						+ (list.indexOf(g) + 1) + "</b></td>";

				for (int i = 0; i < number; i++)
					result += "<td>" + (i + 1) + "</td>";

				result += "<td>" + Language.get("games") + "</td>";
				result += "<td>" + Language.get("sentences") + "</td>";
				result += "<td>" + Language.get("rank") + "</td></tr>";

				for (Player p : g.getPlayers()) {
					result += "<tr><td>" + (g.getPlayers().indexOf(p) + 1)
							+ "</td><td>" + p.toString() + "</td>";
					for (int i = 0; i < number; i++)
						if (g.getPlayers().get(i).equals(p))
							result += "<td>X</td>";
						else
							result += "<td>&nbsp</td>";
					for (int i = 0; i < 3; i++)
						result += "<td>&nbsp</td>";
					result += "</tr>";
				}

				result += "</table>";
			}
		return result;
	}

	/**
	 * Creates non empty group tables in HTML
	 * 
	 * @param list
	 *            List of groups
	 * @return HTML tables (non empty)
	 */
	public static String htmlTabular(List<Group> list) {
		String result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;"
				+ " charset=utf-8\"/><title>"
				+ Language.get("qualifying")
				+ "</title></head>" + "<body>";
		result += htmlTabular(true, list);
		result += "</body></html>";
		return result;
	}

	/**
	 * Converts a List of groups into HTML code, containing the empty group
	 * tables
	 * 
	 * @param list
	 *            list of groups
	 * @return HTML group tables
	 */
	public static String htmlTabularEmpty(List<Group> list) {
		String result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;"
				+ " charset=utf-8\"/><title>"
				+ Language.get("qualifying")
				+ "</title></head>" + "<body>";
		result += htmlTabular(false, list);
		return result;
	}

	/**
	 * returns the maximum of two integers
	 * 
	 * @param i
	 *            integer1
	 * @param j
	 *            integer2
	 * @return maximum
	 */
	public static int max(int i, int j) {
		if (i > j)
			return i;
		return j;
	}

	/**
	 * returns the minimum of two integers
	 * 
	 * @param i
	 *            integer1
	 * @param j
	 *            integer2
	 * @return minimum
	 */
	public static int min(int i, int j) {
		if (i < j)
			return i;
		return j;
	}

	/**
	 * Returns left modulo right
	 * 
	 * @param left
	 *            number
	 * @param right
	 *            module
	 * @return left (mod right) or right if left (mod right)==0
	 */
	private static int mod(int left, int right) {
		int result = left % right;
		if (result == 0)
			return right;
		return result;
	}

	/**
	 * Returns the smallest even number higher or equal to number
	 * 
	 * @param number
	 *            number to start from
	 * @return 2n where 2n=minimum{2n>=number}
	 */
	private static int nextEven(int number) {
		if (number % 2 == 0)
			return number;
		return number + 1;
	}

	/**
	 * 2^(x-1)<i<=2
	 * 
	 * @param i
	 * @return 2
	 */
	public static int nextPowerOfTwo(int i) {
		int result = 1;
		while (result < i)
			result *= 2;
		return result;
	}

	/**
	 * returns true if a Player plays in one of the Games in the List
	 * 
	 * @param list
	 *            Game List
	 * @param player
	 *            Player
	 * @return if player plays
	 */
	public static boolean plays(List<Match> list, Player player) {
		for (Match g : list) {
			if (g.getLeftPlayer().equals(player))
				return true;
			if (g.getRightPlayer().equals(player))
				return true;
		}
		return false;
	}

	/**
	 * Array representation of a tabular containing groups results in qualifying
	 * 
	 * @param g
	 *            group to generate results for
	 * @return tabular for group results
	 */
	public static String[][] tabular(Group g) {
		List<String[]> result = new ArrayList<String[]>();
		int i = 0;
		for (Player p : g.getPlayers()) {
			i++;
			List<String> lst = new ArrayList<String>();
			lst.add("" + i);
			lst.add(p.toString());
			for (Player q : g.getPlayers())
				lst.add(g.getGameResultString(p, q));
			String[] help = p.toArray();
//			lst.add(help[1]);
			lst.add(help[2]);
			lst.add(help[3]);
			lst.add(help[4]);
			result.add(lst.toArray(new String[0]));
		}
		return result.toArray(new String[0][0]);
	}

	/**
	 * Array representation of the tabulars head
	 * 
	 * @param g
	 *            group to generate head for
	 * @return head for group tabular
	 */
	public static String[] tabularHead(Group g) {
		List<String> result = new ArrayList<String>();
		result.add(Language.get("number"));
		result.add(Language.get("name"));
		int i = 0;
		for (@SuppressWarnings("unused")
		Player p : g.getPlayers()) {
			i++;
			result.add("" + i);
		}
//		result.add(Language.get("balls"));
		result.add(Language.get("sentences"));
		result.add(Language.get("games"));
		result.add(Language.get("rank"));
		return result.toArray(new String[0]);
	}
}
