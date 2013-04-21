package util;

import java.util.Collections;
import java.util.List;


public class Nullary implements Tree {

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public List<Tree> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public int countNodes() {
		return 0;
	}

	@Override
	public int depth() {
		return 1;
	}

	@Override
	public int countLeaves() {
		return 1;
	}
}