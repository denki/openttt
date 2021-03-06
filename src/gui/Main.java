package gui;

import gui.components.JTreeView;
import gui.components.StandartButtonPanel;
import gui.templates.IconManager;
import gui.templates.View;
import gui.templates.Watcher;
import gui.views.JKnockOut;
import gui.views.JKnockOutBuilder;
import gui.views.JKnockOutBuilder2;
import gui.views.JKnockOutPreBuilder;
import gui.views.JPlayers;
import gui.views.JProperties;
import gui.views.JQualifying;
import gui.views.JStart;
import gui.views.JSummary;
import gui.watcher.JEmptyGroupTables;
import gui.watcher.JKnockOutWatcherWindow;
import gui.watcher.JPlayersPresence;
import gui.watcher.JQualifyingWatcher;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import database.Calculator;
import database.tournamentParts.Tournament;

public class Main {

	private String currentFile;
	private View currentView;
	private JFrame frame;
	private StandartButtonPanel sbp;
	private Tournament tournament = null;
	private List<Watcher> watchers;

	public Main() {
		watchers = new ArrayList<Watcher>();
		frame = new JFrame(Language.get("openTTT"));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (tournament != null && tournament.isUnsaved()) {
					int n = JOptionPane.showOptionDialog(
							frame,
							Language.get("unsavedChanges"), // question
							Language.get("unsavedChangesShort"),// title
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, // icon
							null, null, null);
					if (n == JOptionPane.YES_OPTION) {
						quit();
					}
				} else {
					quit();
				}
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowOpened(WindowEvent e) {
			}
		});
		frame.setLayout(new BorderLayout());
		frame.setIconImage(IconManager.getImageIcon("main").getImage());
		frame.setSize(900, 600);

		// Locate in center
		DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices()[0].getDisplayMode();
		int screenWidth = dm.getWidth();
		int screenHeight = dm.getHeight();

		int ownWidth = frame.getWidth();
		int ownHeight = frame.getHeight();

		frame.setLocation((screenWidth - ownWidth) / 2,
				(screenHeight - ownHeight) / 2);
		
		//tweak ToolTipManager
		ToolTipManager ttm = ToolTipManager.sharedInstance();
		ttm.setDismissDelay(10000);

		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Preferences.loadFromFile();
		} catch (ClassNotFoundException e1) {
			System.err.println("Preferences: Expected class can't be found.");
		} catch (InstantiationException e1) {
			System.err.println("Preferences: Can't instanciate the class.");
		} catch (IllegalAccessException e1) {
			System.err.println("Preferences: Access not permitted.");
		} catch (UnsupportedLookAndFeelException e1) {
			System.err.println("Preferences: Look and Feel not supported.");
		}
		Main m = new Main();
		if (args.length > 0) {
			m.currentFile = args[0];
			m.tournament = Tournament.loadTournament(m.currentFile);
			if (m.tournament != null)
				m.setView(m.tournament.getState());
			else {
				m.setView(0);
				try {
					((JKnockOutPreBuilder) m.currentView).open(m.currentFile);
				} catch (Exception e) {
					System.err.println("WARNING: Can't open " + m.currentFile
							+ ".");
					m.currentFile = null;
					m.setView(0);
				}
			}
		} else
			m.setView(0);
	}

	public void back() {
		if (tournament == null) {
			setView(0);
		} else {
			tournament.decState();
			setView(tournament.getState());
		}
	}
	
	public void removeWatcher(Watcher w) {
		watchers.remove(w);
	}

	public void cloneTournament() {
		Main m = new Main();
		m.tournament = tournament.getCopy();
		m.setView(m.tournament.getState());
	}

	public JFrame getFrame() {
		return frame;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void newFile() {
		tournament = new Tournament();
		next();
	}

	public void newKOBuilderFile() {
		setView(-1);
	}

	public void next() {
		if (tournament.getState() == 4) {
			((JKnockOutBuilder2) currentView).next();
		}
		tournament.incState();
		setView(tournament.getState());
	}

	public void openFile() {
		if (currentView.handleOpenClose()) {
			currentView.open();
			return;
		}
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile().getAbsolutePath();
			String[] splitted = currentFile.split("\\.");
			if (splitted[splitted.length - 1].equals("otk")) {
				setView(-1);
				try {
					((JKnockOutPreBuilder) currentView).open(currentFile);
				} catch (NumberFormatException e) {
					System.err.println("JKnockOutPreBuilder: Wrong format.");
				}
			} else if (tournament == null) {
				currentFile = chooser.getSelectedFile().getAbsoluteFile()
						.getAbsolutePath();
				tournament = Tournament.loadTournament(currentFile);
				setView(tournament.getState());
			} else {
				Main m = new Main();
				m.currentFile = chooser.getSelectedFile().getAbsoluteFile()
						.getAbsolutePath();
				;
				m.tournament = Tournament.loadTournament(chooser
						.getSelectedFile().getAbsoluteFile()
						.getAbsolutePath());
				m.setView(m.tournament.getState());
			}
		}
		Preferences.addFile(currentFile);
	}

	public void openFile(String fileName) {
		currentFile = fileName;
		if (currentView.handleOpenClose()) {
			currentView.open();
			return;
		}
		String[] splitted = currentFile.split("\\.");
		if (splitted[splitted.length - 1].equals("otk")) {
			setView(-1);
			try {
				((JKnockOutPreBuilder) currentView).open(currentFile);
			} catch (NumberFormatException e) {
				System.err.println("JKnockOutPreBuilder: Wrong format.");
			}
		} else if (tournament == null) {
			tournament = Tournament.loadTournament(currentFile);
			setView(tournament.getState());
		} else {
			Main m = new Main();
			m.tournament = Tournament.loadTournament(currentFile);
			m.setView(m.tournament.getState());
		}
	}

	public void print() {
		int state = tournament.getState();
		switch (state) {
		case 21:
			JEmptyGroupTables.print(Calculator.htmlTabularEmpty(tournament
					.getQualifying().getGroups()));
			break;
		case 3:
			JQualifyingWatcher.print(Calculator.htmlTabular(tournament
					.getQualifying().getGroups()));
			break;
		case 5:
			JTreeView tv = new JTreeView(tournament.getKnockOut());
			JKnockOutWatcherWindow.print(tv);
			break;
		case 6:
			((JSummary) currentView).print();
			break;
		}
	}

	public void printPreview() {
		int state = tournament.getState();
		Watcher watcher = null;
		switch (state) {
		case 21:
		case 22:
			watcher = new JPlayersPresence(this);
			break;
		case 3:
			watcher = new JQualifyingWatcher(tournament.getQualifying(), this);
			break;
		case 5:
			watcher = new JKnockOutWatcherWindow(this);
			break;
		}
		watchers.add(watcher);
		watcher.generateWindow();
	}

	public void properties() {
		new Preferences(this);
	}

	public void quit() {
		frame.dispose();
	}

	public void refreshState() {
		currentView.refresh();
		currentView.repaint();
		frame.repaint();
    	for (Watcher w : watchers) {
			w.refresh();
			w.repaint();
    	}
	}
	
	public void styleChanged() {
		SwingUtilities.updateComponentTreeUI(frame);
		currentView.refresh();
		currentView.repaint();
		for (Watcher w : watchers) {
    		SwingUtilities.updateComponentTreeUI(w);
    		w.refresh();
    		w.repaint();
    	}
	}
	
	public void saveFile() {
		if (currentView.handleOpenClose()) {
			currentView.save();
			return;
		}
		if (currentFile == null)
			saveFileAs();
		else
				currentFile = tournament.saveTournament(currentFile);
		Preferences.addFile(currentFile);
	}

	public void saveFileAs() {
		if (currentView.handleOpenClose()) {
			currentView.saveTo();
			return;
		}
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile().getAbsolutePath();
			saveFile();
		}

	}

	public void setEnabledPattern(String pattern) {
		sbp.setEnabled(pattern);
		frame.repaint();
	}

	public void setView(int idx) {
		// -1 Knock Out Builder
		// 0 Splash Screen
		// 1 Tournament Preferences
		// 21 Players Window (Groups)
		// 22 Players Window (no Groups)
		// 3 Qualifying Window
		// 4 KnockOut Preparation
		// 5 KnockOut
		// 6 Conclusion
		frame.getContentPane().removeAll();
		currentView = null;
		switch (idx) {
		case -1:
			currentView = new JKnockOutPreBuilder(this);
			break;
		case 0:
			currentView = new JStart(this);
			break;
		case 1:
			currentView = new JProperties(this);
			break;
		case 21:
			currentView = new JPlayers(this, true);
			break;
		case 22:
			currentView = new JPlayers(this, false);
			break;
		case 3:
			currentView = new JQualifying(this);
			break;
		case 4:
			currentView = new JKnockOutBuilder2(this);
			break;
		case 5:
			currentView = new JKnockOut(this);
			break;
		case 6:
			currentView = new JSummary(this);
			break;
		}
		setView(currentView);
	}

	private void setView(View v) {
		if (!v.getIconEnabledPattern().equals("")) {
			sbp = new StandartButtonPanel(this);
			sbp.setEnabled(v.getIconEnabledPattern());
			frame.add(sbp, StandartButtonPanel.getPos());
		}
		currentView.generateWindow();
		frame.add(v, BorderLayout.CENTER);
		frame.validate();
	}

	public void refresh(int group) {
		currentView.refresh();
		currentView.repaint();
		frame.repaint();
    	for (Watcher w : watchers) {
			if (w instanceof JQualifyingWatcher & group != -1)
				((JQualifyingWatcher) w).refresh(group);
			else if (group == -1){
				w.refresh();
				w.repaint();
			}
    	}
	}

	public void addWatcher(Watcher watcher) {
		watchers.add(watcher);
	}
}
