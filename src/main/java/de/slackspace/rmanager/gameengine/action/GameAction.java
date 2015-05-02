package de.slackspace.rmanager.gameengine.action;

public interface GameAction {

	public static final int BUY_ESTATE = 1;
	public static final int SELL_ESTATE = 2;
	
	public int getType();
}
