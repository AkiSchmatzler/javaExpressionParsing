package parsing;

/**
 * Parse class specifies how an input String is parsed. It uses the binary 
 * tree structure specified in Node class
 * @author Aki Schmatzler
 * @version 1.0
 */

public class Parse {
	public static final int Noinfo = 0;
	public static final int NoClimbUp = 1;
	public static final int RightAssociative = 2;
	public static final int noFlag = 3;
	public boolean divisionBy0 = false;
	public boolean invalidExpression = false;

	protected double res;
	protected String ex;

	/**
	 * @param ex an arithmetic Expression
	 */
	public Parse (String ex) {
		this.ex = ex;
		Node root = parseExpression (ex);
		this.res = evaluate_expression (root);
	}

	/**
	 * this function inserts a Node at the right place in a tree, given it's precedence and
	 * the precedence of the current node. This function is an intermediate fonction for parseExpression()
	 * @param current the current Node
	 * @param toAdd the Node that is to be added
	 * @return the new current Node
	 */
	public Node insertInTree (Node current, Node toAdd) {
		Node n = null;
		int flag = toAdd.getFlag();

		if (flag != NoClimbUp) {
			if (flag != RightAssociative) {
				while (current.getPrecedence() >= toAdd.getPrecedence())
					current = current.getParent();
			} else {
				while (current.getPrecedence() > toAdd.getPrecedence())
					current = current.getParent();
			}
		}

		if (toAdd.getOperation() == ')') {
			n = current.getParent();
			n.setRight(current.getRight());
			if (current.getRight() != null)
				current.getRight().setParent(n);
			return n;
		}

		else {
			n = toAdd;
			n.setLeft(current.getRight());
			if (current.getRight() != null) 
				current.getRight().setParent(n);
			current.setRight(n);
			n.setParent(current);
			return n;
		}

	}

	/**
	 * This function parses the arithmetic expression into a binary tree
	 * @param ex an arithmetic Expression
	 * @return the root Node of the binary parsed tree
	 */
	public Node parseExpression (String ex) {
		int numBracket = 0;

		Node root = new Node ('(', 1, NoClimbUp);
		Node current = root;
	

		for (int i = 0; i<ex.length(); i++) {
			char c = ex.charAt(i);

			Node newNode = null;

			if 		(c == '(') { newNode = new Node ('(', 1, NoClimbUp); numBracket++; }
			else if (c == ')'){ newNode = new Node (')', 1, RightAssociative); numBracket++; }
			else if (c == '+') newNode = new Node ('+', 2, noFlag);
			else if (c == '*') newNode = new Node ('*', 4, noFlag);
			else if (c == '-') newNode = new Node ('-', 2, noFlag);
			else if (c == '/') newNode = new Node ('/', 4, noFlag);
			else if (c == '^') newNode = new Node ('^', 5, RightAssociative);
			else if (c == '!') newNode = new Node ('!', 6, noFlag);

			else if (c == 'c' || c == 's' || c == 't' || c == 'e') {
				//cos sin or tan or exp
				newNode = new Node (c, 10, noFlag);
				i += 2;
			}

			else if ('0'<= c && c<='9') {
				boolean endNum = false;
				String num = "";
				num += c;
				do {
					if (i+1<ex.length())
						c = ex.charAt(i+1);
					else break;
					if (('0'<= c && c<='9') || c == '.') {
						num+=c;
						i++;
					}
					else endNum = true;
					
				} while (!endNum && i<ex.length() - 1);

				newNode = new Node (Double.parseDouble(num));
			}

			else { //problem
				String err = String.format ("Unrecognized char : %c", c);
				System.out.println (err);
			}

			if (newNode != null) {
				current = insertInTree (current, newNode);
			}
		}

		if (root.getRight() != null) 
			root.getRight().setParent(null);

		if (numBracket%2 != 0) invalidExpression = true;
		return root.getRight();
		
	}

	/**
	 * This function evaluates the value of an expression that has been parsed
	 * @param tree the root Node of the parsed tree
	 * @return the value of the parsed expression
	 */
	public double evaluate_expression (Node tree) {
		double left, right;
		if (tree == null) {
			return 0;
		}
		
		right = evaluate_expression(tree.getRight());
		left = evaluate_expression(tree.getLeft());

		switch (tree.getOperation()) {
			case '+': 
				if (tree.getRight() == null || tree.getLeft() == null)
					invalidExpression = true;
				return left+right;
			case '*': 
				if (tree.getRight() == null || tree.getLeft() == null)
					invalidExpression = true;
				return left*right;
			case '/': 
				if (tree.getRight() == null || tree.getLeft() == null)
					invalidExpression = true; 
				if (right == 0) divisionBy0 = true;
				return left/right;
			case '-':
				if (tree.getRight() == null && tree.getLeft() == null)
					invalidExpression = true; 
				if (tree.getLeft() != null && tree.getRight() != null)
					return left-right;
				if (tree.getLeft() == null && tree.getRight() != null)
					return -right;
			case 'u': 
				return -right;
			case '^':
				if (tree.getRight() == null || tree.getLeft() == null)
					invalidExpression = true;
				return Math.pow (left, right);
			case '!':
				if (tree.getLeft() == null ||  (left - Math.floor(left) != 0))
					invalidExpression = true;
				return factorial ((int) left);
			case 'c':
				//cos
				if (tree.getRight() == null)
					invalidExpression = true;
				return Math.cos (right);
			case 's':
				//sin
				if (tree.getRight() == null)
					invalidExpression = true;
				return Math.sin (right);
			case 't':
				//tan
				if (tree.getRight() == null)
					invalidExpression = true;
				return Math.tan (right);
			case 'e':
				if (tree.getRight() == null)
					invalidExpression = true;
				return Math.exp(right);
			default : 
				return tree.getNumber();
		}
	}

	/**getter
	 * @return the value of the expression
	 */
	public double getRes () {
		return this.res;
	}

	/** Recursively calculates the factorial of a number
	 * 
	 * @param n 
	 * @return the value of n! 
	 */
	private static int factorial (int n) {    
		if (n == 0)
			return 1;
		else
			return(n * factorial(n-1));
	}
	

}