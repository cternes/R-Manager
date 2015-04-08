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
		String playerId = UUID.randomUUID().toString();
		
		Mockito.when(cut.playerRepo.findOne(playerId)).thenReturn(new Player());
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		cut.createMatch(playerId);
		
		Mockito.verify(cut.playerRepo).findOne(playerId);
		Mockito.verify(matchRepository).save(Mockito.any(GameMatch.class));
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenCreateMatchIsCalledWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String playerId = UUID.randomUUID().toString();
		
		cut.createMatch(playerId);
	}
	
	@Test
	public void whenGetMatchWithValidIdShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		
		GameMatch mockMatch = new GameMatch();
		Mockito.when(cut.matchRepo.findOne(matchId)).thenReturn(mockMatch);
		
		GameMatch match = cut.getMatch(matchId);
		
		Assert.assertEquals(mockMatch, match);
		Mockito.verify(cut.matchRepo).findOne(matchId);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenGetMatchWithUnknownIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		
		cut.getMatch(matchId);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenJoinMatchWithUnknownMatchIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		cut.joinMatch(matchId, playerId);
	}
	
	@Test(expected=UnknownObjectException.class)
	public void whenJoinMatchWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findOne(matchId)).thenReturn(match);
		
		cut.joinMatch(matchId, playerId);
	}
	
	@Test
	public void whenJoinMatchWithValidPlayerShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerOneId = UUID.randomUUID().toString();
		String playerTwoId = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		playerOne.setId(playerOneId);
		
		Player playerTwo = new Player("p2");
		playerTwo.setId(playerTwoId);
		
		GameMatch match = new GameMatch(playerOne);
		Mockito.when(cut.matchRepo.findOne(matchId)).thenReturn(match);
		Mockito.when(cut.playerRepo.findOne(playerOneId)).thenReturn(playerOne);
		Mockito.when(cut.playerRepo.findOne(playerTwoId)).thenReturn(playerTwo);
		
		cut.joinMatch(matchId, playerTwoId);
		
		Mockito.verify(cut.matchRepo).save(match);
		Assert.assertEquals(match.getStatus(), MatchStatus.TURNP1);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithSamePlayerAsPlayer1ShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		player.setId(playerId);
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findOne(matchId)).thenReturn(match);
		Mockito.when(cut.playerRepo.findOne(playerId)).thenReturn(player);
		
		cut.joinMatch(matchId, playerId);
		
		Mockito.verify(cut.matchRepo).save(match);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenJoinMatchWithAlreadyFullMatchShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerOneId = UUID.randomUUID().toString();
		String playerTwoId = UUID.randomUUID().toString();
		String playerThreeId = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test");
		playerOne.setId(playerOneId);
		GameMatch match = new GameMatch(playerOne);
		
		Player playerTwo = new Player("test two");
		playerTwo.setId(playerTwoId);
		match.setPlayer2(playerTwo);

		Mockito.when(cut.matchRepo.findOne(matchId)).thenReturn(match);
		
		cut.joinMatch(matchId, playerThreeId);
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
