package com.swissquote.poc.eventLogic.expressions;

import static com.swissquote.poc.eventLogic.expressions.Expression.and;
import static com.swissquote.poc.eventLogic.expressions.Expression.andThen;
import static com.swissquote.poc.eventLogic.expressions.Expression.or;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.Duration;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swissquote.poc.eventLogic.TimedEventEvaluation;

import ch.qos.logback.classic.Level;

/**
 * (c) Swissquote 12/21/15
 *
 * @author bleroux
 */
public class ExpressionTest {
	Logger logger = LoggerFactory.getLogger(AndThen.class);

	/**
	 * A and then (b and c)
	 */
	@Test
	public void A_and_then_B_and_C() {
		TimedEventEvaluation<Boolean> a = new TimedEventEvaluation<Boolean>(value -> value).isTrue();
		TimedEventEvaluation<Boolean> b = new TimedEventEvaluation<Boolean>(value -> value).alwaysTrueDuring(Duration.of(2, MILLIS));
		TimedEventEvaluation<Boolean> c = new TimedEventEvaluation<Boolean>(value -> value).alwaysTrueDuring(Duration.of(3, MILLIS));
		Expression expr = andThen(a, and(b, c));
		a.proceedEvent(0, false);
		a.proceedEvent(1, false);
		a.proceedEvent(2, false);
		a.proceedEvent(3, false);

		b.proceedEvent(0, false);
		b.proceedEvent(1, false);
		b.proceedEvent(2, false);
		b.proceedEvent(3, false);

		c.proceedEvent(0, false);
		c.proceedEvent(1, false);
		c.proceedEvent(2, false);
		c.proceedEvent(3, false);

		Assert.assertFalse(expr.getWhenTrue(4l).size() > 0);
		expr.cleanEvents();

		a.proceedEvent(0, true);
		a.proceedEvent(1, true);
		a.proceedEvent(2, false);
		a.proceedEvent(3, false);

		b.proceedEvent(0, false);
		b.proceedEvent(1, false);
		b.proceedEvent(2, true);
		b.proceedEvent(3, true);

		c.proceedEvent(0, false);
		c.proceedEvent(1, true);
		c.proceedEvent(2, true);
		c.proceedEvent(3, true);

		Assert.assertFalse(expr.getWhenTrue(4l).size() > 0);
	}

	@Test
	public void AND_and_then_OR() {
		TimedEventEvaluation<Boolean> a = new TimedEventEvaluation<Boolean>(value -> value).named("a").isTrue();
		TimedEventEvaluation<Boolean> b = new TimedEventEvaluation<Boolean>(value -> value).named("b").alwaysTrueDuring(Duration.of(4, MILLIS));
		TimedEventEvaluation<Boolean> c = new TimedEventEvaluation<Boolean>(value -> value).named("c").alwaysTrueDuring(Duration.of(3, MILLIS));
		TimedEventEvaluation<Boolean> d = new TimedEventEvaluation<Boolean>(value -> value).named("d").alwaysTrueDuring(Duration.of(5, MILLIS));
		TimedEventEvaluation<Boolean> e = new TimedEventEvaluation<Boolean>(value -> value).named("e").alwaysTrueDuring(Duration.of(6, MILLIS));
		Expression expr = andThen(a, and(b, andThen(c, or(d, e))));

		expr.cleanEvents();
		throwEvents(a, true, true, true, true, true, true, true, true, true, true);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, true, true, true, true, true, true);
		throwEvents(e, true, true, true, true, true, true, true, true, true, true);
		Assert.assertTrue(expr.getWhenTrue(10l).size() > 0);

		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, true, true, true, true, true, true, true, true, true);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, true, true, true, true, true, true);
		throwEvents(e, true, true, true, false);
		Assert.assertTrue(expr.getWhenTrue(10l).size() > 0);

		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, true, true, false);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, false);
		throwEvents(e, true, true, true, true, true, true, true, true, true, true);
		Assert.assertTrue(expr.getWhenTrue(10l).size() > 0);

		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, true, true, true, true, true, true, true, true, true);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, false);
		throwEvents(e, true, true, true, true, false);
		Assert.assertFalse(expr.getWhenTrue(10l).size() > 0);

		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, false, true);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, true, true, true, true, true, true);
		throwEvents(e, true, true, true, true, true, true, true, true, true, true);
		Assert.assertTrue(expr.getWhenTrue(10l).size() > 0);

		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, true, false);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, true, true, true, true, true, true);
		throwEvents(e, true, true, true, true, true, true, true, true, true, true);
		Assert.assertTrue(expr.getWhenTrue(10l).size() > 0);

	}

	@Test
	public void performance() {
		ch.qos.logback.classic.Logger rootLogger = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME));
		Level level = rootLogger.getLevel();
		rootLogger.setLevel(Level.OFF);
		TimedEventEvaluation<Boolean> a = new TimedEventEvaluation<Boolean>(value -> value).named("a").isTrue();
		TimedEventEvaluation<Boolean> b = new TimedEventEvaluation<Boolean>(value -> value).named("b").alwaysTrueDuring(Duration.of(4, MILLIS));
		TimedEventEvaluation<Boolean> c = new TimedEventEvaluation<Boolean>(value -> value).named("c").alwaysTrueDuring(Duration.of(3, MILLIS));
		TimedEventEvaluation<Boolean> d = new TimedEventEvaluation<Boolean>(value -> value).named("d").alwaysTrueDuring(Duration.of(5, MILLIS));
		TimedEventEvaluation<Boolean> e = new TimedEventEvaluation<Boolean>(value -> value).named("e").alwaysTrueDuring(Duration.of(6, MILLIS));
		Expression expr = andThen(a, and(b, andThen(c, or(d, e))));
		logger.debug("new test case");
		expr.cleanEvents();
		throwEvents(a, true, true, false);
		throwEvents(b, true, true, true, true, true, true, true, true, true, true);
		throwEvents(c, true, true, true, true, true, true, true, true, true, true);
		throwEvents(d, true, true, true, true, true, true, true, true, true, true);
		throwEvents(e, true, true, true, true, true, true, true, true, true, true);
		Random random = new Random();
		long nt = System.currentTimeMillis();
		long i = 0;
		long maxTime = 1000;
		while (System.currentTimeMillis() - nt < 1000){
			i++;
			a.proceedEvent(i, random.nextBoolean());
			b.proceedEvent(i, random.nextBoolean());
			c.proceedEvent(i, random.nextBoolean());
			d.proceedEvent(i, random.nextBoolean());
			e.proceedEvent(i, random.nextBoolean());
			expr.getWhenTrue(i);
		}
		rootLogger.setLevel(level);
		logger.info("proceed " + i + " events in " + maxTime + "ms");

	}

	private void throwEvents(TimedEventEvaluation<Boolean> a, boolean... values) {
		for (int i = 0; i < values.length; i++) {
			a.proceedEvent(i, values[i]);
		}
	}

}