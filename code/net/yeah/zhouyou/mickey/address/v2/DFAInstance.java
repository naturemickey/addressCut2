package net.yeah.zhouyou.mickey.address.v2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.yeah.zhouyou.mickey.address.v2.tree.AcceptLeaf;
import net.yeah.zhouyou.mickey.address.v2.tree.NodeCreater;

public class DFAInstance {

	public static final DFA dfa;
	private static final Map<Long, DFA> dfaMap = new ConcurrentHashMap<Long, DFA>();

	private static final DFA PRESENT = DFA.create(new AcceptLeaf(""));

	public static DFA getDFA(Long id) {
		DFA res = dfaMap.get(id);
		if (res == null) {
			synchronized (DFAInstance.class) {
				res = dfaMap.get(id);
				if (res == null) {
					Set<String> names = getNodeNames(id, true);
					if (names.size() > 0) {
						res = DFA.create(NodeCreater.create(names));
					} else {
						res = PRESENT;
					}
					dfaMap.put(id, res);
				}
			}
		}
		return res;
	}

	private static Set<String> getNodeNames(Long id, boolean containParent) {
		Set<String> names = new HashSet<String>();

		List<CityToken> ctList = DataCache.idMap.get(id);
		for (int i = 0; i < ctList.size(); ++i) {
			names.add(ctList.get(i).getName());
		}
		CityToken parent = ctList.get(0).getParent();

		Set<Long> addeds = new HashSet<Long>();
		Deque<Long> pids = new ArrayDeque<Long>();
		pids.push(id);
		while (!pids.isEmpty()) {
			Long pid = pids.pop();
			if (addeds.add(pid)) {
				ctList = DataCache.pIdMap.get(pid);
				if (ctList != null) {
					for (int i = 0; i < ctList.size(); ++i) {
						CityToken ct = ctList.get(i);
						names.add(ct.getName());
						pids.push(ct.getId());
					}
				}
			}
		}

		if (containParent) {
			while (parent != null) {
				List<CityToken> ctList2 = DataCache.idMap.get(parent.getId());
				if (ctList2 != null && ctList2.size() > 0) {
					for (int i = 0; i < ctList2.size(); ++i) {
						names.add(ctList2.get(i).getName());
					}
					parent = ctList2.get(0).parent;
				} else {
					parent = null;
				}
			}
		}
		return names;
	}

	static {
		long initStart = System.currentTimeMillis();

		String cacheName = "dfaObj_v2.cache";
		DFA fa = SerializeUtil.read(cacheName);
		if (fa == null) {
			dfa = DFA.create(NodeCreater.create(DataCache.nameMap.keySet()));
			SerializeUtil.write(dfa, cacheName);
		} else {
			dfa = fa;
		}
		System.out.println("DFA init cost:" + (System.currentTimeMillis() - initStart));
	}
}
