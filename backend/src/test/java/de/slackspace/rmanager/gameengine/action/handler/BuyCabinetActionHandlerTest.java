package de.slackspace.rmanager.gameengine.action.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.slackspace.rmanager.gameengine.action.BuyCabinetAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyCabinetActionHandlerTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	BuyCabinetActionHandler cut = new BuyCabinetActionHandler();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		assertThat(cut.canHandle(new BuyCabinetAction("123", "456", 10)), equalTo(true));
	}
	
	@Test
	public void whenValidShouldAddCabinetToDepartment() {
		Cabinet cabinet = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(400));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		cut.handle(action, player, state);
		
		assertThat(building.getDepartmentByType(DepartmentType.Kitchen).getCabinets(), hasSize(1));
		assertThat(new BigDecimal(200), equalTo(player.getMoney()));
	}
	
	@Test
	public void whenPlayerHasNotEnoughMoneyShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(400));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		exception.expect(GameException.class);
		exception.expectMessage("not enough to pay");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenBuildingIsInvalidShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		BuyCabinetAction action = new BuyCabinetAction("132", cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		exception.expect(GameException.class);
		exception.expectMessage("The player does not own a building");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenCabinetIsUnknownShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		
		exception.expect(GameException.class);
		exception.expectMessage("does not exist on free market");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenBuyingMultipleCabinetsShouldCalculateCorrectPrice() {
		Cabinet cabinet = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Dininghall);
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 10);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(4000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		cut.handle(action, player, state);

		assertThat(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets(), hasSize(1));
		assertThat(10, equalTo(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets().iterator().next().getQuantity()));
		assertThat(new BigDecimal(2000), equalTo(player.getMoney()));
	}
	
	@Test
	public void whenBuyingMoreOfACabinetShouldIncreaseQuantityOfCabinet() {
		Cabinet cabinet = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Dininghall);
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		building.getCabinets().add(cabinet);
		BuyCabinetAction actionOne = new BuyCabinetAction(building.getId(), cabinet.getId(), 5);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(4000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		// buy first charge
		cut.handle(actionOne, player, state);
		
		// buy second charge
		player.setMoney(new BigDecimal(4000));
		BuyCabinetAction actionTwo = new BuyCabinetAction(building.getId(), cabinet.getId(), 3);
		cut.handle(actionTwo, player, state);

		assertThat(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets(), hasSize(1));
		assertThat(building.getDepartmentByType(DepartmentType.Dininghall).getCabinets().iterator().next().getQuantity(), equalTo(8));
		assertThat(new BigDecimal(3400), equalTo(player.getMoney()));
	}
	
	@Test
	public void whenBuyingMoreCabinetThanCapacityInDepartmentShouldThrowException() {
		Cabinet cabinetOne = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Dininghall);
		Cabinet cabinetTwo = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Dininghall);
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		Building building = new Building("abc", buildingType, "123");
		BuyCabinetAction actionOne = new BuyCabinetAction(building.getId(), cabinetOne.getId(), 2);
		BuyCabinetAction actionTwo = new BuyCabinetAction(building.getId(), cabinetTwo.getId(), 3);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(4000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinetOne.getId())).thenReturn(cabinetOne);
		Mockito.when(state.getAvailableCabinetById(cabinetTwo.getId())).thenReturn(cabinetTwo);
		
		cut.handle(actionOne, player, state);
		
		exception.expect(GameException.class);
		exception.expectMessage("Not enough free space units in department 'Dininghall' to add cabinet with");
		
		cut.handle(actionTwo, player, state);
	}
}
