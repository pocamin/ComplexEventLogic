package com.swissquote.poc.eventLogic;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swissquote.poc.eventLogic.expressions.Expression;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class TimedEventEvaluation<E> implements Expression {
	Logger logger = LoggerFactory.getLogger(TimedEventEvaluation.class);
	private final EventEvaluation<E> eventEvaluation;
	private EventEvaluationRule eventEvaluationRule = new SimpleEventRule(true);
	private String name;

	public void proceedEvent(long timestamp, E event) {
		eventEvaluationRule.add(timestamp, eventEvaluation.on(event));
	}

	public TimedEventEvaluation(EventEvaluation<E> eventEvaluation) {
		this.eventEvaluation = eventEvaluation;
	}

	public boolean getResult(long timestamp) {
		return eventEvaluationRule.getResult(timestamp);
	}

	public TimedEventEvaluation<E> isTrue() {
		setEventEvaluationRule(new SimpleEventRule(true));
		return this;
	}

	public TimedEventEvaluation<E> isFalse() {
		setEventEvaluationRule(new SimpleEventRule(false));
		return this;
	}

	public TimedEventEvaluation<E> alwaysTrueDuring(Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, (trueCount, total) -> trueCount == total));
		return this;
	}

	public TimedEventEvaluation<E> alwaysFalseDuring(Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, (trueCount, total) -> trueCount == 0));
		return this;
	}

	public TimedEventEvaluation<E> atLeastOnceTrueDuring(Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, (trueCount, total) -> trueCount > 0));
		return this;
	}

	public TimedEventEvaluation<E> atLeastOnceFalseDuring(Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, (trueCount, total) -> trueCount < total));
		return this;
	}

	public TimedEventEvaluation<E> percentTrueGreaterOrEqualThanDuring(double percent, Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, //
				(trueCount, total) -> total != 0 && (100. * trueCount) / total >= percent));
		return this;
	}

	public TimedEventEvaluation<E> percentFalseGreaterOrEqualThanDuring(double percent, Duration duration) {
		setEventEvaluationRule(new TimedEventEvaluationRule(duration, //
				(trueCount, total) -> total != 0 && (100 - (100. * trueCount) / total >= percent)));
		return this;
	}

	private void setEventEvaluationRule(EventEvaluationRule eventEvaluationRule) {
		this.eventEvaluationRule = eventEvaluationRule;
	}

	@Override
	public Set<Long> getWhenTrue(Long timestamp) {
		final Set<Long> toReturn;
		if (eventEvaluationRule.getResult(timestamp)) {
			toReturn = Collections.singleton(timestamp - eventEvaluationRule.getPeriodLength());
		} else {
			toReturn = Collections.emptySet();
		}
		if (logger.isDebugEnabled()) {
			logger.debug(this + " at " + timestamp  +  "=" + toReturn.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")));
		}
		return toReturn;
	}

	@Override
	public void cleanEvents() {
		eventEvaluationRule.cleanEvents();

	}

	public TimedEventEvaluation<E> named(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return name == null ? "?" : name;
	}
}
