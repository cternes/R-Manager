package de.slackspace.rmanager.gameengine;

import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.action.handler.BuyEstateActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.GameActionHandler;
import de.slackspace.rmanager.gameengine.service.CityService;

public class GameControllerFactory {

	public static GameController getGameControllerInstance() {
		CityService cityService = new CityService();
		
		List<GameActionHandler> actionHandlers = new ArrayList<>();
		actionHandlers.add(new BuyEstateActionHandler());
		
		return new GameController(cityService, actionHandlers);
	}
}
