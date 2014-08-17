package net.yeah.zhouyou.mickey.address.v2.tree;

public class AcceptLeaf implements INode {

	private String key;

	public AcceptLeaf(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
