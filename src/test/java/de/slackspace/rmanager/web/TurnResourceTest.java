package de.slackspace.rmanager.web;

import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.database.TurnRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;
import de.slackspace.rmanager.exception.UnknownObjectException;
import de.slackspace.rmanager.game.GameEngine;

public class TurnResourceTest {

	@Test(expected=UnknownObjectException.class)
	public void whenPlayerIsUnknownShouldThrowException() {
		TurnResource cut = createTurnResource();
		
		cut.takeTurn(UUID.randomUUID().toString(), UUID.randomUUID().toString(), new byte[0]);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenMatchIsUnknownShouldThrowException() {
		TurnResource cut = createTurnResource();
		
		String playerToken = UUID.randomUUID().toString();
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(new Player("test"));
		
		cut.takeTurn(UUID.randomUUID().toString(), playerToken, new byte[0]);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenGameEngineReturnsNullShouldThrowException() {
		TurnResource cut = createTurnResource();
		
		String playerToken = UUID.randomUUID().toString();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(new GameMatch(player, new byte[0]));
		
		cut.takeTurn(matchToken, playerToken, new byte[0]);
	}
	
	private TurnResource createTurnResource() {
		TurnResource cut = new TurnResource();
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		TurnRepository turnRepository = Mockito.mock(TurnRepository.class);
		cut.turnRepo = turnRepository;
		
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		cut.gameEngine = gameEngine;
		
		return cut;
	}
}
