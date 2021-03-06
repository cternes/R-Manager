package de.slackspace.rmanager.gameengine.action.handler;

import de.slackspace.rmanager.gameengine.action.BuyEstateAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyEstateActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof BuyEstateAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		BuyEstateAction buyEstateAction = (BuyEstateAction) action;
		
		Estate estate = state.getEstateById(buyEstateAction.getId());
		
		if(estate == null) {
			throw new GameException("The estate with id '" + buyEstateAction.getId() + "' could not be found");
		}
		
		if(!player.canBuy(estate.getTotalPrice())) {
			throw new GameException("The player's money '" + player.getMoney() + "' is not enough to pay '" + estate.getTotalPrice() + "'");
		}
		
		if(player.getEstates().contains(estate)) {
			throw new GameException("The player owns already the estate with id '" + estate.getId() + "'");
		}
		
		player.pay(estate.getTotalPrice());
		player.getTurnStatistics().increaseConstructionCosts(estate.getTotalPrice());

		estate.setSold(true);
		player.getEstates().add(estate);
	}

}
