package de.slackspace.rmanager.gameengine.domain;

import java.util.List;

public class GameState {

	private RManagerPlayer playerOne;
	private RManagerPlayer playerTwo;
	
	private List<City> cities;
	
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
}
