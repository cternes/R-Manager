package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.BuildingType;

public class BuildingTypeService {

	public List<BuildingType> createBuildingTypes() {
		List<BuildingType> list = new ArrayList<>();
		
		list.add(new BuildingType(1, new BigDecimal(500_000)));
		list.add(new BuildingType(2, new BigDecimal(900_000)));
		list.add(new BuildingType(3, new BigDecimal(1_300_000)));
		list.add(new BuildingType(4, new BigDecimal(1_600_000)));
		
		return list;
	}
}
