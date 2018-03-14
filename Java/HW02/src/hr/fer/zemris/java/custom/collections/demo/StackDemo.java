package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Demonstration program for {@link ObjectStack}. It evaluates a postfix
 * expression of integers and supported operations are +, -, /, * and %.
 * Expression is expected as a command line argument in quotes.
 * 
 * @author Dan
 *
 */
public class StackDemo {

	/**
	 * Evaluates a postfix expression.
	 * 
	 * @param args
	 *            expression for evaluation
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid argument.");
			return;
		}

		String[] values = args[0].split("\\s+");
		ObjectStack stack = new ObjectStack();

		for (String element : values) {
			if (element.matches("-?\\d+")) {
				stack.push(Integer.parseInt(element));
			} else {
				int a;
				int b;
				try {
					b = (int) stack.pop();
					a = (int) stack.pop();
				} catch (EmptyStackException e) {
					System.out.println("Invalid expression.");
					return;
				}

				try {
					int result = calculate(a, b, element);
					stack.push(result);
				} catch (UnsupportedOperationException e1) {
					System.out.println("Operation " + element + " is not supported.");
					return;
				} catch (ArithmeticException e2) {
					System.out.println("Cannot divide by zero.");
					return;
				}
			}
		}

		if (stack.size() != 1) {
			System.out.println("Invalid expression.");
		} else {
			System.out.println("Expression evaluates to " + (int) stack.pop());
		}
	}

	/**
	 * Performs simple arithmetic operations that are supported by this
	 * {@link StackDemo}.
	 * 
	 * @param a
	 *            first number
	 * @param b
	 *            second number
	 * @param operator
	 *            operation between numbers
	 * @return result of the operation
	 */
	private static int calculate(int a, int b, String operator) {

		switch (operator) {
		case "+":
			return a + b;
		case "-":
			return a - b;
		case "/":
			return a / b;
		case "*":
			return a * b;
		case "%":
			return a % b;
		default:
			throw new UnsupportedOperationException();
		}
	}
}
