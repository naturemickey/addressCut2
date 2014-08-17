package net.yeah.zhouyou.mickey.address.v2;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.tree.AbstractLeaf;
import net.yeah.zhouyou.mickey.address.v2.tree.AcceptLeaf;
import net.yeah.zhouyou.mickey.address.v2.tree.INode;
import net.yeah.zhouyou.mickey.address.v2.tree.Leaf;

public class DFA {
	private DFAState startState;

	private DFA(DFAState start) {
		this.startState = start;
	}

	public static DFA create(INode tree) {
		// 龙书3：算法3.36
		Dstates dstates = new Dstates();
		Dstate start = new Dstate(tree.firstpos());
		dstates.addIfNotContains(start);
		for (Dstate s : dstates) {
			s.marked = true;
			for (char a : s.allInputs()) {
				Set<AbstractLeaf> u = new HashSet<AbstractLeaf>();
				for (AbstractLeaf n : s.nodes) {
					if (n instanceof Leaf) {
						Leaf leaf = (Leaf) n;
						if (leaf.getInput() == a) {
							u.addAll(leaf.followpos());
						}
					}
				}
				Dstate du = new Dstate(u);
				dstates.addIfNotContains(du);

				s.path.put(a, du);
			}
		}

		Map<Dstate, DFAState> map = new HashMap<Dstate, DFAState>();
		for (Dstate s : dstates.states) {
			DFAState ds = map.get(s);
			if (ds == null) {
				ds = new DFAState();
				ds.key = s.acceptKey();
				map.put(s, ds);
			}
			for (Map.Entry<Character, Dstate> e : s.path.entrySet()) {
				DFAState ds2 = map.get(e.getValue());
				if (ds2 == null) {
					ds2 = new DFAState();
					ds2.key = e.getValue().acceptKey();
					map.put(e.getValue(), ds2);
				}
				ds.path.put(e.getKey(), ds2);
			}
		}
		return new DFA(map.get(start));
	}

	public List<String> scan(String s) {
		DFAState currentState = this.startState;
		int currentIdx = 0;

		DFAState currentAccepted = null;
		int currentAcceptedIdx = 0;

		int fromIdx = 0;

		char[] bl = s.toCharArray();
		List<String> res = new ArrayList<String>();
		for (; currentIdx < bl.length; ++currentIdx) {
			char a = bl[currentIdx];
			currentState = currentState.tran(a);
			if (currentState == null) {
				if (currentAccepted != null) {
					// res.add(currentAccepted.getName());
					res.add(new String(bl, fromIdx, currentIdx - fromIdx));
					fromIdx = currentAcceptedIdx + 1;
					currentAccepted = null;
					currentIdx = currentAcceptedIdx;
				} else {
					currentIdx = fromIdx;
					fromIdx = fromIdx + 1;
				}
				currentState = this.startState;
			} else if (currentState.isAccepted()) {
				currentAccepted = currentState;
				currentAcceptedIdx = currentIdx;
			}
		}
		if (currentAccepted != null) {
			// res.add(currentAccepted.getName());
			res.add(new String(bl, fromIdx, currentIdx - fromIdx));
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<DFAState> nss = new HashSet<DFAState>();
		Deque<DFAState> stack = new ArrayDeque<DFAState>();
		stack.push(this.startState);

		while (!stack.isEmpty()) {
			DFAState ds = stack.pollFirst();
			nss.add(ds);
			sb.append(ds.createString());
			for (DFAState ds2 : ds.path.values()) {
				if (nss.contains(ds2) == false)
					stack.push(ds2);
			}
		}

		return sb.toString();
	}
}

class Dstates implements Iterable<Dstate> {
	Set<Dstate> states = new HashSet<Dstate>();

	void addIfNotContains(Dstate s) {
		boolean isContains = false;
		for (Dstate state : states) {
			if (state.nodes.equals(s.nodes)) {
				isContains = true;
				break;
			}
		}
		if (!isContains) {
			states.add(s);
		}
	}

	@Override
	public Iterator<Dstate> iterator() {
		return new Iterator<Dstate>() {

			Dstate next = null;

			@Override
			public boolean hasNext() {
				for (Dstate s : states) {
					if (!s.marked) {
						next = s;
						return true;
					}
				}
				return false;
			}

			@Override
			public Dstate next() {
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}

class Dstate {
	boolean marked;
	Set<AbstractLeaf> nodes;
	Map<Character, Dstate> path = new HashMap<Character, Dstate>();

	Dstate(Set<AbstractLeaf> nodes) {
		this.nodes = nodes;
	}

	Set<Character> allInputs() {
		Set<Character> res = new HashSet<Character>();
		for (AbstractLeaf n : nodes) {
			if (n instanceof Leaf) {
				res.add(((Leaf) n).getInput());
			}
		}
		return res;
	}

	Long acceptKey() {
		Long key = null;
		for (AbstractLeaf n : nodes) {
			if (n instanceof AcceptLeaf) {
				AcceptLeaf al = (AcceptLeaf) n;
				if (key == null)
					key = al.getKey();
				else if (!key.equals(al.getKey()))
					throw new RuntimeException();
			}
		}
		return key;
	}
}

class DFAState implements Serializable {

	private static final long serialVersionUID = -6987881824989057409L;

	Map<Character, DFAState> path = new HashMap<Character, DFAState>();;
	Long key;

	DFAState tran(char c) {
		return path.get(c);
	}

	boolean isAccepted() {
		return key != null;
	}

	@Override
	public String toString() {
		String ss = super.toString();
		ss = ss.substring(ss.lastIndexOf('.') + 1);
		if (this.key == null)
			return ss;
		return ss + "{" + this.key + "}";
	}

	public String createString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[').append(toString()).append("]\n");
		for (Entry<Character, DFAState> e : this.path.entrySet()) {
			sb.append('\t').append(':').append(e.getKey() == null ? "_e" : e.getKey()).append("->")
					.append(e.getValue()).append('\n');
		}
		return sb.toString();
	}
}
