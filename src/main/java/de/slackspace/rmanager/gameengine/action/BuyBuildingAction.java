package de.slackspace.rmanager.gameengine.action;

import de.slackspace.rmanager.gameengine.domain.BuildingType;

public class BuyBuildingAction implements GameAction {

	private String estateId;
	private BuildingType buildingType;
	
	protected BuyBuildingAction() {
	}
	
	public BuyBuildingAction(String estateId, BuildingType buildingType) {
		setEstateId(estateId);
		setBuildingType(buildingType);
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

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

}
