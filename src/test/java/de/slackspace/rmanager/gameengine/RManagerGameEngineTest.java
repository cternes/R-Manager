package de.slackspace.rmanager.gameengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.SellEstateAction;


public class RManagerGameEngineTest {

	@Test
	public void whenTakingTurnMustDeserializeDataCorrectly() {
		RManagerGameEngine cut = new RManagerGameEngine();
		
		GameController gameController = Mockito.mock(GameController.class);
		cut.controller = gameController;
		
		byte[] gameState = cut.startNewGame("p1", "p2");
		
		List<GameAction> actions = new ArrayList<>();
		actions.add(new BuyEstateAction("abc"));
		actions.add(new SellEstateAction("def"));
		byte[] turnData = serialize(actions);
		
		cut.makeTurn(gameState, turnData, "p1");
	}
	
	private byte[] serialize(List<GameAction> data) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(outputStream, data);
			
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
