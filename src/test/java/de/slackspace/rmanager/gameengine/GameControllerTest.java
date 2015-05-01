package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.GameAction;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;

public class GameControllerTest {

	@Test
	public void whenStartingNewGameShouldReturnValidGameState() {
		GameState gameState = new GameController().startNewGame("p1", "p2");
		
		RManagerPlayer playerOne = gameState.getPlayerOne();
		Assert.assertEquals(new BigDecimal(1_500_000), playerOne.getMoney());
		Assert.assertEquals("Munich", playerOne.getCurrentCity().getName());
		Assert.assertEquals("p1", playerOne.getName());
		
		RManagerPlayer playerTwo = gameState.getPlayerTwo();
		Assert.assertEquals(new BigDecimal(1_500_000), playerTwo.getMoney());
		Assert.assertEquals("Munich", playerTwo.getCurrentCity().getName());
		Assert.assertEquals("p2", playerTwo.getName());
		
		System.out.println(gameState.getCities());
	}
	
	@Test
	public void whenEndingTurnShouldReturnValidGameState() {
		GameController cut = new GameController();
		GameState gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new GameAction());
		
		cut.endTurn(gameState, "p1", actions);
	}
}
