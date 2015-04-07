package de.slackspace.rmanager.web;

import org.springframework.beans.factory.annotation.Autowired;
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
import de.slackspace.rmanager.exception.DuplicatePlayerException;
import de.slackspace.rmanager.exception.InvalidMatchStateException;
import de.slackspace.rmanager.exception.UnknownMatchException;
import de.slackspace.rmanager.exception.UnknownPlayerException;

@RestController
@RequestMapping("/matches")
@Transactional
public class MatchResource {

	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
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
	public GameMatch getMatch(@PathVariable String id) {
		GameMatch match = matchRepo.findOne(id);
		
		if(match == null) {
			throw new UnknownMatchException();
		}
		
		return match;
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "{id}/join")
	@ResponseBody
	public GameMatch joinMatch(@PathVariable String id, String playerId) {
		GameMatch match = matchRepo.findOne(id);
		
		if(match == null) {
			throw new UnknownMatchException();
		}
		
		if(match.getPlayer1() != null && match.getPlayer2() != null) {
			throw new InvalidMatchStateException();
		}
		
		if(playerId.equals(match.getPlayer1().getId())) {
			throw new DuplicatePlayerException();
		}
		
		Player player2 = playerRepo.findOne(playerId);
		
		if(player2 == null) {
			throw new UnknownPlayerException();
		}
		
		match.setPlayer2(player2);
		match.setStatus(MatchStatus.TURNP1);
		
		return matchRepo.save(match);
	}
}
