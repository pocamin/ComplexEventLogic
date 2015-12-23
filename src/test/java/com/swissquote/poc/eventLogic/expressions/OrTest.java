package com.swissquote.poc.eventLogic.expressions;

import static com.swissquote.poc.eventLogic.expressions.Expression.or;
import static com.swissquote.poc.eventLogic.expressions.Helper.assertSetEquals;

import org.junit.Test;

/**
 * (c) Swissquote 12/23/15
 *
 * @author bleroux
 */
public class OrTest {

	@Test
	public void testGetWhenTrue() throws Exception {
		MockExpression e1 = new MockExpression("e1");
		MockExpression e2 = new MockExpression("e2");
		assertSetEquals(or(e1, e2).getWhenTrue(1L));

		e1.addTrueCases(2L, 1L);
		assertSetEquals(or(e1, e2).getWhenTrue(2L), 1L);

		e1.addTrueCases(3L, 1L);
		e2.addTrueCases(3L, 2L);
		assertSetEquals(or(e1, e2).getWhenTrue(3L), 1L, 2L);

		e1.addTrueCases(10L, 10L, 2L);
		e2.addTrueCases(10L, 6L, 3L);
		assertSetEquals(or(e1, e2).getWhenTrue(10L), 10L, 6L, 2L, 3L);

	}

}