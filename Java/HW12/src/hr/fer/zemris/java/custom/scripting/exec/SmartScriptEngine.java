package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * {@code SmartScriptEngine} executes the document produced by
 * {@code SmartScriptParser}.<br>
 *
 * @author Dan
 * @see SmartScriptParser
 * @see RequestContext
 */
public class SmartScriptEngine {

	/** {@code DocumentNode} containing the parsed document. */
	private DocumentNode documentNode;

	/** Contains context and {@code OutputStream} for this document. */
	private RequestContext requestContext;

	/** {@code ObjectMultistack} used for executing this document. */
	private ObjectMultistack multistack;

	/**
	 * Implementation of {@code INodeVisitor} responsible for executing every
	 * {@code Node}.
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().getName();
			Object startValue = getElementValue(node.getStartExpression());
			Object stepValue = node.getStepExpression() == null ? 1 : getElementValue(node.getStepExpression());
			Object endValue = getElementValue(node.getEndExpression());
			multistack.push(variable, new ValueWrapper(startValue));

			while (multistack.peek(variable).numCompare(endValue) <= 0) {
				int n = node.numberOfChildren();
				for (int i = 0; i < n; i++) {
					node.getChild(i).accept(this);
				}

				multistack.peek(variable).increment(stepValue);
			}

			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();

			for (Element element : node.getElements()) {
				try {
					stack.push(getElementValue(element));
					continue;
				} catch (UnsupportedOperationException e) {
				}

				if (element instanceof ElementVariable) {
					String var = ((ElementVariable) element).getName();
					ValueWrapper value = multistack.peek(var);
					if (value == null) {
						throw new NullPointerException("Variable doesn't exist: " + var);
					}
					stack.push(value.getValue());

				} else if (element instanceof ElementOperator) {
					Object value2 = stack.pop();
					Object value1 = stack.pop();
					stack.push(calculateOperation(value1, value2, ((ElementOperator) element).getSymbol()));

				} else if (element instanceof ElementFunction) {
					executeFunction(((ElementFunction) element).getName(), stack);

				} else {
					throw new UnsupportedOperationException("Invalid element type.");
				}
			}

			if (!stack.isEmpty()) {
				stack.forEach(o -> {
					try {
						requestContext.write(o.toString());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int n = node.numberOfChildren();
			for (int i = 0; i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

		/**
		 * If given {@code Element} is a constant (number or {@code String})
		 * than its value is returned.
		 * 
		 * @param e
		 *            {@code Element} whose value is returned
		 * @return value of the {@code Element}
		 * @throws UnsupportedOperationException
		 *             if given {@code Element} is not a constant
		 */
		private Object getElementValue(Element e) {
			if (e instanceof ElementConstantInteger) {
				return ((ElementConstantInteger) e).getValue();
			}
			if (e instanceof ElementConstantDouble) {
				return ((ElementConstantDouble) e).getValue();
			}
			if (e instanceof ElementString) {
				return ((ElementString) e).getValue();
			}
			throw new UnsupportedOperationException("Given element type contains no value.");
		}

		/**
		 * Calculates supported binary operations and returns the result.
		 * 
		 * @param value1
		 *            first operand
		 * @param value2
		 *            second operand
		 * @param operator
		 *            binary operation
		 * @return result of the operation
		 * @throws UnsupportedOperationException
		 *             if the {@code operator} is not supported
		 */
		private Object calculateOperation(Object value1, Object value2, String operator) {
			ValueWrapper result = new ValueWrapper(value1);
			switch (operator) {
			case "+":
				result.increment(value2);
				break;
			case "-":
				result.decrement(value2);
				break;
			case "*":
				result.multiply(value2);
				break;
			case "/":
				result.divide(value2);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported operator: " + operator);
			}

			return result.getValue();
		}

		/**
		 * Executes the given function.
		 * 
		 * @param function
		 *            function to execute
		 * @param stack
		 *            {@code Stack} for getting parameters and storing results
		 */
		private void executeFunction(String function, Stack<Object> stack) {
			switch (function) {
			case "sin":
				stack.push(Math.sin(getAsDouble(stack.pop()) * Math.PI / 180));
				break;
			case "decfmt":
				DecimalFormat f = new DecimalFormat((String) stack.pop());
				stack.push(f.format(getAsDouble(stack.pop())));
				break;
			case "dup":
				stack.push(stack.peek());
				break;
			case "swap":
				Object a = stack.pop();
				Object b = stack.pop();
				stack.push(a);
				stack.push(b);
				break;
			case "setMimeType":
				requestContext.setMimeType((String) stack.pop());
				break;
			case "paramGet":
				Object defValue = stack.pop();
				Object value = requestContext.getParameter((String) stack.pop());
				stack.push(value == null ? defValue : value);
				break;
			case "pparamGet":
				defValue = stack.pop();
				value = requestContext.getPersistentParameter((String) stack.pop());
				stack.push(value == null ? defValue : value);
				break;
			case "pparamSet":
				requestContext.setPersistentParameter((String) stack.pop(), stack.pop().toString());
				break;
			case "pparamDel":
				requestContext.removePersistentParameter((String) stack.pop());
				break;
			case "tparamGet":
				defValue = stack.pop();
				value = requestContext.getTemporaryParameter((String) stack.pop());
				stack.push(value == null ? defValue : value);
				break;
			case "tparamSet":
				requestContext.setTemporaryParameter((String) stack.pop(), stack.pop().toString());
				break;
			case "tparamDel":
				requestContext.removeTemporaryParameter((String) stack.pop());
				break;
			default:
				throw new UnsupportedOperationException("Unsupported function: " + function);
			}
		}

		/**
		 * Converts the given {@code Object} to a {@code Double} if possible.
		 * 
		 * @param value
		 *            value to convert
		 * @return {@code Double} value of the {@code value}
		 * @throws IllegalArgumentException
		 *             if {@code value} is not convertible to a {@code Double}
		 */
		private Double getAsDouble(Object value) {
			if (value instanceof Double) {
				return (Double) value;
			} else if (value instanceof Integer) {
				return ((Integer) value).doubleValue();
			} else if (value instanceof String) {
				return Double.parseDouble((String) value);
			} else {
				throw new IllegalArgumentException("Given object cannot be converted to Double!");
			}
		}
	};

	/**
	 * Creates a new {@code SmartScriptEngine} with given arguments.
	 * 
	 * @param documentNode
	 *            parsed document to execute
	 * @param requestContext
	 *            context for executing the document
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(documentNode, "Document cannot be null!");
		this.requestContext = Objects.requireNonNull(requestContext, "Context cannot be null!");

		multistack = new ObjectMultistack();
	}

	/**
	 * Starts the execution of the document given in the constructor.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
