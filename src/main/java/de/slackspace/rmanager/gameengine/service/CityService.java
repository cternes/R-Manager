package de.slackspace.rmanager.gameengine.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import de.slackspace.rmanager.gameengine.domain.City;
import de.slackspace.rmanager.gameengine.domain.Estate;
import de.slackspace.rmanager.gameengine.domain.EstateType;

public class CityService {

	public List<City> createCities() {
		List<City> cities = new ArrayList<>();
		cities.add(createCity("Berlin", new BigDecimal("1.6")));
		cities.add(createCity("Bonn", new BigDecimal("1.4")));
		cities.add(createCity("Erfurt", new BigDecimal("1.0")));
		cities.add(createCity("Frankfurt", new BigDecimal("1.5")));
		cities.add(createCity("Hamburg", new BigDecimal("1.9")));
		cities.add(createCity("Munich", new BigDecimal("2.0")));
		cities.add(createCity("Nuremberg", new BigDecimal("2")));
		cities.add(createCity("Stuttgart", new BigDecimal("1.6")));

		return cities;
	}
	
	protected City createCity(String name, BigDecimal rateOfPriceIncrease) {
		City city = new City(name, rateOfPriceIncrease);
		city.getEstates().addAll(createCityEstates(city.getRateOfPriceIncrease()));
		
		return city;
	}
	
	private List<Estate> createCityEstates(BigDecimal rateOfPriceIncrease) {
		List<Estate> estates = new ArrayList<>();
		SecureRandom random = new SecureRandom();
		
		// create must have estates
		estates.add(new Estate(EstateType.ONE_PARCEL, rateOfPriceIncrease, createRandomPriceVariation(random)));
		estates.add(new Estate(EstateType.TWO_PARCEL, rateOfPriceIncrease, createRandomPriceVariation(random)));
		estates.add(new Estate(EstateType.THREE_PARCEL, rateOfPriceIncrease, createRandomPriceVariation(random)));
		estates.add(new Estate(EstateType.FOUR_PARCEL, rateOfPriceIncrease, createRandomPriceVariation(random)));

		int additionalEstates = random.nextInt(12);

		// create randomly optional estates
		for (int i = 0; i < additionalEstates; i++) {
			estates.add(new Estate(EstateType.randomType(), rateOfPriceIncrease, createRandomPriceVariation(random)));
		}
		
		return estates;
	}

	private BigDecimal createRandomPriceVariation(SecureRandom random) {
		double nextDouble = random.nextDouble();
		
		BigDecimal rounded = new BigDecimal(nextDouble).round(new MathContext(1));
		
		return rounded.add(new BigDecimal("1.0"));
	}
}
