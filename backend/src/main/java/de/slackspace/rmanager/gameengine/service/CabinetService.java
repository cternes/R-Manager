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
			list.add(new Cabinet(new BigDecimal(25_000), new BigDecimal(1650), 150, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(2000), 180, type));
			list.add(new Cabinet(new BigDecimal(35_000), new BigDecimal(2300), 210, type));
			list.add(new Cabinet(new BigDecimal(40_000), new BigDecimal(2640), 240, type));
			list.add(new Cabinet(new BigDecimal(65_000), new BigDecimal(4290), 390, type));
			list.add(new Cabinet(new BigDecimal(80_000), new BigDecimal(5280), 480, type));
			list.add(new Cabinet(new BigDecimal(105_000), new BigDecimal(6930), 630, type));
			list.add(new Cabinet(new BigDecimal(120_000), new BigDecimal(7920), 720, type));
			list.add(new Cabinet(new BigDecimal(142_000), new BigDecimal(9372), 852, type));
			list.add(new Cabinet(new BigDecimal(150_000), new BigDecimal(9900), 900, type));
		}
		else if(type.equals(DepartmentType.Dininghall)) {
			list.add(new Cabinet(new BigDecimal(300), new BigDecimal(20), 27, type));
			list.add(new Cabinet(new BigDecimal(600), new BigDecimal(40), 54, type));
			list.add(new Cabinet(new BigDecimal(900), new BigDecimal(60), 81, type));
			list.add(new Cabinet(new BigDecimal(500), new BigDecimal(33), 45, type));
			list.add(new Cabinet(new BigDecimal(250), new BigDecimal(17), 23, type));
			list.add(new Cabinet(new BigDecimal(620), new BigDecimal(41), 56, type));
			list.add(new Cabinet(new BigDecimal(800), new BigDecimal(53), 72, type));
			list.add(new Cabinet(new BigDecimal(1250), new BigDecimal(83), 113, type));
			list.add(new Cabinet(new BigDecimal(330), new BigDecimal(22), 30, type));
		}
		else if(type.equals(DepartmentType.Laundry)) {
			list.add(new Cabinet(new BigDecimal(22_000), new BigDecimal(1320), 132, type));
			list.add(new Cabinet(new BigDecimal(18_500), new BigDecimal(1110), 111, type));
			list.add(new Cabinet(new BigDecimal(27_000), new BigDecimal(1620), 162, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(1800), 180, type));
			list.add(new Cabinet(new BigDecimal(15_000), new BigDecimal(900), 90, type));
			list.add(new Cabinet(new BigDecimal(21_000), new BigDecimal(1260), 126, type));
			list.add(new Cabinet(new BigDecimal(16_000), new BigDecimal(960), 96, type));
			list.add(new Cabinet(new BigDecimal(25_000), new BigDecimal(1500), 150, type));
			list.add(new Cabinet(new BigDecimal(17_000), new BigDecimal(1020), 102, type));
			list.add(new Cabinet(new BigDecimal(33_000), new BigDecimal(1980), 198, type));
		}
		else if(type.equals(DepartmentType.Facilities)) {
			list.add(new Cabinet(new BigDecimal(20_000), new BigDecimal(700), 120, type));
			list.add(new Cabinet(new BigDecimal(28_000), new BigDecimal(980), 168, type));
			list.add(new Cabinet(new BigDecimal(30_000), new BigDecimal(1050), 180, type));
			list.add(new Cabinet(new BigDecimal(55_000), new BigDecimal(1925), 330, type));
			list.add(new Cabinet(new BigDecimal(40_000), new BigDecimal(1400), 240, type));
			list.add(new Cabinet(new BigDecimal(33_000), new BigDecimal(1155), 198, type));
			list.add(new Cabinet(new BigDecimal(48_000), new BigDecimal(1680), 288, type));
		}
		else if(type.equals(DepartmentType.Reefer)) {
			list.add(new Cabinet(new BigDecimal(5000), new BigDecimal(300), 35, type));
			list.add(new Cabinet(new BigDecimal(6000), new BigDecimal(360), 42, type));
			list.add(new Cabinet(new BigDecimal(10_000), new BigDecimal(600), 70, type));
			list.add(new Cabinet(new BigDecimal(9500), new BigDecimal(570), 67, type));
			list.add(new Cabinet(new BigDecimal(7500), new BigDecimal(450), 53, type));
			list.add(new Cabinet(new BigDecimal(9000), new BigDecimal(540), 63, type));
			list.add(new Cabinet(new BigDecimal(10_200), new BigDecimal(612), 72, type));
			list.add(new Cabinet(new BigDecimal(8000), new BigDecimal(480), 56, type));
			list.add(new Cabinet(new BigDecimal(6500), new BigDecimal(390), 46, type));
			list.add(new Cabinet(new BigDecimal(7100), new BigDecimal(426), 50, type));
		}
		
		return list;
	}
}
