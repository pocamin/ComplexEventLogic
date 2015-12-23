package com.swissquote.poc.eventLogic;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public interface EventEvaluationRule {
	void add(long timestamp, boolean on);

	boolean getResult(long timestamp);

	boolean getLast(long timestamp);

	Long getPeriodLength();

	void cleanEvents();
}
