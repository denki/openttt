package gui.views;

import gui.Main;
import gui.components.JGameCommander;
import gui.components.JListGameCommander;
import gui.components.JTreeView;
import gui.templates.View;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import database.tournamentParts.KnockOut;

@SuppressWarnings("serial")
public class JKnockOut extends View {

	private JGameCommander jgc;
	private KnockOut knockout;

	private JTreeView tv;

	public JKnockOut(Main m) {
		super(m);
		knockout = m.getTournament().getKnockOut();
	}

	@Override
	public void generateWindow() {
		tv = new JTreeView(knockout);
		jgc = new JListGameCommander(main, knockout);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tv), jgc);

		setLayout(new GridLayout(1,1));
		add(split);
	}

	@Override
	public String getIconEnabledPattern() {
		return (!knockout.isDone()) ? "1111111110" : "1111111111";
	}

	@Override
	public void refresh() {
		jgc.refresh();
		main.setEnabledPattern(getIconEnabledPattern());
	}
}
