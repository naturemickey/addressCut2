package net.yeah.zhouyou.mickey.address.v2;

import static net.yeah.zhouyou.mickey.address.v2.DFAInstance.dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class AddressScanner {

	private static final Set<String> dCity;

	static {
		dCity = new HashSet<String>();
		dCity.add("北京");
		dCity.add("上海");
		dCity.add("天津");
		dCity.add("重庆");
	}

	private static Pattern p = Pattern.compile("[\\s　]");

	public static Address scan(String txt) {
		// 中文地址中的空白是没有意义的
		txt = p.matcher(txt).replaceAll("");

		List<String> addrList = dfa.scan(txt);

		Address res = new Address(txt);
		if (addrList.size() == 0)
			return res;

		CityToken top = null;
		CityToken bottom = null;
		List<CityToken> ctList = new ArrayList<CityToken>();

		while (!addrList.isEmpty()) {
			String name = getNextAddr(addrList);
			CityToken firstct = findTopCT(name);

			// 中国人写地址一般是“省”、“市”、“区”，对于BSP来说，商家也很少会省略“省”和“市”，如果直接写“区”以下的地址，则全国的地址重名的过多了。
			if (firstct.getLevel() > 3) {
				continue;
			}
			res.setAddr(firstct.getId(), name, firstct.getLevel());
			ctList.add(firstct);
			top = firstct;
			bottom = firstct;
			break;
		}

		while (!addrList.isEmpty()) {
			String name = getNextAddr(addrList);

			List<CityToken> ccl = getccl(top, bottom, name);

			if (ccl.size() == 1) {
				CityToken ct = ccl.get(0);
				if (ct.getLevel() < top.getLevel()) {
					top = ct;
					res.setAddr(ct.getId(), name, ct.getLevel());
					ctList.add(ct);
				} else {
					if (ct.getLevel() < 3 // 当前识别到的为省级或市级
							|| (bottom.getLevel() >= 2 && ct.getLevel() - bottom.getLevel() <= 2) // bottom为市（或以下）级时，当前识别到的与bottom相差在两级以内
							|| name.length() >= 3 // 当前识别到的地址文字长度至少为3个字
							|| DataCache.getIdMap().get(ct.getId()).get(0).getName().endsWith(name) // 当前识别到的地址是一个全称
					) {
						bottom = ct;
						res.setAddr(ct.getId(), name, ct.getLevel());
						ctList.add(ct);
					} else {
						bottom = getNextBottom(addrList, res, top, bottom, ctList, name, ccl);
					}
				}
			} else if (ccl.size() > 1) {
				bottom = getNextBottom(addrList, res, top, bottom, ctList, name, ccl);
			}
		}

		for (CityToken ct : ctList) {
			findParentLevel(res, ct);
		}

		if (res.getCityAddress() == null && res.getProvinceAddress() != null
				&& dCity.contains(res.getProvinceAddress())) {
			// 当只识别到一个地址，并且是直辖市的时候
			List<CityToken> ctl = DataCache.getNameMap().get(res.getProvinceAddress() + "市");
			for (CityToken ct : ctl) {
				if (ct.getParentId() != null && ct.getParentId().equals(res.getAddr(1).getId())) {
					res.setAddr(ct.getId(), null, ct.getLevel());
					break;
				}
			}
		}

		return res;
	}

	private static CityToken getNextBottom(List<String> addrList, Address res, CityToken top, CityToken bottom,
			List<CityToken> ctList, String name, List<CityToken> ccl) {
		if (!addrList.isEmpty()) {
			String name2 = getNextAddr(addrList);
			for (CityToken cct : ccl) {
				List<CityToken> ccl2 = getccl(top, cct, name2);
				if (!ccl2.isEmpty()) {
					// 两级关联之后，就随意取一个。因为此时至少已是三级地址，在同一个区或县内部的冲突就比较小了。
					CityToken ct2 = ccl2.get(0);
					if (ct2.getLevel() > cct.getLevel()) {
						bottom = ct2;
						res.setAddr(cct.getId(), name, cct.getLevel());
						res.setAddr(ct2.getId(), name2, ct2.getLevel());
						ctList.add(cct);
						ctList.add(ct2);
					}
				}
			}
		}
		return bottom;
	}

	/**
	 * 获取能匹配name的，所有可能合法的数据。
	 */
	private static List<CityToken> getccl(CityToken top, CityToken bottom, String name) {
		List<CityToken> ccl = new ArrayList<CityToken>();
		for (CityToken ct : DataCache.getNameMap().get(name)) {
			if (ct.getLevel() < top.getLevel()) {
				if (hasRelationship(ct, top)) {
					ccl.clear();
					ccl.add(ct);
					break;
				}
			} else if (ct.getLevel() > bottom.getLevel()) {
				if (hasRelationship(bottom, ct)) {
					if (!ccl.isEmpty()) {
						if (ccl.get(0).getLevel() > ct.getLevel()) {
							ccl.clear();
							ccl.add(ct);
						} else if (ccl.get(0).getLevel() == ct.getLevel()) {
							ccl.add(ct);
						}
					} else
						ccl.add(ct);
				}
			}
		}
		return ccl;
	}

	private static String getNextAddr(List<String> addrList) {
		String name = addrList.remove(0);
		while (addrList.remove(name))
			;
		return name;
	}

	private static void findParentLevel(Address res, CityToken ct) {
		while (ct.getLevel() > 1) {
			if (ct.getParent() == null)
				break;
			if (res.getAddr(ct.getLevel() - 1) != null)
				break;
			ct = ct.getParent();
			res.setAddr(ct.getId(), null, ct.getLevel());
		}
	}

	private static CityToken findTopCT(String name) {
		CityToken top = null;
		for (CityToken ct : DataCache.getNameMap().get(name)) {
			if (top == null || ct.getLevel() < top.getLevel()) {
				top = ct;
			}
		}
		return top;
	}

	private static boolean hasRelationship(CityToken pct, CityToken ct) {
		if (ct.getParentId() == null || ct.getLevel() <= pct.getLevel())
			return false;
		// 大于两个级别差的关联，相对来说准确率比较低。
		if (ct.getLevel() - pct.getLevel() > 2)
			return false;
		boolean res = ct.getParentId().equals(pct.getId());
		if (!res) {
			CityToken parentCt = ct.getParent();
			if (parentCt != null) {
				// 如果与ct的parent有关系，则与ct有关系
				return hasRelationship(pct, parentCt);
			}
		}
		return res;
	}
}
