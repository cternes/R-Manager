package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class PersonTest {

	@Test
	public void whenGetCapacityWithVeryGoodPersonShouldReturnFive() {
		Person person = new Person(BigDecimal.TEN, 30, 10, 10, DepartmentType.Kitchen);
		
		Assert.assertEquals(5, person.getCapacity());
	}
	
	@Test
	public void whenGetCapacityWithVeryPoorPersonShouldReturnOne() {
		Person person = new Person(BigDecimal.TEN, 30, 1, 1, DepartmentType.Kitchen);
		
		Assert.assertEquals(1, person.getCapacity());
	}
	
	@Test
	public void whenGetCapacityWithModeratePersonShouldReturnOne() {
		Person person = new Person(BigDecimal.TEN, 30, 5, 8, DepartmentType.Kitchen);
		
		Assert.assertEquals(3, person.getCapacity());
	}
}
