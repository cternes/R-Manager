package de.slackspace.rmanager.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.slackspace.rmanager.RManagerApplication;
import de.slackspace.rmanager.database.PlayerRepository;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.MatchStatus;
import de.slackspace.rmanager.domain.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RManagerApplication.class)
public class MatchResourceIT {

	@Autowired
	private PlayerRepository playerRepo;
	
	@Autowired
	private MatchResource cut;
	
	@Autowired
	private PlayerResource playerResource;
	
	@Test
	public void whenCreateMatchWithValidPlayerShouldStoreMatch() {
		Player playerOne = playerResource.createPlayer("p1");
		
		GameMatch match = cut.createMatch(playerOne.getToken());
		
		assertThat(match.getPlayer1().getId(), is(equalTo(playerOne.getId())));
		assertThat(match.getPlayer2(), is(nullValue()));
		assertThat(match.getStatus(), is(equalTo(MatchStatus.WAITINGFORPLAYERS)));
	}
	
	@Test
	public void whenJoinMatchWithValidPlayerShouldSetPlayerTwo() {
		Player playerOne = playerResource.createPlayer("player1");
		Player playerTwo = playerResource.createPlayer("player2");
		
		GameMatch match = cut.createMatch(playerOne.getToken());
		match = cut.joinMatch(match.getToken(), playerTwo.getToken());
		
		assertThat(match.getPlayer1().getId(), is(equalTo(playerOne.getId())));
		assertThat(match.getPlayer2().getId(), is(equalTo(playerTwo.getId())));
		assertThat(match.getStatus(), is(equalTo(MatchStatus.TURNP1)));
	}
	
	@Test
	public void whenSingleMatchIsWaitingForPlayerGetMatchesShouldReturnOne() {
		Player playerOne = playerResource.createPlayer("player1");
		GameMatch match = cut.createMatch(playerOne.getToken());
		
		assertThat(match.getId(), is(equalTo(cut.getMatchesWaitingForPlayer().getId())));
	}
	
	@Test
	public void whenMultipleMatchesWaitingForPlayerGetMatchesShouldReturnOne() {
		Player playerOne = playerResource.createPlayer("player1");
		GameMatch matchOne = cut.createMatch(playerOne.getToken());
		cut.createMatch(playerOne.getToken());
		cut.createMatch(playerOne.getToken());
		
		assertThat(matchOne.getId(), is(equalTo(cut.getMatchesWaitingForPlayer().getId())));
	}
}
