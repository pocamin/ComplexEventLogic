package com.swissquote.poc.eventLogic.expressions;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;

/**
 * (c) Swissquote 12/23/15
 *
 * @author bleroux
 */
public class Helper {
	public static void assertSetEquals(Set<Long> actual, Long... expected) {
		Assert.assertEquals(Stream.of(expected).collect(Collectors.toSet()), actual);
	}

}
