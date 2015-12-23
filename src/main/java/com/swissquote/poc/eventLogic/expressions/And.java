package com.swissquote.poc.eventLogic.expressions;

import java.util.Collections;
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
public class And implements Expression {
	Logger logger = LoggerFactory.getLogger(And.class);

	private final Expression[] expressions;

	public And(Expression[] expressions) {
		this.expressions = expressions;
	}

	@Override
	public Set<Long> getWhenTrue(Long timestamp) {
		final Set<Long> toReturn = Stream.of(expressions)
				.map(expression -> expression.getWhenTrue(timestamp))
				.reduce((acc, whenTrue) -> whenTrue
								.stream()
								.flatMap(n -> acc.stream().map(o -> n > o ? o : n))
								.collect(Collectors.toSet())
				).orElseGet(Collections::emptySet);
		if (logger.isDebugEnabled()) {
			logger.debug(this + " at " + timestamp  +  "=" + toReturn.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));
		}

		return toReturn;
	}

	@Override
	public void cleanEvents() {
		Stream.of(expressions).forEach(Expression::cleanEvents);
	}

	@Override
	public String toString() {
		return "(" + Stream.of(expressions).map(Object::toString).collect(Collectors.joining(" && ")) + ")";
	}
}
