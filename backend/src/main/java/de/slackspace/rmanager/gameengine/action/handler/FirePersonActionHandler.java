package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.FirePersonAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.Department;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class FirePersonActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof FirePersonAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		FirePersonAction fireAction = (FirePersonAction) action;
		
		String buildingId = fireAction.getBuildingId();
		String personId = fireAction.getPersonId();
		
		Building building = player.getBuildingById(buildingId);
		if(building == null) {
			throw new GameException("The player does not own a building with id '" + buildingId + "'");
		}
		
		Person person = building.getPersonById(personId);
		if(person == null) {
			throw new GameException("A person with id '" + personId + "' does not exist in building with id '"+ buildingId + "'");
		}
		
		Department department = building.getDepartmentByType(person.getDepartmentType());
		department.getPersonnel().remove(person);
	}

}
