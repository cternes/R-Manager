package de.slackspace.rmanager.gameengine.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.Cabinet;
import de.slackspace.rmanager.gameengine.domain.DepartmentType;

public class CabinetServiceTest {

	CabinetService cut = new CabinetService();
	
	@Test
	public void whenCreateKitchenCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Kitchen);
		
		assertThat(cabinets, hasSize(10));
	}
	
	@Test
	public void whenCreateDiningHallCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Dininghall);
		
		assertThat(cabinets, hasSize(9));
	}
	
	@Test
	public void whenCreateLaundryCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Laundry);

		assertThat(cabinets, hasSize(10));
	}
	
	@Test
	public void whenCreateFacilitiesCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Facilities);

		assertThat(cabinets, hasSize(7));
	}
	
	@Test
	public void whenCreateReeferCabinetShouldReturnCabinet() {
		List<Cabinet> cabinets = cut.createCabinet(DepartmentType.Reefer);
		
		assertThat(cabinets, hasSize(10));
	}
}
