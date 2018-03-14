package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents one query and implements {@link IFilter} for evaluation.
 * Constructor receives a {@code String} form which is after parsing transformed
 * into {@link IConditionalExpression}.
 * 
 * @author Dan
 *
 */
public class QueryFilter implements IFilter {

	/**
	 * Conditional expression representing this query.
	 */
	private IConditionalExpression expression;

	/**
	 * Creates a new {@code QueryFilter} from given {@code String}.
	 * 
	 * @param query
	 *            {@code String} form of query
	 */
	public QueryFilter(String query) {
		if (query.isEmpty()) {
			throw new IllegalArgumentException("No expression.");
		}

		parseWithAnd(query);
	}

	/**
	 * Parses the query. Only supports multiple expressions separated with
	 * logical AND.
	 * 
	 * @param query
	 *            {@code String} form of query
	 */
	private void parseWithAnd(String query) {
		// special case
		if (query.toLowerCase().endsWith("and")) {
			throw new IllegalArgumentException("Invalid query expression.");
		}

		// split into expressions
		String[] expressions = query.split("\\b(?i)and\\b");

		if (expressions.length == 1) {
			expression = parseExpression(expressions[0]);

		} else {
			expression = new ConditionalExpressionWithAnd();

			for (String expr : expressions) {
				expression.addExpression(parseExpression(expr.trim()));
			}
		}

	}

	/**
	 * Parses one conditional expression form {@code String}.
	 * 
	 * @param expression
	 *            {@code String} form of expression
	 * @return new {@link ConditionalExpression}
	 */
	private ConditionalExpression parseExpression(String expression) {
		Matcher matcher = Pattern.compile("(\\w+)\\s*([=!<>]+)\\s*\"(\\w+)\"", Pattern.UNICODE_CHARACTER_CLASS)
				.matcher(expression);
		Matcher matcherLike = Pattern.compile("(\\w+)\\s+LIKE\\s+\"(\\w*\\*?\\w*)\"", Pattern.UNICODE_CHARACTER_CLASS)
				.matcher(expression);

		String fieldName, operator, literal;
		IFieldValueGetter fieldValueGetter = null;
		IComparisonOperator comparisonOperator = null;

		if (matcher.matches()) {
			fieldName = matcher.group(1);
			operator = matcher.group(2);
			literal = matcher.group(3);

			comparisonOperator = ComparisonOperators.getComparisonOperator(operator);

		} else if (matcherLike.matches()) {
			fieldName = matcherLike.group(1);
			literal = matcherLike.group(2);
			
			if(literal.isEmpty()){
				throw new IllegalArgumentException("Provide value for comparison.");
			}

			comparisonOperator = ComparisonOperators.getComparisonOperator("LIKE");

		} else {
			throw new IllegalArgumentException("Invalid query expression.");
		}

		fieldValueGetter = FieldValueGetters.getFieldValueGetter(fieldName);

		try {
			return new ConditionalExpression(fieldValueGetter, literal, comparisonOperator);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Invalid query expression.");
		}
	}

	@Override
	public boolean accepts(StudentRecord record) {
		return expression.evaluate(record);
	}

}
