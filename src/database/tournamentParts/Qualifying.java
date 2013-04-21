package database.tournamentParts;

import java.util.ArrayList;
import java.util.List;

import database.Calculator;
import database.match.Commandable;
import database.match.Match;
import database.players.Player;

public class Qualifying extends Commandable {
	private List<Group> groups = new ArrayList<Group>();
	private List<Player> unassigned = new ArrayList<Player>();
	private boolean unsaved = false;

	Qualifying(int gruppen) {
		groups = new ArrayList<Group>();
		int i = 0;
		for (i = 0; i < gruppen; i++)
			addGroup();
		unsaved = true;
	}

	public void addGroup() {
		groups.add(new Group(groups.size() + 1));
		unsaved = true;
	}

	@Override
	public boolean addMatch(Match game) {
		return true;
	}

	public void assignPlayer(Player player, Group group) {
		group.addPlayer(player);
		unassigned.remove(player);
		unsaved = true;
	}

	public void delGroup(int idx) {
		groups.remove(idx);
		if (idx < groups.size()) {
			int i = idx;
			for (Group g : groups.subList(idx, groups.size())) {
				i++;
				g.setNum(i);
			}
		}
		unsaved = true;
	}

	@Override
	public void delMatch(Match game) {

	}

	@Override
	public int getFinishedGamesCount() {
		return Calculator.getGamesByState(getMatches(), 2).size();
	}

	@Override
	public List<Match> getMatchByState(int state) {
		List<Match> games = new ArrayList<Match>();
		for (Group g : groups)
			games.addAll(g.getMatchByState(state));
		return games;
	}

	public List<Group> getGroups() {
		return groups;
	}

	@Override
	public List<Match> getMatches() {
		List<Match> result = new ArrayList<Match>();
		for (Group g : groups)
			result.addAll(g.getMatches());
		return result;
	}

	@Override
	public int getTotalGamesCount() {
		return getMatches().size();
	}

	public List<Player> getUnassigned() {
		return unassigned;
	}

	public boolean isDone() {
		return (getMatchByState(0).size() + getMatchByState(1).size() == 0);
	}

	public boolean isUnsaved() {
		boolean result = unsaved;
		for (Group g : groups)
			result = result | g.isUnsaved();
		for (Player p : unassigned)
			result = result | p.isUnsaved();
		return result;
	}

	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
		if (unsaved == false) {
			for (Group g : groups)
				g.setUnsaved(false);
			for (Player p : unassigned)
				p.setUnsaved(false);
		}
	}

	public void unassignPlayer(Player player, Group group) {
		if (group != null)
			group.delPlayer(player);
		unassigned.add(player);
		unsaved = true;
	}

};
