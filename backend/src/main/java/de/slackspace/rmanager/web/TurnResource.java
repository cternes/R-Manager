package de.slackspace.rmanager.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.database.TurnRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.MatchStatus;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.domain.Turn;
import de.slackspace.rmanager.exception.InvalidOperationException;
import de.slackspace.rmanager.exception.UnknownObjectException;
import de.slackspace.rmanager.game.GameEngine;
import de.slackspace.rmanager.game.TurnResult;

@RestController
@RequestMapping("/matches/{id}/turns")
@Transactional
public class TurnResource {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	TurnRepository turnRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	GameEngine gameEngine;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public GameMatch takeTurn(@PathVariable(value = "id") String id, @RequestParam String playerToken, @RequestParam byte[] turnData) {
		Player player = playerRepo.findByToken(playerToken);
		
		if(player == null) {
			logger.warn("The player '"+ playerToken +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The player '"+ playerToken +"' could not be found");
		}
		
		GameMatch match = matchRepo.findByToken(id);
		
		if(match == null) {
			logger.warn("The match '"+ id +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The match '"+ id +"' could not be found");
		}
		
		// validate waiting status
		if(match.getStatus() == MatchStatus.WAITINGFORPLAYERS) {
			logger.warn("The match '"+ id +"' is waiting for players, taking a turn is not allowed");
			throw new InvalidOperationException(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "The match '"+ id +"' is waiting for players, taking a turn is not allowed");
		}
		
		if(match.getStatus() != MatchStatus.RUNNING) {
			logger.warn("The match '"+ id +"' is currently not running, taking a turn is not allowed");
			throw new InvalidOperationException(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "The match '"+ id +"' is currently not running, taking a turn is not allowed");
		}
		
		// validate player
		if(match.getCurrentPlayer() == null) {
			logger.warn("The match '"+ id +"' is not having a current player, taking a turn is not allowed");
			throw new InvalidOperationException(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "The match '"+ id +"' is not having a current player, taking a turn is not allowed");
		}
		
		if(match.getCurrentPlayer().getId() != player.getId()) {
			logger.warn("The match '"+ id +"' is expecting another player to take a turn. Player '"+ player.getToken() + "' is not allowed to take a turn");
			throw new InvalidOperationException(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "The match '"+ id +"' is expecting another player to take a turn. Player '"+ player.getToken() + "' is not allowed to take a turn");
		}
		
		byte[] decodedTurnData = Base64.decodeBase64(turnData);
		
		turnRepo.save(new Turn(match, player, decodedTurnData));
		
		TurnResult turnResult = gameEngine.makeTurn(match.getMatchData(), decodedTurnData, player.getName());
		
		if(turnResult == null) {
			throw new InvalidOperationException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "GameEngine has returned an unexpected result");
		}

		// switch current player
		if(match.getCurrentPlayer().getId() == match.getPlayer1().getId()) {
			match.setCurrentPlayer(match.getPlayer2());
		}
		else {
			match.setCurrentPlayer(match.getPlayer1());
		}
		
		// check if match has ended
		if(turnResult.hasMatchEnded()) {
			match.setMatchResult(turnResult.getMatchResult());
			match.setCurrentPlayer(null);
		}
		
		match.setMatchData(turnResult.getMatchData());
		return matchRepo.save(match);
	}
}
