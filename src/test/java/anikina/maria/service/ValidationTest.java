package anikina.maria.service;

import anikina.maria.exceptions.InvalidInputStringFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
	private static Validation validation;

	@BeforeAll
	static void init() {
		validation = new Validation();
	}

	@Test
	public void inputDataValidation_whenExpressionWithNegativeBrackets_thenReturnInvalidInputStringFormatException() {
		var expression = "())";

		assertThrows(InvalidInputStringFormatException.class,
				() -> validation.inputDataValidation(expression));
	}

	@Test
	public void checkingCorrectnessBrackets_whenExpressionWithOkBrackets_thenReturnTrue() {
		var expression = "(())";

		assertTrue(validation.checkingCorrectnessBrackets(expression));
	}

	@Test
	public void checkingCorrectnessBrackets_whenExpressionWithIncorrectBrackets_thenReturnFalse() {
		var expression = ")(1+6)(";

		assertFalse(validation.checkingCorrectnessBrackets(expression));
	}

	@Test
	public void inputDataValidation_whenExpressionCorrectness_thenReturnTrue() {
		var expression = "(4+5-1)";

		assertTrue(validation.inputDataValidation(expression));
	}

	@Test
	public void inputDataValidation_whenExpressionCorrectness_thenReturnInvalidInputStringFormatException() {
		var expression = "(b+x)";

		assertThrows(InvalidInputStringFormatException.class,
				() -> validation.inputDataValidation(expression));
	}
}