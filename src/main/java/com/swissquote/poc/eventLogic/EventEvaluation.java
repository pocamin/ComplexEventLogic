package com.swissquote.poc.eventLogic;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
@FunctionalInterface
public interface EventEvaluation<E> {
	boolean on(E event);
}
