package de.slackspace.rmanager.web;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void whenCreateMatchIsCalledWithValidPlayerShouldCreateMatch() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(new Player("", "testPwd"));
		
		MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
		cut.matchRepo = matchRepository;
		
		cut.createMatch(playerToken);
		
		Mockito.verify(cut.playerRepo).findByToken(playerToken);
		Mockito.verify(matchRepository).save(Mockito.any(GameMatch.class));
	}
	
	@Test
	public void whenCreateMatchIsCalledWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("could not be found");
		
		cut.createMatch(playerToken);
	}
	
	@Test
	public void whenGetMatchWithValidIdShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		
		GameMatch mockMatch = new GameMatch(new Player("", "testPwd"));
		Mockito.when(cut.matchRepo.findByToken(mockMatch.getToken())).thenReturn(mockMatch);
		
		GameMatch match = cut.getMatch(mockMatch.getToken());
		
		Assert.assertEquals(mockMatch, match);
		Mockito.verify(cut.matchRepo).findByToken(mockMatch.getToken());
	}
	
	@Test
	public void whenGetMatchWithUnknownIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("The requested match");
		
		cut.getMatch(matchToken);
	}
	
	@Test
	public void whenJoinMatchWithUnknownMatchIdShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		String playerToken = UUID.randomUUID().toString();
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("The requested match");
		
		cut.joinMatch(matchToken, playerToken);
	}
	
	@Test
	public void whenJoinMatchWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		String playerToken = UUID.randomUUID().toString();
		
		Player player = new Player("test", "testPwd");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(match.getToken())).thenReturn(match);
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("The requested player");
		
		cut.joinMatch(match.getToken(), playerToken);
	}
	
	@Test
	public void whenJoinMatchWithPlayerNull() {
		MatchResource cut = createMatchResource();
		
		Player player = new Player("test", "testPwd");
		GameMatch match = new GameMatch(player);
		
		Mockito.when(cut.matchRepo.findByToken(match.getToken())).thenReturn(match);
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("The playerId must be set");
		
		cut.joinMatch(match.getToken(), null);
	}
	
	@Test
	public void whenJoinMatchWithValidPlayerShouldReturnMatch() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test", "testPwd");
		Player playerTwo = new Player("p2", "testPwd");
		
		GameMatch match = new GameMatch(playerOne);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerOne.getToken())).thenReturn(playerOne);
		Mockito.when(cut.playerRepo.findByToken(playerTwo.getToken())).thenReturn(playerTwo);
		
		cut.joinMatch(matchToken, playerTwo.getToken());
		
		Mockito.verify(cut.matchRepo).save(match);
		Assert.assertEquals(match.getCurrentPlayer().getId(), playerOne.getId());
		Assert.assertEquals(match.getStatus(), MatchStatus.RUNNING);
	}
	
	@Test
	public void whenJoinMatchWithSamePlayerAsPlayer1ShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test", "testPwd");
		GameMatch match = new GameMatch(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(player.getToken())).thenReturn(player);
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("cannot be joined, because the player");
		
		cut.joinMatch(matchToken, player.getToken());
	}
	
	@Test
	public void whenJoinMatchWithAlreadyFullMatchShouldThrowException() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		String playerThreeToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test", "testPwd");
		GameMatch match = new GameMatch(playerOne);
		
		Player playerTwo = new Player("test two", "testPwd");
		match.setPlayer2(playerTwo);

		Mockito.when(cut.matchRepo.findByToken(matchId)).thenReturn(match);
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("cannot be joined, because it is already full");
		
		cut.joinMatch(matchId, playerThreeToken);
	}
	
	@Test
	public void whenDeleteRunningMatchShouldEndMatchAndUpdatePlayerStats() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test", "testPwd");
		Player playerTwo = new Player("test 2", "testPwd");
		GameMatch match = new GameMatch(playerOne);
		match.setPlayer2(playerTwo);
		match.setStatus(MatchStatus.RUNNING);
		
		Mockito.when(cut.matchRepo.findByToken(matchId)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerOne.getToken())).thenReturn(playerOne);
		
		cut.deleteMatch(matchId, playerOne.getToken());
		
		Assert.assertEquals(MatchStatus.CANCELLED, match.getStatus());
		Assert.assertEquals(null, match.getCurrentPlayer());
		Assert.assertEquals(playerOne.getId(), match.getWinner().getId());
		
		Assert.assertEquals(1, playerOne.getLosses());
		Assert.assertEquals(1, playerTwo.getWins());
		
		Mockito.verify(cut.matchRepo).save(match);
		Mockito.verify(cut.playerRepo).save(playerOne);
		Mockito.verify(cut.playerRepo).save(playerTwo);
	}
	
	@Test
	public void whenDeleteMatchWithUnknownMatchShouldThrowException() {
		MatchResource cut = createMatchResource();
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("The requested match");
		
		cut.deleteMatch("abc", "xyz");
	}
	
	@Test
	public void whenDeleteMatchWithUnknownPlayerShouldThrowException() {
		MatchResource cut = createMatchResource();
		
		exception.expect(UnknownObjectException.class);
		exception.expectMessage("The requested player");
		
		GameMatch match = new GameMatch(new Player("test", "testPwd"));
		Mockito.when(cut.matchRepo.findByToken(match.getToken())).thenReturn(match);
		
		cut.deleteMatch(match.getToken(), "xyz");
	}
	
	@Test
	public void whenDeleteNotRunningMatchShouldEndMatch() {
		MatchResource cut = createMatchResource();
		String matchId = UUID.randomUUID().toString();
		
		Player playerOne = new Player("test", "testPwd");
		GameMatch match = new GameMatch(playerOne);
		match.setStatus(MatchStatus.WAITINGFORPLAYERS);
		
		Mockito.when(cut.matchRepo.findByToken(matchId)).thenReturn(match);
		Mockito.when(cut.playerRepo.findByToken(playerOne.getToken())).thenReturn(playerOne);
		
		cut.deleteMatch(matchId, playerOne.getToken());
		
		Assert.assertEquals(MatchStatus.CANCELLED, match.getStatus());
		Assert.assertEquals(null, match.getCurrentPlayer());
		Assert.assertEquals(null, match.getWinner());
		
		Assert.assertEquals(0, playerOne.getLosses());
		
		Mockito.verify(cut.matchRepo).save(match);
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
