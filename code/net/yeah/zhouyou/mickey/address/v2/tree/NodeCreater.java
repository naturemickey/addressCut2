package net.yeah.zhouyou.mickey.address.v2.tree;

public class NodeCreater {

	public static INode create(String name) {
		INode root = null;

		for (char c : name.toCharArray()) {
			if (root == null) {
				root = new Leaf(c);
			} else {
				root = new Node(Node.Type.CAT, root, new Leaf(c));
			}
		}
		return root;
	}

	public static INode create(Long key, String... names) {
		INode root = null;
		for (String name : names) {
			if (root == null) {
				root = create(name);
			} else {
				root = new Node(Node.Type.OR, root, create(name));
			}
		}
		return new Node(Node.Type.CAT, root, new AcceptLeaf(key));
	}

	public static INode merge(INode... nodes) {
		INode root = null;
		for (INode node : nodes) {
			if (root == null) {
				root = node;
			} else {
				root = new Node(Node.Type.OR, root, node);
			}
		}
		return root;
	}

}
