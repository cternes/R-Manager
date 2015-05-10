package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;


public enum BuildingType {

	ONE_PARCEL(1, new BigDecimal("500000")),
	TWO_PARCEL(2, new BigDecimal("900000")),
	THREE_PARCEL(3, new BigDecimal("1300000")),
	FOUR_PARCEL(4, new BigDecimal("1600000"));
	
	private int requiredParcels;
	private BigDecimal price;
	
	BuildingType(int requiredParcels, BigDecimal price) {
		this.requiredParcels = requiredParcels;
		this.price = price;
	}
	
	public int getRequiredParcels() {
		return requiredParcels;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
}
