package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
		InputStream str = ClassLoader
				.getSystemClassLoader()
				.getResourceAsStream(directory + "ko-" + groupNum + "gr-" + playerNum
								+ "pl.otk");
		System.out.println("file: " + directory + "ko-" + groupNum + "gr-" + playerNum
				+ "pl.otk");
//		File dir = new File(file);
		templates = new ArrayList<Template>();
		templates.add(new Template(str, groupNum, playerNum));
//		FilenameFilter flt = new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				if (groupNum == 1)
//					return name.contains("-" + playerNum + "pl")
//							& name.contains("-" + +"gr-");
//				return name.contains("-" + groupNum + "gr-");
//			}
//		};
//
//		for (File f : dir.listFiles(flt))
//			templates.add(new Template(directory, f.getName()));

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
