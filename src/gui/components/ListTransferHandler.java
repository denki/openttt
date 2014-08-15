package gui.components;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import database.players.Player;

public class ListTransferHandler extends TransferHandler {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1870791247290765578L;
    private JList<Player> list; 
    private DefaultListModel<Player> model;      
    
    public ListTransferHandler(JList<Player> lp, DefaultListModel<Player> lm) {
		super();
    	this.list = lp;
    	this.model = lm;
	}
    
    /**
     * We only support importing strings.
     */
    @Override
	public boolean canImport(TransferHandler.TransferSupport info) {
        // Check for String flavor
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        return true;
   }

    /**
     * Bundle up the selected items in a single list for export.
     * Each line is separated by a newline.
     */
    @Override
	protected Transferable createTransferable(JComponent c) {
        return new PlayerTransferable(list.getSelectedValuesList());
    }
    
    /**
     * We support both copy and move actions.
     */
    @Override
	public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }
    
    /**
     * Perform the actual import.  This demo only supports drag and drop.
     */
    @Override
	public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop())
            return false;
        
           List<Player> data;
		try {
			data = (List<Player>) info.getTransferable().getTransferData(DataFlavor.stringFlavor);
            for (Player p : data)
            	model.addElement(p);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        list.setModel(model);
        return true;
    }

    /**
     * Remove the items moved from the list.
     */
    @Override
	protected void exportDone(JComponent c, Transferable t, int action) {
    	List<Player> data;
		try {
			data = (List<Player>) t.getTransferData(DataFlavor.stringFlavor);
        for (Player p : data)
        	model.removeElement(p);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        list.setModel(model);
    }
}