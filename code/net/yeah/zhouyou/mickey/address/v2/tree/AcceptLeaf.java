package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.CollUtils;

public class AcceptLeaf extends AbstractLeaf implements INode {

	private String key;

	public AcceptLeaf(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String psToString() {
		return "[key=" + key + "]";
	}

	@Override
	public Set<? extends INode> firstPos() {
		return CollUtils.asSet(this);
	}

	@Override
	public Set<? extends INode> lastPos() {
		return CollUtils.asSet(this);
	}
}
