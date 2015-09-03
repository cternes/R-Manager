package de.slackspace.rmanager.gameengine.action.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void whenCanHandleIsCalledWithCorrectClassShouldReturnTrue() {
		assertThat(cut.canHandle(new BuyEstateAction("1234")), equalTo(true));
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
		assertThat(expectedMoney, equalTo(player.getMoney()));
		assertThat(player.getEstates(), hasItem(estate));
		assertThat(player.getEstates(), hasSize(1));
		assertThat(state.getEstateById(estate.getId()).isSold(), equalTo(true));
	}
	
	@Test
	public void whenBuyDuplicateEstateShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Estate estate = new Estate(EstateType.FIVE_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "1234");
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getEstateById(estate.getId())).thenReturn(estate);
		
		BuyEstateAction action = new BuyEstateAction(estate.getId());
		
		exception.expect(GameException.class);
		exception.expectMessage("The player owns already the estate");
		
		cut.handle(action, player, state);
	}
}
