package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.handler.GameActionHandler;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.service.CityService;
import de.slackspace.rmanager.gameengine.service.PersonnelService;

public class GameController {

	CityService cityService;
	PersonnelService personnelService;
	List<GameActionHandler> actionHandlers;
	
	public GameController(CityService cityService, PersonnelService personnelService, List<GameActionHandler> actionHandlers) {
		this.cityService = cityService;
		this.personnelService = personnelService;
		this.actionHandlers = actionHandlers;
	}
	
	public GameState startNewGame(String playerOneName, String playerTwoName) {
		GameState state = new GameState();
		state.setCities(createCities());
		state.setBuildingIds(createBuildingIds(state.getCities()));

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
		List<City> cities = cityService.createCities();
		for (City city : cities) {
			List<Person> personnel = personnelService.createPersonnel(city);
			city.setAvailablePersonnel(personnel);
		}
		
		return cities;
	}
	
	private List<String> createBuildingIds(List<City> cities) {
		int numEstates = 0;
		for (City city : cities) {
			numEstates += city.getEstates().size();
		}
		
		List<String> buildingIds = new ArrayList<>();
		for (int i = 0; i < numEstates; i++) {
			buildingIds.add(UUID.randomUUID().toString());
		}
		
		return buildingIds;
	}
	
	public GameState endTurn(GameState state, String playerName, List<GameAction> actions) {
		RManagerPlayer player = state.getPlayerByName(playerName);
		
		// player actions
		for (GameAction gameAction : actions) {
			for (GameActionHandler handler : actionHandlers) {
				if(handler.canHandle(gameAction)) {
					handler.handle(gameAction, player, state);
				}
			}
		}
		
		// refresh personnel in cities
		refreshCityPersonnel(state);
		
		// pay monthly costs
		for (Building building : player.getBuildings()) {
			player.pay(building.getMonthlyCosts());
		}
		
		return state;
	}
	
	private void refreshCityPersonnel(GameState state) {
		for (City city : state.getCities()) {
			city.setAvailablePersonnel(personnelService.createPersonnel(city));
		}
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
