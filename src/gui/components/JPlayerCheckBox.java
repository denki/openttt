package gui.components;

import javax.swing.JCheckBox;

import database.players.Player;

@SuppressWarnings("serial")
public class JPlayerCheckBox extends JCheckBox {
	private Player player;

	public JPlayerCheckBox(Player p) {
		super(p.getFullName());
		player = p;
	}

	@Override
	public void repaint() {
		if (player != null)
			setSelected(player.isThere());
		super.repaint();
	}

}
