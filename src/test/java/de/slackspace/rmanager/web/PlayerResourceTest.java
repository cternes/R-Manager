package de.slackspace.rmanager.web;


import org.junit.Test;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.InvalidOperationException;

public class PlayerResourceTest {

	private PlayerResource cut = new PlayerResource();
	
	@Test
	public void whenNameIsGivenShouldCreatePlayer() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		cut.createPlayer("test");
		
		Mockito.verify(playerRepository).save(Mockito.any(Player.class));
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenNameIsEmptyShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		cut.createPlayer("");
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenNameIsNullShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		cut.createPlayer(null);
	}
	
	@Test(expected=InvalidOperationException.class)
	public void whenDuplicateNameIsGivenShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;

		Mockito.when(playerRepository.findByName("abc")).thenReturn(new Player("abc"));
		cut.createPlayer("abc");
	}
}
