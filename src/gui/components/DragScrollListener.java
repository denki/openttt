package gui.components;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class DragScrollListener extends MouseAdapter {
	private final Cursor defCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private final Cursor hndCursor = Cursor
			.getPredefinedCursor(Cursor.HAND_CURSOR);
	private final Point pp = new Point();
	private final JTreeView tv;
	private final JScrollPane scr;

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0)
			tv.zoomIn();
		else if (e.getWheelRotation() > 0)
			tv.zoomOut();
		scr.revalidate();
	}

	public DragScrollListener(JTreeView tv, JScrollPane scr) {
		super();
		this.tv = tv;
		this.scr = scr;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		final JComponent jc = (JComponent) e.getSource();
		Container c = jc.getParent();
		if (c instanceof JViewport) {
			JViewport vport = (JViewport) c;
			Point cp = SwingUtilities.convertPoint(jc, e.getPoint(), vport);
			Point vp = vport.getViewPosition();
			vp.translate(pp.x - cp.x, pp.y - cp.y);
			jc.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
			pp.setLocation(cp);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {
			tv.zoomReset();
			scr.revalidate();
		} else {
			JComponent jc = (JComponent) e.getSource();
			Container c = jc.getParent();
			if (c instanceof JViewport) {
				jc.setCursor(hndCursor);
				JViewport vport = (JViewport) c;
				Point cp = SwingUtilities.convertPoint(jc, e.getPoint(),
						vport);
				pp.setLocation(cp);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		((JComponent) e.getSource()).setCursor(defCursor);
	}
}