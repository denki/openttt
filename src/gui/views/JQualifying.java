package gui.views;

import gui.Main;
import gui.components.JGroupTable;
import gui.components.JListGameCommander;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class JQualifying extends View {

	private JListGameCommander jgc;
	private JGroupTable jgt;

	public JQualifying(Main m) {
		super(m);
	}

	@Override
	public void generateWindow() {
		JPanel pan = new JPanel(new BorderLayout());
		jgc = new JListGameCommander(main, main.getTournament().getQualifying());
		jgt = new JGroupTable(main.getTournament().getQualifying());
		pan.add(jgt, BorderLayout.CENTER);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pan, jgc);

		setLayout(new GridLayout(1, 1));
		add(split);
	}

	@Override
	public String getIconEnabledPattern() {
		return (!main.getTournament().getQualifying().isDone()) ? "1111111110"
				: "1111111111";
	}

	@Override
	public void refresh() {
		jgc.refresh();
		jgc.repaint();
		jgt.refresh();
		main.setEnabledPattern(getIconEnabledPattern());
	}
}
