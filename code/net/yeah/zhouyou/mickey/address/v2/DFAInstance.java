package net.yeah.zhouyou.mickey.address.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.yeah.zhouyou.mickey.address.v2.tree.INode;
import net.yeah.zhouyou.mickey.address.v2.tree.NodeCreater;

public class DFAInstance {

	public static final DFA dfa;
	private static final Map<Long, DFA> dfaMap = new ConcurrentHashMap<Long, DFA>();

	public static DFA getDFA(Long id) {
		DFA res = dfaMap.get(id);
		if (res == null) {
			synchronized (DFAInstance.class) {
				res = dfaMap.get(id);
				if (res == null) {
					List<INode> nodeList = getNodeList(id, true);
					if (nodeList.size() > 0) {
						res = DFA.create(NodeCreater.merge(nodeList.toArray(new INode[nodeList.size()])));
					} else {
						res = DFA.create(NodeCreater.create(" "));
					}
					dfaMap.put(id, res);
				}
			}
		}
		return res;
	}

	private static List<INode> getNodeList(Long id, boolean containParent) {
		List<INode> nodeList = new ArrayList<INode>();
		List<CityToken> ctList = DataCache.pIdMap.get(id);
		if (ctList != null) {
			for (CityToken ct : ctList) {
				nodeList.add(NodeCreater.create(ct.getName()));
				nodeList.addAll(getNodeList(ct.getId(), false));

				if (containParent) {
					CityToken parent = ct.parent;
					while (parent != null) {
						List<CityToken> ctList2 = DataCache.idMap.get(parent.getId());
						if (ctList2 != null && ctList2.size() > 0) {
							for (CityToken ct2 : ctList2) {
								nodeList.add(NodeCreater.create(ct2.getName()));
							}
							parent = ctList2.get(0).parent;
						} else {
							parent = null;
						}
					}
				}
			}
		}
		return nodeList;
	}

	static {
		long initStart = System.currentTimeMillis();

		String cacheName = "dfaObj_v2.cache";
		DFA fa = SerializeUtil.read(cacheName);
		if (fa == null) {
			List<INode> nodeList = new ArrayList<INode>();
			for (String name : DataCache.nameMap.keySet()) {
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
