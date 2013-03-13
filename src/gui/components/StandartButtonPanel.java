package gui.components;

import gui.Language;
import gui.Main;
import gui.templates.ButtonPanel;
import gui.templates.IconButton;
import gui.templates.IconManager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

@SuppressWarnings("serial")
public class StandartButtonPanel extends ButtonPanel {
	private static String pos = BorderLayout.SOUTH;
	protected Action aBack = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.back();
		}
	};
	protected Action aClone = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.cloneTournament();
		}
	};

	protected Action aNew = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Main newMain = new Main();
			newMain.setView(0);
		}
	};

	protected Action aNext = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.next();
		}
	};

	protected Action aOpen = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.openFile();
		}
	};

	protected Action aPrint = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.print();
		}
	};

	protected Action aPrintPreview = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.printPreview();
		}
	};

	protected Action aProperties = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.properties();
		}
	};

	protected Action aSave = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.saveFile();
		}
	};

	protected Action aSaveAs = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			m.saveFileAs();
		}
	};

	protected String[] buttonNames = { "new", "open", "save", "saveas",
			"print_preview", "print", "clone", "back", "properties", "next" };

	private Main m;

	public StandartButtonPanel(Main m) {
		super();
		this.m = m;
		for (String buttonName : buttonNames) {
			IconButton b = new IconButton(getAction(buttonName), buttonName);
			b.setToolTipText(Language.get("tooltip_" + buttonName));
			addButton(b);
		}
	}

	public static String getPos() {
		return pos;
	}

	public static void setPos(String newPos) {
		pos = newPos;
	}

	private Action getAction(String name) {
		if (name.equals("new"))
			return aNew;
		if (name.equals("open"))
			return aOpen;
		if (name.equals("save"))
			return aSave;
		if (name.equals("saveas"))
			return aSaveAs;
		if (name.equals("print"))
			return aPrint;
		if (name.equals("print_preview"))
			return aPrintPreview;
		if (name.equals("back"))
			return aBack;
		if (name.equals("clone"))
			return aClone;
		if (name.equals("next"))
			return aNext;
		if (name.equals("properties"))
			return aProperties;
		return null;
	}
	
	@Override
	public void repaint() {
		int i = 0;
		if (buttons != null)
			for (IconButton b : buttons) {
				b.setIcon(IconManager.getImageIcon(buttonNames[i]));
				b.setToolTipText(Language.get("tooltip_" + buttonNames[i]));
				i++;
			}
		super.repaint();
	}
}
