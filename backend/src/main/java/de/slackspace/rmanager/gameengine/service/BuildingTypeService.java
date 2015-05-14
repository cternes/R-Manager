package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.BuildingType;

public class BuildingTypeService {

	public List<BuildingType> createBuildingTypes() {
		List<BuildingType> list = new ArrayList<>();
		
		list.add(new BuildingType("Type A", 1, new BigDecimal(500_000), new BigDecimal("250")));
		list.add(new BuildingType("Type B", 2, new BigDecimal(900_000), new BigDecimal("500")));
		list.add(new BuildingType("Type C", 3, new BigDecimal(1_300_000), new BigDecimal("750")));
		list.add(new BuildingType("Type D", 4, new BigDecimal(1_600_000), new BigDecimal("1000")));
		
		return list;
	}
}
