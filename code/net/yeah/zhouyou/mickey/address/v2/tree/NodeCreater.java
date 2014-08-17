package net.yeah.zhouyou.mickey.address.v2.tree;

public class NodeCreater {

	public static INode create(String name) {

		char[] cs = name.toCharArray();
		int len = cs.length;
		INode root = new Leaf(cs[0]);
		for (int i = 1; i < len; ++i) {
			root = new Node(Node.Type.CAT, root, new Leaf(cs[i]));
		}

		return new Node(Node.Type.CAT, root, new AcceptLeaf(name));
	}

	public static INode merge(INode... nodes) {
		if (nodes.length == 1)
			return nodes[0];

		int mod2 = nodes.length % 2;
		int len = nodes.length / 2 + mod2;
		INode[] nodes2 = new INode[len];
		int idx = 0;
		for (int i = 1; i < nodes.length; i += 2) {
			nodes2[idx++] = new Node(Node.Type.OR, nodes[i - 1], nodes[i]);
		}

		if (mod2 == 1) {
			nodes2[idx] = nodes[nodes.length - 1];
		}
		return merge(nodes2);
	}

}
