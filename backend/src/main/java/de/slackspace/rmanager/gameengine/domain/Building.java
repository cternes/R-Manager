package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Building {

	private String id;
	private BuildingType buildingType;
	private Map<DepartmentType, Department> departments = new HashMap<>();
	private String cityId;
	
	protected Building() {
	}
	
	public Building(String id, BuildingType type, String cityId) {
		departments.put(DepartmentType.Dininghall, new Department(DepartmentType.Dininghall, type.getRequiredParcels() * 4));
		departments.put(DepartmentType.Facilities, new Department(DepartmentType.Facilities, type.getRequiredParcels()));
		departments.put(DepartmentType.Kitchen, new Department(DepartmentType.Kitchen, type.getRequiredParcels()));
		departments.put(DepartmentType.Laundry, new Department(DepartmentType.Laundry, type.getRequiredParcels()));
		departments.put(DepartmentType.Reefer, new Department(DepartmentType.Reefer, type.getRequiredParcels() * 2));
		
		setBuildingType(type);
		setCityId(cityId);
		this.id = id;
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
	
	public List<Person> getPersonnel() {
		List<Person> personnel = new ArrayList<>();
		for (Entry<DepartmentType, Department> entry : departments.entrySet()) {
			personnel.addAll(entry.getValue().getPersonnel());
		}
		
		return personnel;
	}
	
	public List<Cabinet> getCabinets() {
		List<Cabinet> cabinets = new ArrayList<>();
		
		for (Entry<DepartmentType, Department> entry : departments.entrySet()) {
			cabinets.addAll(entry.getValue().getCabinets());
		}
		
		return cabinets;
	}
	
	public BigDecimal getMonthlyPersonnelCosts() {
		BigDecimal monthlyCosts = BigDecimal.ZERO;
		for (Person person : getPersonnel()) {
			monthlyCosts = monthlyCosts.add(person.getMonthlyCosts());
		}
		
		return monthlyCosts;
	}
	
	public BigDecimal getMonthlyCabinetCosts() {
		BigDecimal monthlyCosts = BigDecimal.ZERO;
		
		for (Cabinet cabinet : getCabinets()) {
			monthlyCosts = monthlyCosts.add(cabinet.getTotalMonthlyCosts());
		}
		
		return monthlyCosts;
	}
	
	public BigDecimal getMonthlyOutput() {
		BigDecimal meals = BigDecimal.ZERO;
		
		for (Entry<DepartmentType, Department> entry : departments.entrySet()) {
			meals = meals.add(entry.getValue().getMonthlyOutput());
		}
		
		return meals;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Map<DepartmentType, Department> getDepartments() {
		return departments;
	}

}
