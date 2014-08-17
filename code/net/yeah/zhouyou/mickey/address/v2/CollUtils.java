package net.yeah.zhouyou.mickey.address.v2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.tree.INode;

public class CollUtils {

	public static <T> Set<T> asSet(T... os) {
		Set<T> res = new HashSet<T>();
		res.addAll(Arrays.asList(os));
		return res;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> union(Set<?> c1, Set<?> c2) {
		Set<Object> res = new HashSet<Object>();
		res.addAll(c1);
		res.addAll(c2);
		return (Set<T>) res;
	}

}
