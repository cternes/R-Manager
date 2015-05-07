package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.BuyCabinetAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.Department;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyCabinetActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof BuyCabinetAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		BuyCabinetAction buyAction = (BuyCabinetAction) action;
		
		String buildingId = buyAction.getBuildingId();
		String cabinetId = buyAction.getCabinetId();
		
		Building building = player.getBuildingById(buildingId);
		if(building == null) {
			throw new GameException("The player does not own a building with id '" + buildingId + "'");
		}
		
		Cabinet cabinet = state.getAvailableCabinetById(cabinetId);
		if(cabinet == null) {
			throw new GameException("A cabinet with id '" + cabinetId + "' does not exist on free market");
		}
		
		if(!player.canBuy(cabinet.getPrice())) {
			throw new GameException("The player's money '" + player.getMoney() + "' is not enough to pay '" + cabinet.getPrice() + "'");
		}
		
		player.buy(cabinet.getPrice());
		
		Department department = building.getDepartmentByType(cabinet.getDepartmentType());
		department.getCabinets().add(cabinet);
	}

}
