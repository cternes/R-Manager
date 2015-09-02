package de.slackspace.rmanager.gameengine;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.slackspace.rmanager.gameengine.action.BuyBuildingAction;
import de.slackspace.rmanager.gameengine.action.BuyCabinetAction;
import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.HirePersonAction;
import de.slackspace.rmanager.gameengine.domain.Building;
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

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void whenStartingNewGameShouldReturnValidGameState() {
		GameState gameState = ObjectFactory.getGameControllerInstance().startNewGame("p1", "p2");
		
		RManagerPlayer playerOne = gameState.getPlayerOne();
		assertThat(new BigDecimal(1_500_000), equalTo(playerOne.getMoney()));
		assertThat("Munich", equalTo(playerOne.getCurrentCity().getName()));
		assertThat("p1", equalTo(playerOne.getName()));
		
		RManagerPlayer playerTwo = gameState.getPlayerTwo();
		assertThat(new BigDecimal(1_500_000), equalTo(playerTwo.getMoney()));
		assertThat("Munich", equalTo(playerTwo.getCurrentCity().getName()));
		assertThat("p2", equalTo(playerTwo.getName()));
		
		int numEstates = 0;
		for (City city : gameState.getCities()) {
			assertThat(city.getAvailablePersonnel(), hasSize(6));
			numEstates += city.getEstates().size();

			assertThat(city.getAvailableCabinetByType(DepartmentType.Kitchen), hasSize(10));
			assertThat(city.getAvailableCabinetByType(DepartmentType.Dininghall), hasSize(9));
			assertThat(city.getAvailableCabinetByType(DepartmentType.Facilities), hasSize(7));
			assertThat(city.getAvailableCabinetByType(DepartmentType.Laundry), hasSize(10));
			assertThat(city.getAvailableCabinetByType(DepartmentType.Reefer), hasSize(10));
		}

		assertThat(gameState.getBuildingIds(), hasSize(numEstates));
	}
	
	@Test
	public void whenEndingTurnWithCommandsShouldValidateCommandsAndExecute() {
		GameController cut = ObjectFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		Estate estateToBuy = gameState.getCities().get(4).getEstates().get(2);
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estateToBuy.getId()));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);
		
		BigDecimal expectedMoney = new BigDecimal(1_500_000).subtract(estateToBuy.getTotalPrice());
		assertThat(expectedMoney, equalTo(updatedState.getPlayerOne().getMoney()));
		assertThat(estateToBuy.getId(), equalTo(updatedState.getPlayerOne().getEstates().iterator().next().getId()));
		assertThat(updatedState.getPlayerOne().getEstates(), hasSize(1));
	}
	
	@Test
	public void whenEndingTurnWithInvalidIdInCommandShouldThrowException() {
		GameController cut = ObjectFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction("abcd"));
		
		exception.expect(GameException.class);
		exception.expectMessage("The estate with id 'abcd' could not be found");
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test
	public void whenEndingTurnWithNegativeMoneyShouldThrowException() {
		GameController cut = ObjectFactory.getGameControllerInstance();
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
		
		exception.expect(GameException.class);
		exception.expectMessage("is not enough to pay");
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test
	public void whenEndingTurnWithDuplicateEstatesShouldThrowException() {
		GameController cut = ObjectFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(gameState.getCities().get(0).getEstates().get(3).getId()));
		actions.add(new BuyEstateAction(gameState.getCities().get(0).getEstates().get(3).getId()));
		
		exception.expect(GameException.class);
		exception.expectMessage("The player owns already the estate");
		
		cut.endTurn(gameState, "p1", actions);
	}
	
	@Test
	public void whenEndingTurnShouldSetNewAvailablePersonnel() {
		GameController cut = ObjectFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		String personId = gameState.getCities().get(0).getAvailablePersonnel().get(0).getId();
		
		GameState updatedState = cut.endTurn(gameState, "p1", new ArrayList<GameAction>());
		assertThat(updatedState.getAvailablePersonnelById(personId), is(nullValue()));
		assertThat(6, equalTo(updatedState.getCities().get(0).getAvailablePersonnel().size()));
	}
	
	@Test
	public void whenEndingTurnShouldExecuteAllBuyCommands() {
		GameController cut = ObjectFactory.getGameControllerInstance();
		GameState gameState = cut.startNewGame("p1", "p2");

		Person person = new Person(new BigDecimal(250), 30, 5, 5, DepartmentType.Kitchen);
		List<Person> personnel = new ArrayList<>();
		personnel.add(person);
		
		City city = new City("Stuttgart", BigDecimal.ONE);
		
		Estate estate = new Estate(EstateType.TWO_PARCEL, BigDecimal.ONE, BigDecimal.ONE, city.getId());
		List<Estate> estates = new ArrayList<>();
		estates.add(estate);
		
		Cabinet cabinet = new Cabinet(new BigDecimal(500), new BigDecimal(250), 5, DepartmentType.Dininghall);
		List<Cabinet> cabinets = new ArrayList<>();
		cabinets.add(cabinet);
		
		city.setAvailablePersonnel(personnel);
		city.setEstates(estates);
		city.setAvailableCabinet(DepartmentType.Kitchen, cabinets);
		
		List<City> cities = new ArrayList<>();
		cities.add(city);
		
		List<String> buildingIds = new ArrayList<>();
		buildingIds.add("abc");
		
		BuildingType buildingTypeOne = new BuildingType(1, new BigDecimal(500_000));
		
		List<BuildingType> buildingTypes = new ArrayList<>();
		buildingTypes.add(buildingTypeOne);
		
		gameState.setCities(cities);
		gameState.setBuildingIds(buildingIds);
		gameState.setBuildingTypes(buildingTypes);

		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estate.getId()));
		actions.add(new BuyBuildingAction(estate.getId(), "abc", buildingTypeOne.getId()));
		actions.add(new HirePersonAction("abc", person.getId()));
		actions.add(new BuyCabinetAction("abc", cabinet.getId(), 2));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);

		assertThat(updatedState.getPlayerOne().getEstates(), hasSize(1));
		assertThat(estate.getId(), equalTo(updatedState.getPlayerOne().getEstates().iterator().next().getId()));
		assertTrue(estate.isSold());
		assertTrue(updatedState.getEstateById(estate.getId()).isSold());
		
		assertThat(1, equalTo(updatedState.getPlayerOne().getBuildings().size()));
		assertThat(updatedState.getPlayerOne().getBuildingById("abc"), not(nullValue()));
		
		Building building = updatedState.getPlayerOne().getBuildingById("abc");
		assertThat(building.getDepartmentByType(DepartmentType.Kitchen).getPersonnel(), hasItem(person));
		assertThat(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets(), hasItem(cabinet));

		assertThat(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets(), hasSize(1));
		assertThat(2, equalTo(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets().iterator().next().getQuantity()));

		assertThat(new BigDecimal(948250), equalTo(updatedState.getPlayerOne().getMoney()));
	}
	
	@Test
	public void simulateATurn() {
		GameController cut = ObjectFactory.getGameControllerInstance();
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
		
		BuildingType buildingTypeOne = new BuildingType(2, new BigDecimal(900_000));
		
		List<BuildingType> buildingTypes = new ArrayList<>();
		buildingTypes.add(buildingTypeOne);
		
		gameState.setCities(cities);
		gameState.setBuildingIds(buildingIds);
		gameState.setBuildingTypes(buildingTypes);
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction(estate.getId()));
		actions.add(new BuyBuildingAction(estate.getId(), "abc", buildingTypeOne.getId()));
		actions.add(new HirePersonAction("abc", personOne.getId()));
		actions.add(new HirePersonAction("abc", personTwo.getId()));
		actions.add(new HirePersonAction("abc", personThree.getId()));
		actions.add(new HirePersonAction("abc", personFour.getId()));
		actions.add(new HirePersonAction("abc", personFive.getId()));
		actions.add(new BuyCabinetAction("abc", washingMachine.getId(), 2));
		actions.add(new BuyCabinetAction("abc", fridge.getId(), 1));
		actions.add(new BuyCabinetAction("abc", stove.getId(), 1));
		actions.add(new BuyCabinetAction("abc", sanitary.getId(), 1));
		actions.add(new BuyCabinetAction("abc", table.getId(), 3));
		
		GameState updatedState = cut.endTurn(gameState, "p1", actions);
		
		assertThat(updatedState.getPlayerOne().getEstates(), hasSize(1));
		assertThat(updatedState.getPlayerOne().getEstates(), hasItem(estate));
		
		assertThat(updatedState.getPlayerOne().getBuildings(), hasSize(1));

		Building building = updatedState.getPlayerOne().getBuildingById("abc");
		assertThat(building, not(nullValue()));

		assertThat(building.getDepartmentByType(DepartmentType.Kitchen).getPersonnel(), hasItem(personOne));
		assertThat(building.getDepartmentByType(DepartmentType.Kitchen).getCabinets(), hasSize(1));

		assertThat(new BigDecimal("358650.00"), equalTo(updatedState.getPlayerOne().getMoney()));
	}
}
