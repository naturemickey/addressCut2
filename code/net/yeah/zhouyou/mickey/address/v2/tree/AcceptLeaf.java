package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Collections;
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
	public Set<AbstractLeaf> firstpos() {
		return CollUtils.asSet((AbstractLeaf) this);
	}

	@Override
	public Set<AbstractLeaf> lastpos() {
		return CollUtils.asSet((AbstractLeaf) this);
	}

	@Override
	public Set<AbstractLeaf> followpos() {
		return Collections.emptySet();
	}
}
