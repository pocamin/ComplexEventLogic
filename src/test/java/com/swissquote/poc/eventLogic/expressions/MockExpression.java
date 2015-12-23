package com.swissquote.poc.eventLogic.expressions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (c) Swissquote 12/23/15
 *
 * @author bleroux
 */
public class MockExpression implements Expression {
	Map<Long, Set<Long>> isTrueWhen = new HashMap<>();
	private final String name;

	public MockExpression(String name) {
		this.name = name;
	}

	public void addTrueCases(Long when, Long... goBackTo) {
		isTrueWhen.put(when, Stream.of(goBackTo).collect(Collectors.toSet()));
	}

	@Override
	public Set<Long> getWhenTrue(Long timestamp) {
		return isTrueWhen.computeIfAbsent(timestamp, t -> Collections.emptySet());
	}

	@Override
	public void cleanEvents() {
		isTrueWhen.clear();
	}

	@Override
	public String toString() {
		return name;
	}
}
