package gui;

import gui.components.StandartButtonPanel;
import gui.templates.IconManager;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Preferences extends JFrame implements ItemListener {
	private static List<String> files = new ArrayList<String>();

	private CheckboxGroup cbg1, cbg2;

	private JComboBox cBox1, cBox2;
	private JPanel pan1, pan2, pan3;
	private JLabel lIconSize, lIconBarPosition;

	private List<LookAndFeelInfo> lafs;
	private List<String> langs;
	private Main main;

	public Preferences(Main m) {
		super();
		main = m;
		generateWindow();
	}

	public static void addFile(String fileName) {
		if (fileName != null) {
			files.remove(fileName);
			files.add(fileName);
			saveToFile();
		}
	}

	public static List<String> getFiles() {
		return files;
	}

	public static void loadFromFile() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		String text = Interaction.loadConfig();
		String[] splitted = text.split(System.getProperty("line.separator"));
		for (String line : splitted) {
			String[] line_splitted = line.split("=");
			if (line_splitted[0].equals("lookandfeel"))
				for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
					if (laf.getName().contains(line_splitted[1])
							| line_splitted[1].contains(laf.getName()))
						UIManager.setLookAndFeel(laf.getClassName());
			if (line_splitted[0].equals("files")) {
				files.clear();
				if (line_splitted.length > 1) {
					for (String file : line_splitted[1].split(":"))
						files.add(file);
				}
			}
			if (line_splitted[0].equals("language"))
				Language.setLanguage(line_splitted[1]);
			if (line_splitted[0].equals("iconsize"))
				IconManager.setPx(line_splitted[1]);
			if (line_splitted[0].equals("iconbarposition"))
				StandartButtonPanel.setPos(line_splitted[1]);
		}
	}

	public static void saveToFile() {
		String text = "";

		// L&F
		text += "lookandfeel=";
		text += UIManager.getLookAndFeel().getID();
		text += System.getProperty("line.separator");

		// Language
		text += "language=";
		text += Language.getPrefix().split("_")[0];
		text += System.getProperty("line.separator");

		// IconSize
		text += "iconsize=";
		text += IconManager.getPx();
		text += System.getProperty("line.separator");

		// IconSize
		text += "iconbarposition=";
		text += StandartButtonPanel.getPos();
		text += System.getProperty("line.separator");

		// Files
		text += "files=";
		for (int i = 0; i < files.size() - 1; i++) {
			text += files.get(i);
			text += ":";
		}
		if (files.size() > 0)
			text += files.get(files.size() - 1);
		text += System.getProperty("line.separator");

		Interaction.saveConfig(text);
	}

	public void generateWindow() {
		lafs = new ArrayList<LookAndFeelInfo>();
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			lafs.add(info);

		langs = Language.getLanguages();

		pan1 = new JPanel();
		pan1.setLayout(new GridLayout(langs.size(), 1));
		pan1.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("language")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		pan2 = new JPanel();
		pan2.setLayout(new GridLayout(lafs.size(), 1));
		pan2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("appearance")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		pan3 = new JPanel();
		pan3.setLayout(new GridLayout(4, 1));
		pan3.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("iconBar")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// set up Checkboxes
		cbg1 = new CheckboxGroup();
		for (String lang : langs) {
			Checkbox cb;
			if (lang.substring(3).equals(Language.getLanguage()))
				cb = new Checkbox(lang, cbg1, true);
			else
				cb = new Checkbox(lang, cbg1, false);
			cb.addItemListener(this);
			pan1.add(cb);
		}

		cbg2 = new CheckboxGroup();
		for (LookAndFeelInfo laf : lafs) {
			Checkbox cb;
			if (UIManager.getLookAndFeel().getName().equals(laf.getName()))
				cb = new Checkbox(laf.getName(), cbg2, true);
			else
				cb = new Checkbox(laf.getName(), cbg2, false);
			cb.addItemListener(this);
			pan2.add(cb);
		}

		cBox1 = new JComboBox(new String[] { "12", "16", "20", "22", "24",
				"28", "32", "36", "40", "44", "48", "52", "56", "60" });
		cBox1.setSelectedItem(IconManager.getPx());
		cBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IconManager.setPx((String) cBox1.getSelectedItem());
				main.refreshState();
				// main.refreshState();
				saveToFile();
			}
		});

		cBox2 = new JComboBox(new String[] { Language.get("north"),
				Language.get("south") });
		if (StandartButtonPanel.getPos().equals(BorderLayout.SOUTH))
			cBox2.setSelectedIndex(1);
		if (StandartButtonPanel.getPos().equals(BorderLayout.NORTH))
			cBox2.setSelectedIndex(0);
		cBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cBox2.getSelectedIndex() == 0)
					StandartButtonPanel.setPos(BorderLayout.NORTH);
				if (cBox2.getSelectedIndex() == 1)
					StandartButtonPanel.setPos(BorderLayout.SOUTH);
				main.setView(main.getTournament().getState());
				saveToFile();
			}
		});

		lIconSize = new JLabel(Language.get("iconSize"));
		pan3.add(lIconSize);
		pan3.add(cBox1);
		lIconBarPosition = new JLabel(Language.get("iconBarPosition"));
		pan3.add(lIconBarPosition);
		pan3.add(cBox2);

		// adding Panels to me
		setLayout(new FlowLayout());
		add(pan1);
		add(pan2);
		add(pan3);
		pack();
		setVisible(true);
	}

	public void repaint() {
		if (pan1 != null)
			pan1.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("language")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		if (pan2 != null)
			pan2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("appearance")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		if (pan3 != null)
			pan3.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("iconBar")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		if (lIconSize != null)
				lIconSize.setText(Language.get("iconSize"));
		if (lIconBarPosition != null)
			lIconBarPosition.setText(Language.get("iconBarPosition"));
		if (cBox2 != null)
			cBox2.setModel(new DefaultComboBoxModel(new String[] { Language.get("north"),
					Language.get("south") }));
		super.repaint();
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			if (info.getName().equals(cbg2.getSelectedCheckbox().getLabel()))
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Exception e) {
					System.out.println("Error setting Look&Feel");
				}
		Language.setLanguage(cbg1.getSelectedCheckbox().getLabel()
				.substring(0, 2));
		SwingUtilities.updateComponentTreeUI(this);
		main.styleChanged();
		saveToFile();
	}
}
