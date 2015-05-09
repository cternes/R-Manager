package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;

public class CityServiceTest {

	private CityService cut = new CityService();
	
	@Test
	public void whenCreateCityShouldReturnCityWithEstates() {
		City city = cut.createCity("Test", new BigDecimal("1.0"));
		
		Assert.assertEquals("Test", city.getName());
		Assert.assertTrue(new BigDecimal("1.0").equals(city.getRateOfPriceIncrease()));
		Assert.assertNotNull(city.getEstates());
	}
	
	@Test
	public void whenCreateCityShouldReturnCityWithAtLeastOneEstateOfEachSize() {
		City city = cut.createCity("Test", new BigDecimal("1.0"));
		
		List<Estate> estates = city.getEstates();
		
		Assert.assertNotNull(getEstateByType(estates, EstateType.ONE_PARCEL));
		Assert.assertNotNull(getEstateByType(estates, EstateType.TWO_PARCEL));
		Assert.assertNotNull(getEstateByType(estates, EstateType.THREE_PARCEL));
		Assert.assertNotNull(getEstateByType(estates, EstateType.FOUR_PARCEL));
	}
	
	@Test
	public void whenCreateCitiesShouldReturnEightCities() {
		List<City> cities = cut.createCities();
		
		Assert.assertEquals(8, cities.size());
		
		for (City city : cities) {
			Assert.assertTrue(city.getEstates().size() >= 4);
		}
	}
	
	private Estate getEstateByType(List<Estate> estates, EstateType type) {
		for (Estate estate : estates) {
			if(estate.getEstateType().equals(type)) {
				return estate;
			}
		}
		
		return null;
	}
}
