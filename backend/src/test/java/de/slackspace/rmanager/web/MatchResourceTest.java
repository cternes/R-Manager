package de.slackspace.rmanager.web;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.MatchStatus;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;
import de.slackspace.rmanager.exception.UnknownObjectException;
import de.slackspace.rmanager.game.GameEngine;

public class MatchResourceTest {

	@Test
	public void whenCreateMatchIsCalledWithValidPlayerShouldCreateMatch() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(new Player(""));
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		cut.createMatch(playerToken);
		
		Mockito.verify(cut.playerRepo).findByToken(playerToken);
		Mockito.verify(matchRepository).save(Mockito.any(GameMatch.class));
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenCreateMatchIsCalledWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		cut.createMatch(playerToken);
	}
	
	@Test
	public void whenGetMatchWithValidIdShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		
		GameMatch mockMatch = new GameMatch(new Player(""));
		Mockito.when(cut.matchRepo.findByToken(mockMatch.getToken())).thenReturn(mockMatch);
		
		GameMatch match = cut.getMatch(mockMatch.getToken());
		
		Assert.assertEquals(mockMatch, match);
		Mockito.verify(cut.matchRepo).findByToken(mockMatch.getToken());
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenGetMatchWithUnknownIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		cut.getMatch(matchToken);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenJoinMatchWithUnknownMatchIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		String playerToken = UUID.randomUUID().toString();
		
		cut.joinMatch(matchToken, playerToken);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenJoinMatchWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(match.getToken())).thenReturn(match);
		
		cut.joinMatch(match.getToken(), playerToken);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithPlayerNull() {
		MatchResource cut = createMatchResource();
		
		Player player = new Player("test");
		GameMatch match = new GameMatch(player);
		
		Mockito.when(cut.matchRepo.findByToken(match.getToken())).thenReturn(match);
		
		cut.joinMatch(match.getToken(), null);
	}
	
	@Test
	public void whenJoinMatchWithValidPlayerShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		Player playerTwo = new Player("p2");
		
		GameMatch match = new GameMatch(playerOne);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerOne.getToken())).thenReturn(playerOne);
		Mockito.when(cut.playerRepo.findByToken(playerTwo.getToken())).thenReturn(playerTwo);
		
		cut.joinMatch(matchToken, playerTwo.getToken());
		
		Mockito.verify(cut.matchRepo).save(match);
		Assert.assertEquals(match.getStatus(), MatchStatus.TURNP1);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithSamePlayerAsPlayer1ShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(player.getToken())).thenReturn(player);
		
		cut.joinMatch(matchToken, player.getToken());
		
		Mockito.verify(cut.matchRepo).save(match);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithAlreadyFullMatchShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerThreeToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		GameMatch match = new GameMatch(playerOne);
		
		Player playerTwo = new Player("test two");
		match.setPlayer2(playerTwo);

		Mockito.when(cut.matchRepo.findByToken(matchId)).thenReturn(match);
		
		cut.joinMatch(matchId, playerThreeToken);
	}
	
	private MatchResource createMatchResource() {
		MatchResource cut = new MatchResource();
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		cut.gameEngine = gameEngine;
		
		return cut;
		
	}
	
}
