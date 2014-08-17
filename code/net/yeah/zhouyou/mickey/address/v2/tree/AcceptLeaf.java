package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.CollUtils;

public class AcceptLeaf implements INode {

	private String key;
	private INode parent;

	public AcceptLeaf(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "AcceptLeaf [key=" + key + "]";
	}

	@Override
	public Set<? extends INode> firstPos() {
		return CollUtils.asSet(this);
	}

	@Override
	public Set<? extends INode> lastPos() {
		return CollUtils.asSet(this);
	}

	@Override
	public void setParent(INode node) {
		if (this.parent != null)
			throw new RuntimeException();
		this.parent = node;
	}
}
