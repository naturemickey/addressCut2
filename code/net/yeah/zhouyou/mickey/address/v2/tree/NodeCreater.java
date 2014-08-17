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
			root.firstpos();
		}
		return root;
	}

	public static INode create(Long key, String... names) {
		INode root = null;
		for (String name : names) {
			if (root == null) {
				root = create(name);
			} else {
				root = new Node(Node.Type.OR, create(name), root);
			}
			root.firstpos();
		}
		return new Node(Node.Type.CAT, root, new AcceptLeaf(key));
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
				// _2 = node;
				node = new Node(Node.Type.OR, _1, node);
				nodes2[idx++] = node;
				node.firstpos();
				_1 = null;
				// _2 = null;
			}
		}

		if (_1 != null) {
			nodes2[idx] = _1;
		}
		return merge(nodes2);

		// INode root = null;
		// for (INode node : nodes) {
		// if (root == null) {
		// root = node;
		// } else {
		// root = new Node(Node.Type.OR, node, root);
		// }
		// // root.firstpos();
		// // root.lastpos();
		// }
		// return root;
	}

}
