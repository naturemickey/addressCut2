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
		return new Node(Node.Type.CAT, root, new AcceptLeaf(name));
	}

	public static INode merge(INode... nodes) {

		if (nodes.length == 1)
			return nodes[0];

		INode _1 = null;
		// INode _2 = null;

		int len = nodes.length / 2 + nodes.length % 2;
		INode[] nodes2 = new INode[len];
		int idx = 0;
		for (int i = 0; i < nodes.length; ++i) {
			INode node = nodes[i];
			if (_1 == null) {
				_1 = node;
			} else {
				nodes2[idx++] = new Node(Node.Type.OR, _1, node);
				_1 = null;
			}
		}

		if (_1 != null) {
			nodes2[idx] = _1;
		}
		return merge(nodes2);
	}

}
