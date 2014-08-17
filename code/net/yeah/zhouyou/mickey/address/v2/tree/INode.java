package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

public interface INode {

	Set<? extends INode> firstPos();

	Set<? extends INode> lastPos();

	void setParent(INode node);

	INode getParent();
}
