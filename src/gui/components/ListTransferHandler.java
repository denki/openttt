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
    private Tournament tournament;
    private Group group;
    private DefaultListModel<Player> model;     
    
    public ListTransferHandler(JList<Player> lp, DefaultListModel<Player> lm, Tournament t, Group g) {
		super();
    	this.list = lp;
    	this.model = lm;
    	this.tournament = t;
    	this.group = g;
	}

    @Override
	public boolean canImport(TransferHandler.TransferSupport info) {
        return true;
   }

    @Override
	protected Transferable createTransferable(JComponent c) {
        List<Integer> result = new ArrayList<Integer>();
        for (Player p : list.getSelectedValuesList())
        	result.add(p.getID());
    	return new PlayerTransferable(result);
    }
    
    @Override
	public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }
    
    @Override
	public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop())
            return false;
        List<Integer> data;
		try {
			data = (List<Integer>) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
            int idx = ((JList.DropLocation) info.getDropLocation()).getIndex();
			for (Integer id : data) {
            	model.add(idx, tournament.getPlayer(id));
            	tournament.getQualifying().addToGroup(idx, tournament.getPlayer(id), group);
            	idx++;
            }
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        list.setModel(model);
        return true;
    }

    @Override
	protected void exportDone(JComponent c, Transferable t, int action) {
    	List<Integer> data;
		try {
			data = (List<Integer>) t.getTransferData(DataFlavor.stringFlavor);
        for (Integer id : data) {
        	model.removeElement(tournament.getPlayer(id));
        	tournament.getQualifying().removeFromGroup(tournament.getPlayer(id), group);
        }
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        list.setModel(model);
    }
}