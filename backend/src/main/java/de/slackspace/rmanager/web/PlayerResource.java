package de.slackspace.rmanager.web;

import java.util.List;

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
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;

@RestController
@RequestMapping("/players")
@Transactional
public class PlayerResource {

	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Player createPlayer(@RequestParam String name) {
		if(name == null || name.isEmpty()) {
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player must have a name.");
		}
		
		Player player = playerRepo.findByName(name);
		
		if(player != null) {
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player with this name already exists.");
		}
		
		return playerRepo.save(new Player(name));
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}/matches")
	@ResponseBody
	public List<GameMatch> getActiveMatches(@PathVariable String id) {
		
		Player player = playerRepo.findByToken(id);
		
		return matchRepo.findActiveMatchesByPlayer(player.getId());
	}
}
