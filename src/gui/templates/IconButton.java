package gui.templates;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class IconButton extends JButton {

	public IconButton(Action a, ImageIcon icon) {
		super(a);
		setText("");
		setIcon(icon);
	}

	public IconButton(Action a, String icon) {
		this(a, IconManager.getImageIcon(icon));
	}
	
	public IconButton(Action a, String icon, String tooltip) {
		this(a, IconManager.getImageIcon(icon));
		setToolTipText(tooltip);
	}

}
