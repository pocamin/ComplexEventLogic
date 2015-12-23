package com.swissquote.poc.eventLogic;

public class EventEvaluationResult {
	final long timestamp;
	final boolean result;

	public EventEvaluationResult(long timestamp, boolean result) {
		this.timestamp = timestamp;
		this.result = result;
	}
}
