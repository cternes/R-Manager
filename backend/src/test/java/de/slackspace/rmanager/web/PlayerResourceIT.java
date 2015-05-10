package de.slackspace.rmanager.web;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.slackspace.rmanager.RManagerApplication;
import de.slackspace.rmanager.domain.GameMatch;
import de.slackspace.rmanager.domain.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RManagerApplication.class)
public class PlayerResourceIT {

	@Autowired
	private MatchResource matchResource;
	
	@Autowired
	private PlayerResource cut;
	
	@Test
	public void whenTwoMatchesAreAvailableForPlayerGetActiveMatchesShouldReturnTwoMatches() {
		Player playerOne = cut.createPlayer("p1");
		Player playerTwo = cut.createPlayer("p2");
		
		matchResource.createMatch(playerOne.getToken());
		matchResource.createMatch(playerOne.getToken());
		matchResource.createMatch(playerTwo.getToken());
		
		List<GameMatch> activeMatches = cut.getActiveMatches(playerOne.getToken());
		assertThat(2, is(equalTo(activeMatches.size())));
	}
}
