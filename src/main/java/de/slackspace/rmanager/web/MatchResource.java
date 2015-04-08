package de.slackspace.rmanager.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.MatchStatus;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;
import de.slackspace.rmanager.exception.UnknownObjectException;

@RestController
@RequestMapping("/matches")
@Transactional
public class MatchResource {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public GameMatch createMatch(String player1Id) {
		Player player1 = playerRepo.findOne(player1Id);
		
		if(player1 == null) {
			logger.warn("The requested player '"+ player1Id +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The requested player '"+ player1Id +"' could not be found");
		}
		
		GameMatch match = new GameMatch(player1);
		return matchRepo.save(match);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}")
	@ResponseBody
	public GameMatch getMatch(@PathVariable String id) {
		GameMatch match = matchRepo.findOne(id);
		
		if(match == null) {
			logger.warn("The requested match '"+ id +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The requested match '"+ id +"' could not be found");
		}
		
		return match;
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "{id}/join")
	@ResponseBody
	public GameMatch joinMatch(@PathVariable String id, String playerId) {
		GameMatch match = matchRepo.findOne(id);
		
		if(match == null) {
			logger.warn("The requested match '"+ id +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The requested match '"+ id +"' could not be found");
		}
		
		if(match.getPlayer1() != null && match.getPlayer2() != null) {
			logger.warn("The requested match '"+ id + "'cannot be joined, because it is already full.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "The requested match '"+ id +"'cannot be joined, because it is already full.");
		}
		
		if(playerId.equals(match.getPlayer1().getId())) {
			logger.warn("The requested match "+ id +"' cannot be joined, because the player '"+ playerId +"' has already joined.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "The requested match "+ id +"' cannot be joined, because the player '"+ playerId +"' has already joined.");
		}
		
		Player player2 = playerRepo.findOne(playerId);
		
		if(player2 == null) {
			logger.warn("The requested player '"+ playerId +"' could not be found");
			throw new UnknownObjectException(HttpStatus.NOT_FOUND, "OBJECT_UNKNOWN", "The requested player '"+ playerId +"' could not be found");
		}
		
		match.setPlayer2(player2);
		match.setStatus(MatchStatus.TURNP1);
		
		return matchRepo.save(match);
	}
}
