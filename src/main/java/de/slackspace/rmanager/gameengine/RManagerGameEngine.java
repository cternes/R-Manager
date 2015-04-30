package de.slackspace.rmanager.gameengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.slackspace.rmanager.game.GameEngine;
import de.slackspace.rmanager.game.TurnResult;
import de.slackspace.rmanager.gameengine.domain.GameState;

@Component
public class RManagerGameEngine implements GameEngine {

	@Override
	public byte[] startNewGame(String playerOneName, String playerTwoName) {
		GameController controller = new GameController();
		GameState state = controller.startNewGame(playerOneName, playerTwoName);
		
		return serialize(state);
	}

	@Override
	public TurnResult makeTurn(byte[] rawMatchData, byte[] rawTurnData, boolean isPlayerOne) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private byte[] serialize(GameState data) {
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
	
//	private GuessData deserializeTurnData(byte[] turnData) {
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			GuessData guessData = mapper.readValue(turnData, GuessData.class);
//			return guessData;
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}

}
