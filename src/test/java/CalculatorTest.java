import anikina.maria.exceptions.InvalidInputStringFormatException;
import anikina.maria.service.CalculatorService;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {

	CalculatorService computingService = new CalculatorService();

	@Test
	public void mathematicalOperation_whenOperationExists_thenReturnString() {
		var number1 = "7";
		var number2 = "3";
		var operation = "--";

		assertEquals("10.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationExistsAndAnswerInfinitePeriodicFraction_thenReturnString() {
		var number1 = "7";
		var number2 = "3";
		var operation = "/";

		assertEquals(String.valueOf(7.0 / 3), computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationNotExists_thenReturnInvalidInputStringFormatException() {
		var number1 = "7";
		var number2 = "3";
		var operationNotExists = ":";

		assertThrows(InvalidInputStringFormatException.class,
				() -> computingService.mathematicalOperation(number1, number2, operationNotExists));
	}

	@Test
	public void transformOriginal_whenExpressionTransform_thenReturnString() {
		var expression = "(        -7 + 5*(-2))";

		assertEquals("(0-7+5*(0-2))", computingService.transformOriginalExpression(expression).toString());
	}

	@Test
	public void countInsideBrackets_whenExpressionIsEvaluated_thenReturnAnswer() {
			var expression = "2+5-8";

		assertEquals("-1.0", computingService.countInsideBrackets(expression));
	}

	@Test
	public void solve_whenExpressionNotBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "2+5-8";

		assertEquals("-1.0", computingService.solve(expression));
	}

	@Test
	public void solve_whenExpressionWithBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "(2+5)-8*2";

		assertEquals("-9.0", computingService.solve(expression));
	}

	@Test
	public void solve_whenExpressionWithInnerBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "-6 + ((2+5)-8*2)";

		assertEquals("-15.0", computingService.solve(expression));
	}

	@Test
	public void solve_whenExpressionWithConsecutiveBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "                          3 + (3 - 3) + (2/2 - 1)";

		assertEquals("3.0", computingService.solve(expression));
	}

	@Test
	public void solve_whenExpressionWithNegativeNumbers_thenReturnAnswer() {
		var expression = "(-3 + (3 - (-3)) + (2/2 - 1))";

		assertEquals("3.0", computingService.solve(expression));
	}

	@Test
	public void inputDataValidation_whenExpressionWithNegativeBrackets_thenReturnInvalidInputStringFormatException() {
		var expression = "())";

		assertThrows(InvalidInputStringFormatException.class,
				() -> computingService.inputDataValidation(expression));
	}

	@Test
	public void checkingCorrectnessBrackets_whenExpressionWithOkBrackets_thenReturnTrue() {
		var expression = "(())";

		assertTrue(computingService.checkingCorrectnessBrackets(expression));
	}

	@Test
	public void checkingCorrectnessBrackets_whenExpressionWithIncorrectBrackets_thenReturnFalse() {
		var expression = ")(1+6)(";

		assertFalse(computingService.checkingCorrectnessBrackets(expression));
	}

	@Test
	public void inputDataValidation_whenExpressionCorrectness_thenReturnTrue() {
		var expression = "(4+5-1)";

		assertTrue(computingService.inputDataValidation(expression));
	}

	@Test
	public void inputDataValidation_whenExpressionCorrectness_thenReturnInvalidInputStringFormatException() {
		var expression = "(b+x)";

		assertThrows(InvalidInputStringFormatException.class,
				() -> computingService.inputDataValidation(expression));
	}
}
