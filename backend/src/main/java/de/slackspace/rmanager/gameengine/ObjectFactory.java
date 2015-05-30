package de.slackspace.rmanager.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.slackspace.rmanager.gameengine.action.BuyBuildingAction;
import de.slackspace.rmanager.gameengine.action.BuyCabinetAction;
import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.FirePersonAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.HirePersonAction;
import de.slackspace.rmanager.gameengine.action.SellEstateAction;
import de.slackspace.rmanager.gameengine.action.handler.BuyBuildingActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.BuyCabinetActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.BuyEstateActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.FirePersonActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.GameActionHandler;
import de.slackspace.rmanager.gameengine.action.handler.HirePersonActionHandler;
import de.slackspace.rmanager.gameengine.json.GameActionDeserializer;
import de.slackspace.rmanager.gameengine.service.BuildingTypeService;
import de.slackspace.rmanager.gameengine.service.CabinetService;
import de.slackspace.rmanager.gameengine.service.CityService;
import de.slackspace.rmanager.gameengine.service.PersonnelService;

public class ObjectFactory {

	public static GameController getGameControllerInstance() {
		CityService cityService = new CityService();
		PersonnelService personnelService = new PersonnelService();
		CabinetService cabinetService = new CabinetService();
		BuildingTypeService buildingTypeService = new BuildingTypeService();
		
		List<GameActionHandler> actionHandlers = new ArrayList<>();
		actionHandlers.add(new BuyEstateActionHandler());
		actionHandlers.add(new BuyBuildingActionHandler());
		actionHandlers.add(new HirePersonActionHandler());
		actionHandlers.add(new BuyCabinetActionHandler());
		actionHandlers.add(new FirePersonActionHandler());
		
		return new GameController(cityService, personnelService, cabinetService, buildingTypeService, actionHandlers);
	}
	
	public static GameActionDeserializer getGameActionDeserializer() {
		HashMap<Integer, Class<? extends GameAction>> map = new HashMap<>();
		map.put(GameAction.BUY_ESTATE, BuyEstateAction.class);
		map.put(GameAction.SELL_ESTATE, SellEstateAction.class);
		map.put(GameAction.BUY_BUILDING, BuyBuildingAction.class);
			//map.put(GameAction.SELL_BUILDING, .class);
		map.put(GameAction.BUY_CABINET, BuyCabinetAction.class);
		map.put(GameAction.HIRE_PERSON, HirePersonAction.class);
		map.put(GameAction.FIRE_PERSON, FirePersonAction.class);
		
		return new GameActionDeserializer(map);
	}
}
