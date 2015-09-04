package de.slackspace.rmanager.gameengine.service;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.Person;

public class PersonnelServiceTest {

	PersonnelService cut = new PersonnelService();
	
	@Test
	public void whenCreatePersonShouldReturnPersonAndValidateAttributes() {
		for (int i = 0; i < 100; i++) {
			Person person = cut.createPerson(new BigDecimal("1.2"));

			assertThat(person.getAge(), lessThanOrEqualTo(65));
			assertThat(person.getAge(), greaterThanOrEqualTo(16));
			
			assertThat(person.getPower(), lessThanOrEqualTo(10));
			assertThat(person.getPower(), greaterThanOrEqualTo(1));

			assertThat(person.getQuality(), lessThanOrEqualTo(10));
			assertThat(person.getQuality(), greaterThanOrEqualTo(1));

			assertThat(person.getMonthlyCosts(), greaterThan(new BigDecimal(239)));
			assertThat(person.getMonthlyCosts(), lessThan(new BigDecimal(2401)));
		} 
	}
	
	@Test
	public void whenCreatePersonnelShouldReturnSixPersons() {
		City city = new City("Stuttgart", BigDecimal.ONE);
		List<Person> personnel = cut.createPersonnel(city);

		assertThat(personnel, hasSize(6));
	}
}
