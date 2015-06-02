package de.slackspace.rmanager.gameengine.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameState {

	private RManagerPlayer playerOne;
	private RManagerPlayer playerTwo;
	
	private List<City> cities = new ArrayList<>();
	private List<String> buildingIds = new ArrayList<>();
	private Set<Share> shares = new HashSet<>();
	private List<BuildingType> buildingTypes = new ArrayList<>();
	
	public RManagerPlayer getPlayerOne() {
		return playerOne;
	}
	
	public void setPlayerOne(RManagerPlayer playerOne) {
		this.playerOne = playerOne;
	}
	
	public RManagerPlayer getPlayerTwo() {
		return playerTwo;
	}
	
	public void setPlayerTwo(RManagerPlayer playerTwo) {
		this.playerTwo = playerTwo;
	}
	
	public List<City> getCities() {
		return cities;
	}
	
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public RManagerPlayer getPlayerByName(String name) {
		if(playerOne.getName().equals(name)) {
			return playerOne;
		}
		return playerTwo;
	}
	
	public Estate getEstateById(String id) {
		for (City city : cities) {
			for (Estate estate : city.getEstates()) {
				if(estate.getId().equals(id)) {
					return estate;
				}
			}
		}
		
		return null;
	}
	
	public Person getAvailablePersonnelById(String id) {
		for (City city : cities) {
			for (Person person : city.getAvailablePersonnel()) {
				if(person.getId().equals(id)) {
					return person;
				}
			}
		}
		
		return null;
	}
	
	public Cabinet getAvailableCabinetById(String id) {
		for (City city : cities) {
			Cabinet cabinet = city.getAvailableCabinetById(id);
			if(cabinet != null) {
				return cabinet;
			}
		}
		
		return null;
	}
	
	public boolean isBuildingIdExisting(String id) {
		return buildingIds.stream()
			.filter(i -> i.equals(id))
			.findFirst()
			.isPresent();
	}
	
	public City getCityById(String id) {
		return findById(cities, id);
	}

	public List<String> getBuildingIds() {
		return buildingIds;
	}

	public void setBuildingIds(List<String> buildingIds) {
		this.buildingIds = buildingIds;
	}
	
	public void removeBuildingId(String id) {
		buildingIds.remove(id);
	}

	public Set<Share> getShares() {
		return shares;
	}

	public void setShares(Set<Share> shares) {
		this.shares = shares;
	}
	
	public Share getShareById(String id) {
		return findById(shares, id);
	}

	public List<BuildingType> getBuildingTypes() {
		return buildingTypes;
	}

	public void setBuildingTypes(List<BuildingType> buildingTypes) {
		this.buildingTypes = buildingTypes;
	}

	public BuildingType getBuildingTypeById(String id) {
		return findById(buildingTypes, id);
	}
	
	public <T extends GameEntity> T findById(Collection<T> list, String id) {
		return list.stream()
				.filter(i -> i.getId().equals(id))
				.findFirst()
				.get();
	}

}
