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
					Set<String> names = getNodeNames(id);
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

	private static Set<String> getNodeNames(Long id) {
		Set<String> names = new HashSet<String>();

		CityToken parent = addMyName(id, names);

		addChildrenNames(id, names);

		while (parent != null) {
			parent = addMyName(parent.getId(), names);
		}
		return names;
	}

	/**
	 * 获取当前id对应的节点的名字放到names中，并返回当前节点的上线节点。
	 */
	private static CityToken addMyName(Long id, Set<String> names) {
		// 此处获得的ctList不可能是空的，也不可能size为0
		List<CityToken> ctList = DataCache.idMap.get(id);
		for (int i = 0, n = ctList.size(); i < n; ++i) {
			names.add(ctList.get(i).getName());
		}
		return ctList.get(0).getParent();
	}

	/**
	 * 把id对应节点的所有子节点的名字（递归到所有下级子节点）加入到names中
	 */
	private static void addChildrenNames(Long id, Set<String> names) {
		List<CityToken> ctList;
		Set<Long> addeds = new HashSet<Long>(); // 已处理的id集合
		Deque<Long> pids = new ArrayDeque<Long>(); // 未处理的id栈
		pids.push(id);
		while (!pids.isEmpty()) {
			Long pid = pids.pop();
			if (addeds.add(pid)) {
				ctList = DataCache.pIdMap.get(pid);
				if (ctList != null) {
					for (int i = 0, n = ctList.size(); i < n; ++i) {
						CityToken ct = ctList.get(i);
						names.add(ct.getName());
						pids.push(ct.getId());
					}
				}
			}
		}
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

	private static interface DFASupplier {
		DFA get();
	}

	private static DFASupplier dfa123 = new DFASupplier() {
		public synchronized DFA get() {
			if (dfa123 == this) {
				Set<String> nameSet = new HashSet<String>();
				for (Map.Entry<Long, List<CityToken>> e : DataCache.pIdMap.entrySet()) {
					for (CityToken ct : e.getValue()) {
						if (ct.getLevel() <= 3) {
							nameSet.add(ct.getName());
						} else {
							break;
						}
					}
				}
				final DFA dfa = DFA.create(NodeCreater.create(nameSet));
				dfa123 = new DFASupplier() {
					public DFA get() {
						return dfa;
					}
				};
				return dfa;
			}
			return dfa123.get();
		}
	};

	public static DFA getDFA123() {
		return dfa123.get();
	}
}
