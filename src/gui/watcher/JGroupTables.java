package gui.watcher;

import gui.Main;
import database.Calculator;
import database.tournamentParts.Qualifying;

@SuppressWarnings("serial")
public class JGroupTables extends JEmptyGroupTables {

	public JGroupTables(Qualifying qualifying, Main main) {
		super(qualifying, main);
	}

	@Override
	public void refresh() {
		area.setText(Calculator.htmlTabular(qualifying.getGroups()));
		repaint();
	}

}
