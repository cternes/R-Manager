package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;
import de.slackspace.rmanager.gameengine.domain.Person;

public class PersonnelService {

	SecureRandom random = new SecureRandom();
	
	public List<Person> createPersonnel(City city) {
		List<Person> personnel = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			personnel.add(createPerson(city.getRateOfPriceIncrease()));
		}
		
		return personnel;
	}
	
	protected Person createPerson(BigDecimal rateOfPriceIncrease) {
		int power = random.nextInt(10) + 1;
		int quality = random.nextInt(10) + 1;
		int age = random.nextInt(49) + 16;
		
		BigDecimal monthlyCosts = new BigDecimal(power + quality).multiply(new BigDecimal(100)).multiply(rateOfPriceIncrease);
		
		return new Person(monthlyCosts, age, quality, power, DepartmentType.randomType());
	}
}
