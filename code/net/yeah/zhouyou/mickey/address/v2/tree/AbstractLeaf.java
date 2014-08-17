package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Collections;
import java.util.Set;

import net.yeah.zhouyou.mickey.address.v2.CollUtils;

public abstract class AbstractLeaf extends AbstractNode implements INode {

	Set<AbstractLeaf> fop = null;

	public Set<AbstractLeaf> followpos() {
		if (fop == null) {
			fop = Collections.emptySet();
			Node parent = (Node) this.getParent();
			INode current = this;

			while (parent != null && (parent.getType() == Node.Type.OR || parent.getLeft() != current)) {
				current = parent;
				parent = (Node) current.getParent();
			}

			if (parent != null) {
				// 以下一行代码成立，必须先证明：this必然在parent.getLeft().lastPos()中。
				fop = parent.getRight().firstpos();
			}
		}

		return fop;
	}

	Set<AbstractLeaf> fp = null;

	@Override
	public Set<AbstractLeaf> firstpos() {
		if (fp == null)
			fp = CollUtils.asSet((AbstractLeaf) this);
		return fp;
	}

	@Override
	public String toString() {
		String ss = super.toString();
		return ss.substring(ss.lastIndexOf('.') + 1);
	}

	@Override
	public String createString() {
		return this.toString() + psToString() + ".followpos:" + this.followpos();
	}

	abstract protected String psToString();
}
