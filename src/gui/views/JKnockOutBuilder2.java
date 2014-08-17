package gui.views;

import gui.Language;
import gui.Main;
import gui.components.ListTransferHandler;
import gui.components.TreeTransferHandler;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import database.Calculator;
import database.players.Player;
import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;

public class JKnockOutBuilder2 extends View {

	private List<Player> players;
	private TreePanel treePan;
	private JSpinner jOffset;
	private JSpinner jToPlace;
	private Qualifying qualifying;
	private DefaultListModel<Player> mPlayers;
	private JList<Player> lPlayers;
	private GridBagConstraints gbc;

	public JKnockOutBuilder2(Main m) {
		super(m);
		players = new ArrayList<Player>();
	}

	class PlayerListRenderer implements ListCellRenderer<Player> {
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Player> list, Player value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JLabel result;
			if (value != null) {
				if (value.isNobody()) {
					result = new JLabel("<" + Language.get("nobody") + ">");
				} else {
					result = new JLabel(value.getPlayerPlaceGroup());
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

	class TreePanel extends JPanel {
		List<JLabel> labels = new ArrayList<JLabel>();
		private Map<JLabel, Player> playersMap;
		private int n;
		private int OFFSET;
		private int FACTOR = 30;

		public TreePanel(int num) {
			super();
			playersMap = new HashMap<JLabel, Player>();
			n = Calculator.nextPowerOfTwo(num);
			OFFSET = (int) (Math.log(n) / Math.log(2)) * FACTOR + 20;
			setLayout(new GridBagLayout());
			Border b = BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(2, 10, 2, OFFSET),
					BorderFactory.createLineBorder(getForeground(), 1, true));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			MouseListener listener = new java.awt.event.MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					JComponent comp = (JComponent) me.getSource();
					TransferHandler handler = comp.getTransferHandler();
					handler.exportAsDrag(comp, me, TransferHandler.MOVE);
				}
			};
			for (int i = 1; i <= n; i++) {
				JLabel l = new JLabel();
				l.setText("<" + Player.getNobody().toString() + ">");
				l.setBorder(b);
				l.setOpaque(true);
				l.setTransferHandler(new TreeTransferHandler(-1, l, main
						.getTournament()));
				l.addMouseListener(listener);
				l.setSize(l.getPreferredSize());
				labels.add(l);
				playersMap.put(l, null);
				add(l, gbc);
				gbc.gridy++;
			}

		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Graphics2D gr = (Graphics2D) g;
			int a = 0;
			List<Point> points = new ArrayList<>();
			for (int i = 0; i < n; i++){
				Rectangle bnds1 = labels.get(i).getBounds();
				Point p1 = new Point(bnds1.x + bnds1.width - OFFSET ,
						(int) (bnds1.y + .5 * bnds1.getHeight()));
				points.add(p1);
			}
			System.out.println(points.size());
			while (points.size() >= 2) {
				List<Point> nPoints = new ArrayList<>();
				for (int i = 0; i < points.size(); i += 2) {
					Point p1 = points.get(i);
					Point p2 = points.get(i+1);
					nPoints.add(new Point(p1.x, (p1.y + p2.y)/2));
					gr.drawLine(p1.x + a, p1.y, p1.x + a + FACTOR, p1.y);
					gr.drawLine(p2.x + a, p2.y, p2.x + a + FACTOR, p2.y);
					gr.drawLine(p1.x + a + FACTOR, p1.y, p1.x + a + FACTOR, p2.y);
				}
				points.clear();
				points.addAll(nPoints);
				a += FACTOR;
			}
		}
	}

	@Override
	public void generateWindow() {
		lPlayers = new JList<Player>();
		mPlayers = new DefaultListModel<Player>();

		qualifying = main.getTournament().getQualifying();

		lPlayers.setDragEnabled(true);
		lPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListCellRenderer<Player> rnd = new ListCellRenderer<Player>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends Player> list, Player plr, int index,
					boolean isSelected, boolean cellHasFocus) {
				final JLabel cb = new JLabel();
				final Color selBG = javax.swing.UIManager.getDefaults()
						.getColor("List.selectionBackground");
				final Color selFG = javax.swing.UIManager.getDefaults()
						.getColor("List.selectionForeground");
				cb.setText("<html>" + plr.getFullName() + "<br/> "
						+ plr.getClub() + "</html>");
				Border border = BorderFactory.createLineBorder(getForeground(),
						1, true);
				cb.setBorder(border);
				if (isSelected) {
					cb.setOpaque(true);
					cb.setForeground(selFG);
					cb.setBackground(selBG);
				}
				JLabel cb2 = new JLabel("<html><strong>"
						+ plr.getPlayerPlaceGroup() + ": </strong></html>");
				cb2.setFont(cb.getFont().deriveFont(14));
				JPanel pan = new JPanel();
				pan.setOpaque(false);
				pan.add(cb2, BorderLayout.WEST);
				pan.add(cb, BorderLayout.CENTER);
				pan.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 10));
				return pan;
			}
		};
		lPlayers.setCellRenderer(rnd);
		lPlayers.setDropMode(DropMode.INSERT);
		lPlayers.setTransferHandler(new ListTransferHandler(lPlayers, mPlayers,
				players, main.getTournament()));
		lPlayers.setLayoutOrientation(JList.VERTICAL_WRAP);
		lPlayers.setVisibleRowCount(-1);

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

		treePan = new TreePanel(players.size());

		setLayout(new GridBagLayout());

		gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(lPlayers, gbc);

		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridy = 1;
		add(jOffset, gbc);

		gbc.gridx = 1;
		add(jToPlace, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 4;
		gbc.weighty = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 2;
		add(treePan, gbc);

		refresh();

		// JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// lPlayers, treePan);
		// add(split);
		// split.setDividerLocation(.3);
	}

	@Override
	public String getIconEnabledPattern() {
		return "1111001111";
	}

	@Override
	public void refresh() {
		players.clear();
		for (Group g : qualifying.getGroups())
			players.addAll(g.getPlayersByPlace((Integer) jOffset.getValue(),
					Math.min(g.getSize(), (Integer) jToPlace.getValue())));

		mPlayers.clear();
		for (Player p : players)
			mPlayers.addElement(p);
		lPlayers.setModel(mPlayers);
		remove(treePan);
		treePan = new TreePanel(players.size());
		add(treePan, gbc);
	}

	public void next() {
		List<Player> plrs = new ArrayList<Player>();
		int id;
		for (JLabel lab : treePan.labels) {
			id = ((TreeTransferHandler) lab.getTransferHandler()).getID();
			if (id != -1)
				plrs.add(main.getTournament().getPlayer(id));
			else
				plrs.add(Player.getNobody());
		}
		main.getTournament().getKnockOut().setPlayers(plrs);
	}
}
