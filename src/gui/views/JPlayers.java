package gui.views;

import gui.Language;
import gui.Main;
import gui.components.ListTransferHandler;
import gui.components.PlayerListRenderer;
import gui.popups.ImportPlayers;
import gui.popups.JPlayerDetails;
import gui.templates.IconButton;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import database.players.Double;
import database.players.Player;
import database.players.Single;
import database.players.Team2;
import database.tournamentParts.Group;
import database.tournamentParts.Tournament;
import exceptions.InconsistentStateException;
import exceptions.InputFormatException;

@SuppressWarnings("serial")
public class JPlayers extends View implements KeyListener {

	private boolean enableGroups;
	private Tournament tournament;
	private List<Group> groups;
	private List<Player> unassignedPlayers;

	private DefaultListModel<Player> mUnassignedPlayers, mGroupedPlayers;

	private JLabel lPlayer, lSearch;

	private JButton jNewPlayer, jRemovePlayer, jAssignPlayer, jUnassignPlayer,
			jAddGroup, jDelGroup, jPlayerDown, jPlayerUp, jImport;

	private JTextField jPlayerName, jFilter;
	private JList<Player> jUnassignedPlayers, jGroupedPlayers;
	private JComboBox<Group> jGroups;

	class DefaultMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	Action addGroup = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			tournament.getQualifying().addGroup();
			main.refreshState();
			jGroups.setSelectedIndex(jGroups.getItemCount() - 1);
		}
	};

	Action assignPlayer = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (jUnassignedPlayers.getSelectedValuesList().size() > 0) {
				Group group = ((Group) jGroups.getSelectedItem());
				for (Object o : jUnassignedPlayers.getSelectedValuesList()) {
					Player player = (Player) o;
					tournament.getQualifying().assignPlayer(player, group);
					mUnassignedPlayers.removeElement(o);
					mGroupedPlayers.addElement(player);
				}
				jUnassignedPlayers.setModel(mUnassignedPlayers);
				jGroupedPlayers.setModel(mGroupedPlayers);
				main.refreshState();
			}
		}
	};

	Action clearSearch = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			jFilter.setText("");
			refreshLists();
			// TODO list models
		}
	};

	Action delGroup = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			unassignedPlayers.addAll(((Group) jGroups.getSelectedItem())
					.getPlayers());
			tournament.getQualifying().delGroup(jGroups.getSelectedIndex());
			main.refreshState();
		}
	};

	Action importPlayers = new AbstractAction(Language.get("import")) {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".csv")
							| f.getName().toLowerCase().endsWith(".xls")
							| f.getName().toLowerCase().endsWith(".xlsx")
							| f.getName().toLowerCase().endsWith(".ods")
							| f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "Tables (*.csv, *.xls, *.ods, *.xlsx)";
				}
			});
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String file = chooser.getSelectedFile().getAbsoluteFile()
						.getAbsolutePath();
				importPlayers(file);
				main.refreshState();
			}

		}
	};

	Action playerDown = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = jGroupedPlayers.getSelectedIndex();
			if (jGroupedPlayers.getSelectedIndex() < ((Group) jGroups
					.getSelectedItem()).getSize() - 1) {
				((Group) jGroups.getSelectedItem()).swapPlayers(
						jGroupedPlayers.getSelectedIndex(),
						jGroupedPlayers.getSelectedIndex() + 1);
				index = jGroupedPlayers.getSelectedIndex() + 1;
			}
			main.refreshState();
			jGroupedPlayers.setSelectedIndex(index);
		}
	};

	Action playerUp = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = jGroupedPlayers.getSelectedIndex();
			if (jGroupedPlayers.getSelectedIndex() > 0) {
				((Group) jGroups.getSelectedItem()).swapPlayers(
						jGroupedPlayers.getSelectedIndex(),
						jGroupedPlayers.getSelectedIndex() - 1);
				index = jGroupedPlayers.getSelectedIndex() - 1;
			}
			main.refreshState();
			jGroupedPlayers.setSelectedIndex(index);
		}
	};

	Action refresh = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			main.refreshState();
		}
	};

	ListCellRenderer<Player> rnd = new PlayerListRenderer();

	ListCellRenderer<Player> rnd2 = new ListCellRenderer<Player>() {
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Player> list, Player plr, int index,
				boolean isSelected, boolean cellHasFocus) {
			final JLabel cb = new JLabel();
			final Color selBG = javax.swing.UIManager.getDefaults().getColor(
					"List.selectionBackground");
			final Color selFG = javax.swing.UIManager.getDefaults().getColor(
					"List.selectionForeground");
			cb.setText("<html>" + plr.getFullName() + "<br/> " + plr.getClub()
					+ "</html>");
			Border border = BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(2, 2, 2, 2),
					BorderFactory.createLineBorder(getForeground(), 1, true));
			cb.setBorder(border);
			if (isSelected) {
				cb.setOpaque(true);
				cb.setForeground(selFG);
				cb.setBackground(selBG);
			}
			JLabel cb2 = new JLabel("<html><strong>" + (index + 1)
					+ ". </strong></html>");
			cb2.setFont(cb.getFont().deriveFont(14));
			JPanel pan = new JPanel();
			pan.setOpaque(false);
			pan.add(cb2, BorderLayout.WEST);
			pan.add(cb, BorderLayout.CENTER);
			pan.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 10));
			return pan;
		}
	};

	Action unassignPlayer = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!jGroupedPlayers.getSelectedValuesList().isEmpty()) {
				Group group = ((Group) jGroups.getSelectedItem());
				for (Object o : jGroupedPlayers.getSelectedValuesList()) {
					Player player = (Player) o;
					tournament.getQualifying().unassignPlayer(player, group);
				}
				main.refreshState();
			}
		}
	};

	Action newPlayer = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String name = jPlayerName.getText();
			Player player = null;
			if (name.length() != 0) {
				String[] splitted = name.split(",");
				for (int i = 0; i < splitted.length; i++)
					while (splitted[i].startsWith(" "))
						splitted[i] = splitted[i].substring(1);
				if (tournament.getSingle()) {
					player = tournament.newSingle(splitted);
				} else if (tournament.getDouble()) {
					player = tournament.newDouble(splitted);
				} else if (tournament.get2Team()) {
					player = tournament.newTeam2(splitted);
				}
			}
			if (player != null) {
				unassignedPlayers.add(player);
				mUnassignedPlayers.addElement(player);
				jPlayerName.setText("");
				// jUnassignedPlayers.setModel(mUnassignedPlayers);
				main.refreshState();
			} else {
				jPlayerName.setBackground(Color.RED);
			}
		}
	};

	Action removePlayer = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!jUnassignedPlayers.getSelectedValuesList().isEmpty()) {
				for (Object o : jUnassignedPlayers.getSelectedValuesList()) {
					unassignedPlayers.remove(o);
					mUnassignedPlayers.removeElement(o);
				}
				// jUnassignedPlayers.setModel(mUnassignedPlayers);
				main.refreshState();
			}
		}
	};

	public JPlayers(Main m, boolean enableGroups) {
		super(m);
		this.enableGroups = enableGroups;
		tournament = m.getTournament();
		unassignedPlayers = tournament.getQualifying().getUnassigned();
		groups = tournament.getQualifying().getGroups();
	}

	@Override
	public void generateWindow() {
		// non-button elements
		mUnassignedPlayers = new DefaultListModel<Player>();
		jUnassignedPlayers = new JList<Player>(mUnassignedPlayers);
		jUnassignedPlayers.addMouseListener(new DefaultMouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// double x = arg0.getPoint().getX();
				switch (arg0.getButton()) {
				case MouseEvent.BUTTON1:
					if (arg0.getClickCount() == 2)
						jAssignPlayer.doClick();
					// else if (arg0.getClickCount() == 1) {
					// if (x < 21.0) {
					// int idx1 =
					// jUnassignedPlayers.locationToIndex(arg0.getPoint());
					// if (idx1 != -1) {
					// Player pl =
					// jUnassignedPlayers.getModel().getElementAt(idx1);
					// pl.setThere(!pl.isThere());
					// main.setEnabledPattern(getIconEnabledPattern());
					// main.refreshState();
					// }
					// }
					// }
					break;
				// case MouseEvent.BUTTON2:
				// if (arg0.getClickCount() == 1) {
				// int idx2 =
				// jUnassignedPlayers.locationToIndex(arg0.getPoint());
				// if (idx2 != -1) {
				// Player pl = jUnassignedPlayers.getModel().getElementAt(idx2);
				// pl.setThere(!pl.isThere());
				// main.setEnabledPattern(getIconEnabledPattern());
				// repaint();
				// }
				// }
				// break;
				case MouseEvent.BUTTON3:
					int idx3 = jUnassignedPlayers.locationToIndex(arg0
							.getPoint());
					jUnassignedPlayers.setSelectedIndex(idx3);
					for (Object o : jUnassignedPlayers.getSelectedValuesList()) {
						new JPlayerDetails(((Player) o).getPersons(), main);
					}
					break;
				}
			}
		});
		jUnassignedPlayers.setCellRenderer(rnd);
		jUnassignedPlayers.setDragEnabled(true);
		jUnassignedPlayers.setDropMode(DropMode.INSERT);
		jUnassignedPlayers.setTransferHandler(new ListTransferHandler(
				jUnassignedPlayers, mUnassignedPlayers, tournament
						.getQualifying().getUnassigned(), tournament));
		jUnassignedPlayers.setLayoutOrientation(JList.VERTICAL_WRAP);
		jUnassignedPlayers.setVisibleRowCount(-1);

		mGroupedPlayers = new DefaultListModel<Player>();
		jGroupedPlayers = new JList<Player>(mGroupedPlayers);
		jGroupedPlayers.addMouseListener(new DefaultMouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// double x = arg0.getPoint().getX();
				switch (arg0.getButton()) {
				case MouseEvent.BUTTON1:
					if (arg0.getClickCount() == 2)
						jUnassignPlayer.doClick();
					// else if (arg0.getClickCount() == 1) {
					// if (x < 21.0) {
					// int idx1 =
					// jGroupedPlayers.locationToIndex(arg0.getPoint());
					// if (idx1 != -1) {
					// Player pl =
					// jGroupedPlayers.getModel().getElementAt(idx1);
					// pl.setThere(!pl.isThere());
					// main.setEnabledPattern(getIconEnabledPattern());
					// main.refreshState();
					// }
					// }
					// }
					break;
				// case MouseEvent.BUTTON2:
				// if (arg0.getClickCount() == 1) {
				// int idx2 = jGroupedPlayers.locationToIndex(arg0.getPoint());
				// if (idx2 != -1) {
				// Player pl = jGroupedPlayers.getModel().getElementAt(idx2);
				// pl.setThere(!pl.isThere());
				// main.setEnabledPattern(getIconEnabledPattern());
				// repaint();
				// }
				// }
				// break;
				case MouseEvent.BUTTON3:
					int idx3 = jGroupedPlayers.locationToIndex(arg0.getPoint());
					if (idx3 != -1) {
						jGroupedPlayers.setSelectedIndex(idx3);
						for (Object o : jGroupedPlayers.getSelectedValuesList())
							new JPlayerDetails(((Player) o).getPersons(), main);
					}
					break;
				}
			}
		});
		jGroupedPlayers.setCellRenderer(rnd2);
		jGroupedPlayers.setDragEnabled(true);
		jGroupedPlayers.setDropMode(DropMode.INSERT);
		jGroupedPlayers.setTransferHandler(new ListTransferHandler(
				jGroupedPlayers, mGroupedPlayers, tournament.getQualifying()
						.getGroups().get(0).getPlayers(), tournament));
		jGroupedPlayers.setLayoutOrientation(JList.VERTICAL_WRAP);
		jGroupedPlayers.setVisibleRowCount(-1);

		jPlayerName = new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 0);
			}
		};
		jPlayerName.setDragEnabled(true);
		jPlayerName.addKeyListener(this);
		jFilter = new JTextField() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 0);
			}
		};
		jFilter.addKeyListener(this);
		jGroups = new JComboBox<Group>(groups.toArray(new Group[0])) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 0);
			}
		};
		jGroups.setAction(refresh);
		jGroups.setSelectedIndex(0);
		refreshLists();

		// buttons
		jNewPlayer = new IconButton(newPlayer, "add_small");
		jRemovePlayer = new IconButton(removePlayer, "remove_small");
		jAssignPlayer = new IconButton(assignPlayer, "next_small");
		jUnassignPlayer = new IconButton(unassignPlayer, "back_small");
		jPlayerUp = new IconButton(playerUp, "up_small");
		jPlayerDown = new IconButton(playerDown, "down_small");
		jAddGroup = new IconButton(addGroup, "add_small");
		jDelGroup = new IconButton(delGroup, "remove_small");
		jImport = new IconButton(importPlayers, "open") {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, getIcon().getIconHeight());
			}
		};
		;

		// building layout
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.1;
		c.weighty = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jPlayerName, c);

		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		lPlayer = new JLabel(Language.get("player"));
		add(lPlayer, c);

		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jRemovePlayer, c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jNewPlayer, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		lSearch = new JLabel(Language.get("search"));
		add(lSearch, c);

		c.weightx = 0.1;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		add(jFilter, c);

		c.weightx = 0;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new IconButton(clearSearch, "clear_small"), c);

		c.weighty = 0.8;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 6;
		JScrollPane sUnassignedPlayers = new JScrollPane(jUnassignedPlayers) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 0);
			}
		};
		add(sUnassignedPlayers, c);
		c.weighty = 0;
		c.weightx = 0;

		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 4;
		c.gridheight = 1;
		add(jImport, c);

		c.weighty = 0;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jAssignPlayer, c);

		c.gridx = 4;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jUnassignPlayer, c);

		c.gridx = 4;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jPlayerUp, c);

		c.gridx = 4;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jPlayerDown, c);

		c.weightx = 0.1;
		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jGroups, c);

		c.weightx = 0;
		c.gridx = 6;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jAddGroup, c);

		c.gridx = 7;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(jDelGroup, c);

		if (!enableGroups) {
			jGroups.setVisible(false);
			jAddGroup.setVisible(false);
			jDelGroup.setVisible(false);
		}

		c.weighty = 0.8;
		c.weightx = 0.5;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 8;
		JScrollPane sGroupedPlayers = new JScrollPane(jGroupedPlayers) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 0);
			}
		};
		add(sGroupedPlayers, c);
		c.weighty = 0;
	}

	@Override
	public String getIconEnabledPattern() {
		boolean quali = tournament.getProperties().DO_QUALIFYING;
		return isReady() ? (quali ? "1111111111" : "1111101111")
				: (quali ? "1111111110" : "1111101110");
	}

	private void importPlayers(String file) {
		try {
			main.addWatcher(new ImportPlayers(main, file));
		} catch (InconsistentStateException e) {
			System.err.println("Tournament state is inconsistent.");
		}
	}

	public boolean isReady() {
		if (groups.isEmpty())
			return false;

		for (Group g : groups) {
			if (g.getSize() < 2)
				return false;
			// for (Player p : g.getPlayers())
			// if (!p.isThere())
			// return false;
		}

		return true;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getSource() == jPlayerName) {
			jPlayerName.setBackground(Color.WHITE);
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				jNewPlayer.doClick();
				jPlayerName.setText("");
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if ((arg0.getSource() == jFilter))
			refreshLists();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void refresh() {
		main.setEnabledPattern(getIconEnabledPattern());
		refreshLists();
		repaint();
	}

	private void refreshLists() {
		if (groups.size() != jGroups.getModel().getSize()) {
			DefaultComboBoxModel<Group> model = new DefaultComboBoxModel<Group>(
					groups.toArray(new Group[0]));
			jGroups.setModel(model);
		}
		List<Player> lst = new ArrayList<Player>();
		mUnassignedPlayers.clear();
		mGroupedPlayers.clear();
		String text = jFilter.getText();
		text = text.toLowerCase();
		if (text.equals(""))
			lst.addAll(unassignedPlayers);
		else
			for (Player p : unassignedPlayers) {
				boolean contains = true;
				for (String s : text.split(" "))
					contains = contains
							& p.getFullName().toLowerCase().contains(s);
				if (contains)
					lst.add(p);
			}
		for (Player p : lst)
			mUnassignedPlayers.addElement(p);
		if (jGroups.getSelectedItem() != null) {
			jGroupedPlayers.setTransferHandler(new ListTransferHandler(
					jGroupedPlayers, mGroupedPlayers, ((Group) jGroups
							.getSelectedItem()).getPlayers(), tournament));
			for (Player p : ((Group) jGroups.getSelectedItem()).getPlayers())
				mGroupedPlayers.addElement(p);
		} else
			jGroupedPlayers.setTransferHandler(new ListTransferHandler(
					jGroupedPlayers, mGroupedPlayers, tournament
							.getQualifying().getUnassigned(), tournament));
		jUnassignedPlayers.setModel(mUnassignedPlayers);
		jGroupedPlayers.setModel(mGroupedPlayers);
	}

	@Override
	public void repaint() {
		if (lPlayer != null)
			lPlayer.setText(Language.get("player"));
		if (lSearch != null)
			lSearch.setText(Language.get("search"));
		if (jImport != null)
			jImport.setText(Language.get("import"));
		super.repaint();
	}
};
