package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class DepartmentTest {

	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForPersonnel() {
		Department department = new Department();
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0));
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0));
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0));
		department.getPersonnel().add(new Person(BigDecimal.ONE, 0, 0, 0));
		
		Assert.assertEquals(new BigDecimal(31), department.getMonthlyCosts());
	}
	
	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForCabinet() {
		Department department = new Department();
		department.getCabinets().add(new Cabinet(BigDecimal.ONE));
		department.getCabinets().add(new Cabinet(BigDecimal.ONE));
		
		Assert.assertEquals(new BigDecimal(2), department.getMonthlyCosts());
	}
	
	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForPersonnelAndCabinet() {
		Department department = new Department();
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0));
		department.getPersonnel().add(new Person(BigDecimal.ONE, 0, 0, 0));
		department.getCabinets().add(new Cabinet(BigDecimal.ONE));
		department.getCabinets().add(new Cabinet(BigDecimal.ONE));
		
		Assert.assertEquals(new BigDecimal(13), department.getMonthlyCosts());
	}
}
