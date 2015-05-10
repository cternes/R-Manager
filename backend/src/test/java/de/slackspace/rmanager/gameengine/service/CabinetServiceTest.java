package de.slackspace.rmanager.gameengine.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;

public class CabinetServiceTest {

	CabinetService cut = new CabinetService();
	
	@Test
	public void whenCreateKitchenCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Kitchen);
		
		Assert.assertEquals(10, cabinets.size());
	}
	
	@Test
	public void whenCreateDiningHallCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Dininghall);
		
		Assert.assertEquals(9, cabinets.size());
	}
	
	@Test
	public void whenCreateLaundryCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Laundry);
		
		Assert.assertEquals(10, cabinets.size());
	}
	
	@Test
	public void whenCreateFacilitiesCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Facilities);
		
		Assert.assertEquals(7, cabinets.size());
	}
	
	@Test
	public void whenCreateReeferCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Reefer);
		
		Assert.assertEquals(10, cabinets.size());
	}
}
