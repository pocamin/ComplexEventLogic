package com.swissquote.poc.eventLogic.expressions;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class Or implements Expression {
	Logger logger = LoggerFactory.getLogger(Or.class);
	private final Expression[] expressions;

	public Or(Expression[] expressions) {
		this.expressions = expressions;
	}

	@Override
	public Set<Long> getWhenTrue(Long timestamp) {
		final Set<Long> toReturn =
				Stream.of(expressions).flatMap(expression -> expression.getWhenTrue(timestamp).stream()).collect(Collectors.toSet());
		if (logger.isDebugEnabled()) {
			logger.debug(this + " at " + timestamp  + "=" + toReturn.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));
		}
		return toReturn;
	}

	@Override
	public void cleanEvents() {
		Stream.of(expressions).forEach(Expression::cleanEvents);
	}

	@Override
	public String toString() {
		return "(" + Stream.of(expressions).map(Object::toString).collect(Collectors.joining(" || ")) + ")";
	}
}
