package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.action.BuyBuildingAction;
import de.slackspace.rmanager.gameengine.action.BuyCabinetAction;
import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.HirePersonAction;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class GameControllerTest {

	@Test
	public void whenStartingNewGameShouldReturnValidGameState() {
		GameState gameState = GameControllerFactory.getGameControllerInstance().startNewGame("p1", "p2");
		
		RManagerPlayer playerOne = gameState.getPlayerOne();
		Assert.assertEquals(new BigDecimal(1_500_000), playerOne.getMoney());
		Assert.assertEquals("Munich", playerOne.getCurrentCity().getName());
		Assert.assertEquals("p1", playerOne.getName());
		
		RManagerPlayer playerTwo = gameState.getPlayerTwo();
		Assert.assertEquals(new BigDecimal(1_500_000), playerTwo.getMoney());
		Assert.assertEquals("Munich", playerTwo.getCurrentCity().getName());
		Assert.assertEquals("p2", playerTwo.getName());
		
		int numEstates = 0;
		for (City city : gameState.getCities()) {
			Assert.assertEquals(6, city.getAvailablePersonnel().size());
			numEstates += city.getEstates().size();
			
//			System.out.println("");
//			System.out.println(city.getName());
//			System.out.println("===========");
//			
//			for (Estate estate: city.getEstates()) {
//				System.out.println("" + estate.getEstateType() + ":" + estate.getTotalPrice());
//			}
		}
		
		Assert.assertEquals(numEstates, gameState.getBuildingIds().size());
	}
	
	@Test
	public void whenEndingTurnWithCommandsShouldValidateCommandsAndExecute() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		Estate estateToBuy = gameState.getCities().get(4).getEstates().get(2);
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estateToBuy.getId()));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);
		
		BigDecimal expectedMoney = new BigDecimal(1_500_000).subtract(estateToBuy.getTotalPrice());
		Assert.assertEquals(expectedMoney, updatedState.getPlayerOne().getMoney());
		Assert.assertEquals(estateToBuy.getId(), updatedState.getPlayerOne().getEstates().iterator().next().getId());
		Assert.assertEquals(1, updatedState.getPlayerOne().getEstates().size());
	}
	
	@Test(expected=GameException.class)
	public void whenEndingTurnWithInvalidIdInCommandShouldThrowException() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction("abcd"));
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test(expected=GameException.class)
	public void whenEndingTurnWithNegativeMoneyShouldThrowException() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(gameState.getCities().get(0).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(1).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(2).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(3).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(4).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(5).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(6).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(7).getEstates().get(3).getId()));
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test(expected=GameException.class)
	public void whenEndingTurnWithDuplicateEstatesShouldThrowException() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(gameState.getCities().get(0).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(0).getEstates().get(3).getId()));
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test
	public void whenEndingTurnShouldSetNewAvailablePersonnel() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		String personId = gameState.getCities().get(0).getAvailablePersonnel().get(0).getId();
		
		GameState updatedState = cut.endTurn(gameState, "p1", new ArrayList<GameAction>());
		Assert.assertNull(updatedState.getAvailablePersonnelById(personId));
		Assert.assertEquals(6, updatedState.getCities().get(0).getAvailablePersonnel().size());
	}
	
	@Test
	public void whenEndingTurnShouldExecuteAllBuyCommands() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");

		Person person = new Person(new BigDecimal(250), 30, 5, 5, DepartmentType.Kitchen);
		List<Person> personnel = new ArrayList<>();
		personnel.add(person);
		
		Estate estate = new Estate(EstateType.TWO_PARCEL, BigDecimal.ONE, BigDecimal.ONE, UUID.randomUUID().toString());
		List<Estate> estates = new ArrayList<>();
		estates.add(estate);
		
		Cabinet cabinet = new Cabinet(new BigDecimal(500), new BigDecimal(250), 5, DepartmentType.Kitchen);
		List<Cabinet> cabinets = new ArrayList<>();
		cabinets.add(cabinet);
		
		City city = new City("Stuttgart", BigDecimal.ONE);
		city.setAvailablePersonnel(personnel);
		city.setEstates(estates);
		city.setAvailableCabinet(cabinets);
		
		List<City> cities = new ArrayList<>();
		cities.add(city);
		
		List<String> buildingIds = new ArrayList<>();
		buildingIds.add("abc");
		
		gameState.setCities(cities);
		gameState.setBuildingIds(buildingIds);
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estate.getId()));
		actions.add(new BuyBuildingAction(estate.getId(), "abc", BuildingType.ONE_PARCEL));
		actions.add(new HirePersonAction("abc", person.getId()));
		actions.add(new BuyCabinetAction("abc", cabinet.getId(), 10));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);
		
		Assert.assertEquals(1, updatedState.getPlayerOne().getEstates().size());
		Assert.assertEquals(estate.getId(), updatedState.getPlayerOne().getEstates().iterator().next().getId());
		
		Assert.assertEquals(1, updatedState.getPlayerOne().getBuildings().size());
		Assert.assertNotNull(updatedState.getPlayerOne().getBuildingById("abc"));
		
		Assert.assertEquals(person.getId(), updatedState.getPlayerOne().getBuildingById("abc").getDepartmentByType(DepartmentType.Kitchen).getPersonnel().get(0).getId());
		Assert.assertEquals(cabinet.getId(), updatedState.getPlayerOne().getBuildingById("abc").getDepartmentByType(DepartmentType.Kitchen).getCabinets().iterator().next().getId());
		Assert.assertEquals(1, updatedState.getPlayerOne().getBuildingById("abc").getDepartmentByType(DepartmentType.Kitchen).getCabinets().size());
		
		Assert.assertEquals(new BigDecimal(942400), updatedState.getPlayerOne().getMoney());
	}
}
