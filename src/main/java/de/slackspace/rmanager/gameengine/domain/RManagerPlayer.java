package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RManagerPlayer {

	private String name;
	
	private BigDecimal money = BigDecimal.ZERO;
	
	private City currentCity;
	
	private Set<Estate> estates = new HashSet<>();

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public City getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Estate> getEstates() {
		return estates;
	}
	
	public void setEstates(Set<Estate> estates) {
		this.estates = estates;
	}
	
	public List<Building> getBuildings() {
		List<Building> buildings = new ArrayList<>();
		for (Estate estate : estates) {
			if(estate.getBuilding() != null) {
				buildings.add(estate.getBuilding());
			}
		}
		
		return buildings;
	}
	
	public Building getBuildingById(String id) {
		for (Building building : getBuildings()) {
			if(building.getId().equals(id)) {
				return building;
			}
		}
		
		return null;
	}
	
	public Person getPersonnelById(String id) {
		List<Person> personnel = new ArrayList<>();
		for (Building building : getBuildings()) {
			personnel.addAll(building.getPersonnel());
		}
		
		for (Person person : personnel) {
			if(person.getId().equals(id)) {
				return person;
			}
		}
		
		return null;
	}
	
	public boolean canBuy(BigDecimal price) {
		BigDecimal tmpMoney = money.subtract(price);
		
		if(tmpMoney.compareTo(BigDecimal.ZERO) == -1) {
			return false;
		}
		
		return true;
	}
	
	public void pay(BigDecimal price) {
		money = money.subtract(price);
	}
	
	public void earn(BigDecimal earnings) {
		money = money.add(earnings);
	}
	
}
