package util;

import java.util.List;

public interface Tree {
	boolean hasChildren();
	List<Tree> getChildren();
	int countNodes();
	int countLeaves();
	int depth();
}

