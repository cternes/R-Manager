package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class BuildingType implements GameEntity {

	private String id = UUID.randomUUID().toString();
	private String name;
	private int requiredParcels;
	private BigDecimal price;
	private BigDecimal squareMeters;
	
	protected BuildingType() {
	}
	
	public BuildingType(int requiredParcels, BigDecimal price) {
		this.requiredParcels = requiredParcels;
		this.price = price;
	}
	
	public BuildingType(String name, int requiredParcels, BigDecimal price, BigDecimal squareMeters) {
		this.name = name;
		this.requiredParcels = requiredParcels;
		this.price = price;
		this.squareMeters = squareMeters;
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

	public BigDecimal getSquareMeters() {
		return squareMeters;
	}

	public void setSquareMeters(BigDecimal squareMeters) {
		this.squareMeters = squareMeters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
