package gui.views;

import gui.Language;
import gui.Main;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import database.tournamentParts.Tournament;

@SuppressWarnings("serial")
public class JProperties extends View implements ItemListener {

	private Checkbox qualifying, knockout;
	private Checkbox single, pair, team2;
	private JPanel pLeft, pRight;

	public JProperties(Main m) {
		super(m);
	}

	@Override
	public void generateWindow() {
		// set up Panels
		JPanel pan = new JPanel(new BorderLayout());
		pLeft = new JPanel(new GridLayout(3, 1));
		pLeft.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("playerType")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		pRight = new JPanel(new GridLayout(2, 1));
		pRight.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("components")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// set up Checkboxes
		CheckboxGroup cbg = new CheckboxGroup();
		single = new Checkbox(Language.get("single"), cbg, true);
		single.addItemListener(this);
		pair = new Checkbox(Language.get("doubles"), cbg, false);
		pair.addItemListener(this);
		team2 = new Checkbox(Language.get("team2"), cbg, false);
		team2.addItemListener(this);

		qualifying = new Checkbox(Language.get("qualifying"), true);
		qualifying.addItemListener(this);
		knockout = new Checkbox(Language.get("knockOut"), true);
		knockout.addItemListener(this);

		if (main.getTournament() != null) {
			Tournament t = main.getTournament();
			single.setState(t.getSingle());
			pair.setState(t.getDouble());
			team2.setState(t.get2Team());
			qualifying.setState(t.getDoQualifying());
			knockout.setState(t.getDoKnockOut());
		}

		// add Components to Panels
		pLeft.add(single);
		pLeft.add(pair);
		pLeft.add(team2);
		pRight.add(qualifying);
		pRight.add(knockout);

		// adding Panels to me
		pan.setLayout(new FlowLayout());
		pan.add(pLeft, BorderLayout.WEST);
		pan.add(pRight, BorderLayout.EAST);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;

		add(pan, gbc);

		validate();
	}

	@Override
	public String getIconEnabledPattern() {
		if (qualifying == null | knockout == null) {
			return "1111001111";
		}
		if (qualifying.getState() | knockout.getState()) {
			return "1111001111";
		} else {
			return "1111001110";
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		Tournament t = main.getTournament();
		if (t == null
				& ((!single.getState()) | (!knockout.getState()) | (!qualifying
						.getState())))
			t = new Tournament();
		if (t != null) {
			t.setSingle(single.getState());
			t.setDouble(pair.getState());
			t.set2Team(team2.getState());
			t.setDoKnockOut(knockout.getState());
			t.setDoQualifying(qualifying.getState());
		}
		refresh();
	}

	@Override
	public void refresh() {
		main.setEnabledPattern(getIconEnabledPattern());
		repaint();
	}
	
	public void repaint() {
		if (single != null)
			single.setLabel(Language.get("single"));
		if (pair != null)
			pair.setLabel(Language.get("doubles"));
		if (team2 != null)
			team2.setLabel(Language.get("team2"));
		if (qualifying != null)
			qualifying.setLabel(Language.get("qualifying"));
		if (knockout != null)
			knockout.setLabel(Language.get("knockOut"));
		if (pLeft != null)
			pLeft.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("playerType")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		if (pRight != null)
			pRight.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(Language.get("components")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		super.repaint();
	}

}