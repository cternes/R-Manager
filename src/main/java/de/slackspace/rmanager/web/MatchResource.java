package de.slackspace.rmanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.UnknownPlayerException;

@RestController
@RequestMapping("/matches")
@Transactional
public class MatchResource {

	@Autowired
	private MatchRepository matchRepo;
	
	@Autowired
	private PlayerRepository playerRepo;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public GameMatch createMatch(String player1Id) {
		Player player1 = playerRepo.findOne(player1Id);
		
		if(player1 == null) {
			throw new UnknownPlayerException();
		}
		
		GameMatch match = new GameMatch(player1);
		return matchRepo.save(match);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}")
	@ResponseBody
	public GameMatch getMatch(@PathVariable String id) throws NoSuchRequestHandlingMethodException {
		GameMatch match = matchRepo.findOne(id);
		
		if(match == null) {
			throw new UnknownPlayerException();
		}
		
		return match;
	}
}
