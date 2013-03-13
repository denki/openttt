package gui.components;

import gui.Language;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.Calculator;
import database.players.Player;

@SuppressWarnings("serial")
public class JRanking extends JPanel {

	private JEditorPane jEdit;

	private List<Player> list;

	public JRanking(List<Player> list) {
		String text = getText(list);
		setLayout(new GridLayout(1, 1));
		this.list = list;
		jEdit = new JEditorPane("text/html", text);
		add(new JScrollPane(jEdit));
	}

	public static String getText(List<Player> list) {
		String result = "<h2>" + Language.get("ranking") + "</h2>";
		result += "<table border=1 width=100% style=\"border-collapse:collapse;\">"
				+ "<tr><td></td><td>" + Language.get("name") + "</td></tr>";
		int i = 0;
		int j, prevj = -1;
		boolean doKO = list.get(0).getTournament().getProperties().DO_KNOCKOUT;
		for (Player p : list) {
			i++;
			j = Calculator.min(i, Calculator.nextPowerOfTwo((i + 1) / 2) + 1);
			if (doKO) {
				result += (j != prevj) ? "<tr><td>" + j + "</td><td>"
						+ p.getFullName() + "</td></tr>" : "<tr><td></td><td>"
						+ p.getFullName() + "</td></tr>";
				prevj = j;
			} else {
				result += "<tr><td>" + i + "</td><td>" + p.getFullName()
						+ "</td></tr>";
			}
		}
		result += "</table>";
		return result;
	}

	public String getText() {
		return jEdit.getText();
	}

	@Override
	public void repaint() {
		if (jEdit != null)
			jEdit.setText(getText(list));
		super.repaint();
	}
}
