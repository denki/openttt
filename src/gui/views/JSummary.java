package gui.views;

import gui.Interaction;
import gui.Language;
import gui.Main;
import gui.components.JGroupTable;
import gui.components.JRanking;
import gui.components.JScrollableTreeView;
import gui.templates.View;
import gui.watcher.JKnockOutWatcherWindow;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import database.Calculator;
import database.match.Match;
import database.players.Player;
import database.tournamentParts.Tournament;

@SuppressWarnings("serial")
public class JSummary extends View {

	private JTextField jName;
	private JRanking jRanking;
	private Tournament tournament;
	private JScrollableTreeView<Player, Match> tv;

	public JSummary(Main m) {
		super(m);
		tournament = m.getTournament();
	}

	@Override
	public void generateWindow() {
		setLayout(new BorderLayout());

		JPanel pan = new JPanel(new GridBagLayout());

		jName = new JTextField(tournament.getName());
		jName.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				tournament.setName(jName.getText());
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});

		jRanking = new JRanking(tournament.getRanking());

		GridBagConstraints c = new GridBagConstraints();

		JPanel panTop = new JPanel();
		panTop.setLayout(new BorderLayout());
		panTop.add(new JLabel(Language.get("name")), BorderLayout.WEST);
		panTop.add(jName, BorderLayout.CENTER);
		add(panTop, BorderLayout.NORTH);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weighty = 1;
		if (!tournament.getDoQualifying() | !tournament.getDoKnockOut())
			c.gridheight = 2;
		c.weightx = 1;
		if (tournament.getDoQualifying()) {
			JGroupTable jQualifying = new JGroupTable(
					tournament.getQualifying());
			pan.add(jQualifying, c);
		}

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1;
		if (tournament.getDoKnockOut()) {
			tv = new JScrollableTreeView<Player, Match>(tournament
					.getKnockOut().getTree(), tournament.getKnockOut()
					.getMatches());
			pan.add(tv, c);
		}

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pan,
				jRanking);
		int splitLocation;
		if (tv != null) {
			splitLocation = Calculator.min(tv.getPreferredSize().width, main
					.getFrame().getWidth() / 10 * 7);
		} else {
			splitLocation = Calculator.min(pan.getPreferredSize().width, main
					.getFrame().getWidth() / 10 * 7);
		}

		split.setDividerLocation(splitLocation);
		add(split, BorderLayout.CENTER);

	}

	public String getHtml() {
		String result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;"
				+ " charset=utf-8\"/><title>"
				+ jName.getText()
				+ "</title></head>" + "<body><h1>" + jName.getText() + "</h1>";
		if (main.getTournament().getDoQualifying())
			result += Calculator.htmlTabular(true, tournament.getQualifying()
					.getGroups());
		if (main.getTournament().getDoKnockOut())
			result += JKnockOutWatcherWindow.getHtml(tv.getTreeView());
		result += jRanking.getText();
		result += "</body></html>";
		return result;

	}

	@Override
	public String getIconEnabledPattern() {
		return "1111011110";
	}

	public void print() {
		String text = getHtml();
		String fileName = "summary";
		Interaction.saveHtml(fileName, text);
		Interaction.print(fileName);
	}

	@Override
	public void refresh() {
		// does nothing
	}

}
