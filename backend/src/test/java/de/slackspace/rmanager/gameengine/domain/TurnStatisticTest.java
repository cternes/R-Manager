package de.slackspace.rmanager.gameengine.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class TurnStatisticTest {

	@Test
	public void whenIncreaseConstructionCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseConstructionCosts(new BigDecimal(1000));
		
		assertThat(new BigDecimal(-1000), equalTo(cut.getConstructionCost()));
	}
	
	@Test
	public void whenIncreasePersonnelCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increasePersonnelCosts(new BigDecimal(1000));
		
		assertThat(new BigDecimal(-1000), equalTo(cut.getPersonnelCosts()));
	}
	
	@Test
	public void whenIncreaseRunningCostsShouldReturnNegativeCosts() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseRunningCosts(new BigDecimal(1000));
		
		assertThat(new BigDecimal(-1000), equalTo(cut.getRunningCosts()));
	}
	
	@Test
	public void whenGetProfitShouldCalculateProfif() {
		TurnStatistic cut = new TurnStatistic();
		cut.increaseConstructionCosts(new BigDecimal("1000"));
		cut.increasePersonnelCosts(new BigDecimal("2500.50"));
		cut.increaseRunningCosts(new BigDecimal("1250"));
		
		cut.increaseEarnings(new BigDecimal("2000"));

		assertThat(new BigDecimal("-2750.50"), equalTo(cut.getProfit()));
	}
}
