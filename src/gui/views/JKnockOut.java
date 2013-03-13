package gui.views;

import gui.Main;
import gui.components.JGameCommander;
import gui.components.JListGameCommander;
import gui.components.JScrollableTreeView;
import gui.components.JTreeView;
import gui.templates.View;

import java.awt.GridLayout;

import javax.swing.JSplitPane;

import database.match.Match;
import database.players.Player;
import database.tournamentParts.KnockOut;

@SuppressWarnings("serial")
public class JKnockOut extends View {

	private JGameCommander jgc;
	private KnockOut knockout;

	private JScrollableTreeView<Player, Match> tv;

	public JKnockOut(Main m) {
		super(m);
		knockout = m.getTournament().getKnockOut();
	}

	@Override
	public void generateWindow() {
		tv = new JScrollableTreeView<Player, Match>(knockout.getTree(),
				knockout.getMatches());
		jgc = new JListGameCommander(main, knockout);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tv, jgc);

		setLayout(new GridLayout(1, 1));
		add(split);
	}

	@Override
	public String getIconEnabledPattern() {
		return (!knockout.isDone()) ? "1111111110" : "1111111111";
	}

	public JTreeView<Player, Match> getTreeView() {
		return tv.getTreeView();
	}

	@Override
	public void refresh() {
		tv.setEdges(knockout.getMatches());
		jgc.refresh();
		tv.repaint();
		main.setEnabledPattern(getIconEnabledPattern());
	}
}
