package de.slackspace.rmanager.gameengine.action;

public interface GameAction {

	public static final int BUY_ESTATE 		= 1;
	public static final int SELL_ESTATE 	= 2;
	public static final int BUY_BUILDING 	= 3;
	public static final int SELL_BUILDING 	= 4;
	public static final int HIRE_PERSON 	= 5;
	public static final int FIRE_PERSON 	= 6;
	public static final int BUY_CABINET 	= 7;
	public static final int SELL_CABINET 	= 8;
	
	public int getType();
}
