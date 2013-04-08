package database.tournamentParts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.match.Commandable;
import database.match.Match;
import database.players.Player;

public class KnockOut2 extends Commandable {

	protected interface Tree {
		boolean hasChildren();

		List<Tree> getChildren();
	}

	protected class Nullary implements Tree {
		public boolean hasChildren() {
			return false;
		}

		public List<Tree> getChildren() {
			return Collections.emptyList();
		}
	}

	protected class Binary implements Tree {
		private Tree left, right;

		public Binary(Tree left, Tree right) {
			this.left = left;
			this.right = right;
		}

		public boolean hasChildren() {
			return true;
		}

		public List<Tree> getChildren() {
			List<Tree> result = new ArrayList<>();
			result.add(left);
			result.add(right);
			return result;
		}
	}

	private Tree tree;
	private Map<Tree, Player> players;
	private Map<Tree, Match> matches;

	public KnockOut2() {
		tree = null;
		players = new HashMap<Tree, Player>();
		matches = new HashMap<Tree, Match>();
	}

	public void setPlayers(List<Player> players) {
		if (tree == null) {
			tree = setPlayers(players.toArray(new Player[players.size()]),
					this.players);
		}
	}

	public Tree setPlayers(Player[] players, Map<Tree, Player> plrs) {
		if (players.length == 1) {
			Tree result = new Nullary();
			plrs.put(result, players[0]);
			return result;
		} else if (players.length == 2) {
			if (players[0] == null) {
				Tree result = new Nullary();
				plrs.put(result, players[1]);
				return result;
			} else if (players[1] == null) {
				Tree result = new Nullary();
				plrs.put(result, players[0]);
				return result;
			} else {
				Tree l = setPlayers(Arrays.copyOfRange(players, 0, 0), plrs);
				Tree r = setPlayers(Arrays.copyOfRange(players, 1, 1), plrs);
				return new Binary(l, r);
			}
		} else {
			Player[] pL = Arrays.copyOfRange(players, 0, players.length / 2);
			Player[] pR = Arrays.copyOfRange(players, players.length / 2 + 1,
					players.length);
			Tree l = setPlayers(pL, plrs);
			Tree r = setPlayers(pR, plrs);
			return new Binary(l, r);
		}
	}

	@Override
	public boolean addMatch(Match game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delMatch(Match game) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFinishedGamesCount() {
		return matches.size();
	}

	@Override
	public List<Match> getGamesByState(int state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Match> getMatches() {
		return null;
	}

	@Override
	public int getTotalGamesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
