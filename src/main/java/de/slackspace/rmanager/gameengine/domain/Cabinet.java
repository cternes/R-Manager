package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class Cabinet {

	private BigDecimal monthlyCosts;
	
	protected Cabinet() {
	}
	
	public Cabinet(BigDecimal monthlyCosts) {
		setMonthlyCosts(monthlyCosts);
	}

	public BigDecimal getMonthlyCosts() {
		return monthlyCosts;
	}

	public void setMonthlyCosts(BigDecimal monthlyCosts) {
		this.monthlyCosts = monthlyCosts;
	}
}
