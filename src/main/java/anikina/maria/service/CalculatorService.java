package anikina.maria.service;

import anikina.maria.exceptions.InvalidInputStringFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CalculatorService {
	private final Validation validation;

	public CalculatorService(Validation validation) {
		this.validation = validation;
	}

	public String solve(String expression) {
		expression = "(" + expression + ")";
		String answer = null;
		StringBuilder expressionBuilder = new StringBuilder(expression);
		StringBuilder exp = transformOriginalExpression(expression);
		validation.inputDataValidation(exp.toString());
		int numberOfBrackets = 0;
		while (expressionBuilder.indexOf("(") != -1) {
			expressionBuilder.replace(expressionBuilder.indexOf("("), expressionBuilder.indexOf("(") + 1, "");
			numberOfBrackets++;
			while (numberOfBrackets != 0) {
				int indexLastOpenBracket = exp.lastIndexOf("(");
				int indexFirstCloseParenthesis = indexLastOpenBracket + exp.substring(exp.lastIndexOf("(")).indexOf(")");
				String substring = exp
						.substring(
								indexLastOpenBracket + 1, indexFirstCloseParenthesis
						)
						.replaceAll(" ", ""
						);
				answer = countInsideBrackets(substring);
				exp.delete(indexLastOpenBracket, indexLastOpenBracket + exp.substring(exp.lastIndexOf("(")).indexOf(")") + 1);
				exp.insert(indexLastOpenBracket, answer);
				exp = transformOriginalExpression(exp.toString());
				numberOfBrackets--;
			}
		}
		return answer;
	}

	public StringBuilder transformOriginalExpression(String exp) {
		exp = exp.replace(" ", "");
		StringBuilder expressionNew = new StringBuilder();
		for (int i = 0; i < exp.length(); i++) {
			char symbol = exp.replace(" ", "").charAt(i);
			if (symbol == '-') {
				if (exp.indexOf(symbol) == 1) {
					expressionNew.append("0");
				}
				if (exp.indexOf(symbol - 1) == '(') {
					expressionNew.append("0");
				}
			}
			expressionNew.append(symbol);
		}
		return expressionNew;
	}

	public String countInsideBrackets(String substring) {
		List<String> numbers = Arrays.stream(substring.split("[^0-9\\.]"))
				.filter(s -> !s.equals(""))
				.collect(Collectors.toList());
		List<String> signs = Arrays.stream(substring.split("[0-9\\.]+"))
				.filter(s -> !s.equals(""))
				.map(String::trim).collect(Collectors.toList());
		while (signs.contains("/") || signs.contains("*")) {
			int currentActionIndex;
			if (signs.contains("/") && signs.contains("*")) {
				currentActionIndex = Math.min(signs.indexOf("/"), signs.indexOf("*"));
			} else if (signs.contains("/")) {
				currentActionIndex = signs.indexOf("/");
			} else {
				currentActionIndex = signs.indexOf("*");
			}
			String response = mathematicalOperation(numbers.get(currentActionIndex), numbers.get(currentActionIndex + 1),
					signs.get(currentActionIndex));
			numbers.set(currentActionIndex, response);
			numbers.remove(currentActionIndex + 1);
			signs.remove(currentActionIndex);
		}
		while (numbers.size() != 1) {
			String response = mathematicalOperation(numbers.get(0), numbers.get(1),
					signs.get(0));
			numbers.set(0, response);
			numbers.remove(1);
			signs.remove(0);
		}
		return numbers.get(0);
	}

	public String mathematicalOperation(String number1, String number2, String operation) {
		BiFunction<Double, Double, Double> doubleBiFunction;
		switch (operation) {
			case "*":
				doubleBiFunction = (d1, d2) -> d1 * d2;
				break;
			case "/":
				if (Double.parseDouble(number2) != 0.0) {
					doubleBiFunction = (d1, d2) -> d1 / d2;
				} else {
					throw new ArithmeticException("Делить на ноль нельзя(");
				}
				break;
			case "+":
			case "--":
				doubleBiFunction = Double::sum;
				break;
			case "-":
			case "+-":
				doubleBiFunction = (d1, d2) -> d1 - d2;
				break;
			case "*-":
				doubleBiFunction = (d1, d2) -> d1 * d2 * (-1);
				break;
			case "/-":
				if (Double.parseDouble(number2) != 0.0) {
					doubleBiFunction = (d1, d2) -> d1 / d2 * (-1);
				} else {
					throw new ArithmeticException("Делить на ноль нельзя(");
				}
				break;
			default:
				throw new InvalidInputStringFormatException("Unexpected value: " + operation);
		}
		return doubleBiFunction.apply(Double.parseDouble(number1), Double.parseDouble(number2)).toString();
	}
}
