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
		String matchToken = UUID.randomUUID().toString();
		
		GameMatch mockMatch = new GameMatch(new Player(""));
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(mockMatch);
		
		GameMatch match = cut.getMatch(matchToken);
		
		Assert.assertEquals(mockMatch, match);
		Mockito.verify(cut.matchRepo).findByToken(matchToken);
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
		String matchToken = UUID.randomUUID().toString();
		String playerToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		
		cut.joinMatch(matchToken, playerToken);
	}
	
	@Test
	public void whenJoinMatchWithValidPlayerShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		String playerOneToken = UUID.randomUUID().toString();
		String playerTwoToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		playerOne.setToken(playerOneToken);
		
		Player playerTwo = new Player("p2");
		playerTwo.setToken(playerTwoToken);
		
		GameMatch match = new GameMatch(playerOne);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerOneToken)).thenReturn(playerOne);
		Mockito.when(cut.playerRepo.findByToken(playerTwoToken)).thenReturn(playerTwo);
		
		cut.joinMatch(matchToken, playerTwoToken);
		
		Mockito.verify(cut.matchRepo).save(match);
		Assert.assertEquals(match.getStatus(), MatchStatus.TURNP1);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithSamePlayerAsPlayer1ShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		String playerToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		player.setToken(playerToken);
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(player);
		
		cut.joinMatch(matchToken, playerToken);
		
		Mockito.verify(cut.matchRepo).save(match);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithAlreadyFullMatchShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerOneToken = UUID.randomUUID().toString();
		String playerTwoToken = UUID.randomUUID().toString();
		String playerThreeToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		playerOne.setToken(playerOneToken);
		GameMatch match = new GameMatch(playerOne);
		
		Player playerTwo = new Player("test two");
		playerTwo.setToken(playerTwoToken);
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
		
		return cut;
		
	}
	
}
