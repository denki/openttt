package database.match;

/**
 * Edge in a TreeView
 * 
 * @author Tobias Denkinger
 * 
 * @param <T>
 *            Type of Nodes
 */
public abstract class Edge<T> {
	/**
	 * Prints the Edges Bottom Label
	 * 
	 * @return edges bottomlabel
	 */
	public abstract String edgePrintBottom();

	/**
	 * Prints the Edges Top Label
	 * 
	 * @return edges top label
	 */
	public abstract String edgePrintTop();

	/**
	 * Return left node
	 * 
	 * @return left node
	 */
	public abstract T getLeft();

	/**
	 * Return right node
	 * 
	 * @return right node
	 */
	public abstract T getRight();
	
	public abstract T getWinner();
	
	public abstract T getLoser();
}
