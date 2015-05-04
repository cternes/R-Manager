package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Building {

	private String id = UUID.randomUUID().toString();
	
	private BuildingType buildingType;
	
	protected Building() {
	}
	
	public Building(BuildingType type) {
		setBuildingType(type);
	}

	public String getId() {
		return id;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}
	
	public BigDecimal getPrice() {
		return buildingType.getPrice();
	}
	
}
