package net.yeah.zhouyou.mickey.address.v2.tree;

public class Leaf implements INode {

	private char input;

	public Leaf(char input) {
		this.input = input;
	}

	public char getInput() {
		return input;
	}

}
