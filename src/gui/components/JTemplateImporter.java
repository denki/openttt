package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import database.tournamentParts.Template;

@SuppressWarnings("serial")
public class JTemplateImporter extends JList<Template> {
	private int groupNum;
	private String dir;
	private List<Template> templates;

	public JTemplateImporter(String directory, int groupNum,
			final int playerNum) {
		super();
		templates = new ArrayList<Template>();
		dir = directory;
		this.groupNum = groupNum;
		InputStream str = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(
						directory + "ko-" + groupNum + "gr-" + playerNum
								+ "pl.otk");
		if (str != null)
			templates.add(new Template(directory + "ko-" + groupNum + "gr-"
					+ playerNum + "pl.otk", groupNum, playerNum));

		setListData(templates.toArray(new Template[0]));
		setCellRenderer(new ListCellRenderer<Template>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Template> list,
					Template value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel lbl = new JLabel(value.toString());
				lbl.setToolTipText(value.getDescription());
				if (isSelected) {
					lbl.setOpaque(true);
					lbl.setForeground(Color.white);
					lbl.setBackground(new Color(0, 170, 0));
				}
				return lbl;
			}
		});

	}

	public int count() {
		return templates.size();
	}

	public void setPlayersPerGroup(int ppg) {
		templates.clear();
		InputStream str = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(
						dir + "ko-" + groupNum + "gr-" + ppg
								+ "pl.otk");
		if (str != null)
			templates.add(new Template(dir + "ko-" + groupNum + "gr-"
					+ ppg + "pl.otk", groupNum, ppg));
		setListData(templates.toArray(new Template[0]));
	}
}
