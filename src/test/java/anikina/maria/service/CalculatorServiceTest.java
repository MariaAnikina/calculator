package anikina.maria.service;

import anikina.maria.exceptions.InvalidInputStringFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

	@Mock
	private Validation validation;
	@InjectMocks
	private CalculatorService computingService;


	@Test
	public void solve_whenExpressionNotBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "2+5-8";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("-1.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void solve_whenExpressionWithBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "(2+5)-8*2";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("-9.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void solve_whenExpressionWithInnerBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "-6 + ((2+5)-8*2)";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("-15.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void solve_whenExpressionWithConsecutiveBracketsIsEvaluated_thenReturnAnswer() {
		var expression = "                          3 + (3 - 3) + (2/2 - 1)";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("3.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void solve_whenExpressionWithNegativeNumbers_thenReturnAnswer() {
		var expression = "(-3 + (3 - (-3)) + (2/2 - 1))";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("3.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void solve_whenExpressionReadyEvaluated_thenReturnAnswer() {
		var expression = "-2";
		when(validation.inputDataValidation(anyString())).thenReturn(true);

		assertEquals("-2.0", computingService.solve(expression));
		verify(validation, times(1)).inputDataValidation(any(String.class));
	}

	@Test
	public void mathematicalOperation_whenOperationExists_thenReturnString() {
		var number1 = "7";
		var number2 = "3";
		var operation = "--";

		assertEquals("10.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationDivisionExistsAndAnswerInfinitePeriodicFraction_thenReturnString() {
		var number1 = "7";
		var number2 = "3";
		var operation = "/";

		assertEquals(String.valueOf(7.0 / 3), computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationDivisionByNegativeNumber_thenReturnString() {
		var number1 = "6";
		var number2 = "3";
		var operation = "/-";

		assertEquals("-2.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationMultiplicationByNegativeNumber_thenReturnString() {
		var number1 = "6";
		var number2 = "3";
		var operation = "*-";

		assertEquals("-18.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationMultiplication_thenReturnString() {
		var number1 = "6";
		var number2 = "3";
		var operation = "*";

		assertEquals("18.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationSum_thenReturnString() {
		var number1 = "6";
		var number2 = "3";
		var operation = "+";

		assertEquals("9.0", computingService.mathematicalOperation(number1, number2, operation));
	}

	@Test
	public void mathematicalOperation_whenOperationSubtraction_thenReturnString() {
		var number1 = "6";
		var number2 = "3";
		var operation = "-";

		assertEquals("3.0", computingService.mathematicalOperation(number1, number2, operation));
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
	public void mathematicalOperation_whenDivisionByZero_thenReturnInvalidInputStringFormatException() {
		var number1 = "7";
		var number2 = "0";
		var operationNotExists = "/";

		assertThrows(ArithmeticException.class,
				() -> computingService.mathematicalOperation(number1, number2, operationNotExists));
	}

	@Test
	public void mathematicalOperation_whenDivisionByNegativeZero_thenReturnInvalidInputStringFormatException() {
		var number1 = "7";
		var number2 = "0";
		var operationNotExists = "/-";

		assertThrows(ArithmeticException.class,
				() -> computingService.mathematicalOperation(number1, number2, operationNotExists));
	}

	@Test
	public void transformOriginal_whenExpressionTransform_thenReturnString() {
		var expression = "(        -7 + 5*(-2))";

		assertEquals("(0-7+5*(0-2))", computingService.transformOriginalExpression(expression).toString());
	}

	@Test
	public void countInsideBrackets_whenExpressionContainsAdditionAndSubtraction_thenReturnAnswer() {
			var expression = "2+5-8";

		assertEquals("-1.0", computingService.countInsideBrackets(expression));
	}

	@Test
	public void countInsideBrackets_whenExpressionContainsAllArithmeticSymbols_thenReturnAnswer() {
		var expression = "2*5-8/2+0";

		assertEquals("6.0", computingService.countInsideBrackets(expression));
	}
}
