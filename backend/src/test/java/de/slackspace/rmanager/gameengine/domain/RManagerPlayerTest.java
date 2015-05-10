package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.Building;
import de.slackspace.rmanager.gameengine.domain.BuildingType;
import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;
import de.slackspace.rmanager.gameengine.domain.RManagerPlayer;

public class RManagerPlayerTest {

	@Test
	public void whenGetCapitalShouldSumValueOfAllEstatesBuildingsAndCabinets() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(100_000));
		
		// price of estate = 50.000
		Estate estateOne = new Estate(EstateType.TWO_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "abc");

		// price of building = 500.000
		estateOne.setBuilding(new Building("1", BuildingType.ONE_PARCEL, "abc"));
		player.getEstates().add(estateOne);
		
		// price of estate = 120.000
		Estate estateTwo = new Estate(EstateType.FOUR_PARCEL, new BigDecimal("1.2"), BigDecimal.ONE, "abc");
		
		// price of building = 900.000
		Building building = new Building("2", BuildingType.TWO_PARCEL, "abc");
		
		// price of kitchen cabinet = 60000
		building.getDepartmentByType(DepartmentType.Kitchen).getCabinets().add(new Cabinet(new BigDecimal("30000"), BigDecimal.ONE, 1, DepartmentType.Kitchen, 2));
		estateTwo.setBuilding(building);
		player.getEstates().add(estateTwo);
		
		Assert.assertEquals(new BigDecimal("1730000.0"), player.getCapital());
	}
}
