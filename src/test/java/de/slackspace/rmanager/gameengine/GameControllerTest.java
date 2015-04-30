package de.slackspace.rmanager.gameengine;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

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
	}
}
