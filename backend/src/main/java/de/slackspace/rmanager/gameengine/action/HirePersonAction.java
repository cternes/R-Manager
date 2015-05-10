package de.slackspace.rmanager.gameengine.action;

public class HirePersonAction implements GameAction {

	private String buildingId;
	private String personId;
	
	protected HirePersonAction() {
	}
	
	public HirePersonAction(String buildingId, String personId) {
		setBuildingId(buildingId);
		setPersonId(personId);
	}
	
	@Override
	public int getType() {
		return GameAction.HIRE_PERSON;
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
