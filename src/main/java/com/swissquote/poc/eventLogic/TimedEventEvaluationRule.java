package com.swissquote.poc.eventLogic;

import java.time.Duration;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class TimedEventEvaluationRule extends SimpleEventRule {
	private final Deque<EventEvaluationResult> individualResults = new LinkedList<>();
	private int trueCount;
	private int totalCount;
	private long firstEventTimestamp;
	private final long durationInMs;
	private final CountTrueBasedEvaluation evaluation;

	public TimedEventEvaluationRule(Duration duration, CountTrueBasedEvaluation evaluation) {
		super(true);
		this.evaluation = evaluation;
		this.durationInMs = duration.toMillis();
	}

	@Override
	public void add(long timestamp, boolean eventResult) {
		addToIndividualResults(timestamp, eventResult);
		cleanIndividualResults(timestamp);
		addToTimedResults(timestamp);
	}

	@Override
	public Long getPeriodLength() {
		return durationInMs;
	}

	@Override
	public void cleanEvents() {
		trueCount = 0;
		totalCount = 0;
		individualResults.clear();
		super.cleanEvents();
	}

	private void addToTimedResults(long timestamp) {
		if (firstEventTimestamp + durationInMs <= timestamp) {
			super.add(timestamp, evaluation.evaluate(trueCount, totalCount));
		}
	}

	private void addToIndividualResults(long timestamp, boolean eventResult) {
		if (individualResults.isEmpty()) {
			firstEventTimestamp = timestamp;
		}

		totalCount++;
		if (eventResult) {
			trueCount++;
		}
		individualResults.add(new EventEvaluationResult(timestamp, eventResult));
	}

	protected void cleanIndividualResults(long timestamp) {
		long minTime = timestamp - durationInMs;
		for (Iterator<EventEvaluationResult> iterator = individualResults.iterator(); iterator.hasNext(); ) {
			EventEvaluationResult oldResult = iterator.next();
			if (oldResult.timestamp >= minTime) {
				break;
			} else {
				iterator.remove();
				if (oldResult.result) {
					trueCount--;
				}
				totalCount--;
			}
		}
	}

	@FunctionalInterface
	public interface CountTrueBasedEvaluation {
		boolean evaluate(int trueCount, int totalCount);
	}
}
