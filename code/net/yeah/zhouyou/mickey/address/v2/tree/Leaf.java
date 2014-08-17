package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.CollUtils;

public class Leaf implements INode {

	private char input;
	private INode parent;

	public Leaf(char input) {
		this.input = input;
	}

	public char getInput() {
		return input;
	}

	@Override
	public String toString() {
		return "Leaf [input=" + input + "]";
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
