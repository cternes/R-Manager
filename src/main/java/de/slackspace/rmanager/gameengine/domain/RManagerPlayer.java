package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class RManagerPlayer {

	private String name;
	
	private BigDecimal money;
	
	private City currentCity;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public City getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
