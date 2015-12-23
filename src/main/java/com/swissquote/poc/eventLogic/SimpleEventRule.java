package com.swissquote.poc.eventLogic;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class SimpleEventRule implements EventEvaluationRule {
	private final boolean expectToBe;
	private final NavigableMap<Long, Boolean> timedResults = new TreeMap<>();

	public SimpleEventRule(boolean expectToBe) {
		this.expectToBe = expectToBe;
	}

	@Override
	public void add(long timestamp, boolean eventResult) {
		timedResults.put(timestamp, (!expectToBe) ^ eventResult);
		cleanResults(timestamp);
	}

	@Override
	public boolean getResult(long timestamp) {
		return Optional.ofNullable(timedResults.floorEntry(timestamp))
				.map(Map.Entry::getValue)
				.orElse(false);
	}

	@Override
	public boolean getLast(long timestamp) {
		return timedResults.isEmpty() ? false : timedResults.lastEntry().getValue();
	}

	@Override
	public Long getPeriodLength() {
		return 0L;
	}

	@Override
	public void cleanEvents() {
		timedResults.clear();
	}

	private void cleanResults(long timestamp) {
		// TODO need the whole expression to know the bounds.
	}

}
