package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Font;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * This is a model of a simple calculator. <br>
 * Standard binary and unary operations, as well as stack operations are
 * supported. <br>
 * Maximum number of input digits is set to: {@value #MAX_INPUT_LENGTH}. <br>
 * All operations are performed using {@link Double} values, so some limitations
 * and imprecision are possible.
 * 
 * @author Dan
 * @see CalcLayout
 *
 */
public class Calculator extends JFrame {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Program entry point. Creates a new frame {@link Calculator}.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}

	/** Font used for all buttons. */
	private static final Font FONT = new Font(null, Font.PLAIN, 20);

	/** Color used for all buttons. */
	private static final Color COLOR = new Color(115, 159, 208);

	/** Border color for all buttons. */
	private static final Color BORDER_COLOR = Color.BLUE;

	/** Maximum number of digits entered by the user. */
	private static final int MAX_INPUT_LENGTH = 16;

	/** Panel with all components. */
	private JPanel panel;

	/** Calculator's screen component. */
	private JLabel screen;

	/** Inverse {@code JCheckBox}; when selected inverse functions appear. */
	private JCheckBox inverse;

	/** Calculator's stack. */
	private Stack<Double> stack;

	/** {@code true} when user didn't enter anything. */
	private boolean newInput;

	/** Current stored result. */
	private Double result;

	/** Current binary operation. */
	private BiFunction<Double, Double, Double> operation;

	/**
	 * Creates a new frame {@link Calculator} and draws it.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Calculator");
		setLocation(200, 200);
		setSize(650, 450);
		initGUI();
		setMinimumSize(getMinimumSize());
	}

	/**
	 * Initializes the GUI and all calculator components.
	 */
	private void initGUI() {
		panel = new JPanel(new CalcLayout(10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(panel);

		screen = new JLabel();
		panel.add(screen, CalcLayout.FIRST_POSITION);
		screen.setFont(new Font(null, Font.BOLD, 30));
		screen.setHorizontalAlignment(JLabel.RIGHT);
		screen.setBackground(Color.ORANGE);
		screen.setOpaque(true);
		newInput = true;
		screen.setText("0");

		JButton sign = new JButton("+/-");
		panel.add(sign, new RCPosition(5, 4));
		setComponentDesign(sign);
		sign.addActionListener(e -> {
			String input = screen.getText();
			if (input.equals("0")) {
				return;
			}
			if (input.startsWith("-")) {
				input = input.substring(1);
			} else {
				input = "-" + input;
			}
			screen.setText(input);
		});

		JButton decimal = new JButton(".");
		panel.add(decimal, new RCPosition(5, 5));
		setComponentDesign(decimal);
		decimal.addActionListener(e -> {
			if (newInput) {
				screen.setText("0.");
				newInput = false;
			} else {
				String input = screen.getText();
				if (!input.contains(".")) {
					input += ".";
					screen.setText(input);
				}
			}
		});

		JButton equals = new JButton("=");
		panel.add(equals, new RCPosition(1, 6));
		setComponentDesign(equals);
		equals.addActionListener(e -> {
			if (operation == null || result == null) {
				return;
			}
			result = operation.apply(result, getScreen());
			setScreen(result);
			newInput = true;
			operation = null;
		});

		JButton clr = new JButton("clr");
		panel.add(clr, new RCPosition(1, 7));
		setComponentDesign(clr);
		clr.addActionListener(e -> {
			newInput = true;
			screen.setText("0");
		});

		JButton reset = new JButton("res");
		panel.add(reset, new RCPosition(2, 7));
		setComponentDesign(reset);
		reset.addActionListener(e -> {
			newInput = true;
			screen.setText("0");
			result = null;
			operation = null;
		});

		inverse = new JCheckBox("inv");
		panel.add(inverse, new RCPosition(5, 7));
		setComponentDesign(inverse);

		initDigits();
		initBinary();
		initUnary();
		initStack();
	}

	/**
	 * Sets the given number on the screen.
	 * 
	 * @param number
	 *            number to set
	 */
	private void setScreen(double number) {
		String s;
		if (number == Math.round(number)) {
			s = Long.toString(Math.round(number));
		} else {
			s = Double.toString(number);
		}
		screen.setText(s);
	}

	/**
	 * @return current number on screen as {@code Double}
	 */
	private double getScreen() {
		return Double.parseDouble(screen.getText());
	}

	/**
	 * Sets the default font and color to the given {@link JComponent}.
	 * 
	 * @param comp
	 *            component to change
	 */
	private void setComponentDesign(JComponent comp) {
		comp.setBackground(COLOR);
		comp.setFont(FONT);
		comp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
	}

	/**
	 * Adds digit buttons.
	 */
	private void initDigits() {
		panel.add(new DigitButton(0), new RCPosition(5, 3));
		panel.add(new DigitButton(1), new RCPosition(4, 3));
		panel.add(new DigitButton(2), new RCPosition(4, 4));
		panel.add(new DigitButton(3), new RCPosition(4, 5));
		panel.add(new DigitButton(4), new RCPosition(3, 3));
		panel.add(new DigitButton(5), new RCPosition(3, 4));
		panel.add(new DigitButton(6), new RCPosition(3, 5));
		panel.add(new DigitButton(7), new RCPosition(2, 3));
		panel.add(new DigitButton(8), new RCPosition(2, 4));
		panel.add(new DigitButton(9), new RCPosition(2, 5));
	}

	/**
	 * Adds binary operations.
	 */
	private void initBinary() {
		panel.add(new BinaryOperationButton("+", Double::sum), new RCPosition(5, 6));
		panel.add(new BinaryOperationButton("-", (a, b) -> a - b), new RCPosition(4, 6));
		panel.add(new BinaryOperationButton("*", (a, b) -> a * b), new RCPosition(3, 6));
		panel.add(new BinaryOperationButton("/", (a, b) -> a / b), new RCPosition(2, 6));
		panel.add(new BinaryOperationButton("x^n", "n√x", (x, n) -> Math.pow(x, n), (n, x) -> Math.pow(x, 1. / n)),
				new RCPosition(5, 1));
	}

	/**
	 * Adds unary operations.
	 */
	private void initUnary() {
		panel.add(new UnaryOperationButton("1/x", x -> 1. / x), new RCPosition(2, 1));
		panel.add(new UnaryOperationButton("log", "10^x", x -> Math.log10(x), x -> Math.pow(10, x)),
				new RCPosition(3, 1));
		panel.add(new UnaryOperationButton("ln", "e^x", x -> Math.log(x), x -> Math.pow(Math.E, x)),
				new RCPosition(4, 1));
		panel.add(new UnaryOperationButton("sin", "asin", x -> Math.sin(x), x -> Math.asin(x)), new RCPosition(2, 2));
		panel.add(new UnaryOperationButton("cos", "acos", x -> Math.cos(x), x -> Math.acos(x)), new RCPosition(3, 2));
		panel.add(new UnaryOperationButton("tan", "atan", x -> Math.tan(x), x -> Math.atan(x)), new RCPosition(4, 2));
		panel.add(new UnaryOperationButton("ctg", "actg", x -> 1 / Math.tan(x), x -> 1 / Math.atan(x)),
				new RCPosition(5, 2));
	}

	/**
	 * Adds buttons for working with stack.
	 */
	private void initStack() {
		stack = new Stack<>();

		JButton push = new JButton("push");
		panel.add(push, new RCPosition(3, 7));
		setComponentDesign(push);
		push.addActionListener(e -> {
			double input = getScreen();
			stack.push(input);
		});

		JButton pop = new JButton("pop");
		panel.add(pop, new RCPosition(4, 7));
		setComponentDesign(pop);
		pop.addActionListener(e -> {
			if (stack.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Stack is empty!", "", JOptionPane.INFORMATION_MESSAGE);
			} else {
				setScreen(stack.pop());
			}
		});
	}

	/**
	 * This class represents a digit {@link JButton}.
	 * 
	 * @author Dan
	 *
	 */
	private class DigitButton extends JButton {

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * Creates a new {@link DigitButton} with given argument.
		 * 
		 * @param digit
		 *            number this digit represents
		 */
		private DigitButton(int digit) {
			super(Integer.toString(digit));
			setComponentDesign(this);
			addActionListener(e -> {
				String input;
				if (newInput) {
					input = "";
					newInput = false;
				} else {
					input = screen.getText();
				}

				if (input.replace(".", "").replace("-", "").length() >= MAX_INPUT_LENGTH) {
					return;
				}
				if (input.equals("0")) {
					input = "";
				}
				input += digit;
				screen.setText(input);
			});
		}
	}

	/**
	 * This class represents a binary operation {@link JButton}. <br>
	 * Two constructors are offered, one for operations without an inverse
	 * operation and one for operations with an inverse operation.
	 * 
	 * @author Dan
	 *
	 */
	private class BinaryOperationButton extends JButton {

		/** */
		private static final long serialVersionUID = 1L;

		/** Binary operation that this object performs. */
		private BiFunction<Double, Double, Double> op;

		/**
		 * Creates a new {@link BinaryOperationButton} with given arguments that
		 * has no inverse operation.
		 * 
		 * @param text
		 *            text the button should display
		 * @param op
		 *            binary operation that this object performs
		 */
		public BinaryOperationButton(String text, BiFunction<Double, Double, Double> op) {
			super(text);
			this.op = op;
			setComponentDesign(this);
			addActionListener(e -> {
				double input = getScreen();
				if (operation != null && result != null && !newInput) {
					result = operation.apply(result, input);
					setScreen(result);
				} else {
					result = input;
				}

				operation = this.op;
				newInput = true;
			});
		}

		/**
		 * Creates a new {@link BinaryOperationButton} with given arguments that
		 * has an inverse operation.
		 * 
		 * @param text
		 *            text the button should display when normal operation is
		 *            active
		 * @param invText
		 *            text the button should display when inverse operation is
		 *            active
		 * @param op
		 *            binary operation that this object performs
		 * @param invOp
		 *            inverse binary operation that this object performs
		 */
		public BinaryOperationButton(String text, String invText, BiFunction<Double, Double, Double> op,
				BiFunction<Double, Double, Double> invOp) {
			this(text, op);
			inverse.addActionListener(e -> {
				if (inverse.isSelected()) {
					setText(invText);
					this.op = invOp;
				} else {
					setText(text);
					this.op = op;
				}
			});
		}
	}

	/**
	 * This class represents a unary operation {@link JButton}. <br>
	 * Two constructors are offered, one for operations without an inverse
	 * operation and one for operations with an inverse operation.
	 * 
	 * @author Dan
	 *
	 */
	private class UnaryOperationButton extends JButton {

		/** */
		private static final long serialVersionUID = 1L;

		/** Unary operation that this object performs. */
		private Function<Double, Double> op;

		/**
		 * Creates a new {@link UnaryOperationButton} with given arguments that
		 * has no inverse operation.
		 * 
		 * @param text
		 *            text the button should display
		 * @param op
		 *            unary operation that this object performs
		 */
		public UnaryOperationButton(String text, Function<Double, Double> op) {
			super(text);
			this.op = op;
			setComponentDesign(this);
			addActionListener(e -> {
				setScreen(this.op.apply(getScreen()));
				newInput = true;
			});
		}

		/**
		 * Creates a new {@link UnaryOperationButton} with given arguments that
		 * has an inverse operation.
		 * 
		 * @param text
		 *            text the button should display when normal operation is
		 *            active
		 * @param invText
		 *            text the button should display when inverse operation is
		 *            active
		 * @param op
		 *            unary operation that this object performs
		 * @param invOp
		 *            inverse unary operation that this object performs
		 */
		public UnaryOperationButton(String text, String invText, Function<Double, Double> op,
				Function<Double, Double> invOp) {
			this(text, op);
			inverse.addActionListener(e -> {
				if (inverse.isSelected()) {
					setText(invText);
					this.op = invOp;
				} else {
					setText(text);
					this.op = op;
				}
			});
		}
	}

}
