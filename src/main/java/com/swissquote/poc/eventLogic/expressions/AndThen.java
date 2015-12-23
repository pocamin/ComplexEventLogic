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
public class AndThen implements Expression {
	Logger logger = LoggerFactory.getLogger(AndThen.class);
	Expression expressionFrom;
	Expression expressionTo;

	public AndThen(Expression expressionFrom, Expression expressionTo) {
		this.expressionFrom = expressionFrom;
		this.expressionTo = expressionTo;
	}

	@Override
	public Set<Long> getWhenTrue(Long timestamp) {
		final Set<Long> toReturn = expressionTo.getWhenTrue(timestamp).stream().flatMap(
				toResult -> expressionFrom
						.getWhenTrue(toResult).stream()
		).collect(Collectors.toSet());

		if (logger.isDebugEnabled()) {
			logger.debug(this + " at " + timestamp + "=" + toReturn.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));
		}

		return toReturn;

	}

	@Override
	public void cleanEvents() {
		Stream.of(expressionFrom, expressionTo).forEach(Expression::cleanEvents);
	}

	@Override
	public String toString() {
		return expressionFrom + "->" + expressionTo;
	}
}
