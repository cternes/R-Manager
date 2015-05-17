package de.slackspace.rmanager.web;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.Player;
import de.slackspace.rmanager.exception.GeneralWebException;
import de.slackspace.rmanager.exception.InvalidOperationException;

public class PlayerResourceTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private PlayerResource cut = new PlayerResource();
	
	@Test
	public void whenValidShouldCreatePlayer() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		cut.createPlayer("test", "testPwd");
		
		Mockito.verify(playerRepository).save(Mockito.any(Player.class));
	}
	
	@Test
	public void whenNameIsEmptyShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("A player must have a name");
		
		cut.createPlayer("", "test");
	}
	
	@Test
	public void whenNameIsNullShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("A player must have a name");
		
		cut.createPlayer(null, "test");
	}
	
	@Test
	public void whenPasswordIsNullShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("A player must have a password");
		
		cut.createPlayer("test", null);
	}
	
	@Test
	public void whenPasswordIsEmptyShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("A player must have a password");
		
		cut.createPlayer("test", "");
	}
	
	@Test
	public void whenDuplicateNameIsGivenShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;

		Mockito.when(playerRepository.findByName("abc")).thenReturn(new Player("abc", "testPwd"));
		
		exception.expect(InvalidOperationException.class);
		exception.expectMessage("A player with name 'abc' already exists");
		
		cut.createPlayer("abc", "test");
	}
	
	@Test
	public void whenLoginPlayerWithValidPasswordShouldReturnPlayer() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		Player player = new Player("abc", cut.encryptPassword("testPwd"));
		Mockito.when(playerRepository.findByName("abc")).thenReturn(player);		
		
		Assert.assertNotNull(cut.loginPlayer("abc", "testPwd"));
	}
	
	@Test
	public void whenLoginPlayerWithWrongPasswordShouldThrowException() {
		PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
		cut.playerRepo = playerRepository;
		
		Player player = new Player("abc", cut.encryptPassword("testPwd"));
		Mockito.when(playerRepository.findByName("abc")).thenReturn(player);		
		
		exception.expect(GeneralWebException.class);
		exception.expectMessage("Could not login player");
		
		Assert.assertNotNull(cut.loginPlayer("abc", "wrongPassword"));
	}
}
