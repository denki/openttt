package database.tournamentParts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import util.Binary;
import util.Nullary;
import util.Tree;

import database.match.Commandable;
import database.match.Match;
import database.players.Player;

public class KnockOut extends Commandable {

	private Tree tree;
	private boolean unsaved = false;
	private boolean done = false;
	private Map<Tree, Player> players;
	private Map<Tree, Match> matches;
	private Map<Set<Player>, Match> blackBox;

	public KnockOut() {
		tree = null;
		players = new HashMap<Tree, Player>();
		matches = new HashMap<Tree, Match>();
	}

	public void setPlayers(List<Player> pls) {
		Queue<Tree> st = new LinkedList<Tree>();
		players.clear();
		for (Match m : getMatches()){
			Set<Player> plrs = new HashSet<Player>();
			plrs.add(m.getLeft());
			plrs.add(m.getRight());
			getBlackBox().put(plrs, m);
		}
		matches.clear();
		for (Player p : pls) {
			if (p.isNobody() || p == null)
				st.add(null);
			else {
				Tree n = new Nullary();
				st.add(n);
				players.put(n, p);
			}
		}
		tree = setPlayers(st);
		visitNodes();
		unsaved = true;
	}

	private Tree setPlayers(Queue<Tree> st) {
		if (st.size() == 1)
			return st.poll();
		Tree t1, t2;
		Queue<Tree> st1 = new LinkedList<Tree>();
		while (!st.isEmpty()) {
			t1 = st.poll();
			t2 = st.poll();
			if (t1 == null)
				st1.add(t2);
			else if (t2 == null)
				st1.add(t1);
			else
				st1.add(new Binary(t1, t2));
		}
		return setPlayers(st1);
	}

	@Override
	public boolean addMatch(Match game) {
		visitNodes();
		unsaved = true;
		return true;
	}

	@Override
	public void delMatch(Match game) {
		visitNodes();
		unsaved = true;
	}

	private void visitNodes() {
		done = visitNodes(tree, 1);
	}

	private boolean visitNodes(Tree t, double priority) {
		if (!t.hasChildren())
			return true;
		boolean done = true;
		for (Tree t1 : t.getChildren())
			done = done & visitNodes(t1, priority / 2);

		Player pl = players.get(t.getChildren().get(0));
		Player pr = players.get(t.getChildren().get(1));

		// children do not exist
		if (pl == null || pr == null) {
			matches.remove(t);
			players.remove(t);
			return false;
		}

		// match is empty or players do not match
		if (matches.get(t) == null || !matches.get(t).getLeft().equals(pl)
				|| !matches.get(t).getRight().equals(pr)) {
			players.remove(t);
			matches.put(t, newMatch(pl, pr, priority));
		}

		// there exists a winner
		if (matches.get(t).getWinner() != null)
			players.put(t, matches.get(t).getWinner());
		else
			players.remove(t);
		
		return done & (players.get(t) != null);
	}

	private Match newMatch(Player pl, Player pr, double priority) {
		Match m = new Match(priority, pl, pr);
		Set<Player> plrs = new HashSet<Player>();
		plrs.add(pl);
		plrs.add(pr);
		if (getBlackBox().containsKey(plrs)){
			Match m2 = getBlackBox().get(plrs);
			if (m2.getLeft().equals(pl))
				m = m2;
			else
				m = m2.flip();
		}
		return m;
	}

	private Map<Set<Player>, Match> getBlackBox() {
		if (blackBox == null)
			blackBox = new HashMap<Set<Player>, Match>();
		return blackBox;
	}

	@Override
	public int getFinishedGamesCount() {
		return getMatchByState(Match.STATE_FINISHED).size();
	}

	@Override
	public List<Match> getMatchByState(int state) {
		List<Match> result = new ArrayList<Match>();
		for (Match e : getMatches())
			if (e.getState() == state)
				result.add(e);
		return result;
	}

	@Override
	public List<Match> getMatches() {
		List<Match> result = new ArrayList<Match>();
		for (Entry<Tree, Match> e : matches.entrySet())
			result.add(e.getValue());
		return result;
	}

	@Override
	public int getTotalGamesCount() {
		return tree.countNodes();
	}

	public boolean isUnsaved() {
		return unsaved;
	}

	public void setUnsaved(boolean b) {
		unsaved = b;
	}

	public List<Player> getRanking() {
		List<Player> result = new ArrayList<Player>();
		List<Tree> trees = Collections.singletonList(tree);
		rank(result, trees);
		return result;
	}

	private void rank(List<Player> result, List<Tree> trees) {
		List<Tree> trees1 = new ArrayList<Tree>();
		for (Tree t : trees) {
			if (!players.containsKey(t))
				result.add(Player.getNobody());
			else if (!result.contains(players.get(t)))
				result.add(players.get(t));
			trees1.addAll(t.getChildren());
		}
		if (!trees1.isEmpty())
			rank(result, trees1);
	}

	public boolean isDone() {
		return done;
	}

	public Tree getTree() {
		return tree;
	}

	public Map<Tree, Player> getPlayersMap() {
		return players;
	}

	public Map<Tree, Match> getMatchesMap() {
		return matches;
	}

}
