package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class Cabinet {

	private BigDecimal price;
	private BigDecimal monthlyCosts;
	private int capacity;
	
	protected Cabinet() {
	}
	
	public Cabinet(BigDecimal price, BigDecimal monthlyCosts, int capacity)  {
		setMonthlyCosts(monthlyCosts);
		setPrice(price);
		setCapacity(capacity);
	}

	public BigDecimal getMonthlyCosts() {
		return monthlyCosts;
	}

	public void setMonthlyCosts(BigDecimal monthlyCosts) {
		this.monthlyCosts = monthlyCosts;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
