package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class City {

	private String id = UUID.randomUUID().toString();
	
	private String name;
	
	private BigDecimal rateOfPriceIncrease;
	
	private List<Estate> estates = new ArrayList<>();
	
	protected City() {
	}

	public City(String name, BigDecimal rateOfPriceIncrease) {
		setName(name);
		this.rateOfPriceIncrease = rateOfPriceIncrease;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Estate> getEstates() {
		return estates;
	}

	public void setEstates(List<Estate> estates) {
		this.estates = estates;
	}

	public BigDecimal getRateOfPriceIncrease() {
		return rateOfPriceIncrease;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", rateOfPriceIncrease=" + rateOfPriceIncrease + ", estates=" + estates + "]";
	}

	public String getId() {
		return id;
	}
}
