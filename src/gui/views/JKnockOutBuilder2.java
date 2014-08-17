package gui.views;

import gui.Language;
import gui.Main;
import gui.components.ListTransferHandler;
import gui.components.TreeTransferHandler;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import database.Calculator;
import database.players.Player;
import database.tournamentParts.Group;
import database.tournamentParts.Qualifying;

public class JKnockOutBuilder2 extends View {

	private List<Player> players;
	private TreePanel treePan;

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

		public TreePanel(int num) {
			super();
			playersMap = new HashMap<JLabel, Player>();
			int n = Calculator.nextPowerOfTwo(num);
			setLayout(new GridBagLayout());
			Border b = BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(2, 10, 2, 2),
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
	}

	@Override
	public void generateWindow() {
		JList<Player> lPlayers = new JList<Player>();
		DefaultListModel<Player> mPlayers = new DefaultListModel<Player>();

		Qualifying qualifying = main.getTournament().getQualifying();
		int from = 1, to = 2;
		for (Group g : qualifying.getGroups())
			players.addAll(g.getPlayersByPlace(from, Math.min(g.getSize(), to)));

		mPlayers.clear();
		for (Player p : players)
			mPlayers.addElement(p);
		lPlayers.setModel(mPlayers);
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
				Border border = BorderFactory.createLineBorder(getForeground(), 1, true);
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

		treePan = new TreePanel(players.size());

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 2;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lPlayers, gbc);

		gbc.weightx = 3;
		gbc.gridx = 1;
		add(treePan, gbc);
		
//		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lPlayers, treePan);
//		add(split);
//		split.setDividerLocation(.3);
	}

	@Override
	public String getIconEnabledPattern() {
		return "1111001111";
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

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
