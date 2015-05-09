package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.gameengine.action.BuyShareAction;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.domain.Share;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyShareActionHandlerTest {

	BuyShareActionHandler cut = new BuyShareActionHandler();
	
	@Test
	public void whenBuyShareShouldReduceAvailableShareAndAddShareToPlayer() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Share share = new Share(UUID.randomUUID().toString(), 25, BigDecimal.TEN);
		BuyShareAction action = new BuyShareAction(share.getId(), 10);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getShareById(share.getId())).thenReturn(share);
		
		cut.handle(action, player, state);
		
		Assert.assertEquals(1, player.getShares().size());
		Assert.assertEquals(share.getId(), player.getShares().iterator().next().getId());
		Assert.assertEquals(10, player.getShares().iterator().next().getSharePercent());
		Assert.assertEquals(15, share.getSharePercent());
	}
	
	@Test(expected=GameException.class)
	public void whenInvalidShareShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		BuyShareAction action = new BuyShareAction("abc", 10);
		
		GameState state = Mockito.mock(GameState.class);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuyNegativeSharePercentShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Share share = new Share(UUID.randomUUID().toString(), 0, BigDecimal.TEN);
		BuyShareAction action = new BuyShareAction(share.getId(), -10);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getShareById(share.getId())).thenReturn(share);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuyOver100SharePercentShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Share share = new Share(UUID.randomUUID().toString(), 0, BigDecimal.TEN);
		BuyShareAction action = new BuyShareAction(share.getId(), 101);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getShareById(share.getId())).thenReturn(share);
		
		cut.handle(action, player, state);
	}
	
	@Test(expected=GameException.class)
	public void whenBuyTooMuchShareShouldThrowException() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(1_500_000));
		
		Share share = new Share(UUID.randomUUID().toString(), 50, BigDecimal.TEN);
		BuyShareAction action = new BuyShareAction(share.getId(), 51);
		
		GameState state = Mockito.mock(GameState.class);
		Mockito.when(state.getShareById(share.getId())).thenReturn(share);
		
		cut.handle(action, player, state);
	}
}
