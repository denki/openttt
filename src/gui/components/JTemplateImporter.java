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
public class JTemplateImporter extends JList {
	List<Template> templates;

	public JTemplateImporter(String directory, final int groupNum,
			final int playerNum){
		super();
		templates = new ArrayList<Template>();
		InputStream str = ClassLoader
				.getSystemClassLoader()
				.getResourceAsStream(directory + "ko-" + groupNum + "gr-" + playerNum
								+ "pl.otk");
		if (str != null)
			templates.add(new Template(str, groupNum, playerNum));

		setListData(templates.toArray(new Template[0]));
		setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel lbl = new JLabel(value.toString());
				lbl.setToolTipText(((Template) value).getDescription());
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
}
