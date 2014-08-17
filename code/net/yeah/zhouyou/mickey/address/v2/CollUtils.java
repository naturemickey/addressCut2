package net.yeah.zhouyou.mickey.address.v2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollUtils {

	@SuppressWarnings("unchecked")
	public static <T> Set<T> asSet(T... os) {
		Set<T> res = new HashSet<T>();
		res.addAll(Arrays.asList(os));
		return res;
	}

	public static <T> Set<T> union(Set<T> c1, Set<T> c2) {
		Set<T> res = new HashSet<T>();
		res.addAll(c1);
		res.addAll(c2);
		return res;
	}

}
