package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.gameengine.action.BuyBuildingAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyBuildingActionHandlerTest {

	BuyBuildingActionHandler cut = new BuyBuildingActionHandler();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		Assert.assertTrue(cut.canHandle(new BuyBuildingAction("", "", BuildingType.ONE_PARCEL)));
	}
	
	@Test
	public void whenBuyBuildingShouldReduceMoneyAndAddBuilding() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", BuildingType.ONE_PARCEL);
		
		cut.handle(action, player, state);
		
		Assert.assertEquals(new BigDecimal(1_000_000), player.getMoney());
		Assert.assertEquals(BuildingType.ONE_PARCEL, player.getEstates().iterator().next().getBuilding().getBuildingType());
		Assert.assertEquals("1234", player.getEstates().iterator().next().getBuilding().getCityId());
		Mockito.verify(state).removeBuildingId("abc");
	}
	
	@Test(expected=GameException.class)
	public void whenBuyBuildingWithEstateWrongIdShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		GameState state = Mockito.mock(GameState.class);
		
		BuyBuildingAction action = new BuyBuildingAction(UUID.randomUUID().toString(), "abc", BuildingType.ONE_PARCEL);
		
		cut.handle(action, player, state);
		
		Assert.assertEquals(new BigDecimal(1_000_000), player.getMoney());
		Assert.assertEquals(BuildingType.ONE_PARCEL, player.getEstates().iterator().next().getBuilding().getBuildingType());
	}
	
	
	@Test(expected=GameException.class)
	public void whenBuyBuildingWithNotEnoughMoneyShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(100_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", BuildingType.ONE_PARCEL);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuyTooBigBuildingForEstateShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.ONE_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", BuildingType.TWO_PARCEL);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuyDuplicateBuildingShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		estate.setBuilding(new Building("abc", BuildingType.ONE_PARCEL, estate.getCityId()));
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", BuildingType.ONE_PARCEL);
		
		cut.handle(action, player, state);
	}
}
