package gui.views;

import gui.Language;
import gui.Main;
import gui.components.JTemplateImporter;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.Calculator;
import database.players.Player;
import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;
import database.tournamentParts.Template;

@SuppressWarnings("serial")
public class JKnockOutBuilder extends View {
	class ComboRenderer implements ListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel result;
			if (value != null) {
				if (((Player) value).isNobody()) {
					result = new JLabel("<" + Language.get("nobody") + ">");
				} else {
					result = new JLabel(((Player) value).getPlayerPlaceGroup());
				}
			} else {
				result = new JLabel("<" + Language.get("nobody") + ">");
			}
			if (isSelected) {
				result.setOpaque(true);
				result.setForeground(getBackground());
				result.setBackground(getForeground());
			}
			return result;
		}
	}

	private List<JPanel> boxes;
	private List<JComboBox> jComboBoxes;
	private JButton jImport;
	private JSpinner jOffset, jToPlace;
	private JLabel lSurvivors;
	private JPanel pan2;
	private List<Player> players;

	private Qualifying qualifying;
	private JTemplateImporter ti;

	public JKnockOutBuilder(Main main) {
		super(main);
		boxes = new ArrayList<JPanel>();
		players = new ArrayList<Player>();
		players.add(Player.getNobody());
		qualifying = main.getTournament().getQualifying();
		int from = 1, to = 2;
		for (Group g : qualifying.getGroups())
			if (g.getSize() < to)
				players.addAll(g.getPlayersByPlace(from, g.getSize() - 1));
			else
				players.addAll(g.getPlayersByPlace(from, to));

		ti = new JTemplateImporter("ko_templates/", qualifying.getGroups().size(),
				qualifying.getGroups().get(0).getSize());
		ti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ti.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!ti.isSelectionEmpty()) {
					int idx = ti.getSelectedIndex();
					InputStream str = ((Template) ti.getSelectedValue()).getInputStream();
					if (str != null)
						importFile(str);
					ti.setSelectedIndex(idx);
				}
			}
		});
	}

	@Override
	public void generateWindow() {
		JPanel pan1, pan1_1, pan;
		pan1 = new JPanel();
		pan1_1 = new JPanel();
		pan2 = new JPanel();
		pan = new JPanel(new BorderLayout());

		// build pan1_1
		SpinnerModel sm = new SpinnerNumberModel(1, 1, 1000, 1);
		jOffset = new JSpinner(sm);
		jOffset.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();
			}
		});

		sm = new SpinnerNumberModel(2, 1, 1000, 1);
		jToPlace = new JSpinner(sm);
		jToPlace.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();
			}
		});

		lSurvivors = new JLabel(Language.get("to"));

		// build pan1_1
		pan1_1.add(jOffset);
		pan1_1.add(lSurvivors);
		pan1_1.add(jToPlace);

		// build pan2
		jComboBoxes = new ArrayList<JComboBox>();
		refresh();

		Action aImport = new AbstractAction(Language.get("import")) {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					InputStream str;
					try {
						str = new FileInputStream(chooser.getSelectedFile().getAbsoluteFile()
								.getAbsolutePath());
						importFile(str);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		};

		jImport = new JButton(aImport);

		// build pan1
		pan1.setLayout(new BorderLayout());
		if (main.getTournament().getDoQualifying())
			pan1.add(pan1_1, BorderLayout.NORTH);
		else {
			jToPlace.setValue(main.getTournament().getQualifying().getGroups()
					.get(0).getSize());
			refresh();
		}

		pan1.add(ti, BorderLayout.CENTER);

		pan1.add(jImport, BorderLayout.SOUTH);

		// build main panel
		pan.add(pan1, BorderLayout.WEST);
		pan.add(new JScrollPane(pan2), BorderLayout.CENTER);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(pan, gbc);
		List<List<Player>> tree = main.getTournament().getKnockOut().getTree();
		if (tree.isEmpty()) {
			if (ti.count() == 1)
				ti.setSelectedIndex(0);
		} else {
			int i = 0;
			for (JComboBox jcb : jComboBoxes) {
				if (tree.get(0).size() > i) {
					jcb.setSelectedItem(tree.get(0).get(i));
					i++;
				}
			}
		}
	}

	@Override
	public String getIconEnabledPattern() {
		if (jComboBoxes == null)
			return "1111001110";
		for (int i = 0; i < jComboBoxes.size(); i = i + 2) {
			Player i1 = (Player) jComboBoxes.get(i).getSelectedItem();
			Player i2 = (Player) jComboBoxes.get(i + 1).getSelectedItem();
			if (i1 == null)
				i1 = Player.getNobody();
			if (i2 == null)
				i2 = Player.getNobody();
			if (i1.isNobody() & i2.isNobody())
				return "1111001110";
		}
		return "1111001111";
	}

	public void importFile(InputStream file) {
		try {
			// System.out.println(fileName);
			// InputStream str =
			// ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(file));
			String line = null;
			String[] splitted;
			int from = 0, to = 0;
			boolean firstLine = true;
			while ((line = in.readLine()) != null) {
				splitted = line.split("#");
				if (firstLine) {
					if (Integer.parseInt(splitted[0]) != qualifying.getGroups()
							.size()) {
						JOptionPane.showMessageDialog(this,
								Language.get("wrongGroups"));
						break;
					}
					from = Integer.parseInt(splitted[1]);
					to = Integer.parseInt(splitted[2]);
					jToPlace.setValue(((Integer) jOffset.getValue()) + to
							- from);
					firstLine = false;
				} else {
					int boxNum = Integer.parseInt(splitted[0]);
					if (splitted.length < 3)
						jComboBoxes.get(boxNum - 1).setSelectedIndex(0);
					else {
						int group = Integer.parseInt(splitted[2]);
						int place = Integer.parseInt(splitted[1]);
						jComboBoxes.get(boxNum - 1).setSelectedIndex(
								(group - 1) * (to - from + 1) + place);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			System.out.println("ERROR: File not accessible.");
		}
	}

	public void next() {
		List<Player> plrs = new ArrayList<Player>();
		for (JComboBox box : jComboBoxes)
			plrs.add((Player) box.getSelectedItem());
		main.getTournament().getKnockOut().setPlayers(plrs);
	}

	@Override
	public void refresh() {
		ti.clearSelection();
		players = new ArrayList<Player>();
		players.add(Player.getNobody());
		List<Group> groups = qualifying.getGroups();

		for (Group g : groups)
			if (g.getSize() < ((Integer) jToPlace.getValue()))
				players.addAll(g.getPlayersByPlace(
						((Integer) jOffset.getValue()), g.getSize()));
			else
				players.addAll(g.getPlayersByPlace(
						((Integer) jOffset.getValue()),
						((Integer) jToPlace.getValue()) + 1));
		pan2.removeAll();
		jComboBoxes = new ArrayList<JComboBox>();
		for (int i = 0; i < Calculator.nextPowerOfTwo(players.size() - 1); i++) {
			JComboBox box = new JComboBox(players.toArray());
			box.setRenderer(new ComboRenderer());
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.setEnabledPattern(getIconEnabledPattern());
					repaint();
				}
			});

			jComboBoxes.add(box);
		}

		int ysize = Calculator.min(
				Calculator.nextPowerOfTwo(players.size() - 1), 16);
		int xsize = (players.size() - 1) / ysize + 1;
		GridLayout gl = new GridLayout(ysize, xsize);

		pan2.setLayout(gl);

		int j = 0;
		for (JComboBox box : jComboBoxes) {
			j++;
			JPanel panTemp = new JPanel();
			panTemp.add(new JLabel("#" + j + ":"));
			panTemp.add(box);
			boxes.add(panTemp);
			pan2.add(panTemp);
		}
		main.setEnabledPattern(getIconEnabledPattern());
		repaint();
	}

	@Override
	public void repaint() {
		if (jImport != null)
			jImport.setText(Language.get("import"));
		if (lSurvivors != null)
			lSurvivors.setText(Language.get("to"));
		super.repaint();
	}

}
