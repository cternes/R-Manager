package de.slackspace.rmanager.gameengine.action;

public class FirePersonAction implements GameAction {

	private String buildingId;
	private String personId;
	
	protected FirePersonAction() {
	}
	
	public FirePersonAction(String buildingId, String personId) {
		setBuildingId(buildingId);
		setPersonId(personId);
	}
	
	@Override
	public int getType() {
		return GameAction.FIRE_PERSON;
	}
	
	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

}
