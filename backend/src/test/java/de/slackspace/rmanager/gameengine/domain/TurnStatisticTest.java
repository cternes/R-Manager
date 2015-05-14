package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class TurnStatisticTest {

	@Test
	public void whenIncreaseConstructionCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseConstructionCosts(new BigDecimal(1000));
		
		Assert.assertEquals(new BigDecimal(-1000), cut.getConstructionCost());
	}
	
	@Test
	public void whenIncreasePersonnelCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increasePersonnelCosts(new BigDecimal(1000));
		
		Assert.assertEquals(new BigDecimal(-1000), cut.getPersonnelCosts());
	}
	
	@Test
	public void whenIncreaseRunningCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseRunningCosts(new BigDecimal(1000));
		
		Assert.assertEquals(new BigDecimal(-1000), cut.getRunningCosts());
	}
	
	@Test
	public void whenGetProfitShouldCalculateProfif() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseConstructionCosts(new BigDecimal("1000"));
		cut.increasePersonnelCosts(new BigDecimal("2500.50"));
		cut.increaseRunningCosts(new BigDecimal("1250"));
		
		cut.increaseEarnings(new BigDecimal("2000"));
		
		Assert.assertEquals(new BigDecimal("-2750.50"), cut.getProfit());
	}
}
