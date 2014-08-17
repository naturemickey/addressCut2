package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.CollUtils;

public class Leaf extends AbstractLeaf implements INode {

	private char input;

	public Leaf(char input) {
		this.input = input;
	}

	public char getInput() {
		return input;
	}

	@Override
	public String psToString() {
		return "[input=" + input + "]";
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
