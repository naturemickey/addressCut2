package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Collections;
import java.util.Set;

public class AcceptLeaf extends AbstractLeaf implements INode {

	private Long key;

	public AcceptLeaf(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}

	@Override
	public String psToString() {
		return "[key=" + key + "]";
	}

	@Override
	public Set<AbstractLeaf> followpos() {
		return Collections.emptySet();
	}
}
