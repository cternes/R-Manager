package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;

import de.slackspace.rmanager.gameengine.action.BuyShareAction;
import de.slackspace.rmanager.gameengine.action.GameAction;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.domain.Share;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class BuyShareActionHandler implements GameActionHandler {

	@Override
	public boolean canHandle(GameAction action) {
		return action instanceof BuyShareAction;
	}

	@Override
	public void handle(GameAction action, RManagerPlayer player, GameState state) {
		BuyShareAction buyShareAction = (BuyShareAction) action;
		
		if(buyShareAction.getPercent() < 1) {
			throw new GameException("Cannot buy less than 1% of a share");
		}
		
		if(buyShareAction.getPercent() > 100) {
			throw new GameException("Cannot buy more than 100% of a share");
		}
		
		Share marketShare = state.getShareById(buyShareAction.getShareId());
		
		if(marketShare == null){
			throw new GameException("The share with id '" + buyShareAction.getShareId() + "' could not be found"); 
		}
		
		if(!marketShare.canReduceShareBy(buyShareAction.getPercent())) {
			throw new GameException("Cannot buy '"+ buyShareAction.getPercent() +"' percent of share with id '" + buyShareAction.getShareId() + "' because only '"+ marketShare.getSharePercent() + "' percent are available on the market");
		}
		
		BigDecimal totalPrice = marketShare.getTotalPrice(buyShareAction.getPercent());
		
		if(!player.canBuy(totalPrice)) {
			throw new GameException("The player's money '" + player.getMoney() + "' is not enough to pay '" + totalPrice + "'");
		}
		
		Share playerShare = player.getShareById(marketShare.getId());
		if(playerShare == null) {
			playerShare = new Share(marketShare.getId(), buyShareAction.getPercent(), marketShare.getValue());
		}
		
		player.pay(totalPrice);
		player.getShares().add(playerShare);
		
		marketShare.reduceShare(buyShareAction.getPercent());
	}

}
