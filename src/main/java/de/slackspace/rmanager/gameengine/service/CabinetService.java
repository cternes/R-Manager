package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;

public class CabinetService {

	public List<Cabinet> createCabinet(DepartmentType type) {
		List<Cabinet> list = new ArrayList<>();
		
		if(type.equals(DepartmentType.Kitchen)) {
			list.add(new Cabinet(new BigDecimal(25_000), new BigDecimal(1650), 50, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(2000), 60, type));
			list.add(new Cabinet(new BigDecimal(35_000), new BigDecimal(2300), 70, type));
			list.add(new Cabinet(new BigDecimal(40_000), new BigDecimal(2640), 80, type));
			list.add(new Cabinet(new BigDecimal(65_000), new BigDecimal(4290), 130, type));
			list.add(new Cabinet(new BigDecimal(80_000), new BigDecimal(5280), 160, type));
			list.add(new Cabinet(new BigDecimal(105_000), new BigDecimal(6930), 210, type));
			list.add(new Cabinet(new BigDecimal(120_000), new BigDecimal(7920), 240, type));
			list.add(new Cabinet(new BigDecimal(142_000), new BigDecimal(9372), 284, type));
			list.add(new Cabinet(new BigDecimal(150_000), new BigDecimal(9900), 300, type));
		}
		else if(type.equals(DepartmentType.Dininghall)) {
			list.add(new Cabinet(new BigDecimal(300), new BigDecimal(20), 15, type));
			list.add(new Cabinet(new BigDecimal(600), new BigDecimal(40), 30, type));
			list.add(new Cabinet(new BigDecimal(900), new BigDecimal(60), 45, type));
			list.add(new Cabinet(new BigDecimal(500), new BigDecimal(33), 25, type));
			list.add(new Cabinet(new BigDecimal(250), new BigDecimal(17), 13, type));
			list.add(new Cabinet(new BigDecimal(620), new BigDecimal(41), 31, type));
			list.add(new Cabinet(new BigDecimal(800), new BigDecimal(53), 40, type));
			list.add(new Cabinet(new BigDecimal(1250), new BigDecimal(83), 62, type));
			list.add(new Cabinet(new BigDecimal(330), new BigDecimal(22), 16, type));
		}
		else if(type.equals(DepartmentType.Laundry)) {
			list.add(new Cabinet(new BigDecimal(22_000), new BigDecimal(1320), 44, type));
			list.add(new Cabinet(new BigDecimal(18_500), new BigDecimal(1110), 37, type));
			list.add(new Cabinet(new BigDecimal(27_000), new BigDecimal(1620), 54, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(1800), 60, type));
			list.add(new Cabinet(new BigDecimal(15_000), new BigDecimal(900), 30, type));
			list.add(new Cabinet(new BigDecimal(21_000), new BigDecimal(1260), 42, type));
			list.add(new Cabinet(new BigDecimal(16_000), new BigDecimal(960), 32, type));
			list.add(new Cabinet(new BigDecimal(25_000), new BigDecimal(1500), 50, type));
			list.add(new Cabinet(new BigDecimal(17_000), new BigDecimal(1020), 34, type));
			list.add(new Cabinet(new BigDecimal(33_000), new BigDecimal(1980), 66, type));
		}
		else if(type.equals(DepartmentType.Facilities)) {
			list.add(new Cabinet(new BigDecimal(20_000), new BigDecimal(700), 20, type));
			list.add(new Cabinet(new BigDecimal(28_000), new BigDecimal(980), 28, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(1050), 30, type));
			list.add(new Cabinet(new BigDecimal(55_000), new BigDecimal(1925), 55, type));
			list.add(new Cabinet(new BigDecimal(40_000), new BigDecimal(1400), 40, type));
			list.add(new Cabinet(new BigDecimal(33_000), new BigDecimal(1155), 33, type));
			list.add(new Cabinet(new BigDecimal(48_000), new BigDecimal(1680), 48, type));
		}
		else if(type.equals(DepartmentType.Reefer)) {
			list.add(new Cabinet(new BigDecimal(5000), new BigDecimal(300), 10, type));
			list.add(new Cabinet(new BigDecimal(6000), new BigDecimal(360), 12, type));
			list.add(new Cabinet(new BigDecimal(10_000), new BigDecimal(600), 20, type));
			list.add(new Cabinet(new BigDecimal(9500), new BigDecimal(570), 19, type));
			list.add(new Cabinet(new BigDecimal(7500), new BigDecimal(450), 15, type));
			list.add(new Cabinet(new BigDecimal(9000), new BigDecimal(540), 18, type));
			list.add(new Cabinet(new BigDecimal(10_200), new BigDecimal(612), 20, type));
			list.add(new Cabinet(new BigDecimal(8000), new BigDecimal(480), 16, type));
			list.add(new Cabinet(new BigDecimal(6500), new BigDecimal(390), 13, type));
			list.add(new Cabinet(new BigDecimal(7100), new BigDecimal(426), 14, type));
		}
		
		return list;
	}
}
