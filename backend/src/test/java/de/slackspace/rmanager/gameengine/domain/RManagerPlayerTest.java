package de.slackspace.rmanager.gameengine.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class RManagerPlayerTest {

	@Test
	public void whenGetCapitalShouldSumValueOfAllEstatesBuildingsAndCabinets() {
		RManagerPlayer player = new RManagerPlayer();
		player.setMoney(new BigDecimal(100_000));
		
		// price of estate = 50.000
		Estate estateOne = new Estate(EstateType.TWO_PARCEL, BigDecimal.ONE, BigDecimal.ONE, "abc");

		// price of building = 500.000
		BuildingType buildingType = new BuildingType(1, new BigDecimal(500_000));
		estateOne.setBuilding(new Building("1", buildingType, "abc"));
		player.getEstates().add(estateOne);
		
		// price of estate = 120.000
		Estate estateTwo = new Estate(EstateType.FOUR_PARCEL, new BigDecimal("1.2"), BigDecimal.ONE, "abc");
		
		// price of building = 900.000
		BuildingType buildingTypeTwo = new BuildingType(2, new BigDecimal(900_000));
		Building building = new Building("2", buildingTypeTwo, "abc");
		
		// price of kitchen cabinet = 60000
		building.getDepartmentByType(DepartmentType.Kitchen).getCabinets().add(new Cabinet(new BigDecimal("30000"), BigDecimal.ONE, 1, DepartmentType.Kitchen, 2));
		estateTwo.setBuilding(building);
		player.getEstates().add(estateTwo);
		
		assertThat(new BigDecimal("1730000.0"), equalTo(player.getCapital()));
	}
}
