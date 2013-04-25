package gui.watcher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import database.players.Player;
import database.tournamentParts.Group;

import gui.Language;
import gui.Main;
import gui.templates.IconButton;
import gui.templates.Watcher;

@SuppressWarnings("serial")
public class JPlayersPresence extends Watcher {

	private JTable table;
	private JTextField aFilter;
	private JButton bFilter;
	private JLabel lFilter;
	private List<Player> players;

	public class PlayersSorter implements Comparator<Player> {
		@Override
		public int compare(Player arg0, Player arg1) {
			if (arg0.getClub() != null & arg1.getClub() != null) {
				int clubComparison = arg0.getClub().compareTo(arg1.getClub());
				if (clubComparison != 0)
					return clubComparison;
			}
			return arg0.getFullName().compareTo(arg1.getFullName());
		}
	}

	public JPlayersPresence(Main main) {
		super(Language.get("presence"), main);
		players = new ArrayList<Player>();
		refreshList();

		lFilter = new JLabel(Language.get("search"));

		aFilter = new JTextField("");
		aFilter.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				refresh();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		bFilter = new IconButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aFilter.setText("");
				refresh();
			}
		}, "clear_small");

		table = new JTable(getTableCells(), new String[] {
				Language.get("present"), Language.get("paid"),
				Language.get("player") });
		TableCellEditor df = new DefaultCellEditor(new JCheckBox(""));
		TableCellRenderer cr = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (column < 2) {
					boolean checked = (Boolean) value;
					return new JCheckBox("", checked);
				}
				String v = (String) value;
				return new JLabel(v);
			}
		};

		table.getColumnModel().getColumn(0).setCellEditor(df);
		table.getColumnModel().getColumn(1).setCellEditor(df);
		table.getColumnModel().getColumn(0).setCellRenderer(cr);
		table.getColumnModel().getColumn(1).setCellRenderer(cr);
		table.getColumnModel().getColumn(2).setCellRenderer(cr);
		int size = 60;
		table.getColumnModel().getColumn(0).setMaxWidth(size);
		table.getColumnModel().getColumn(1).setMaxWidth(size);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int column = table.columnAtPoint(arg0.getPoint());
				int row = table.rowAtPoint(arg0.getPoint());

				switch (column) {
				case 0:
					players.get(row).setThere(!players.get(row).isThere());
					break;
				case 1:
					players.get(row).setPaid(!players.get(row).isPaid());
				}

				apply();
			}
		});

	}

	@Override
	public void generateWindow() {
		setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(lFilter, BorderLayout.WEST);
		p.add(aFilter, BorderLayout.CENTER);
		p.add(bFilter, BorderLayout.EAST);
		add(p, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	public void apply() {
		main.refreshState();
	}

	@Override
	public void refresh() {
		refreshList();
		table.setModel(new DefaultTableModel(getTableCells(), new String[] {
				Language.get("present"), Language.get("paid"),
				Language.get("player") }));
		TableCellEditor df = new DefaultCellEditor(new JCheckBox(""));
		TableCellRenderer cr = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if (column < 2) {
					boolean checked = (Boolean) value;
					return new JCheckBox("", checked);
				}
				String v = (String) value;
				return new JLabel(v);
			}
		};

		table.getColumnModel().getColumn(0).setCellEditor(df);
		table.getColumnModel().getColumn(1).setCellEditor(df);
		table.getColumnModel().getColumn(0).setCellRenderer(cr);
		table.getColumnModel().getColumn(1).setCellRenderer(cr);
		table.getColumnModel().getColumn(2).setCellRenderer(cr);
		int size = 60;
		table.getColumnModel().getColumn(0).setMaxWidth(size);
		table.getColumnModel().getColumn(1).setMaxWidth(size);

		repaint();
	}

	private void refreshList() {
		players.clear();
		players.addAll(main.getTournament().getQualifying().getUnassigned());
		for (Group g : main.getTournament().getQualifying().getGroups()) {
			players.addAll(g.getPlayers());
		}
		filter();
		Collections.sort(players, new PlayersSorter());
	}

	private void filter() {
		if (aFilter != null) {
			List<Player> toRemove = new ArrayList<Player>();
			String filter = aFilter.getText();
			for (Player p : players)
				if (!matches(p, filter))
					toRemove.add(p);
			players.removeAll(toRemove);
		}
	}

	private Object[][] getTableCells() {
		List<Object[]> result = new ArrayList<Object[]>();
		for (Player p : players) {
			result.add(new Object[] { p.isThere(), p.isPaid(), p.getFullName() });
		}
		return result.toArray(new Object[0][0]);
	}

	private boolean matches(Player p, String filter) {
		for (String exp : filter.toLowerCase().split(" "))
			if (!p.getFullName().toLowerCase().contains(exp))
				return false;
		return true;
	}

}
