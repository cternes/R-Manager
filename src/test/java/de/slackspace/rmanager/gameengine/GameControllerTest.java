package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.GameState;
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
		
		// just for debug
//		for (City city : gameState.getCities()) {
//			System.out.println("");
//			System.out.println(city.getName());
//			System.out.println("===========");
//			
//			for (Estate estate: city.getEstates()) {
//				System.out.println("" + estate.getEstateType() + ":" + estate.getTotalPrice());
//			}
//		}
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
}
