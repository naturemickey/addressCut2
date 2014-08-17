package net.yeah.zhouyou.mickey.address.v2.tree;

public class Node {

	public static enum Type {
		CAT, OR
	};

	private Type type;
	private INode left;
	private INode right;

	public Node(Type type, INode left, INode right) {
		this.type = type;
		this.left = left;
		this.right = right;
	}

	public Type getType() {
		return type;
	}

	public INode getLeft() {
		return left;
	}

	public INode getRight() {
		return right;
	}

}
