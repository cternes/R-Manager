package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.List;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.handlers.GameActionHandler;
import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.service.CityService;

public class GameController {

	CityService cityService;
	List<GameActionHandler> actionHandlers;
	
	public GameController(CityService cityService, List<GameActionHandler> actionHandlers) {
		this.cityService = cityService;
		this.actionHandlers = actionHandlers;
	}
	
	public GameState startNewGame(String playerOneName, String playerTwoName) {
		GameState state = new GameState();
		state.setCities(cityService.createCities());

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
	
	public GameState endTurn(GameState state, String playerName, List<GameAction> actions) {
		RManagerPlayer player = state.getPlayerByName(playerName);
		
		
		
		for (GameAction gameAction : actions) {
			for (GameActionHandler handler : actionHandlers) {
				if(handler.canHandle(gameAction)) {
					handler.handle(gameAction, player, state);
				}
			}
		}
		
		return state;
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
