package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class TurnStatistic {

	private BigDecimal constructionCost = BigDecimal.ZERO;
	private BigDecimal personnelCosts = BigDecimal.ZERO;
	private BigDecimal runningCosts = BigDecimal.ZERO;
	private BigDecimal misc = BigDecimal.ZERO;
	private BigDecimal customers = BigDecimal.ZERO;
	private BigDecimal earnings = BigDecimal.ZERO;
	
	public BigDecimal getConstructionCost() {
		return constructionCost;
	}
	
	public BigDecimal getPersonnelCosts() {
		return personnelCosts;
	}
	
	public BigDecimal getMisc() {
		return misc;
	}
	
	public void setMisc(BigDecimal misc) {
		this.misc = misc;
	}
	
	public BigDecimal getCustomers() {
		return customers;
	}
	
	public BigDecimal getEarnings() {
		return earnings;
	}
	
	public BigDecimal getProfit() {
		return constructionCost.add(personnelCosts).add(runningCosts).add(misc).add(earnings);
	}
	
	public BigDecimal getRunningCosts() {
		return runningCosts;
	}
	
	public void increaseConstructionCosts(BigDecimal costs) {
		constructionCost = constructionCost.subtract(costs);
	}
	
	public void increasePersonnelCosts(BigDecimal costs) {
		personnelCosts = personnelCosts.subtract(costs);
	}
	
	public void increaseRunningCosts(BigDecimal costs) {
		runningCosts = runningCosts.subtract(costs);
	}
	
	public void increaseCustomers(BigDecimal cust) {
		customers = customers.add(cust);
	}
	
	public void increaseEarnings(BigDecimal earn) {
		earnings = earnings.add(earn);
	}

	@Override
	public String toString() {
		return "TurnStatistic [constructionCost=" + constructionCost + ", personnelCosts=" + personnelCosts + ", runningCosts=" + runningCosts + ", misc=" + misc + ", customers=" + customers
				+ ", earnings=" + earnings + "]";
	}

}
