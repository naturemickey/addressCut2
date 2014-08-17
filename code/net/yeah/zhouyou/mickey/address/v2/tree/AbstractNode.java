package net.yeah.zhouyou.mickey.address.v2.tree;

public abstract class AbstractNode implements INode {

	private INode parent;

	@Override
	public void setParent(INode node) {
		if (this.parent != null)
			throw new RuntimeException();
		this.parent = node;
	}

	@Override
	public INode getParent() {
		return this.parent;
	}

}
