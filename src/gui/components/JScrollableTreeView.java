package gui.components;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.match.Edge;
import database.match.Node;

@SuppressWarnings("serial")
public class JScrollableTreeView<T extends Node, U extends Edge<T>> extends
		JPanel {
	private JTreeView<T, U> tv;

	public JScrollableTreeView(List<List<T>> tree, List<U> edges) {
		super();
		tv = new JTreeView<T, U>(tree, edges);
		setLayout(new GridLayout(1, 1));
		add(new JScrollPane(tv));
	}

	public JTreeView<T, U> getTreeView() {
		return tv;
	}

	public void setEdges(List<U> edges) {
		tv.setEdges(edges);
	}
}
