package gui.templates;

import gui.Main;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class View extends JPanel {
	protected Main main;

	public View(Main m) {
		super();
		main = m;
	}

	public abstract void generateWindow();

	public abstract String getIconEnabledPattern();

	public boolean handleOpenClose() {
		return false;
	}

	public void open() {

	}

	public abstract void refresh();

	public void save() {
		// do nothing by default
	}

	public void saveTo() {
		// do nothing by default
	}
}
