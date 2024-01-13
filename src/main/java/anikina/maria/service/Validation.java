package anikina.maria.service;

import anikina.maria.exceptions.InvalidInputStringFormatException;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Validation {
	public static final List<Character> SUPPORTED_CHARTERS = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '.', '+', '-', '*', '/', '(', ')', ' ');

	public boolean inputDataValidation(String exp) {
		for (char ch : exp.toCharArray()) {
			if (!SUPPORTED_CHARTERS.contains(ch)) {
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
}
