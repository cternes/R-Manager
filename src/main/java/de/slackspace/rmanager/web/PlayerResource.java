package de.slackspace.rmanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;

@RestController
@RequestMapping("/players")
@Transactional
public class PlayerResource {

	@Autowired
	PlayerRepository playerRepo;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Player createPlayer(String name) {
		Player player = playerRepo.findByName(name);
		
		if(player != null) {
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player with this name already exists.");
		}
		
		return playerRepo.save(new Player(name));
	}
}
