package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.BuyBuildingAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyBuildingActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof BuyBuildingAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		BuyBuildingAction buyAction = (BuyBuildingAction) action;
		
		Estate estate = state.getEstateById(buyAction.getEstateId());
		BuildingType buildingType = state.getBuildingTypeById(buyAction.getBuildingTypeId());
		
		if(estate == null) {
			throw new GameException("The estate with id '" + buyAction.getEstateId() + "' could not be found");
		}
		
		if(buildingType == null) {
			throw new GameException("A building type with id '"+ buyAction.getBuildingTypeId() + "' does not exists");
		}
		
		if(!estate.canBuild(buildingType)) {
			throw new GameException("The estate '"+ buyAction.getEstateId() + "' is not big enough to build a building of type '" + buildingType.getRequiredParcels() + "'");
		}
		
		if(estate.getBuilding() != null) {
			throw new GameException("The estate '"+ buyAction.getEstateId() + "' already has a building");
		}
		
		if(!state.isBuildingIdExisting(buyAction.getBuildingId())) {
			throw new GameException("A building with id '"+ buyAction.getBuildingId() + "' does not exists");
		}
		
		Building building = new Building(buyAction.getBuildingId(), buildingType, estate.getCityId());
		
		if(!player.canBuy(building.getPrice())) {
			throw new GameException("The player's money '" + player.getMoney() + "' is not enough to pay '" + building.getPrice() + "'");
		}
		
		player.pay(building.getPrice());
		player.getTurnStatistics().increaseConstructionCosts(building.getPrice());
		estate.setBuilding(building);
		
		state.removeBuildingId(buyAction.getBuildingId());
	}

	
}
