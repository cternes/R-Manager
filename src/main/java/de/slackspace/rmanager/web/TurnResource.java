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
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.domain.Turn;
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
		
		byte[] decodedTurnData = Base64.decodeBase64(turnData);
		
		turnRepo.save(new Turn(match, player, decodedTurnData));
		
		TurnResult turnResult = gameEngine.makeTurn(match.getMatchData(), decodedTurnData, match.getPlayer1().equals(player));
		
		if(turnResult.hasMatchEnded()) {
			match.setMatchResult(turnResult.getMatchResult());
		}
		
		match.setMatchData(turnResult.getMatchData());
		return matchRepo.save(match);
	}
}
