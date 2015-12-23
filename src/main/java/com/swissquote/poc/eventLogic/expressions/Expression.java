package com.swissquote.poc.eventLogic.expressions;

import java.util.Set;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public interface Expression {
	Set<Long> getWhenTrue(Long timestamp);

	static And and(Expression... expressions) {
		return new And(expressions);
	}

	static Or or(Expression... expressions) {
		return new Or(expressions);
	}

	static AndThen andThen(Expression expression1, Expression expression2) {
		return new AndThen(expression1, expression2);
	}

	void cleanEvents();
}
