package org.cellularautomaton.space.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cellularautomaton.cell.ICell;
import org.cellularautomaton.space.BadFileContentException;
import org.cellularautomaton.util.Coords;

public class ExpressionHelper {

	private final static String regexRef = "(%[0-9]+%)";
	private final static String regexCell = "(\\([+-]?[0-9]+(,[+-]?[0-9]+)*\\))";
	private final static String regexCellSet = String.format("%s(\\+%s)*",
			regexCell, regexCell);
	private static String regexValue;
	private static String regexValueSet;
	private static String regexComparison;
	private static String regexExpression;
	private static String regexAndChain;
	private static String regexOrChain;
	private static String regexLeafParenthesis;

	public static Expression parseRulePart(String description,
			Collection<Character> states) {
		initRegex(states);

		description = description.replaceAll("\\s", "");
		final List<Expression> expressionBuffer = new ArrayList<Expression>();

		// compute expression
		while (!description.matches(regexRef)) {
			Matcher parenthesisMatcher = Pattern.compile(regexLeafParenthesis)
					.matcher(description);
			Matcher andChainMatcher = Pattern.compile(regexAndChain).matcher(
					description);
			Matcher orChainMatcher = Pattern.compile(regexOrChain).matcher(
					description);

			// explicit priority : parenthesis
			if (parenthesisMatcher.find()) {
				String extract = parenthesisMatcher.group();
				description = description.replaceAll(Pattern.quote(extract),
						"%" + expressionBuffer.size() + "%");
				extract = extract.substring(1, extract.length() - 1);
				expressionBuffer.add(parseRulePart(extract, states));
			}
			// high implicit priority : and
			else if (andChainMatcher.find()) {
				String extract = andChainMatcher.group();
				description = description.replaceAll(Pattern.quote(extract),
						"%" + expressionBuffer.size() + "%");
				AndExpression expression = new AndExpression();
				expressionBuffer.add(expression);
				for (String component : extract.split("&")) {
					if (component.matches(regexComparison)) {
						expression.addExpression(parseRulePart(component,
								states));
					} else if (component.matches(regexRef)) {
						expression.addExpression(expressionBuffer.get(Integer
								.parseInt(component.replaceAll("%", ""))));
					} else {
						throw new BadFileContentException(
								"unreadable statement : " + component);
					}
				}
			}
			// low implicit priority : or
			else if (orChainMatcher.find()) {
				String extract = orChainMatcher.group();
				description = description.replaceAll(Pattern.quote(extract),
						"%" + expressionBuffer.size() + "%");
				OrExpression expression = new OrExpression();
				expressionBuffer.add(expression);
				for (String component : extract.split("\\|")) {
					if (component.matches(regexComparison)) {
						expression.addExpression(parseRulePart(component,
								states));
					} else if (component.matches(regexRef)) {
						expression.addExpression(expressionBuffer.get(Integer
								.parseInt(component.replaceAll("%", ""))));
					} else {
						throw new BadFileContentException(
								"unreadable statement in AND chain : "
										+ component);
					}
				}
			}
			// last priority : cell=value
			else if (description.matches(regexComparison)) {
				String[] splitExpression = description.split("=");
				description = "%" + expressionBuffer.size() + "%";
				CellComparisonExpression expression = new CellComparisonExpression();
				for (String target : splitExpression[0].substring(1,
						splitExpression[0].length() - 1).split("\\)\\+\\(")) {
					expression.addTarget(new Coords("(" + target + ")"));
				}
				for (String constraint : splitExpression[1].split("\\+")) {
					if (constraint.matches(regexValue)) {
						constraint = "1" + constraint;
					}
					Character state = constraint
							.charAt(constraint.length() - 1);
					Integer count = Integer.parseInt(constraint.substring(0,
							constraint.length() - 1));
					expression.addConstraint(state, count);
				}
				expressionBuffer.add(expression);
			}
			// unrecognized statement
			else {
				throw new BadFileContentException("unreadable statement : "
						+ description);
			}
		}
		return expressionBuffer.get(Integer.parseInt(description.replaceAll(
				"%", "")));
	}

	private static void initRegex(Collection<Character> states) {
		regexValue = getValueRegexFor(states);
		regexValueSet = String.format("([0-9]*%s(\\+[0-9]*%s)*)", regexValue,
				regexValue);
		regexComparison = String.format("(%s=%s)", regexCellSet, regexValueSet);
		regexExpression = String.format("(%s|%s)", regexComparison, regexRef);
		regexAndChain = String.format("(%s(&%s)+)", regexExpression,
				regexExpression);
		regexOrChain = String.format("(%s(\\|%s)+)", regexExpression,
				regexExpression);
		regexLeafParenthesis = String.format("(\\(%s([&|]%s)*\\))",
				regexExpression, regexExpression);
	}

	public static void setExpressionForOriginCell(Expression expression,
			ICell<Character> origin) {
		if (expression instanceof ComposedExpression) {
			for (Expression subexpression : ((ComposedExpression) expression)
					.getExpressions()) {
				setExpressionForOriginCell(subexpression, origin);
			}
		} else if (expression instanceof CellComparisonExpression) {
			((CellComparisonExpression) expression).setOrigin(origin);
		}
	}

	private static String getValueRegexFor(Collection<Character> states) {
		String regexValue = "";
		for (Character state : states) {
			regexValue += "|" + Pattern.quote(state.toString());
		}
		regexValue = "(" + regexValue.substring(1) + ")";
		return regexValue;
	}
}
