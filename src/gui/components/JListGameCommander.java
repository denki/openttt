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

public class JListGameCommander extends JGameCommander {

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
		if (((JList) jRunning).getSelectedIndex() != -1)
			try {
				Match game = (Match) ((JList) jRunning).getSelectedValue();
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
		((JList) jDone).setListData(commandable.getMatchByState(2).toArray());
	}

	@Override
	public void finishGame() {
		Match game = (Match) ((JList) jRunning).getSelectedValue();
		game.endGame();
		main.refresh(game.getGroup());
	}

	@Override
	public void generateLists() {
		jWaiting = new JList();
		jRunning = new JList();
		jDone = new JList();

		((JList) jWaiting).setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList arg0,
					Object arg1, int arg2, boolean isSelected, boolean arg4) {
				Match g = (Match) arg1;
				JLabel lbl;
				if (g.getGroup() == -1)
					lbl = new JLabel(g.toString());
				else
					lbl = new JLabel("[" + g.getGroup() + "] " + g.toString());
				boolean plays = Calculator.plays(lRunning,
						((Match) arg1).getLeftPlayer())
						| Calculator.plays(lRunning,
								((Match) arg1).getRightPlayer());
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
		
		((JList) jWaiting).addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (((JList) jWaiting).isSelectionEmpty())
					bStartGame.setEnabled(false);
				else
					bStartGame.setEnabled(true);
			}
		});

		((JList) jRunning).setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList arg0,
					Object arg1, int arg2, boolean arg3, boolean arg4) {
				Match g = (Match) arg1;
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
		
		((JList) jRunning).addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (((JList) jRunning).isSelectionEmpty())
					bUnsetGame.setEnabled(false);
				else
					bUnsetGame.setEnabled(true);
			}
		});

		((JList) jDone).setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList arg0,
					Object arg1, int arg2, boolean arg3, boolean arg4) {
				Match g = (Match) arg1;
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
		
		((JList) jDone).addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (((JList) jDone).isSelectionEmpty())
					bUnfinishGame.setEnabled(false);
				else
					bUnfinishGame.setEnabled(true);
			}
		});
	}

	@Override
	public void refresh() {
		int idxhole, idxrunning, idxfinished;
		idxhole = ((JList) jWaiting).getSelectedIndex();
		idxrunning = ((JList) jRunning).getSelectedIndex();
		idxfinished = ((JList) jDone).getSelectedIndex();
		lWaiting = commandable.getMatchByState(0);
		lRunning = commandable.getMatchByState(1);
		lDone = commandable.getMatchByState(2);
		Collections.sort(lWaiting);
		Collections.sort(lDone);
		((JList) jWaiting).setListData(lWaiting.toArray());
		((JList) jRunning).setListData(lRunning.toArray());
		((JList) jDone).setListData(lDone.toArray());
		((JList) jWaiting).setSelectedIndex(idxhole);
		((JList) jRunning).setSelectedIndex(idxrunning);
		((JList) jDone).setSelectedIndex(idxfinished);
		if (((JList) jRunning).getSelectedIndex() == -1
				& ((JList) jRunning).getModel().getSize() > 0)
			((JList) jRunning).setSelectedIndex(0);
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
		((JList) jDone).setListData(lst.toArray());

	}

	@Override
	public void sentenceDown() {
		int actIdx = ((JList) jRunning).getSelectedIndex();
		((JList) jRunning).setSelectedIndex(Calculator.min(actIdx + 1,
				lRunning.size()));
	}

	@Override
	public void sentenceUp() {
		int actIdx = ((JList) jRunning).getSelectedIndex();
		((JList) jRunning).setSelectedIndex(Calculator.max(actIdx - 1, 0));
	}

	@Override
	public void startGame() {
		if (((JList) jWaiting).isSelectionEmpty())
			return;
		for (Object o : ((JList) jWaiting).getSelectedValuesList()){
			Match game = (Match) o;
			game.startGame();
			refresh();
		}
	}

	@Override
	public void unfinishGame() {
		if (((JList) jDone).isSelectionEmpty())
			return;
		for (Object o : ((JList) jDone).getSelectedValuesList()){
			Match game = (Match) o;
			game.setState(1);
			commandable.delMatch(game);
			main.refresh(game.getGroup());
		}
		
	}

	@Override
	public void unstartGame() {
		if (((JList) jRunning).isSelectionEmpty())
			return;
		for (Object o : ((JList) jRunning).getSelectedValuesList()){
			Match game = (Match) o;
			game.setState(0);
			refresh();
		}
		
	}

}
