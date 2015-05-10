package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;

public interface GameActionHandler {

	public boolean canHandle(GameAction action);
	
	public void handle(GameAction action, RManagerPlayer player, GameState state);

}
