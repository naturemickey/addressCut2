package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractLeaf extends AbstractNode implements INode {

	public Set<? extends INode> followPos() {
		Node parent = (Node) this.getParent();
		INode current = this;

		if (this instanceof AcceptLeaf)
			return Collections.emptySet();

		if (this instanceof AbstractLeaf) {
			while (parent != null && (parent.getType() == Node.Type.OR || parent.getLeft() != current)) {
				current = parent;
				parent = (Node) current.getParent();
			}
			if (parent != null) {
				// 以下一行代码成立，必须先证明：this必然在parent.getLeft().lastPos()中。
				return parent.getRight().firstPos();
			}
		}
		return Collections.emptySet();
	}

	@Override
	public String toString() {
		String ss = super.toString();
		return ss.substring(ss.lastIndexOf('.') + 1);
	}

	@Override
	public String createString() {
		return this.toString() + psToString() + "followpos:" + this.followPos();
	}

	abstract protected String psToString();
}
