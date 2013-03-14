package gui.views;

import gui.Language;
import gui.Main;
import gui.Preferences;
import gui.templates.IconButton;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JStart extends View {

	public JStart(Main m) {
		super(m);
	}

	@Override
	public void generateWindow() {
		List<String> files = Preferences.getFiles();
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		JPanel panOpen = new JPanel(new BorderLayout());
		JPanel panNew1 = new JPanel(new BorderLayout());
		JPanel panNew2 = new JPanel(new BorderLayout());
		JPanel panQuit = new JPanel(new BorderLayout());

		Action aOpen = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.openFile();
			}
		};

		Action aNew1 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.newFile();
			}
		};

		Action aNew2 = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.newKOBuilderFile();
			}
		};

		Action aQuit = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				main.quit();
			}
		};

		panOpen.add(new IconButton(aOpen, "open"), BorderLayout.WEST);
		panOpen.add(new JLabel(Language.get("open")), BorderLayout.CENTER);

		panNew1.add(new IconButton(aNew1, "new"), BorderLayout.WEST);
		panNew1.add(new JLabel(Language.get("newTournament")),
				BorderLayout.CENTER);

		panNew2.add(new IconButton(aNew2, "new"), BorderLayout.WEST);
		panNew2.add(new JLabel(Language.get("newKOTemplate")),
				BorderLayout.CENTER);

		panQuit.add(new IconButton(aQuit, "quit"), BorderLayout.WEST);
		panQuit.add(new JLabel(Language.get("quit")), BorderLayout.CENTER);

		pan.add(panOpen);
		if (!files.isEmpty()) {
			JPanel panFiles = new JPanel(new GridLayout(files.size(), 0));
			panFiles.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createTitledBorder(Language.get("recentlyOpened")),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			for (final String fileName : files) {
				final JLabel lbl = new JLabel(fileName);
				lbl.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							if (new File(lbl.getText()).exists())
								main.openFile(lbl.getText());
							else
								Preferences.removeFile(fileName);
						}
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
				});
				lbl.setForeground(Color.BLUE);
				panFiles.add(lbl);
			}
			pan.add(panFiles);
		}
		pan.add(panNew1);
		pan.add(panNew2);
		pan.add(panQuit);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		setLayout(new GridBagLayout());
		add(pan, gbc);
		setVisible(true);
	}

	@Override
	public String getIconEnabledPattern() {
		return "";
	}

	@Override
	public void refresh() {
		// Does nothing.
	}

}
