package de.slackspace.rmanager.gameengine.action;

public class BuyShareAction implements GameAction {

	private String shareId;
	private int percent;

	protected BuyShareAction() {
	}
	
	public BuyShareAction(String shareId, int percent) {
		setShareId(shareId);
		setPercent(percent);
	}
	
	@Override
	public int getType() {
		return GameAction.BUY_SHARE;
	}

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
	
}
