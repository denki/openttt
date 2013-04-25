package gui.views;

import gui.Main;
import gui.components.DragScrollListener;
import gui.components.JListGameCommander;
import gui.components.JTreeView;
import gui.templates.View;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import database.tournamentParts.KnockOut;

@SuppressWarnings("serial")
public class JKnockOut extends View {

	private JListGameCommander jgc;
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

		JScrollPane scroll = new JScrollPane(tv);
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, jgc);
		
		DragScrollListener dsl = new DragScrollListener(tv, scroll);
		tv.addMouseMotionListener(dsl);
		tv.addMouseListener(dsl);
		tv.addMouseWheelListener(dsl);

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
