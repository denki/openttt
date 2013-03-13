package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;

import database.match.Edge;
import database.match.Node;

@SuppressWarnings("serial")
public class JTreeView<T extends Node, U extends Edge<T>> extends JPanel
		implements MouseListener {
	/**
	 * Distances between the cells
	 */
	private static final int dx1 = 10, dx2 = 90, dy1 = 5, dy2 = 8;
	/**
	 * Height and Length of a Cell
	 */
	private static final int h = 15, l = 180;
	/**
	 * Offset for Window Decoration
	 */
	private static final int seedX = 5, seedY = 5 + h;

	private List<U> edges;

	private Graphics graphic;

	private List<List<T>> tree;

	/**
	 * Constructor for the Tree View
	 * 
	 * @param tree
	 *            top level list represents the levels of the ko-tree, low level
	 *            list represents the players in the level
	 */
	public JTreeView(List<List<T>> tree, List<U> edges) {
		super();
		this.tree = tree;
		this.edges = edges;
		int sizeX = tree.size() * l + (tree.size() - 1) * (dx1 + dx2) + 2
				* seedX;
		int sizeY = tree.get(0).size() * h + (tree.get(0).size() - 1) * dy1
				+ (tree.get(0).size() / 2 - 1) * (dy2 - dy1) + 2 * seedY - 2
				* h + 1;
		setPreferredSize(new Dimension(sizeX, sizeY));
		addMouseListener(this);
	}

	private void drawChildLine(Graphics graphic, int layer, int elemNum) {
		if (layer == 0)
			return;
		int[] foot1 = getFootCoord(layer - 1, elemNum * 2);
		int[] foot2 = getFootCoord(layer - 1, elemNum * 2 + 1);
		int[] foot12 = getFootCoord(layer, elemNum);

		U edge = null;
		for (U edge1 : edges) {
			T node1 = tree.get(layer - 1).get(elemNum * 2);
			T node2 = tree.get(layer - 1).get(elemNum * 2 + 1);
			if (edge1.getLeft().equals(node1) & edge1.getRight().equals(node2)) {
				edge = edge1;
				break;
			}
		}
			
		graphic.drawLine(foot1[0] + l, foot1[1] - h / 2, foot1[0] + l + dx1,
				foot1[1] - h / 2);
		graphic.drawLine(foot2[0] + l, foot2[1] - h / 2, foot2[0] + l + dx1,
				foot2[1] - h / 2);
		graphic.drawLine(foot2[0] + l + dx1, foot2[1] - h / 2, foot1[0] + l
				+ dx1, foot1[1] - h / 2);
		graphic.drawLine(foot12[0], foot12[1] - h / 2, foot1[0] + l + dx1,
				foot12[1] - h / 2);
		
		if (edge != null) {
			graphic.drawString(edge.edgePrintTop(), foot1[0] + l + dx1 + 2,
					foot12[1] - h / 2 - 2);
			graphic.drawString(edge.edgePrintBottom(), foot1[0] + l + dx1 + 2,
					foot12[1] + h / 2 - 2);
		
			if (edge.getWinner() != null) {
				graphic.drawLine(foot12[0], foot12[1] - h / 2 - 1, foot1[0] + l + dx1,
						foot12[1] - h / 2 - 1);
				
				if (edge.getWinner().equals(edge.getLeft())) {
					graphic.drawLine(foot1[0] + l, foot1[1] - h / 2 - 1, foot1[0] + l + dx1,
							foot1[1] - h / 2 - 1);
					graphic.drawLine(foot1[0] + l + dx1 + 1, foot1[1] - h / 2, foot1[0] + l + dx1 + 1
							, foot12[1] - h / 2);
				} else {
					graphic.drawLine(foot2[0] + l, foot2[1] - h / 2 - 1, foot2[0] + l + dx1,
							foot2[1] - h / 2 - 1);
					graphic.drawLine(foot2[0] + l + dx1 + 1, foot2[1] - h / 2, foot2[0] + l + dx1 + 1
							, foot12[1] - h / 2);
				}
			}
		}
	}

	private void drawNode(Graphics graphic, String text, int layer, int elemNum) {
		int[] footCoord = getFootCoord(layer, elemNum);

		int lbx = footCoord[0];
		int lby = footCoord[1];

		int rbx = lbx + l;
		int rby = lby;

		int ltx = lbx;
		int lty = lby - h;

		int rtx = rbx;
		int rty = lty;

		graphic.drawString(text, lbx + 1, lby - 2);
		graphic.drawLine(lbx, lby, rbx, rby); // bottom line
		graphic.drawLine(lbx, lby, ltx, lty); // left line
		graphic.drawLine(rbx, rby, rtx, rty); // right line
		graphic.drawLine(ltx, lty, rtx, rty); // top line
	}

//	public void drawTree() {
//		drawTree(graphic);
//	}

	public void drawTree(Graphics graphics) {
		recursiveDraw(graphics, tree.size() - 1, 0, true);
	}
	
	public void recursiveDraw(Graphics graphics, int x, int y, boolean drawIfNull) {
		boolean drawIfNull1 = drawIfNull;
		if (tree.get(x).get(y) != null) {
			drawNode(graphics, tree.get(x).get(y).toString(), x, y);
			drawIfNull1 = false;
		}
		else if (drawIfNull1)
			drawNode(graphics, "", x, y);
		if (x > 0) {
			if (tree.get(x - 1).get(2 * y) != null & tree.get(x - 1).get(2 * y + 1) != null){
				recursiveDraw(graphics, x - 1, 2 * y, drawIfNull1);
				recursiveDraw(graphics, x - 1, 2 * y + 1, drawIfNull1);
				drawChildLine(graphic, x, y);
			} else if (drawIfNull1) {
				recursiveDraw(graphics, x - 1, 2 * y, drawIfNull1);
				recursiveDraw(graphics, x - 1, 2 * y + 1, drawIfNull1);
				drawChildLine(graphic, x, y);
			}
		}
	}

	private int[] getFootCoord(int layer, int elemNum) {
		int[] result = new int[2];
		result[0] = seedX + (l + (dx1 + dx2)) * layer;

		if (layer == 0)
			result[1] = seedY + (dy1 + h) * (elemNum) + (dy2 - dy1)
					* (elemNum / 2);
		else
			result[1] = getFootCoord(layer - 1, 2 * elemNum)[1]
					+ (getFootCoord(layer - 1, 2 * elemNum + 1)[1] - getFootCoord(
							layer - 1, 2 * elemNum)[1]) / 2;

		return result;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			PrintJob pj = tk.getPrintJob(new Frame(), "", new Properties());
			if (pj != null) {
				Graphics g = pj.getGraphics();
				printAll(g);
				g.dispose();
				pj.end();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		graphic = g;
		g.setColor(Color.BLACK);
		drawTree(g);
	}

	@Override
	public void print(Graphics g) {
		Color oldBack = getBackground();
		setOpaque(true);
		setBackground(Color.WHITE);
		paint(g);
		setBackground(oldBack);
	}

	public void setEdges(List<U> edges) {
		this.edges = edges;
	}

}
