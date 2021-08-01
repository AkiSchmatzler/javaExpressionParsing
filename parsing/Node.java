package parsing;

/**
 * Node class specifies the structure of a node in a binary tree.
 * It's used to make binary tree out of an arithmetic expression
 * @author Aki Schmatzler
 * @version 1.0
 */

public class Node {
	protected char operation;
	protected double number = -1;
	protected int flag;
	protected int precedence = 10; //warning
	protected Node left = null;
	protected Node right = null;
	protected Node parent = null;

	/**
	 * Constructor for the Node class
	 * @param operation a char corresponding to the operation
	 * @param precedence the precedence of the given operation
	 * @param flag either noFlag, noClimbUp or RightAssociative
	 */
	public Node (char operation, int precedence, int flag) {
		this.operation = operation;
		this.precedence = precedence;
		this.flag = flag;
	}

	/**
	 * Constructor for the Node class
	 * @param number a number
	 * All leaves are numbers in our binary tree
	 */
	public Node (double number) {
		this.number = number;
		this.operation  = 'n';
		this.flag = 3; //noFlag
	}

	/**
	 * @return number of the Node (used if the node is a leaf node)
	 */
	public double getNumber () {
		return this.number;
	}

	/**
	 * @return the flag of the node
	 */
	public int getFlag () {
		return this.flag;
	}

	/**
	 * @return the char representing the operation of the node (if not leaf node)
	 */
	public char getOperation () {
		return this.operation;
	}

	/**
	 * @return the precedence of the node
	 */
	public int getPrecedence () {
		return this.precedence;
	}

	/**
	 * @return the right child of the node
	 */
	public Node getRight () {
		return this.right;
	}

	/**
	 * @return the left child of the node
	 */
	public Node getLeft () {
		return this.left;
	}

	/**
	 * @return the parent node of the node
	 */
	public Node getParent () {
		return this.parent;
	}

	/**Sets the left child to left
	 * @param left a Node that is to be set
	 */
	public void setLeft (Node left) {
		this.left = left;
	}

	/**Sets the right child to right
	 * @param right a Node that is to be set
	 */
	public void setRight (Node right) {
		this.right = right;
	}

	/**Sets the parent node to parent
	 * @param parent a Node that is to be set
	 */
	public void setParent (Node parent) {
		this.parent = parent;
	}
}