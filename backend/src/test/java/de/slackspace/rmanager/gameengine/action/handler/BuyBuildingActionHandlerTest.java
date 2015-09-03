package de.slackspace.rmanager.gameengine.action.handler;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	BuyBuildingActionHandler cut = new BuyBuildingActionHandler();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		assertThat(cut.canHandle(new BuyBuildingAction("", "", "")), equalTo(true));
	}
	
	@Test
	public void whenBuyBuildingShouldReduceMoneyAndAddBuilding() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		Mockito.when(state.getBuildingTypeById(buildingType.getId())).thenReturn(buildingType);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", buildingType.getId());
		
		cut.handle(action, player, state);

		assertThat(new BigDecimal(1_000_000), equalTo(player.getMoney()));
		assertThat(1, equalTo(player.getEstates().iterator().next().getBuilding().getBuildingType().getRequiredParcels()));
		assertThat("1234", equalTo(player.getEstates().iterator().next().getBuilding().getCityId()));
		Mockito.verify(state).removeBuildingId("abc");
	}
	
	@Test
	public void whenBuyBuildingWithEstateWrongIdShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		GameState state = Mockito.mock(GameState.class);
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		BuyBuildingAction action = new BuyBuildingAction(UUID.randomUUID().toString(), "abc", buildingType.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("could not be found");
		
		cut.handle(action, player, state);

		assertThat(new BigDecimal(1_000_000), equalTo(player.getMoney()));
		assertThat(1, equalTo(player.getEstates().iterator().next().getBuilding().getBuildingType()));
	}
	
	@Test
	public void whenBuyBuildingWithNotEnoughMoneyShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(100_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		Mockito.when(state.getBuildingTypeById(buildingType.getId())).thenReturn(buildingType);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", buildingType.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("is not enough to pay");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenBuyTooBigBuildingForEstateShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.ONE_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		BuildingType buildingType = new BuildingType(2, new BigDecimal(900_000));
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		Mockito.when(state.getBuildingTypeById(buildingType.getId())).thenReturn(buildingType);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", buildingType.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("is not big enough to build a building of type");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenBuyDuplicateBuildingShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		estate.setBuilding(new Building("abc", buildingType, estate.getCityId()));
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		Mockito.when(state.getBuildingTypeById(buildingType.getId())).thenReturn(buildingType);
		
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", buildingType.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("already has a building");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenBuyBuildingWithUnknownBuildingTypeShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		Mockito.when(state.isBuildingIdExisting("abc")).thenReturn(true);
		
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		BuyBuildingAction action = new BuyBuildingAction(estate.getId(), "abc", buildingType.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("A building type with");
		
		cut.handle(action, player, state);
	}
}
