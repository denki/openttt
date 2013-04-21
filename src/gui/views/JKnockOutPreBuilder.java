package gui.views;

import gui.Language;
import gui.Main;
import gui.templates.View;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import database.Calculator;
import database.tournamentParts.PreKnockOut;

@SuppressWarnings("serial")
public class JKnockOutPreBuilder extends View implements ActionListener {
	private class ChangeAction extends AbstractAction {
		private int idx;

		public ChangeAction(int idx) {
			super();
			this.idx = idx;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			preKnockOut.setPlace(idx, pComboBoxes.get(idx).getSelectedIndex());
		}
	}

	private String actualFile;
	private JSpinner jGroupNum, jPlaceFrom, jPlaceTo;
	private List<JComboBox> pComboBoxes;
	private PreKnockOut preKnockOut;

	private JPanel pRight, pan;

	public JKnockOutPreBuilder(Main m) {
		super(m);

		SpinnerModel sm = new SpinnerNumberModel(1, 1, 1000, 1);
		jPlaceFrom = new JSpinner(sm);
		jPlaceFrom.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();
			}
		});

		sm = new SpinnerNumberModel(2, 1, 1000, 1);
		jPlaceTo = new JSpinner(sm);
		jPlaceTo.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();
			}
		});

		sm = new SpinnerNumberModel(4, 1, 100, 1);
		jGroupNum = new JSpinner(sm);
		jGroupNum.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();
			}
		});

		pComboBoxes = new ArrayList<JComboBox>();
		pRight = new JPanel();
		generateWindow();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		refresh();
	}

	@Override
	public void generateWindow() {
		pan = new JPanel(new BorderLayout());
		JPanel pLeft = new JPanel();
		JPanel pTopLeft = new JPanel();

		pLeft.setLayout(new BorderLayout());
		pTopLeft.setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		pTopLeft.add(jGroupNum, BorderLayout.NORTH);
		pTopLeft.add(jPlaceFrom, BorderLayout.WEST);
		pTopLeft.add(jPlaceTo, BorderLayout.EAST);

		pLeft.add(pTopLeft, BorderLayout.NORTH);

		refresh();
		pan.add(pLeft, BorderLayout.WEST);
		pan.add(new JScrollPane(pRight), BorderLayout.CENTER);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(pan, gbc);

	}

	@Override
	public String getIconEnabledPattern() {
		return "1111000110";
	}

	@Override
	public boolean handleOpenClose() {
		return true;
	}

	@Override
	public void open() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(main.getFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			actualFile = chooser.getSelectedFile().getAbsoluteFile()
					.getAbsolutePath();
		}
		try {
			preKnockOut.open(actualFile);
			jGroupNum.setValue(preKnockOut.getGroup());
			jPlaceFrom.setValue(preKnockOut.getStart());
			jPlaceTo.setValue(preKnockOut.getEnd());
			System.out.println("refreshing");
			refresh();
			for (int i = 0; i < preKnockOut.getPlaces().length; i++) {
				pComboBoxes.get(i).setSelectedIndex(preKnockOut.getPlaces()[i]);
			}
		} catch (NumberFormatException e) {
			System.out.println("ERROR: File " + actualFile + " is corrupted.");
		} catch (IOException e) {
			System.out.println("ERROR: Can not access " + actualFile);
		}
	}

	public void open(String fileName) {
		actualFile = fileName;
		try {
			preKnockOut.open(fileName);
			jGroupNum.setValue(preKnockOut.getGroup());
			jPlaceFrom.setValue(preKnockOut.getStart());
			jPlaceTo.setValue(preKnockOut.getEnd());
			refresh();
			int[] places = preKnockOut.getPlaces();
			for (int i = 0; i < places.length; i++) {
				pComboBoxes.get(i).setSelectedIndex(places[i]);
			}
		} catch (NumberFormatException e) {
			System.out.println("ERROR: File " + fileName + " is corrupted.");
		} catch (IOException e) {
			System.out.println("ERROR: Can not access " + fileName);
		}
	}

	@Override
	public void refresh() {
		int placeFrom = ((Integer) jPlaceFrom.getValue()).intValue();
		int placeTo = ((Integer) jPlaceTo.getValue()).intValue();
		int group = ((Integer) jGroupNum.getValue()).intValue();

		pRight.removeAll();
		pComboBoxes.clear();

		List<String> plrs = new ArrayList<String>();
		plrs.add("<nobody>");
		for (int j = 0; j < group; j++)
			for (int i = placeFrom - 1; i < placeTo; i++)
				plrs.add((i + 1) + ". " + Language.get("group") + " " + (j + 1));

		int ysize = Calculator.min(Calculator.nextPowerOfTwo(plrs.size() - 1),
				16);
		int xsize = (plrs.size() - 1) / ysize + 1;
		GridLayout gl = new GridLayout(ysize, xsize);

		pRight.setLayout(gl);

		if (preKnockOut == null) {
			preKnockOut = new PreKnockOut(placeFrom, placeTo, group);
		}

		if (placeFrom != preKnockOut.getStart()
				| placeTo != preKnockOut.getEnd()
				| group != preKnockOut.getGroup()) {
			preKnockOut = new PreKnockOut(placeFrom, placeTo, group);
		}
		
		for (int i = 0; i < Calculator.nextPowerOfTwo(plrs.size() - 1); i++) {
			final JComboBox jCB = new JComboBox(plrs.toArray());
			jCB.setAction(new ChangeAction(i));
			pComboBoxes.add(jCB);
			JPanel jPan = new JPanel();
			jPan.add(new JLabel("#" + (i + 1) + ":"), BorderLayout.WEST);
			jPan.add(jCB, BorderLayout.EAST);
			pRight.add(jPan);
		}

		if (getParent() != null)
			getParent().repaint();
	}
	
	@Override
	public void repaint() {
		if (pComboBoxes != null) {
			int placeFrom = ((Integer) jPlaceFrom.getValue()).intValue();
			int placeTo = ((Integer) jPlaceTo.getValue()).intValue();
			int group = ((Integer) jGroupNum.getValue()).intValue();
	
			List<String> plrs = new ArrayList<String>();
			plrs.add("<" + Language.get("nobody") + ">");
			for (int j = 0; j < group; j++)
				for (int i = placeFrom - 1; i < placeTo; i++)
					plrs.add((i + 1) + ". " + Language.get("group") + " " + (j + 1));
			
			for (JComboBox jcb : pComboBoxes) {
				int idx = jcb.getSelectedIndex();
				jcb.setModel(new DefaultComboBoxModel(plrs.toArray()));
				jcb.setSelectedIndex(idx);
			}
		}
		super.repaint();
	}

	@Override
	public void save() {
		if (actualFile != null)
			try {
				preKnockOut.save(actualFile);
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
			} catch (IOException e) {
				System.out.println("File not accessible.");
			}
		else
			saveTo();
	}

	public void save(String fileName) {
		actualFile = fileName;
		save();
	}

	@Override
	public void saveTo() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			actualFile = chooser.getSelectedFile().getAbsoluteFile()
					.getAbsolutePath();
			save();
		}
	}

}
