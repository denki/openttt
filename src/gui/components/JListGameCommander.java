package gui.components;

import gui.Main;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.Calculator;
import database.match.Commandable;
import database.match.Match;

public class JListGameCommander extends JGameCommander<JList<Match>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7151793366730354015L;

	public JListGameCommander(Main main, Commandable commandable) {
		super(main, commandable);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				jRunning.repaint();
			}
		}, 5000, 5000);
	}

	@Override
	public void addSentence() {
		if (jRunning.getSelectedIndex() != -1)
			try {
				Match game = jRunning.getSelectedValue();
				String[] splitted = tGame.getText().split(",|;|:");
				if (splitted.length == 2) {
					int leftadd = new Integer(splitted[0]);
					int rightadd = new Integer(splitted[1]);
					game.addSentence(leftadd, rightadd);
				} else if (splitted.length == 1) {
					game.addSentence(splitted[0]);
				} else {
					throw new NumberFormatException();
				}
				if (game.getState() == 2) {
					commandable.addMatch(game);
					main.refresh(game.getGroup());
				} else {
					refresh();
				}
			} catch (NumberFormatException e) {
				tGame.setBackground(Color.RED);
			}
		tGame.setText("");
	}

	@Override
	public void clearSearch() {
		tSearch.setText("");
		jDone.setListData(commandable.getMatchByState(2).toArray(new Match[0]));
	}

	@Override
	public void finishGame() {
		Match game = jRunning.getSelectedValue();
		game.endGame();
		main.refresh(game.getGroup());
	}

	@Override
	public void generateLists() {
		jWaiting = new JList<Match>();
		jRunning = new JList<Match>();
		jDone = new JList<Match>();

		jWaiting.setCellRenderer(new ListCellRenderer<Match>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Match> arg0,
					Match g, int arg2, boolean isSelected, boolean arg4) {
				JLabel lbl;
				if (g.getGroup() == -1)
					lbl = new JLabel(g.toString());
				else
					lbl = new JLabel("[" + g.getGroup() + "] " + g.toString());
				boolean plays = Calculator.plays(lRunning,
						g.getLeftPlayer())
						| Calculator.plays(lRunning,
								g.getRightPlayer());
				if (isSelected) {
					lbl.setOpaque(true);
					lbl.setBackground(getForeground());
					lbl.setForeground(getBackground());
					if (plays) {
						lbl.setForeground(Color.yellow);
						lbl.setBackground(Color.red);
					}
				} else if (plays)
					lbl.setForeground(Color.red);
				return lbl;
			}
		});
		
		jWaiting.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (jWaiting.isSelectionEmpty())
					bStartGame.setEnabled(false);
				else
					bStartGame.setEnabled(true);
			}
		});

		jRunning.setCellRenderer(new ListCellRenderer<Match>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Match> arg0,
					Match g, int arg2, boolean arg3, boolean arg4) {
				JLabel lbl;
				if (g.getGroup() == -1)
					lbl = new JLabel(g.shortInfo());
				else
					lbl = new JLabel("[" + g.getGroup() + "] " + g.shortInfo());
				lbl.setToolTipText(g.mediumInfo());
				if (arg3) {
					lbl.setOpaque(true);
					lbl.setBackground(getForeground());
					lbl.setForeground(getBackground());
				}
				return lbl;
			}
		});
		
		jRunning.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (jRunning.isSelectionEmpty())
					bUnsetGame.setEnabled(false);
				else
					bUnsetGame.setEnabled(true);
			}
		});

		jDone.setCellRenderer(new ListCellRenderer<Match>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Match> arg0,
					Match g, int arg2, boolean arg3, boolean arg4) {
				JLabel lbl;
				if (g.getGroup() == -1)
					lbl = new JLabel(g.shortInfo());
				else
					lbl = new JLabel("[" + g.getGroup() + "] " + g.shortInfo());
				lbl.setToolTipText(g.longInfo());
				if (arg3) {
					lbl.setOpaque(true);
					lbl.setBackground(getForeground());
					lbl.setForeground(getBackground());
					if (!g.isOK()) {
						lbl.setForeground(Color.yellow);
						lbl.setBackground(Color.red);
					}
				} else if (!g.isOK())
					lbl.setForeground(Color.red);
				return lbl;
			}
		});
		
		jDone.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (jDone.isSelectionEmpty())
					bUnfinishGame.setEnabled(false);
				else
					bUnfinishGame.setEnabled(true);
			}
		});
	}

	@Override
	public void refresh() {
		int idxhole, idxrunning, idxfinished;
		idxhole = jWaiting.getSelectedIndex();
		idxrunning = jRunning.getSelectedIndex();
		idxfinished = jDone.getSelectedIndex();
		lWaiting = commandable.getMatchByState(0);
		lRunning = commandable.getMatchByState(1);
		lDone = commandable.getMatchByState(2);
		Collections.sort(lWaiting);
		Collections.sort(lRunning);
		Collections.sort(lDone);
		jWaiting.setListData(lWaiting.toArray(new Match[0]));
		jRunning.setListData(lRunning.toArray(new Match[0]));
		jDone.setListData(lDone.toArray(new Match[0]));
		jWaiting.setSelectedIndex(idxhole);
		jRunning.setSelectedIndex(idxrunning);
		jDone.setSelectedIndex(idxfinished);
		if (jRunning.getSelectedIndex() == -1
				&& jRunning.getModel().getSize() > 0)
			jRunning.setSelectedIndex(0);
		repaint();
	}

	@Override
	public void search() {
		List<Match> lst = new ArrayList<Match>();
		String text = tSearch.getText();
		text = text.toLowerCase();
		if (text.equals(""))
			lst.addAll(commandable.getMatchByState(2));
		else
			for (Match g : commandable.getMatchByState(2)) {
				boolean contains = true;
				for (String s : text.split(" "))
					contains = contains
							& (g.getLeft().getFullName().toLowerCase()
									.contains(s) | g.getRight().getFullName()
									.toLowerCase().contains(s));
				if (contains)
					lst.add(g);
			}
		jDone.setListData(lst.toArray(new Match[0]));

	}

	@Override
	public void sentenceDown() {
		int actIdx = jRunning.getSelectedIndex();
		jRunning.setSelectedIndex(Calculator.min(actIdx + 1,
				lRunning.size()));
	}

	@Override
	public void sentenceUp() {
		int actIdx = jRunning.getSelectedIndex();
		jRunning.setSelectedIndex(Calculator.max(actIdx - 1, 0));
	}

	@Override
	public void startGame() {
		if (jWaiting.isSelectionEmpty())
			return;
		for (Match m : jWaiting.getSelectedValuesList()){
			m.startGame();
			refresh();
		}
	}

	@Override
	public void unfinishGame() {
		if (jDone.isSelectionEmpty())
			return;
		for (Match m : jDone.getSelectedValuesList()){
			m.setState(1);
			commandable.delMatch(m);
			main.refresh(m.getGroup());
		}
		
	}

	@Override
	public void unstartGame() {
		if (jRunning.isSelectionEmpty())
			return;
		for (Match m : jRunning.getSelectedValuesList()){
			m.setState(0);
			refresh();
		}
		
	}

}
