package gui.watcher;

import gui.Interaction;
import gui.Language;
import gui.Main;
import gui.components.JScrollableTreeView;
import gui.components.JTreeView;
import gui.templates.Watcher;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import database.match.Match;
import database.players.Player;

@SuppressWarnings("serial")
public class JKnockOutWatcherWindow extends Watcher {
	private Main main;
	private JScrollableTreeView<Player, Match> treeView;

	public JKnockOutWatcherWindow(Main m) {
		super(Language.get("knockOutWatcher"), m);
		main = m;
		treeView = new JScrollableTreeView<Player, Match>(main.getTournament()
				.getKnockOut().getTree(), main.getTournament().getKnockOut()
				.getMatches());

	}

	public static String getHtml(JTreeView<Player, Match> jTreeView) {
		jTreeView.repaint();
		BufferedImage img = new BufferedImage(jTreeView.getWidth(),
				jTreeView.getHeight(), BufferedImage.TYPE_INT_ARGB);
		jTreeView.print(img.getGraphics());
		img = img.getSubimage(0, 0, jTreeView.getPreferredSize().width, jTreeView.getPreferredSize().height);

		File file;
		if (System.getProperty("java.io.tmpdir").charAt(0) == '/')
			file = new File(System.getProperty("java.io.tmpdir")
					+ "/treeImg.png");
		else
			file = new File(System.getProperty("java.io.tmpdir")
					+ "\\treeImg.png");

		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			System.out.println("ERROR: Image not writable.");
		}

		return "<h2>" + Language.get("knockOut")
				+ "</h2><p><img src=\"treeImg.png\" alt=\"treeImg.png\"></p>";
	}

	public static void print(JTreeView<Player, Match> treeView) {
		Interaction.saveHtml("groupTable",
				"<html><head><title>" + Language.get("knockOut")
						+ "</title></head><body>" + getHtml(treeView)
						+ "</body></html>");
		Interaction.print("groupTable");
	}

	@Override
	public void generateWindow() {
		add(treeView);
		pack();
		setVisible(true);
	}

	public void print() {
		print(treeView.getTreeView());
	}

	@Override
	public void refresh() {
		treeView.setEdges(main.getTournament().getKnockOut().getMatches());
		treeView.repaint();
		repaint();
	}
}
