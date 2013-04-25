package database.tournamentParts;

import gui.Language;

public class Template {
	private String path;
	private int groupNum, playerNum;

	public Template(String path, int groupNum2, int playerNum2) {
		this.path = path;
		groupNum = groupNum2;
		playerNum = playerNum2;
	}

	public String getDescription() {
		return "<html>" + Language.get("groups") + ": " + groupNum + "<br>"
				+ Language.get("players") + ": " + playerNum + "<html>";
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return "ko-" + groupNum + "gr-" + playerNum + "pl";
	}
}
