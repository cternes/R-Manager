package de.slackspace.rmanager.gameengine.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class PersonTest {

	@Test
	public void whenGetCapacityWithVeryGoodPersonShouldReturnFive() {
		Person person = new Person(BigDecimal.TEN, 30, 10, 10, DepartmentType.Kitchen);
		
		assertThat(5, equalTo(person.getCapacity()));
	}
	
	@Test
	public void whenGetCapacityWithVeryPoorPersonShouldReturnOne() {
		Person person = new Person(BigDecimal.TEN, 30, 1, 1, DepartmentType.Kitchen);
		
		assertThat(1, equalTo(person.getCapacity()));
	}
	
	@Test
	public void whenGetCapacityWithModeratePersonShouldReturnOne() {
		Person person = new Person(BigDecimal.TEN, 30, 5, 8, DepartmentType.Kitchen);

		assertThat(3, equalTo(person.getCapacity()));
	}
}
