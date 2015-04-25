package de.slackspace.rmanager.game;

import org.springframework.stereotype.Component;

@Component
public interface GameEngine {

	public byte[] startNewGame();
	
	public TurnResult makeTurn(byte[] rawMatchData, byte[] rawTurnData, boolean isPlayerOne);
}
