package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EstateType {

	ONE_PARCEL(new BigDecimal("250")),
	TWO_PARCEL(new BigDecimal("500")),
	THREE_PARCEL(new BigDecimal("750")),
	FOUR_PARCEL(new BigDecimal("1000")),
	FIVE_PARCEL(new BigDecimal("1250"));
	
	private BigDecimal squareMeters;
	
	EstateType(BigDecimal squareMeters) {
		this.squareMeters = squareMeters;
	}
	
	public BigDecimal getSquareMeters() {
		return squareMeters;
	}
	
	private static final List<EstateType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new SecureRandom();

	public static EstateType randomType()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
