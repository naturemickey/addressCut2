package net.yeah.zhouyou.mickey.address.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache {

	private static final Map<String, List<CityToken>> nameMap;
	private static final Map<Long, List<CityToken>> idMap;

	public static Map<String, List<CityToken>> getNameMap() {
		return nameMap;
	}

	public static Map<Long, List<CityToken>> getIdMap() {
		return idMap;
	}

	static {
		long initStart = System.currentTimeMillis();

		Map<String, List<CityToken>> nm = new HashMap<String, List<CityToken>>();
		Map<Long, List<CityToken>> im = new HashMap<Long, List<CityToken>>();

		for (String line : new CityBasedataReader()) {
			String[] ss = line.split(",");
			if (ss.length <= 2)
				continue;
			Long id = Long.valueOf(ss[0]);
			Long parentId = Long.valueOf(ss[1]);
			String level = ss[2];

			for (int i = 3; i < ss.length; ++i) {
				String name = ss[i];

				CityToken ct = new CityToken(id, parentId, Integer.valueOf(level), name);
				List<CityToken> ctList = nm.get(name);
				if (ctList == null) {
					ctList = new ArrayList<CityToken>();
					nm.put(name, ctList);
				}
				ctList.add(ct);

				List<CityToken> actList = im.get(ct.getId());
				if (actList == null) {
					actList = new ArrayList<CityToken>();
					im.put(ct.getId(), actList);
				}
				boolean isShort = false;
				for (CityToken act : actList) {
					if (act.getName().length() >= ct.getName().length()) {
						isShort = true;
						break;
					}
				}
				if (isShort)
					actList.add(ct);
				else
					actList.add(0, ct);
			}
		}
		for (List<CityToken> ctl : nm.values()) {
			Collections.sort(ctl, new Comparator<CityToken>() {
				@Override
				public int compare(CityToken o1, CityToken o2) {
					return o1.getLevel() - o2.getLevel();
				}
			});

			for (CityToken ct : ctl) {
				if (ct.getParentId() != null) {
					List<CityToken> pctl = im.get(ct.getParentId());
					if (pctl != null && pctl.size() > 0)
						ct.parent = pctl.get(0);
				}
			}
		}
		nameMap = nm;
		idMap = im;
		System.out.println("DataCache init cost:" + (System.currentTimeMillis() - initStart));
	}
}
