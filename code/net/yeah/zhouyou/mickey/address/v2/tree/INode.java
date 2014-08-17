package net.yeah.zhouyou.mickey.address.v2.tree;

import java.util.Set;

public interface INode {

	Set<AbstractLeaf> firstpos();

	Set<AbstractLeaf> lastpos();

	/**
	 * 只能set一次非空值。
	 */
	void setParent(INode node);

	INode getParent();

	/**
	 * 测试打印结果使用。
	 */
	String createString();
}
