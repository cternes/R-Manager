package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyEstateActionHandlerTest {

	BuyEstateActionHandler cut = new BuyEstateActionHandler();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		Assert.assertTrue(cut.canHandle(new BuyEstateAction("1234")));
	}
	
	@Test
	public void whenBuyEstateShouldReduceMoneyAndAddEstate() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		City city = new City("Munich", new BigDecimal("1.0"));
		List<Estate> estates = new ArrayList<>();
		Estate estate = new Estate(EstateType.FIVE_PARCEL, city.getRateOfPriceIncrease(), BigDecimal.ONE, city.getId());
		estates.add(estate);
		city.setEstates(estates);
		
		GameState state = new GameState();
		List<City> cities = new ArrayList<>();
		cities.add(city);
		state.setCities(cities);
		
		BuyEstateAction action = new BuyEstateAction(estate.getId());
		
		cut.handle(action, player, state);
		
		BigDecimal expectedMoney = new BigDecimal(1_500_000).subtract(estate.getTotalPrice());
		Assert.assertEquals(expectedMoney, player.getMoney());
		Assert.assertEquals(estate.getId(), player.getEstates().iterator().next().getId());
		Assert.assertEquals(1, player.getEstates().size());
	}
	
	@Test(expected=GameException.class)
	public void whenBuyDuplicateEstateShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FIVE_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		
		BuyEstateAction action = new BuyEstateAction(estate.getId());
		
		cut.handle(action, player, state);
	}
}
