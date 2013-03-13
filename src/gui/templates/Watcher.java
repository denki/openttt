package gui.templates;

import gui.Main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class Watcher extends JFrame {
	protected Main main;
	
	public Watcher(String name, Main main) {
		super(name);
		this.main = main;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		setIconImage(IconManager.getImageIcon("main").getImage());
	}

	public abstract void generateWindow();

	public abstract void refresh();
	
	public void close() {
		main.removeWatcher(this);
		dispose();
	}
}
