package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EstateType {

	ONE_PARCEL(1, new BigDecimal("250")),
	TWO_PARCEL(2, new BigDecimal("500")),
	THREE_PARCEL(3, new BigDecimal("750")),
	FOUR_PARCEL(4, new BigDecimal("1000")),
	FIVE_PARCEL(5, new BigDecimal("1250"));
	
	private int parcels;
	private BigDecimal squareMeters;
	
	EstateType(int parcels, BigDecimal squareMeters) {
		this.parcels = parcels;
		this.squareMeters = squareMeters;
	}
	
	public int getParcels() {
		return parcels;
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
	
	public static BigDecimal getMeanPricePerSquareMeter() {
		return new BigDecimal("100");
	}
}
