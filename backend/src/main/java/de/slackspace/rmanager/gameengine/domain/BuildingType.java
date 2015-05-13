package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class BuildingType {

	private String id = UUID.randomUUID().toString();
	
	private int requiredParcels;
	private BigDecimal price;
	
	protected BuildingType() {
	}
	
	public BuildingType(int requiredParcels, BigDecimal price) {
		this.requiredParcels = requiredParcels;
		this.price = price;
	}
	
	public int getRequiredParcels() {
		return requiredParcels;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public String getId() {
		return id;
	}
}
