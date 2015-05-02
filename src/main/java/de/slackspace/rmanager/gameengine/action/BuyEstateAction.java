package de.slackspace.rmanager.gameengine.action;

public class BuyEstateAction implements GameAction {

	private String id;
	
	protected BuyEstateAction() {
	}
	
	public BuyEstateAction(String id) {
		setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
