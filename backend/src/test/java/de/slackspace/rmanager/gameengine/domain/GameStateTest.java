package de.slackspace.rmanager.gameengine.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GameStateTest {

	@Test
	public void whenCheckingIsBuildingIdExistingShouldReturnTrue() {
		GameState cut = new GameState();
		
		List<String> buildingIds = Arrays.asList("abc", "def", "123456789");
		cut.setBuildingIds(buildingIds);
		
		assertThat(cut.isBuildingIdExisting("def"), equalTo(true));
	}
}
