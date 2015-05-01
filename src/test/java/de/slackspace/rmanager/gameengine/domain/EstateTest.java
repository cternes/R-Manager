package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class EstateTest {

	@DataPoints
	public static TestData[] positiveIntegers() {
		return new TestData[] {
			new TestData("100.00", "1.0", "1.0", "25000.00"),
			new TestData("150.00", "1.5", "1.0", "37500.00"),
			new TestData("170.00", "1.0", "1.7", "42500.00"),
			new TestData("192.00", "1.6", "1.2", "48000.00")
		};
	}

	@Theory
	public void whenCreateEstateShouldCalculateCorrectPrices(TestData testData) {
		Estate cut = new Estate(EstateType.ONE_PARCEL, new BigDecimal(testData.getRateOfPriceIncrease()), new BigDecimal(testData.getRateOfPriceVariation()), "12345");
		
		Assert.assertTrue("Price was: " + cut.getPricePerSquareMeter() + "Expected: " + testData.getExpected(), new BigDecimal(testData.getExpected()).equals(cut.getPricePerSquareMeter()));
		Assert.assertTrue("Total Price was: " + cut.getTotalPrice() + "Expected: " + testData.getExpectedTotal(), new BigDecimal(testData.getExpectedTotal()).equals(cut.getTotalPrice()));
	}
	
	static class TestData {
		private String expected;
		private String rateOfPriceIncrease;
		private String rateOfPriceVariation;
		private String expectedTotal;
		
		public TestData(String expected, String rateOfPriceIncrease, String rateOfPriceVariation, String expectedTotal) {
			this.expected = expected;
			this.rateOfPriceIncrease = rateOfPriceIncrease;
			this.rateOfPriceVariation = rateOfPriceVariation;
			this.expectedTotal = expectedTotal;
		}

		public String getExpected() {
			return expected;
		}

		public String getRateOfPriceIncrease() {
			return rateOfPriceIncrease;
		}
		
		public String getRateOfPriceVariation() {
			return rateOfPriceVariation;
		}

		public String getExpectedTotal() {
			return expectedTotal;
		}
	}
}
