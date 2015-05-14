package de.slackspace.rmanager.web;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.MatchRepository;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.database.TurnRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.MatchResult;
import de.slackspace.rmanager.domain.MatchStatus;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;
import de.slackspace.rmanager.exception.UnknownObjectException;
import de.slackspace.rmanager.game.GameEngine;
import de.slackspace.rmanager.game.TurnResult;

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
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(new GameMatch(player));
		
		cut.takeTurn(matchToken, playerToken, new byte[0]);
	}
	
	@Test
	public void whenTakeTurnReturnsPlayer1WinsThenMatchShouldBeSavedAndMatchResultEqualsPlayer1Wins() {
		TurnResource cut = createTurnResource();
		
		String playerToken = UUID.randomUUID().toString();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch gameMatch = new GameMatch(player);
		gameMatch.setMatchData(new byte[0]);
		gameMatch.setCurrentPlayer(player);
		gameMatch.setStatus(MatchStatus.RUNNING);
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(gameMatch);
		Mockito.when(cut.gameEngine.makeTurn(new byte[0], new byte[0], "test")).thenReturn(new TurnResult(new byte[0], MatchResult.PLAYER1WINS));
		
		cut.takeTurn(matchToken, playerToken, new byte[0]);
		
		Mockito.verify(cut.matchRepo).save(gameMatch);
		Assert.assertEquals(gameMatch.getMatchResult(), MatchResult.PLAYER1WINS);
	}
	
	@Test
	public void whenTakeTurnReturnsDrawThenMatchShouldBeSavedAndMatchResultEqualsPlayer1Wins() {
		TurnResource cut = createTurnResource();
		
		String playerToken = UUID.randomUUID().toString();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch gameMatch = new GameMatch(player);
		gameMatch.setMatchData(new byte[0]);
		gameMatch.setCurrentPlayer(player);
		gameMatch.setStatus(MatchStatus.RUNNING);
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(gameMatch);
		Mockito.when(cut.gameEngine.makeTurn(new byte[0], new byte[0], "test")).thenReturn(new TurnResult(new byte[0], MatchResult.DRAW));
		
		cut.takeTurn(matchToken, playerToken, new byte[0]);
		
		Mockito.verify(cut.matchRepo).save(gameMatch);
		Assert.assertEquals(gameMatch.getMatchResult(), MatchResult.DRAW);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenMatchIsWaitingForPlayersShouldThrowException() {
		TurnResource cut = createTurnResource();
		
		String playerToken = UUID.randomUUID().toString();
		String matchToken = UUID.randomUUID().toString();
		
		Player player = new Player("test");
		GameMatch gameMatch = new GameMatch(player);
		
		Mockito.when(cut.playerRepo.findByToken(playerToken)).thenReturn(player);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(gameMatch);
		
		cut.takeTurn(matchToken, playerToken, new byte[0]);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenWrongPlayerIsTakingATurnShouldThrowException() {
		TurnResource cut = createTurnResource();
		
		String playerOneToken = UUID.randomUUID().toString();
		String matchToken = UUID.randomUUID().toString();
		
		Player playerOne = new Player("p1");
		Player playerTwo = new Player("p2");
		GameMatch gameMatch = new GameMatch(playerOne);
		gameMatch.setPlayer2(playerTwo);
		gameMatch.setCurrentPlayer(playerTwo);
		
		Mockito.when(cut.playerRepo.findByToken(playerOneToken)).thenReturn(playerOne);
		Mockito.when(cut.matchRepo.findByToken(matchToken)).thenReturn(gameMatch);
		Mockito.when(cut.gameEngine.makeTurn(new byte[0], new byte[0], "p1")).thenReturn(new TurnResult(new byte[0], MatchResult.DRAW));
		
		cut.takeTurn(matchToken, playerOneToken, new byte[0]);
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
