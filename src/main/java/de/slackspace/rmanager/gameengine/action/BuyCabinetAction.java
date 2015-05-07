package de.slackspace.rmanager.gameengine.action;

public class BuyCabinetAction implements GameAction {

	private String buildingId;
	
	private String cabinetId;
	
	private int quantity;
	
	protected BuyCabinetAction() {
	}
	
	public BuyCabinetAction(String buildingId, String cabinetId, int quantity) {
		setBuildingId(buildingId);
		setCabinetId(cabinetId);
		setQuantity(quantity);
	}
	
	@Override
	public int getType() {
		return GameAction.BUY_CABINET;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
