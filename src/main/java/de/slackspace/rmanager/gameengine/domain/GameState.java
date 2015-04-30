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
}
