package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			
			Assert.assertEquals(10, city.getAvailableCabinetByType(DepartmentType.Kitchen).size());
			Assert.assertEquals(9, city.getAvailableCabinetByType(DepartmentType.Dininghall).size());
			Assert.assertEquals(7, city.getAvailableCabinetByType(DepartmentType.Facilities).size());
			Assert.assertEquals(10, city.getAvailableCabinetByType(DepartmentType.Laundry).size());
			Assert.assertEquals(10, city.getAvailableCabinetByType(DepartmentType.Reefer).size());
			
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
		
		City city = new City("Stuttgart", BigDecimal.ONE);
		
		Estate estate = new Estate(EstateType.TWO_PARCEL, BigDecimal.ONE, BigDecimal.ONE, city.getId());
		List<Estate> estates = new ArrayList<>();
		estates.add(estate);
		
		Cabinet cabinet = new Cabinet(new BigDecimal(500), new BigDecimal(250), 5, DepartmentType.Kitchen);
		List<Cabinet> cabinets = new ArrayList<>();
		cabinets.add(cabinet);
		
		city.setAvailablePersonnel(personnel);
		city.setEstates(estates);
		city.setAvailableCabinet(DepartmentType.Kitchen, cabinets);
		
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
		
		Assert.assertEquals(new BigDecimal(942750), updatedState.getPlayerOne().getMoney());
	}
	
	@Test
	public void simulateATurn() {
		GameController cut = GameControllerFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");

		Person personOne = new Person(new BigDecimal(2550), 30, 8, 8, DepartmentType.Kitchen);
		Person personTwo = new Person(new BigDecimal(1400), 30, 5, 5, DepartmentType.Dininghall);
		Person personThree = new Person(new BigDecimal(1400), 30, 5, 5, DepartmentType.Facilities);
		Person personFour = new Person(new BigDecimal(1400), 30, 5, 5, DepartmentType.Laundry);
		Person personFive = new Person(new BigDecimal(1400), 30, 5, 5, DepartmentType.Reefer);
		List<Person> personnel = new ArrayList<>();
		personnel.add(personOne);
		personnel.add(personTwo);
		personnel.add(personThree);
		personnel.add(personFour);
		personnel.add(personFive);
		
		City city = new City("Stuttgart", BigDecimal.ONE);
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, new BigDecimal("1.17"), BigDecimal.ONE, city.getId());
		List<Estate> estates = new ArrayList<>();
		estates.add(estate);
		
		Cabinet washingMachine = new Cabinet(new BigDecimal(22000), new BigDecimal(1300), 50, DepartmentType.Laundry);
		Cabinet fridge = new Cabinet(new BigDecimal(24000), new BigDecimal(1500), 50, DepartmentType.Reefer);
		Cabinet stove = new Cabinet(new BigDecimal(25000), new BigDecimal(1650), 50, DepartmentType.Kitchen);
		Cabinet sanitary = new Cabinet(new BigDecimal(20000), new BigDecimal(700), 50, DepartmentType.Facilities);
		Cabinet table = new Cabinet(new BigDecimal(330), new BigDecimal(20), 15, DepartmentType.Dininghall);
		
		city.setAvailablePersonnel(personnel);
		city.setEstates(estates);
		city.setAvailableCabinet(DepartmentType.Kitchen, Arrays.asList(new Cabinet[] {stove}));
		city.setAvailableCabinet(DepartmentType.Reefer, Arrays.asList(new Cabinet[] {fridge}));
		city.setAvailableCabinet(DepartmentType.Laundry, Arrays.asList(new Cabinet[] {washingMachine}));
		city.setAvailableCabinet(DepartmentType.Facilities, Arrays.asList(new Cabinet[] {sanitary}));
		city.setAvailableCabinet(DepartmentType.Dininghall, Arrays.asList(new Cabinet[] {table}));
		
		List<City> cities = new ArrayList<>();
		cities.add(city);
		
		List<String> buildingIds = new ArrayList<>();
		buildingIds.add("abc");
		
		gameState.setCities(cities);
		gameState.setBuildingIds(buildingIds);
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estate.getId()));
		actions.add(new BuyBuildingAction(estate.getId(), "abc", BuildingType.TWO_PARCEL));
		actions.add(new HirePersonAction("abc", personOne.getId()));
		actions.add(new HirePersonAction("abc", personTwo.getId()));
		actions.add(new HirePersonAction("abc", personThree.getId()));
		actions.add(new HirePersonAction("abc", personFour.getId()));
		actions.add(new HirePersonAction("abc", personFive.getId()));
		actions.add(new BuyCabinetAction("abc", washingMachine.getId(), 2));
		actions.add(new BuyCabinetAction("abc", fridge.getId(), 1));
		actions.add(new BuyCabinetAction("abc", stove.getId(), 1));
		actions.add(new BuyCabinetAction("abc", sanitary.getId(), 1));
		actions.add(new BuyCabinetAction("abc", table.getId(), 20));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);
		
		Assert.assertEquals(1, updatedState.getPlayerOne().getEstates().size());
		Assert.assertEquals(estate.getId(), updatedState.getPlayerOne().getEstates().iterator().next().getId());
		
		Assert.assertEquals(1, updatedState.getPlayerOne().getBuildings().size());
		Assert.assertNotNull(updatedState.getPlayerOne().getBuildingById("abc"));
		
		Assert.assertEquals(personOne.getId(), updatedState.getPlayerOne().getBuildingById("abc").getDepartmentByType(DepartmentType.Kitchen).getPersonnel().get(0).getId());
		Assert.assertEquals(1, updatedState.getPlayerOne().getBuildingById("abc").getDepartmentByType(DepartmentType.Kitchen).getCabinets().size());
		
		Assert.assertEquals(new BigDecimal("353900.00"), updatedState.getPlayerOne().getMoney());
	}
}
