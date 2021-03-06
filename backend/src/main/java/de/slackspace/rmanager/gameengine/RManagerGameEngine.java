package de.slackspace.rmanager.gameengine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.slackspace.rmanager.game.GameEngine;
import de.slackspace.rmanager.game.TurnResult;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.json.GameActionDeserializer;

@Component
public class RManagerGameEngine implements GameEngine {

	private Log logger = LogFactory.getLog(getClass());
	
	GameActionDeserializer actionDeserializer = ObjectFactory.getGameActionDeserializer();
	GameController controller = ObjectFactory.getGameControllerInstance();
	
	@Override
	public byte[] startNewGame(String playerOneName, String playerTwoName) {
		GameState state = controller.startNewGame(playerOneName, playerTwoName);
		
		return serialize(state);
	}

	@Override
	public TurnResult makeTurn(byte[] rawMatchData, byte[] rawTurnData, String playerName) {
		GameState state = deserialize(rawMatchData, new TypeReference<GameState>(){});
		List<GameAction> actions = deserializeGameActions(rawTurnData);
		
		GameState updatedState = controller.endTurn(state, playerName, actions);
		
		return new TurnResult(serialize(updatedState), null);
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
	
	private <T> T deserialize(byte[] matchData, TypeReference<T> type) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(matchData, type);
		} catch (IOException e) {
			logger.error("Could not deserialize GameState", e);
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<GameAction> deserializeGameActions(byte[] matchData) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			SimpleModule module = new SimpleModule();
			module.addDeserializer(List.class, actionDeserializer);
			mapper.registerModule(module);
			
			return mapper.readValue(matchData, List.class);
		} catch (IOException e) {
			logger.error("Could not deserialize GameState", e);
			throw new RuntimeException(e);
		}
	}

}
