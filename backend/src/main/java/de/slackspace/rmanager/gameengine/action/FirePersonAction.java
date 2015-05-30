package de.slackspace.rmanager.gameengine.action;

public class FirePersonAction extends HirePersonAction {

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

}
