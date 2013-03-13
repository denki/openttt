package database.match;

import java.util.List;

/**
 * Class that is commandable by gui.components.JGameCommander
 * 
 * @author Tobias Denkinger
 * 
 */
public abstract class Commandable {
	/**
	 * Adds a game and its results
	 * 
	 * @param game
	 *            game to add
	 */
	public abstract boolean addMatch(Match game);

	/**
	 * Removes the game and its results
	 * 
	 * @param game
	 *            game to remove
	 */
	public abstract void delMatch(Match game);

	/**
	 * Count of finished games
	 * 
	 * @return count of finished games
	 */
	public abstract int getFinishedGamesCount();

	/**
	 * All games in specified state
	 * 
	 * @param state
	 *            specified state
	 * @return games in specified state
	 */
	public abstract List<Match> getGamesByState(int state);

	/**
	 * List of all encapsuled games
	 * 
	 * @return list of encapsuled games
	 */
	public abstract List<Match> getMatches();

	/**
	 * Count of all games that will ever be played
	 * 
	 * @return count of all games
	 */
	public abstract int getTotalGamesCount();

}
