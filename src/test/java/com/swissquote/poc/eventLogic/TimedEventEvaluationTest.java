package com.swissquote.poc.eventLogic;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class TimedEventEvaluationTest {

	@Test
	public void testIsTrue() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.isTrue();
		evaluation.proceedEvent(1, "ko");
		Assert.assertFalse(evaluation.getResult(1));
		Assert.assertFalse(evaluation.getResult(2));
		evaluation.proceedEvent(2, "ko");
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		evaluation.proceedEvent(3, "ok");
		Assert.assertFalse(evaluation.getResult(1));
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertTrue(evaluation.getResult(3));
		Assert.assertTrue(evaluation.getResult(4));
	}

	@Test
	public void testIsFalse() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.isFalse();
		evaluation.proceedEvent(1, "ko");
		Assert.assertTrue(evaluation.getResult(1));
		Assert.assertTrue(evaluation.getResult(2));
		evaluation.proceedEvent(2, "ko");
		Assert.assertTrue(evaluation.getResult(2));
		Assert.assertTrue(evaluation.getResult(3));
		evaluation.proceedEvent(3, "ok");
		Assert.assertTrue(evaluation.getResult(1));
		Assert.assertTrue(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		Assert.assertFalse(evaluation.getResult(4));
	}

	@Test
	public void testAlwaysTrueDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.alwaysTrueDuring(Duration.of(2, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ko");
		Assert.assertFalse(evaluation.getResult(1));
		evaluation.proceedEvent(2, "ok");
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		evaluation.proceedEvent(3, "ok");
		Assert.assertFalse(evaluation.getResult(3));
		Assert.assertFalse(evaluation.getResult(4));
		evaluation.proceedEvent(4, "ok");
		Assert.assertTrue(evaluation.getResult(4));
		Assert.assertTrue(evaluation.getResult(5));
		evaluation.proceedEvent(5, "ko");
		Assert.assertFalse(evaluation.getResult(1));
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		Assert.assertTrue(evaluation.getResult(4));
		Assert.assertFalse(evaluation.getResult(5));
	}

	@Test
	public void testAlwaysFalseDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.alwaysFalseDuring(Duration.of(2, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ok");
		Assert.assertFalse(evaluation.getResult(1));
		evaluation.proceedEvent(2, "ko");
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		evaluation.proceedEvent(3, "ko");
		Assert.assertFalse(evaluation.getResult(3));
		Assert.assertFalse(evaluation.getResult(4));
		evaluation.proceedEvent(4, "ko");
		Assert.assertTrue(evaluation.getResult(4));
		Assert.assertTrue(evaluation.getResult(5));
		evaluation.proceedEvent(5, "ok");
		Assert.assertFalse(evaluation.getResult(1));
		Assert.assertFalse(evaluation.getResult(2));
		Assert.assertFalse(evaluation.getResult(3));
		Assert.assertTrue(evaluation.getResult(4));
		Assert.assertFalse(evaluation.getResult(5));
	}

	@Test
	public void testAtLeastOnceTrueDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.atLeastOnceTrueDuring(Duration.of(2, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ko");
		Assert.assertFalse(evaluation.getResult(1));
		evaluation.proceedEvent(3, "ok");
		Assert.assertTrue(evaluation.getResult(3));
		evaluation.proceedEvent(4, "ko");
		Assert.assertTrue(evaluation.getResult(4));
		evaluation.proceedEvent(6, "ko");
		Assert.assertFalse(evaluation.getResult(6));
	}

	@Test
	public void testAtLeastOnceFalseDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.atLeastOnceFalseDuring(Duration.of(2, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ok");
		Assert.assertFalse(evaluation.getResult(1));
		evaluation.proceedEvent(3, "ko");
		Assert.assertTrue(evaluation.getResult(3));
		evaluation.proceedEvent(4, "ok");
		Assert.assertTrue(evaluation.getResult(4));
		evaluation.proceedEvent(6, "ok");
		Assert.assertFalse(evaluation.getResult(6));
	}

	@Test
	public void testPercentTrueGreaterOrEqualThanDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.percentTrueGreaterOrEqualThanDuring(50, Duration.of(3, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ko");
		evaluation.proceedEvent(2, "ko");
		evaluation.proceedEvent(3, "ko");
		evaluation.proceedEvent(4, "ko");
		Assert.assertFalse(evaluation.getResult(4));
		evaluation.proceedEvent(5, "ok");
		Assert.assertFalse(evaluation.getResult(5));
		evaluation.proceedEvent(6, "ok");
		Assert.assertTrue(evaluation.getResult(6));
		evaluation.proceedEvent(7, "ok");
		Assert.assertTrue(evaluation.getResult(7));
		evaluation.proceedEvent(8, "ko");
		Assert.assertTrue(evaluation.getResult(8));
		evaluation.proceedEvent(9, "ko");
		Assert.assertTrue(evaluation.getResult(9));
		evaluation.proceedEvent(10, "ko");
		Assert.assertFalse(evaluation.getResult(10));
	}

	@Test
	public void testPercentFalseGreaterOrEqualThanDuring() throws Exception {
		TimedEventEvaluation<String> evaluation = new TimedEventEvaluation<>("ok"::equalsIgnoreCase)
				.percentFalseGreaterOrEqualThanDuring(50, Duration.of(3, ChronoUnit.MILLIS));
		evaluation.proceedEvent(1, "ok");
		evaluation.proceedEvent(2, "ok");
		evaluation.proceedEvent(3, "ok");
		evaluation.proceedEvent(4, "ok");
		Assert.assertFalse(evaluation.getResult(4));
		evaluation.proceedEvent(5, "ko");
		Assert.assertFalse(evaluation.getResult(5));
		evaluation.proceedEvent(6, "ko");
		Assert.assertTrue(evaluation.getResult(6));
		evaluation.proceedEvent(7, "ko");
		Assert.assertTrue(evaluation.getResult(7));
		evaluation.proceedEvent(8, "ok");
		Assert.assertTrue(evaluation.getResult(8));
		evaluation.proceedEvent(9, "ok");
		Assert.assertTrue(evaluation.getResult(9));
		evaluation.proceedEvent(10, "ok");
		Assert.assertFalse(evaluation.getResult(10));
	}
}