package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
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

	BuyCabinetActionHandler cut = new BuyCabinetActionHandler();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		Assert.assertTrue(cut.canHandle(new BuyCabinetAction("123", "456", 10)));
	}
	
	@Test
	public void whenValidShouldAddCabinetToDepartment() {
		Cabinet cabinet = new Cabinet(new BigDecimal(200), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		Building building = new Building("abc", BuildingType.FOUR_PARCEL);
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(400));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		cut.handle(action, player, state);
		
		Assert.assertEquals(1, building.getDepartmentByType(DepartmentType.Kitchen).getCabinets().size());
		Assert.assertEquals(new BigDecimal(200), player.getMoney());
	}
	
	@Test(expected=GameException.class)
	public void whenPlayerHasNotEnoughMoneyShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		Building building = new Building("abc", BuildingType.FOUR_PARCEL);
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(400));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuildingIsInvalidShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		BuyCabinetAction action = new BuyCabinetAction("132", cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getAvailableCabinetById(cabinet.getId())).thenReturn(cabinet);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenCabinetIsUnknownShouldThrowException() {
		Cabinet cabinet = new Cabinet(new BigDecimal(500), BigDecimal.TEN, 5, DepartmentType.Kitchen);
		
		Building building = new Building("abc", BuildingType.FOUR_PARCEL);
		BuyCabinetAction action = new BuyCabinetAction(building.getId(), cabinet.getId(), 1);
		
		RManagerPlayer player = new RManagerPlayer();
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		
		cut.handle(action, player, state);
	}
	
	public void whenDepartmentIsFullThrowException() {
		
	}

	public void whenBuyingMultipleCabinetsShouldCalculateCorrectPrice() {
		
	}
}
