package de.slackspace.rmanager.gameengine.domain;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GameStateTest {

	@Test
	public void whenCheckingIsBuildingIdExistingShouldReturnTrue() {
		GameState cut = new GameState();
		
		List<String> buildingIds = Arrays.asList("abc", "def", "123456789");
		cut.setBuildingIds(buildingIds);
		
		Assert.assertTrue(cut.isBuildingIdExisting("def"));
	}
}
