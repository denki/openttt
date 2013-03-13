package gui.watcher;

import gui.Interaction;
import gui.Language;
import gui.Main;
import gui.templates.Watcher;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import database.Calculator;
import database.tournamentParts.Qualifying;

@SuppressWarnings("serial")
public class JEmptyGroupTables extends Watcher {

	protected JEditorPane area;
	protected Qualifying qualifying;

	public JEmptyGroupTables(Qualifying qualifying2, Main main) {
		super(Language.get("groupTables"), main);
		this.qualifying = qualifying2;
	}
	
	public static void print(String text) {
		Interaction.saveHtml("groupTable", text);
		Interaction.print("groupTable");

	}

	@Override
	public void generateWindow() {
		area = new JEditorPane("text/html", "");
		refresh();
		add(new JScrollPane(area));
		pack();
		setVisible(true);
	}

	@Override
	public void refresh() {
		area.setText(Calculator.htmlTabularEmpty(qualifying.getGroups()));
		repaint();
	}

}
