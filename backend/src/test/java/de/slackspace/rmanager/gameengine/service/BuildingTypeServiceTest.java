package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.BuildingType;

public class BuildingTypeServiceTest {

	BuildingTypeService cut = new BuildingTypeService();
	
	@Test
	public void whenCreateBuildingTypesShouldReturn4Types() {
		List<BuildingType> list = cut.createBuildingTypes();
		
		Assert.assertEquals(4, list.size());
		
		Assert.assertEquals(new BigDecimal(500_000), list.get(0).getPrice());
		Assert.assertEquals(1, list.get(0).getRequiredParcels());
		Assert.assertNotEquals("", list.get(0).getId());
	}
}
