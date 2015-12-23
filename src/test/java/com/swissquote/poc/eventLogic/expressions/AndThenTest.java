package com.swissquote.poc.eventLogic.expressions;

import static com.swissquote.poc.eventLogic.expressions.Expression.andThen;
import static com.swissquote.poc.eventLogic.expressions.Helper.assertSetEquals;

import org.junit.Test;

/**
 * (c) Swissquote 12/23/15
 *
 * @author bleroux
 */
public class AndThenTest {

	@Test
	public void testGetWhenTrue() throws Exception {
		MockExpression e1 = new MockExpression("e1");
		MockExpression e2 = new MockExpression("e2");

		final AndThen expression = andThen(e1, e2);
		assertSetEquals(expression.getWhenTrue(1L));
		expression.cleanEvents();

		e1.addTrueCases(2L, 1L);
		assertSetEquals(expression.getWhenTrue(2L));
		expression.cleanEvents();

		e1.addTrueCases(2L, 1L);
		e2.addTrueCases(3L, 2L);
		assertSetEquals(expression.getWhenTrue(3L), 1L);
		expression.cleanEvents();

		e1.addTrueCases(1L, 1L); // should go back to 2L and not 1L
		e2.addTrueCases(3L, 2L);
		assertSetEquals(expression.getWhenTrue(3L));
		expression.cleanEvents();

		// 2 possible cases, 2 valid
		e1.addTrueCases(3L, 1L);
		e1.addTrueCases(6L, 2L);
		e2.addTrueCases(10L, 6L, 3L);
		assertSetEquals(expression.getWhenTrue(10L), 1L, 2L);
		expression.cleanEvents();

		// 2 possible cases, 1 valid
		e1.addTrueCases(3L, 1L);
		e2.addTrueCases(10L, 6L, 3L);
		assertSetEquals(expression.getWhenTrue(10L), 1L);
		expression.cleanEvents();


	}

}