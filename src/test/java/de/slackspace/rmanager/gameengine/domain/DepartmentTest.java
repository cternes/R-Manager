package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class DepartmentTest {

	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForPersonnel() {
		Department department = new Department();
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0, DepartmentType.Kitchen));
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0, DepartmentType.Kitchen));
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0, DepartmentType.Kitchen));
		department.getPersonnel().add(new Person(BigDecimal.ONE, 0, 0, 0, DepartmentType.Kitchen));
		
		Assert.assertEquals(new BigDecimal(31), department.getMonthlyCosts());
	}
	
	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForCabinet() {
		Department department = new Department();
		Cabinet cabinet = new Cabinet(BigDecimal.ONE, BigDecimal.ONE, 1, DepartmentType.Kitchen);
		cabinet.increaseQuantity(2);
		department.getCabinets().add(cabinet);
		
		Assert.assertEquals(new BigDecimal(2), department.getMonthlyCosts());
	}
	
	@Test
	public void whenGetMonthlyCostsShouldReturnMonthlyCostsForPersonnelAndCabinet() {
		Department department = new Department();
		department.getPersonnel().add(new Person(BigDecimal.TEN, 0, 0, 0, DepartmentType.Kitchen));
		department.getPersonnel().add(new Person(BigDecimal.ONE, 0, 0, 0, DepartmentType.Kitchen));
		
		Cabinet cabinet = new Cabinet(BigDecimal.ONE, BigDecimal.ONE, 1, DepartmentType.Kitchen);
		cabinet.increaseQuantity(2);
		department.getCabinets().add(cabinet);
		
		Assert.assertEquals(new BigDecimal(13), department.getMonthlyCosts());
	}
	
}
