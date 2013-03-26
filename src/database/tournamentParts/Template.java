package database.tournamentParts;

import java.io.InputStream;

import gui.Language;

public class Template {
	private InputStream stream;
	private int groupNum, playerNum;

	public Template(InputStream str, int groupNum2, int playerNum2) {
		stream = str;
		groupNum = groupNum2;
		playerNum = playerNum2;
	}

	public String getDescription() {
		return "<html>" + Language.get("groups") + ": " + groupNum + "<br>"
				+ Language.get("players") + ": " + playerNum + "<html>";
	}

	public InputStream getInputStream() {
		return stream;
	}

	@Override
	public String toString() {
		return "ko-" + groupNum + "gr-" + playerNum + "pl";
	}
}
