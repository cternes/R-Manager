package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.Person;

public class PersonnelServiceTest {

	PersonnelService cut = new PersonnelService();
	
	@Test
	public void whenCreatePersonShouldReturnPersonAndValidateAttributes() {
		for (int i = 0; i < 100; i++) {
			Person person = cut.createPerson(new BigDecimal("1.2"));
			
			Assert.assertTrue(person.getAge() <= 65);
			Assert.assertTrue(person.getAge() >= 1);
			
			Assert.assertTrue(person.getPower() <= 10);
			Assert.assertTrue(person.getPower() >= 1);

			Assert.assertTrue(person.getQuality() <= 10);
			Assert.assertTrue(person.getQuality() >= 1);	

			Assert.assertTrue(person.getMonthlyCosts().compareTo(new BigDecimal(239)) == 1);
			Assert.assertTrue(person.getMonthlyCosts().compareTo(new BigDecimal(2401)) == -1);
		} 
	}
}
