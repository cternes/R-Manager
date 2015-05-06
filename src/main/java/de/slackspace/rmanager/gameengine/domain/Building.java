package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Building {

	private String id = UUID.randomUUID().toString();
	
	private BuildingType buildingType;
	
	private Map<DepartmentType, Department> departments = new HashMap<>();
	
	protected Building() {
		departments.put(DepartmentType.Dininghall, new Department(DepartmentType.Dininghall));
		departments.put(DepartmentType.Facilities, new Department(DepartmentType.Facilities));
		departments.put(DepartmentType.Kitchen, new Department(DepartmentType.Kitchen));
		departments.put(DepartmentType.Laundry, new Department(DepartmentType.Laundry));
		departments.put(DepartmentType.Reefer, new Department(DepartmentType.Reefer));
	}
	
	public Building(BuildingType type) {
		this();
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
	
	public Department getDepartmentByType(DepartmentType type) {
		return departments.get(type);
	}
}
