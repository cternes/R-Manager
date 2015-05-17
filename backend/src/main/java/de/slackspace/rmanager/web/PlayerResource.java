package de.slackspace.rmanager.web;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.GeneralWebException;
import de.slackspace.rmanager.exception.InvalidOperationException;

@RestController
@RequestMapping("/players")
@Transactional
public class PlayerResource {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	MatchRepository matchRepo;
	
	@Autowired
	PlayerRepository playerRepo;
	
	private MessageDigest md;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Player createPlayer(@RequestParam("name") String name, @RequestParam("password") String password) {
		if(name == null || name.isEmpty()) {
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player must have a name.");
		}
		
		if(password == null || password.isEmpty()) {
			logger.error("A player must have a password.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player must have a password.");
		}
		
		Player player = playerRepo.findByName(name);
		
		if(player != null) {
			logger.error("A player with name '" + name + "'already exists.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player with name '" + name + "' already exists.");
		}
		
		return playerRepo.save(new Player(name, encryptPassword(password)));
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}")
	@ResponseBody
	public Player getPlayer(@PathVariable(value = "name") String id) {
		Player player = playerRepo.findByToken(id);
		
		if(player == null) {
			logger.error("A player with id " + id + " does not exists.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player with id " + id + " does not exists.");
		}
		
		return player;
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "{id}/matches")
	@ResponseBody
	public List<GameMatch> getActiveMatches(@PathVariable String id) {
		
		Player player = playerRepo.findByToken(id);
		
		if(player == null) {
			logger.error("A player with id " + id + " does not exists.");
			throw new InvalidOperationException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A player with id " + id + " does not exists.");
		}
		
		return matchRepo.findActiveMatchesByPlayer(player.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/login")
	@ResponseBody
	public Player loginPlayer(@RequestParam("name") String name, @RequestParam("password") String password) {
		Player player = playerRepo.findByName(name);
		
		if(player == null) {
			logger.error("A player with name " + name + " does not exists.");
			throw new GeneralWebException(HttpStatus.FORBIDDEN, "FORBIDDEN", "Could not login player. Username or password wrong.");
		}
		
		if(player.getPassword().equals(encryptPassword(password))) {
			return player;
		}
		else {
			logger.error("Password for player with name " + name + " is wrong.");
			throw new GeneralWebException(HttpStatus.FORBIDDEN, "FORBIDDEN", "Could not login player. Username or password wrong.");
		}
	}
	
	protected String encryptPassword(String password) {
		if (md == null) {
			try {
				md = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				logger.error("Unknown algorithm", e);
			}
		}

		try {
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return Base64.encodeBase64String(digest).toString();
		} catch (UnsupportedEncodingException e) {
			logger.error("Unsupported coding", e);
		}
		
		return null;
	}
}
