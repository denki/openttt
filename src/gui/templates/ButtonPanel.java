package gui.templates;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ButtonPanel extends JPanel {
	protected List<IconButton> buttons;

	public ButtonPanel() {
		super();
		buttons = new ArrayList<IconButton>();
		setLayout(new FlowLayout());
	}

	protected void addButton(IconButton button) {
		buttons.add(button);
		add(button);
		repaint();
	}

	public void setEnabled(boolean enabled, int idx) {
		buttons.get(idx).setEnabled(enabled);
	}

	public void setEnabled(String pattern) {
		for (int idx = 0; idx < pattern.length(); idx++)
			if (pattern.substring(idx, idx + 1).equals("0"))
				setEnabled(false, idx);
			else
				setEnabled(true, idx);
	}

}
