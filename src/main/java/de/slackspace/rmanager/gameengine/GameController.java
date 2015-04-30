package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;

public class GameController {

	public GameState startNewGame(String playerOneName, String playerTwoName) {
		GameState state = new GameState();
		state.setCities(createCities());

		RManagerPlayer playerOne = new RManagerPlayer();
		playerOne.setMoney(new BigDecimal(1_500_000));
		playerOne.setCurrentCity(getCity(state, "Munich"));
		playerOne.setName(playerOneName);
		state.setPlayerOne(playerOne);
		
		RManagerPlayer playerTwo = new RManagerPlayer();
		playerTwo.setMoney(new BigDecimal(1_500_000));
		playerTwo.setCurrentCity(getCity(state, "Munich"));
		playerTwo.setName(playerTwoName);
		state.setPlayerTwo(playerTwo);
		
		return state;
	}
	
	private List<City> createCities() {
		List<City> cities = new ArrayList<>();
		cities.add(new City("Berlin"));
		cities.add(new City("Bonn"));
		cities.add(new City("Erfurt"));
		cities.add(new City("Frankfurt"));
		cities.add(new City("Hamburg"));
		cities.add(new City("Munich"));
		cities.add(new City("Nuremberg"));
		cities.add(new City("Stuttgart"));

		return cities;
	}
	
	private City getCity(GameState state, String cityName) {
		for (City city : state.getCities()) {
			if(city.getName().equals(cityName)) {
				return city;
			}
		}
		
		return null;
	}
}
