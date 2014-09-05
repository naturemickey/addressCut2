package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

public class NodeCreater {

	public static INode create(String name) {
		char[] cs = name.toCharArray();
		INode[] nodes = new INode[cs.length + 1];
		for (int i = 0, n = cs.length; i < n; ++i) {
			nodes[i] = new Leaf(cs[i]);
		}
		nodes[cs.length] = new AcceptLeaf(name);
		return merge(Node.Type.CAT, nodes);
	}

	private static INode merge(Node.Type type, INode... nodes) {
		while (nodes.length > 1) {
			int mod2 = nodes.length % 2;
			INode[] nodes2 = new INode[nodes.length / 2 + mod2];
			int idx = 0;
			for (int i = 1, n = nodes.length; i < n; i += 2) {
				nodes2[idx++] = new Node(type, nodes[i - 1], nodes[i]);
			}

			if (mod2 == 1) {
				nodes2[idx] = nodes[nodes.length - 1];
			}
			nodes = nodes2;
		}
		return nodes[0];
	}

	public static INode create(Set<String> names) {
		INode[] nodes = new INode[names.size()];
		int idx = -1;
		for (String name : names) {
			nodes[++idx] = create(name);
		}
		return merge(Node.Type.OR, nodes);
	}

}
