package de.slackspace.rmanager.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Turn takeTurn(@PathVariable(value = "id") String id, @RequestParam String playerToken) {
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
		
		return turnRepo.save(new Turn(match, player));
	}
}
