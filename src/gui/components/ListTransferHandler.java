package gui.components;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import database.players.Player;
import database.tournamentParts.Group;
import database.tournamentParts.Tournament;

public class ListTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1870791247290765578L;
	private JList<Player> list;
	private List<Player> players;
	private Tournament tournament;
	private int[] indices;
	private int index;
	private boolean sameComponent;
	private boolean exporting;
	private JComponent comp;
	private DefaultListModel<Player> model;

	public ListTransferHandler(JList<Player> lp, DefaultListModel<Player> lm,
			List<Player> plrs, Tournament t) {
		super();
		list = lp;
		model = lm;
		players = plrs;
		tournament = t;
		indices = null;
		sameComponent = false;
		exporting = false;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		return true;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		exporting = true;
		comp = c;
		List<Integer> result = new ArrayList<Integer>();
		for (Player p : list.getSelectedValuesList())
			result.add(p.getID());
		indices = ((JList) c).getSelectedIndices();
		return new PlayerTransferable(result);
	}

	@Override
	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		sameComponent = info.getComponent() == comp;
		if (!info.isDrop())
			return false;
		List<Integer> data;
		try {
			data = (List<Integer>) info.getTransferable().getTransferData(
					DataFlavor.stringFlavor);
			index = ((JList.DropLocation) info.getDropLocation()).getIndex();
			int idx = index;
			for (Integer id : data) {
				if (id != -1) {
					model.add(idx, tournament.getPlayer(id));
					players.add(idx, tournament.getPlayer(id));
				}
				idx++;
			}
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!exporting)
			list.setModel(model);
		return true;
	}

	@Override
	protected void exportDone(JComponent c, Transferable t, int action) {
		if (action != MOVE)
			return;
		List<Integer> data;
		try {
			data = (List<Integer>) t.getTransferData(DataFlavor.stringFlavor);
			int at;
			for (int i = indices.length - 1; i >= 0; i--) {
				at = indices[i];
				if (sameComponent && at > index)
					at += indices.length;
				model.removeElementAt(at);
			}
			for (Integer id : data) {
				players.remove(tournament.getPlayer(id));
			}
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		list.setModel(model);
	}
}