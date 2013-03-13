package gui.components;

import gui.Language;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.players.Person;

@SuppressWarnings("serial")
public class PersonPanel extends JPanel {
	private Person person;
	private JTextField tSurname, tPrename, tClub;

	public PersonPanel(Person person) {
		super();

		this.person = person;

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		setLayout(gbl);

		tSurname = new JTextField(person.getSurname());
		tPrename = new JTextField(person.getPrename());
		tClub = new JTextField(person.getClub());

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		add(new JLabel(Language.get("surname")), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		add(tSurname, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		add(new JLabel(Language.get("prename")), gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		add(tPrename, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		add(new JLabel(Language.get("club")), gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		add(tClub, gbc);
	}

	@Override
	public void addKeyListener(KeyListener kl) {
		tSurname.addKeyListener(kl);
		tPrename.addKeyListener(kl);
		tClub.addKeyListener(kl);
	}

	public void save() {
		person.setSurname(tSurname.getText());
		person.setPrename(tPrename.getText());
		person.setClub(tClub.getText());
	}
}
