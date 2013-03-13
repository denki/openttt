package database.tournamentParts;

import gui.Language;

public class Template {
	private String fileName, directory;
	private int groupNum, playerNum;

	public Template(String directory, String fileName) {
		this.directory = directory;
		this.fileName = fileName;

		String[] splitted = fileName.split("-");
		splitted[1] = splitted[1].split("gr")[0];
		splitted[2] = splitted[2].split("pl")[0];

		groupNum = Integer.parseInt(splitted[1]);
		playerNum = Integer.parseInt(splitted[2]);
	}

	public String getDescription() {
		return "<html>" + Language.get("groups") + ": " + groupNum + "<br>"
				+ Language.get("players") + ": " + playerNum + "<html>";
	}

	public String getPath() {
		return directory + "/" + fileName;
	}

	@Override
	public String toString() {
		return fileName;
	}
}
