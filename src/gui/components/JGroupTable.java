package gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.Calculator;
import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;

@SuppressWarnings("serial")
public class JGroupTable extends JPanel implements ActionListener {
	public static int NAME_ROW_WIDTH = 280;
	private JComboBox<Group> groups;
	private Qualifying qualifying;
	private JTable table;
	private JCommandableProgress prog;

	public class ReadOnlyTableModel extends DefaultTableModel {
		@Override
		public boolean isCellEditable(int rowIndex, int mColIndex) {
			return false;
		 }
	}
	
	public JGroupTable(Qualifying q) {
		super();
		qualifying = q;
		prog = new JCommandableProgress(q.getGroups().get(0));
		generateWindow();
	}

	public JGroupTable(Qualifying q, Group g) {
		this(q);
		groups.setSelectedItem(g);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		refresh();
	}

	public void generateWindow() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		groups = new JComboBox<Group>(qualifying.getGroups().toArray(new Group[0]));
		groups.addActionListener(this);
		panel.add(groups, BorderLayout.NORTH);

		// data[row][column]
		String[][] data = Calculator.tabular((Group) groups.getSelectedItem());
		String[] columnNames = Calculator.tabularHead((Group) groups
				.getSelectedItem());

		table = new JTable();
		ReadOnlyTableModel tm = new ReadOnlyTableModel();
		tm.setDataVector(data, columnNames);
		table.setModel(tm);

		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(NAME_ROW_WIDTH);
		JScrollPane scrollpane = new JScrollPane(table);

		add(panel, BorderLayout.NORTH);
		add(scrollpane, BorderLayout.CENTER);
		add(prog, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(420, 200));
		setVisible(true);
	}

	public void refresh() {
		refresh(true);
	}
	
	public void refresh(boolean repaint) {
		String[][] data = Calculator.tabular((Group) groups.getSelectedItem());
		String[] columnNames = Calculator.tabularHead((Group) groups
				.getSelectedItem());
		prog.setCommandable((Group) groups.getSelectedItem());
		ReadOnlyTableModel tm = new ReadOnlyTableModel();
		tm.setDataVector(data, columnNames);
		table.setModel(tm);
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(NAME_ROW_WIDTH);
		if (repaint) {
			repaint();
		}
	};

	@Override
	public void repaint() {
		if (groups != null & table != null) {
			table.getColumnModel().getColumn(0).setPreferredWidth(45);
			table.getColumnModel().getColumn(1).setPreferredWidth(NAME_ROW_WIDTH);
		}
		if (prog != null)
			prog.repaint();
		super.repaint();
	}

	public void setComboBoxEditable(boolean b) {
		groups.setEditable(b);
	}
}
