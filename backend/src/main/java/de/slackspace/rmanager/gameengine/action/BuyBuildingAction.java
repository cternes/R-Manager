package de.slackspace.rmanager.gameengine.action;


public class BuyBuildingAction implements GameAction {

	private String estateId;
	private String buildingId;
	private String buildingTypeId;
	
	protected BuyBuildingAction() {
	}
	
	public BuyBuildingAction(String estateId, String buildingId, String buildingTypeId) {
		setEstateId(estateId);
		setBuildingId(buildingId);
		setBuildingTypeId(buildingTypeId);
	}
	
	@Override
	public int getType() {
		return GameAction.BUY_BUILDING;
	}

	public String getEstateId() {
		return estateId;
	}

	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}

	public String getBuildingTypeId() {
		return buildingTypeId;
	}

	public void setBuildingTypeId(String buildingType) {
		this.buildingTypeId = buildingType;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

}
