package gui.components;

import gui.Language;
import gui.Main;
import gui.templates.IconButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import database.match.Commandable;
import database.match.Match;

@SuppressWarnings("serial")
public abstract class JGameCommander<T extends JComponent> extends JPanel {
	protected JButton bUnfinishGame, bUnsetGame, bStartGame, bEndGame, bClearSearch;
	protected Commandable commandable;
	protected JCommandableProgress jte;
	protected T jWaiting, jRunning, jDone;
	protected JScrollPane sWaiting, sRunning, sDone;
	protected JLabel lblSearch, lblPrepared, lblRunning, lblDone, lblGame;
	protected List<Match> lWaiting, lRunning, lDone;
	protected Main main;
	protected JPanel pCenter, pGame, pSearch;
	protected Action aUnsetGame, aUnfinishGame, aClearSearch, aStartGame, aEndGame;
	protected KeyListener kSearch, kGame;
	protected MouseListener mWaiting;

	protected JTextField tGame, tSearch;

	public JGameCommander(Main main, Commandable commandable) {
		super();
		this.main = main;
		this.commandable = commandable;
		jte = new JCommandableProgress(commandable);
		
		// generate Lists
		generateLists();
		
		//scroll Pane's
		sWaiting = new JScrollPane(jWaiting);
		sWaiting.setPreferredSize(new Dimension(300, 150));
		
		sRunning = new JScrollPane(jRunning);
		sRunning.setPreferredSize(new Dimension(300, 150));
		
		sDone = new JScrollPane(jDone);
		sDone.setPreferredSize(new Dimension(300, 150));
		
		// actions
		aUnsetGame = new AbstractAction(Language.get("breakGame")) {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				unstartGame();
			}
		};
		
		aUnfinishGame = new AbstractAction(Language.get("continueGame")) {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				unfinishGame();
			}
		};
		
		aClearSearch = new AbstractAction(Language.get("clearSearch")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSearch();
			}
		};
		
		aStartGame = new AbstractAction(Language.get("startGame")) {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startGame();
			}
		};
		
		aEndGame = new AbstractAction(Language.get("finishGame")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		
		//key listeners
		kGame = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				tGame.setBackground(Color.WHITE);
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					addSentence();
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
					sentenceDown();
				if (arg0.getKeyCode() == KeyEvent.VK_UP)
					sentenceUp();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			
			}
		};
		
		kSearch = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			
			}

			@Override
			public void keyReleased(KeyEvent e) {
				search();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			
			}
		};
		
		//mouse listener
		mWaiting = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					startGame();
				}
			}

		};
		
		//labels
		lblSearch = new JLabel(Language.get("search"));
		lblPrepared = new JLabel(Language.get("prepared"));
		lblRunning = new JLabel(Language.get("running"));
		lblDone = new JLabel(Language.get("done"));
		lblGame = new JLabel(Language.get("sentence"));
		
		//lists		
		jWaiting.addMouseListener(mWaiting);
		
		//text fields
		tGame = new JTextField();
		tGame.addKeyListener(kGame);
		tGame.setToolTipText(Language.get("tooltip_tGame"));
		
		tSearch = new JTextField();
		tSearch.addKeyListener(kSearch);
		
		//buttons
		bUnsetGame = new IconButton(aUnsetGame, "back_small");
		bUnsetGame.setEnabled(false);
		bClearSearch = new IconButton(aClearSearch, "clear_small");
		bUnfinishGame = new IconButton(aUnfinishGame, "back_small");
		bUnfinishGame.setEnabled(false);
		bStartGame = new IconButton(aStartGame, "next_small");
		bStartGame.setEnabled(false);
		bEndGame = new IconButton(aEndGame, "next_small");
		bEndGame.setEnabled(false);
		
		//panels
		pGame = new JPanel();
		pGame.setLayout(new BorderLayout());
		pGame.add(lblGame, BorderLayout.WEST);
		pGame.add(tGame, BorderLayout.CENTER);

		pSearch = new JPanel();
		pSearch.setLayout(new BorderLayout());
		pSearch.add(lblSearch, BorderLayout.WEST);
		pSearch.add(tSearch, BorderLayout.CENTER);
		pSearch.add(bClearSearch, BorderLayout.EAST);
		
		pCenter = new JPanel();
		pCenter.setLayout(new GridBagLayout());
		
		setLayout(new BorderLayout());
		
		generateWindow();
	}

	public abstract void addSentence();

	public abstract void clearSearch();

	public abstract void finishGame();

	public abstract void generateLists();

	public void generateWindow() {
		// initializing other Arrays:
		refresh();

		// Building Panel
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridheight = 1; gbc.gridwidth = 1;
		gbc.weightx = 0; gbc.weighty = 0;
		
		//adding labels
		gbc.gridx = 0; gbc.gridy = 0;
		pCenter.add(lblPrepared, gbc);
		
		gbc.gridx = 2; gbc.gridy = 0;
		pCenter.add(lblRunning, gbc);
		
		gbc.gridx = 4; gbc.gridy = 0;
		pCenter.add(lblDone, gbc);
		
		//adding panels
		gbc.gridx = 2; gbc.gridy = 4;
		pCenter.add(pGame, gbc);
		
		gbc.gridx = 4; gbc.gridy = 1;
		pCenter.add(pSearch, gbc);
		
		//adding buttons
		gbc.gridx = 1; gbc.gridy = 1;
		pCenter.add(bStartGame, gbc);
		
		gbc.gridx = 1; gbc.gridy = 2;
		pCenter.add(bUnsetGame, gbc);
		
		gbc.gridx = 3; gbc.gridy = 1;
		pCenter.add(bEndGame, gbc);
		
		gbc.gridx = 3; gbc.gridy = 2;
		pCenter.add(bUnfinishGame, gbc);
		
		//adding lists
		gbc.weightx = 1; gbc.weighty = 1;
		
		gbc.gridheight = 4; gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 1;
		pCenter.add(sWaiting, gbc);
		
		gbc.gridheight = 3; gbc.gridwidth = 1;
		gbc.gridx = 2; gbc.gridy = 1;
		pCenter.add(sRunning, gbc);
		
		gbc.gridheight = 3; gbc.gridwidth = 1;
		gbc.gridx = 4; gbc.gridy = 2;
		pCenter.add(sDone, gbc);
		
		add(pCenter, BorderLayout.CENTER);
		add(jte, BorderLayout.SOUTH);
	}

	public abstract void refresh();

	@Override
	public void repaint() {
		if (lblSearch != null)
			lblSearch.setText(Language.get("search"));
		if (lblPrepared != null)
			lblPrepared.setText(Language.get("prepared"));
		if (lblRunning != null)
			lblRunning.setText(Language.get("running"));
		if (lblDone != null)
			lblDone.setText(Language.get("done"));
		if (lblGame != null)
			lblGame.setText(Language.get("sentence"));
		if (lblSearch != null)
			lblSearch.setText(Language.get("search"));
		if (tGame != null)
			tGame.setToolTipText(Language.get("tooltip_tGame"));
		if (jte != null)
			jte.repaint();
		super.repaint();
	}

	public abstract void search();

	public abstract void sentenceDown();

	public abstract void sentenceUp();

	public abstract void startGame();

	public abstract void unfinishGame();

	public abstract void unstartGame();

}