package de.slackspace.rmanager.gameengine.action.handler;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.slackspace.rmanager.gameengine.action.FirePersonAction;
import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.GameState;
import de.slackspace.rmanager.gameengine.domain.Person;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;
import de.slackspace.rmanager.gameengine.exception.GameException;

public class FirePersonActionHandlerTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void whenPlayerHasNoEstateShouldThrowException() {
		FirePersonActionHandler cut = new FirePersonActionHandler();
		
		FirePersonAction action = new FirePersonAction("123", "465");
		
		RManagerPlayer player = new RManagerPlayer();
		GameState state = Mockito.mock(GameState.class);
		
		exception.expect(GameException.class);
		exception.expectMessage("The player does not own a building");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenPersonDoesNotExistsThrowException() {
		FirePersonActionHandler cut = new FirePersonActionHandler();
		
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		FirePersonAction action = new FirePersonAction(building.getId(), "def");
		
		RManagerPlayer player = new RManagerPlayer();
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		
		exception.expect(GameException.class);
		exception.expectMessage("A person with id 'def' does not exist in building with id 'abc'");
		
		cut.handle(action, player, state);
	}
	
	@Test
	public void whenValidShouldRemovePersonFromDepartment() {
		FirePersonActionHandler cut = new FirePersonActionHandler();
		
		Person person = new Person(BigDecimal.TEN, 20, 10, 10, DepartmentType.Kitchen);
		BuildingType buildingType = new BuildingType(4, new BigDecimal(1_600_000));
		Building building = new Building("abc", buildingType, "123");
		building.getDepartmentByType(DepartmentType.Kitchen).getPersonnel().add(person);
		
		FirePersonAction action = new FirePersonAction(building.getId(), person.getId());
		
		RManagerPlayer player = new RManagerPlayer();
		
		Estate estate = new Estate(EstateType.FOUR_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "123");
		estate.setBuilding(building);
		player.getEstates().add(estate);
		
		GameState state = Mockito.mock(GameState.class);
		
		Assert.assertEquals(1, building.getDepartmentByType(DepartmentType.Kitchen).getPersonnel().size());
		
		cut.handle(action, player, state);
		
		Assert.assertEquals(0, building.getDepartmentByType(DepartmentType.Kitchen).getPersonnel().size());
	}
	
}
