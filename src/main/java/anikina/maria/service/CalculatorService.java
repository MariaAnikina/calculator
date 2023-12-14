package anikina.maria.service;

import anikina.maria.exceptions.InvalidInputStringFormatException;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CalculatorService {
	public final List<Character> supportedCharacters = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '.', '+', '-', '*', '/', '(', ')', ' ');
	public String solve(String expression) {
		expression = "(" + expression + ")";
		String answer = null;
		StringBuilder expressionBuilder = new StringBuilder(expression);
		StringBuilder exp = transformOriginalExpression(expression);
		inputDataValidation(exp.toString());
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

	public boolean inputDataValidation(String exp) {
		for (char ch : exp.toCharArray()) {
			if (!supportedCharacters.contains(ch)) {
				throw new InvalidInputStringFormatException("Выражение содержит недопустимые символы");
			}
		}
		if (!checkingCorrectnessBrackets(exp)) {
			throw new InvalidInputStringFormatException("Скобки расставлены неккоректно");
		}
		return true;
	}

	public boolean checkingCorrectnessBrackets(String exp) {
		List<String> bracketsCharacters = Arrays.stream(exp.split(""))
				.filter(ch -> ch.equals("(") || ch.equals(")"))
				.collect(Collectors.toList());
		Deque<String> stack = new LinkedList<>();
		for (String bracket : bracketsCharacters) {
			if (bracket.equals("(")) {
				stack.push(bracket);
			} else if (bracket.equals(")")) {
				if (stack.isEmpty() || !stack.pop().equals("(")) {
					return false;
				}
			}
		}
		return stack.isEmpty();
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
		return new StringBuilder(expressionNew.toString());
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
				doubleBiFunction = (d1, d2) -> d1 / d2;
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
				doubleBiFunction = (d1, d2) -> d1 / d2 * (-1);
				break;
			default:
				throw new InvalidInputStringFormatException("Unexpected value: " + operation);
		}
		return doubleBiFunction.apply(Double.parseDouble(number1), Double.parseDouble(number2)).toString();
	}
}
