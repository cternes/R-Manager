package de.slackspace.rmanager.web;

import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.UnknownPlayerException;

public class MatchResourceTest {

	private MatchResource cut = new MatchResource();
	
	@Test
	public void whenCreateMatchIsCalledWithValidPlayerShouldCreateMatch() {
		String playerId = UUID.randomUUID().toString();
		
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		Mockito.when(playerRepository.findOne(playerId)).thenReturn(new Player());
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		cut.createMatch(playerId);
		
		Mockito.verify(playerRepository).findOne(playerId);
		Mockito.verify(matchRepository).save(Mockito.any(GameMatch.class));
	}
	
	@Test(expected=UnknownPlayerException.class)
	public void whenCreateMatchIsCalledWithUnknownPlayerShouldThrowException() {
		String playerId = UUID.randomUUID().toString();
		
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		cut.createMatch(playerId);
	}
	
	
}
