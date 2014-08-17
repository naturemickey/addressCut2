package net.yeah.zhouyou.mickey.address.v2;

import java.util.ArrayList;
import java.util.List;

import net.yeah.zhouyou.mickey.address.v2.tree.INode;
import net.yeah.zhouyou.mickey.address.v2.tree.NodeCreater;

public class DFAInstance {

	public static final DFA dfa;

	static {
		long initStart = System.currentTimeMillis();

		String cacheName = "dfaObj.cache";
		DFA fa = SerializeUtil.read(cacheName);
		if (fa == null) {

			List<INode> nodeList = new ArrayList<INode>();

			for (String line : new CityBasedataReader()) {
				String[] ss = line.split(",");
				if (ss.length <= 2)
					continue;
				Long id = Long.valueOf(ss[0]);
				// Long parentId = Long.valueOf(ss[1]);
				// String level = ss[2];
				String[] names = new String[ss.length - 3];
				System.arraycopy(ss, 3, names, 0, names.length);

				nodeList.add(NodeCreater.create(id, names));
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
