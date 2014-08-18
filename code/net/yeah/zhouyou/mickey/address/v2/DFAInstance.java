package net.yeah.zhouyou.mickey.address.v2;

import java.util.ArrayList;
import java.util.List;

import net.yeah.zhouyou.mickey.address.v2.tree.INode;
import net.yeah.zhouyou.mickey.address.v2.tree.NodeCreater;

public class DFAInstance {

	public static final DFA dfa;

	static {
		long initStart = System.currentTimeMillis();

		String cacheName = "dfaObj_v2.cache";
		DFA fa = SerializeUtil.read(cacheName);
		if (fa == null) {
			List<INode> nodeList = new ArrayList<INode>();
			for (String name : DataCache.getNameMap().keySet()) {
				nodeList.add(NodeCreater.create(name));
			}
			INode root = NodeCreater.merge(nodeList.toArray(new INode[nodeList.size()]));

			dfa = DFA.create(root);
			SerializeUtil.write(dfa, cacheName);
		} else {
			dfa = fa;
		}

		System.out.println("DFA init cost:" + (System.currentTimeMillis() - initStart));
	}
}
