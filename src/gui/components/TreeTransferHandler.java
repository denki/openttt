package gui.components;

import gui.Language;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import database.tournamentParts.Tournament;

public class TreeTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id = -1;
	private Tournament t;
	private JLabel component;

	public TreeTransferHandler(int id, JLabel component, Tournament t) {
		super();
		this.id = id;
		this.t = t;
		this.component = component;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		return true;
	}
	
	public int getID() {
		return id;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		return new PlayerTransferable(Collections.singletonList(id));
	}

	@Override
	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		if (!info.isDrop())
			return false;
		if (id != -1)
			return false;
		Integer data;
		try {
			data = ((List<Integer>) info.getTransferable().getTransferData(
					DataFlavor.stringFlavor)).get(0);
			id = data;
			if (id != -1)
				component.setText("<html>" + t.getPlayer(id).getFullName()
						+ "<br/> " + t.getPlayer(id).getClub() + "</html>");
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		component.getParent().repaint();
		return true;
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int action) {
		if (action != MOVE)
			return;
		id = -1;
		component.setText("<" + Language.get("nobody") + ">");
		component.repaint();
	}
}
