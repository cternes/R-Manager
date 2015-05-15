package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.handler.GameActionHandler;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.domain.TurnStatistic;
import de.slackspace.rmanager.gameengine.exception.GameException;
import de.slackspace.rmanager.gameengine.service.BuildingTypeService;
import de.slackspace.rmanager.gameengine.service.CabinetService;
import de.slackspace.rmanager.gameengine.service.CityService;
import de.slackspace.rmanager.gameengine.service.PersonnelService;

public class GameController {

	private Log logger = LogFactory.getLog(getClass());
	
	CityService cityService;
	PersonnelService personnelService;
	CabinetService cabinetService;
	BuildingTypeService buildingTypeService;
	List<GameActionHandler> actionHandlers;
	
	public GameController(CityService cityService, PersonnelService personnelService, CabinetService cabinetService,
			BuildingTypeService buildingTypeService, List<GameActionHandler> actionHandlers) {
		this.cityService = cityService;
		this.personnelService = personnelService;
		this.cabinetService = cabinetService;
		this.buildingTypeService = buildingTypeService;
		this.actionHandlers = actionHandlers;
	}
	
	public GameState startNewGame(String playerOneName, String playerTwoName) {
		GameState state = new GameState();
		state.setCities(createCities());
		state.setBuildingIds(createBuildingIds(state.getCities()));
		state.setBuildingTypes(buildingTypeService.createBuildingTypes());

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
			
			city.setAvailableCabinet(DepartmentType.Kitchen, cabinetService.createCabinet(DepartmentType.Kitchen));
			city.setAvailableCabinet(DepartmentType.Dininghall, cabinetService.createCabinet(DepartmentType.Dininghall));
			city.setAvailableCabinet(DepartmentType.Facilities, cabinetService.createCabinet(DepartmentType.Facilities));
			city.setAvailableCabinet(DepartmentType.Reefer, cabinetService.createCabinet(DepartmentType.Reefer));
			city.setAvailableCabinet(DepartmentType.Laundry, cabinetService.createCabinet(DepartmentType.Laundry));
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
		player.setTurnStatistics(new TurnStatistic());
		
		logger.debug("Ending turn for player '" + playerName +"'");
		
		logger.debug("Found '" + actions.size() + "' actions for player '" + playerName + "'");
		
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
			player.pay(building.getMonthlyPersonnelCosts());
			player.getTurnStatistics().increasePersonnelCosts(building.getMonthlyPersonnelCosts());
			
			player.pay(building.getMonthlyCabinetCosts());
			player.getTurnStatistics().increaseRunningCosts(building.getMonthlyCabinetCosts());
			
			logger.debug("Monthly costs of building '" + building.getId() + "' = " + building.getMonthlyPersonnelCosts().add(building.getMonthlyCabinetCosts()));
		}
		
		// sell meals
		for (Building building : player.getBuildings()) {
			City city = state.getCityById(building.getCityId());
			if(city == null) {
				throw new GameException("City with id '" + building.getCityId() + "' could not be found");
			}
			
			BigDecimal meals = building.getMonthlyOutput();
			player.getTurnStatistics().increaseCustomers(meals);
			logger.debug("Monthly output of building '" + building.getId() + "' = " + meals);
			
			BigDecimal earnings = meals.multiply(new BigDecimal(10).multiply(city.getRateOfPriceIncrease())); // meal price = 10
			player.earn(earnings);
			player.getTurnStatistics().increaseEarnings(earnings);
			
			logger.debug("Monthly income of building '" + building.getId() + "' = " + earnings);
		}
		
		logger.debug("TurnStatistics: " + player.getTurnStatistics().toString());
		
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
