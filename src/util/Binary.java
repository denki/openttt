package util;

import java.util.ArrayList;
import java.util.List;


public class Binary implements Tree {
	private Tree left, right;

	public Binary(Tree left, Tree right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public List<Tree> getChildren() {
		List<Tree> result = new ArrayList<Tree>();
		result.add(left);
		result.add(right);
		return result;
	}

	@Override
	public int countNodes() {
		return 1 + left.countNodes() + right.countNodes();
	}

	@Override
	public int depth() {
		return 1 + Math.max(left.depth(), right.depth());
	}

	@Override
	public int countLeaves() {
		return left.countLeaves() + right.countLeaves();
	}
	
	
}