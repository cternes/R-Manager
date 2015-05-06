package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class Person {

	private BigDecimal monthlyCosts;
	
	protected Person() {
	}
	
	public Person(BigDecimal monthlyCosts) {
		setMonthlyCosts(monthlyCosts);
	}

	public BigDecimal getMonthlyCosts() {
		return monthlyCosts;
	}

	public void setMonthlyCosts(BigDecimal monthlyCosts) {
		this.monthlyCosts = monthlyCosts;
	}
}
