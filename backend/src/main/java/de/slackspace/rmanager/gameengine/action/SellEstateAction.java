package de.slackspace.rmanager.gameengine.action;

public class SellEstateAction implements GameAction {

	private String id;
	
	protected SellEstateAction() {
	}
	
	public SellEstateAction(String id) {
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public int getType() {
		return GameAction.SELL_ESTATE;
	}

}
