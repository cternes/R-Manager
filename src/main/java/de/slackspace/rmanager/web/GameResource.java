package de.slackspace.rmanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import de.slackspace.rmanager.database.GameRepository;
import de.slackspace.rmanager.domain.Game;

@RestController
@RequestMapping("/game")
public class GameResource {

	@Autowired
	private GameRepository repo;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Game createGame() {
		Game game = new Game();
		repo.save(game);
		return game;
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}")
	@ResponseBody
	public Game getGame(@PathVariable long id) throws NoSuchRequestHandlingMethodException {
		
		throw new NoSuchRequestHandlingMethodException("show", Game.class);
	}
}
