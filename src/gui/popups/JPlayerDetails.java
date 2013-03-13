package gui.popups;

import gui.Language;
import gui.Main;
import gui.components.PersonPanel;
import gui.templates.IconButton;
import gui.templates.Watcher;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.WindowConstants;

import database.players.Person;

@SuppressWarnings("serial")
public class JPlayerDetails extends Watcher {

	private List<PersonPanel> personPanels;
	private List<Person> persons;

	public JPlayerDetails(List<Person> persons, Main main) {
		super(Language.get("player"), main);
		this.persons = persons;
		personPanels = new ArrayList<PersonPanel>();
		generateWindow();
	}

	@Override
	public void generateWindow() {
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;

		KeyListener kl = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					save();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					quit();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		};

		final IconButton bSave = new IconButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		}, "save");
		final IconButton bClear = new IconButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		}, "clear");

		gbc.weightx = 1;
		for (Person person : persons) {
			gbc.gridx++;
			PersonPanel personPanel = new PersonPanel(person);
			personPanel.addKeyListener(kl);
			personPanels.add(personPanel);
			add(personPanel, gbc);
		}

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1;
		add(bClear, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridy = 3;
		gbc.weightx = 1;
		add(bSave, gbc);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400, 180);
		setLocation();
		setVisible(true);
	}

	public void quit() {
		dispose();
	}

	@Override
	public void refresh() {
		repaint();
	}

	public void save() {
		for (PersonPanel personPanel : personPanels)
			personPanel.save();
		quit();
	}

	private void setLocation() {
		int xx = (new Double(main.getFrame().getLocation().getX())).intValue();
		int yy = (new Double(main.getFrame().getLocation().getY())).intValue();
		int xsize = (new Double(main.getFrame().getSize().getWidth()))
				.intValue();
		int ysize = (new Double(main.getFrame().getSize().getHeight()))
				.intValue();
		int xownsize = (new Double(getSize().getWidth())).intValue();
		int yownsize = (new Double(getSize().getHeight())).intValue();
		setLocation(xx + (xsize - xownsize) / 2, yy + (ysize - yownsize) / 2);
	}

}
