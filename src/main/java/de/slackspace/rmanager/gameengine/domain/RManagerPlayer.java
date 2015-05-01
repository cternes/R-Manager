package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RManagerPlayer {

	private String name;
	
	private BigDecimal money;
	
	private City currentCity;
	
	private List<Estate> estates = new ArrayList<>();

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

	public List<Estate> getEstates() {
		return estates;
	}

	public void setEstates(List<Estate> estates) {
		this.estates = estates;
	}
	
	public boolean canBuy(BigDecimal price) {
		BigDecimal tmpMoney = money.subtract(price);
		
		if(tmpMoney.compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		
		return true;
	}
	
	public void buy(BigDecimal price) {
		money = money.subtract(price);
	}
	
}
