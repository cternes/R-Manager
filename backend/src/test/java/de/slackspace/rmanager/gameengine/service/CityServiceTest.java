package de.slackspace.rmanager.gameengine.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;

public class CityServiceTest {

	private CityService cut = new CityService();
	
	@Test
	public void whenCreateCityShouldReturnCityWithEstates() {
		City city = cut.createCity("Test", new BigDecimal("1.0"));
		
		assertThat(city.getName(), equalTo("Test"));
		assertThat(new BigDecimal("1.0"), equalTo(city.getRateOfPriceIncrease()));
		assertThat(city.getEstates(), not(equalTo(nullValue())));
	}
	
	@Test
	public void whenCreateCityShouldReturnCityWithAtLeastOneEstateOfEachSize() {
		City city = cut.createCity("Test", new BigDecimal("1.0"));
		
		List<Estate> estates = city.getEstates();

		assertThat(getEstateByType(estates, EstateType.ONE_PARCEL), not(equalTo(nullValue())));
		assertThat(getEstateByType(estates, EstateType.TWO_PARCEL), not(equalTo(nullValue())));
		assertThat(getEstateByType(estates, EstateType.THREE_PARCEL), not(equalTo(nullValue())));
		assertThat(getEstateByType(estates, EstateType.FOUR_PARCEL), not(equalTo(nullValue())));
	}
	
	@Test
	public void whenCreateCitiesShouldReturnEightCities() {
		List<City> cities = cut.createCities();
		
		assertThat(cities, hasSize(8));

		for (City city : cities) {
			assertThat(city.getEstates(), hasSize(greaterThanOrEqualTo(4)));	
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
