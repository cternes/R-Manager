package de.slackspace.rmanager.gameengine.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.BuildingType;

public class BuildingTypeServiceTest {

	BuildingTypeService cut = new BuildingTypeService();
	
	@Test
	public void whenCreateBuildingTypesShouldReturn4Types() {
		List<BuildingType> list = cut.createBuildingTypes();
		
		assertThat(list, hasSize(4));

		assertThat(new BigDecimal(500_000), equalTo(list.get(0).getPrice()));
		assertThat(1, equalTo(list.get(0).getRequiredParcels()));
		assertThat("", not(equalTo(list.get(0).getId())));
	}
}
