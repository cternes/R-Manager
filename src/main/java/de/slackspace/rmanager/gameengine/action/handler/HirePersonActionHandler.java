package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.action.HirePersonAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.Department;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class HirePersonActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof HirePersonAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		HirePersonAction hireAction = (HirePersonAction) action;
		
		String buildingId = hireAction.getBuildingId();
		String personId = hireAction.getPersonId();
		
		Building building = player.getBuildingById(buildingId);
		if(building == null) {
			throw new GameException("The player does not own a building with id '" + buildingId + "'");
		}
		
		Person person = state.getAvailablePersonnelById(personId);
		if(person == null) {
			throw new GameException("A person with id '" + personId + "' does not exist on free market");
		}
		
		Department department = building.getDepartmentByType(person.getDepartmentType());
		department.getPersonnel().add(person);
	}

}
