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
	private JComboBox gruppen;
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
		gruppen.setSelectedItem(g);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		refresh();
	}

	public void generateWindow() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		gruppen = new JComboBox(qualifying.getGroups().toArray());
		gruppen.addActionListener(this);
		panel.add(gruppen, BorderLayout.NORTH);

		// data[row][column]
		String[][] data = Calculator.tabular((Group) gruppen.getSelectedItem());
		String[] columnNames = Calculator.tabularHead((Group) gruppen
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
		String[][] data = Calculator.tabular((Group) gruppen.getSelectedItem());
		String[] columnNames = Calculator.tabularHead((Group) gruppen
				.getSelectedItem());
		prog.setCommandable((Group) gruppen.getSelectedItem());
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
		if (gruppen != null & table != null) {
			table.getColumnModel().getColumn(0).setPreferredWidth(45);
			table.getColumnModel().getColumn(1).setPreferredWidth(NAME_ROW_WIDTH);
		}
		if (prog != null)
			prog.repaint();
		super.repaint();
	}

	public void setComboBoxEditable(boolean b) {
		gruppen.setEditable(false);
	}
}
