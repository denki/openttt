package gui.watcher;

import gui.Language;
import gui.Main;
import gui.components.JGroupTable;
import gui.templates.Watcher;

import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;

@SuppressWarnings("serial")
public class JQualifyingWatcher extends Watcher {

	private List<JGroupTable> groupTables;
	private Qualifying qualifying;

	public JQualifyingWatcher(Qualifying q, Main main) {
		super(Language.get("qualifyingWatcher"), main);
		qualifying = q;
		groupTables = new ArrayList<JGroupTable>();
	}

	public static void print(String text) {
		JEmptyGroupTables.print(text);
	}

	@Override
	public void generateWindow() {
		generateWindow(3);
		pack();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = getWidth() / 330;
				generateWindow(width);
				repaint();
			}
		});
		setVisible(true);
	}

	public void generateWindow(int width) {
		getContentPane().removeAll();
		int length = qualifying.getGroups().size();
		GridLayout gl = new GridLayout((length - 1) / width + 1, width);
		setLayout(gl);
		groupTables.clear();
		for (Group g : qualifying.getGroups()) {
			JGroupTable jw = new JGroupTable(qualifying, g);
			jw.setComboBoxEditable(false);
			groupTables.add(jw);
			add(jw);
		}
	}
	
	public void refresh() {
		//do nothing
	}
	
	public void refresh(int group) {
		JGroupTable table = groupTables.get(group - 1);
		table.refresh();
	}

}
