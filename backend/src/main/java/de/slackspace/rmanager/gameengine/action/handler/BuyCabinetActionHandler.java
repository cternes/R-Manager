package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;

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
		
		if(buyAction.getQuantity() < 1) {
			throw new GameException("A quantity < 1 is not allowed");
		}
		
		Building building = player.getBuildingById(buildingId);
		if(building == null) {
			throw new GameException("The player does not own a building with id '" + buildingId + "'");
		}
		
		Cabinet cabinet = state.getAvailableCabinetById(cabinetId);
		if(cabinet == null) {
			throw new GameException("A cabinet with id '" + cabinetId + "' does not exist on free market");
		}
		
		Department department = building.getDepartmentByType(cabinet.getDepartmentType());
		if(!department.canAddCabinet(cabinet.getRequiredSpaceUnits() * buyAction.getQuantity())) {
			throw new GameException("Not enough free space units in department '" + cabinet.getDepartmentType() + "' to add cabinet with '" + cabinet.getRequiredSpaceUnits() * buyAction.getQuantity() + "' required space units");
		}
		
		BigDecimal totalPrice = calculateTotalPrice(cabinet.getPrice(), buyAction.getQuantity());
		
		if(!player.canBuy(totalPrice)) {
			throw new GameException("The player's money '" + player.getMoney() + "' is not enough to pay '" + totalPrice + "'");
		}
		
		player.pay(totalPrice);
		cabinet.increaseQuantity(buyAction.getQuantity());
		
		department.getCabinets().add(cabinet);
	}

	private BigDecimal calculateTotalPrice(BigDecimal price, int quantity) {
		return price.multiply(new BigDecimal(quantity));
	}
}
