package gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import database.players.Player;

public final class PlayerListRenderer implements ListCellRenderer<Player> {
	@Override
	public Component getListCellRendererComponent(
			JList<? extends Player> list, Player plr, int index,
			boolean isSelected, boolean cellHasFocus) {
		final JLabel cb = new JLabel();
		final Color selBG = javax.swing.UIManager.getDefaults().getColor(
				"List.selectionBackground");
		final Color selFG = javax.swing.UIManager.getDefaults().getColor(
				"List.selectionForeground");
		cb.setText("<html>" + plr.getFullName() + "<br/> " + plr.getClub()
				+ "</html>");
		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2, 2, 2, 2),
				BorderFactory.createLineBorder(cb.getForeground(), 1, true));
		cb.setBorder(border);
		if (isSelected) {
			cb.setOpaque(true);
			cb.setForeground(selFG);
			cb.setBackground(selBG);
		}
		return cb;
	}
}