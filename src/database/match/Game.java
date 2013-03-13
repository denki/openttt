package database.match;

import database.Calculator;

/**
 * Represents a game between two players (a tuple of int with some features)
 * 
 * @author Tobias Denkinger
 * 
 */
public class Game {
	private int leftBalls = 0, rightBalls = 0;

	/**
	 * Constructor for a game
	 * @param leftBalls number of balls, the left player won
	 * @param rightBalls number of balls, the right player won
	 */
	public Game(int leftBalls, int rightBalls) {
		this.leftBalls = leftBalls;
		this.rightBalls = rightBalls;
	}

	/**
	 * Constructor for game
	 * @param tendenceString String representing a short form of the Game outcome
	 */
	public Game(String tendenceString) {
		int tendence = Integer.parseInt(tendenceString.replace("+", ""));
		if (tendence < 0) {
			if (tendence > -10) {
				leftBalls = -tendence;
				rightBalls = 11;
			} else {
				leftBalls = -tendence;
				rightBalls = 2 - tendence;
			}
		} else if (tendence > 0) {
			if (tendence < 10) {
				leftBalls = 11;
				rightBalls = tendence;
			} else {
				rightBalls = tendence;
				leftBalls = tendence + 2;
			}
		} else if (tendenceString.startsWith("-")) {
			leftBalls = 0;
			rightBalls = 11;
		} else {
			leftBalls = 11;
			rightBalls = 0;
		}
	}

	/**
	 * returns the left balls
	 * @return left balls
	 */
	public int getLeftBalls() {
		return leftBalls;
	}

	/**
	 * returns the right balls
	 * @return right balls
	 */
	public int getRightBalls() {
		return rightBalls;
	}

	/**
	 * returns the tendence
	 * @return tendence
	 */
	public String getTendence() {
		if (leftBalls > rightBalls)
			return "+" + rightBalls;
		else
			return "-" + leftBalls;
	}

	/**
	 * determines if Game is valid
	 * @return true if valid, otherwise false
	 */
	public boolean isOK() {
		int high = Calculator.max(leftBalls, rightBalls);
		int low = Calculator.min(leftBalls, rightBalls);

		boolean gt11 = (high > 11);
		int diff = high - low;
		if (gt11)
			return (diff == 2);
		else
			return (diff > 1);
	}

	@Override
	public String toString() {
		return leftBalls + ":" + rightBalls;
	}

}
