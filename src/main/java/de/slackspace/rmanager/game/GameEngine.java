package de.slackspace.rmanager.game;

public interface GameEngine {

	public byte[] startNewGame();
	
	public TurnResult makeTurn(byte[] rawMatchData, byte[] rawTurnData, boolean isPlayerOne);
}
