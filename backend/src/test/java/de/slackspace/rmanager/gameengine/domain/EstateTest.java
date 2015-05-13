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
	public static PriceTestData[] prices() {
		return new PriceTestData[] {
			new PriceTestData("100.00", "1.0", "1.0", "25000.00"),
			new PriceTestData("150.00", "1.5", "1.0", "37500.00"),
			new PriceTestData("170.00", "1.0", "1.7", "42500.00"),
			new PriceTestData("192.00", "1.6", "1.2", "48000.00")
		};
	}
	
	@Theory
	public void whenCreateEstateShouldCalculateCorrectPrices(PriceTestData testData) {
		Estate cut = new Estate(EstateType.ONE_PARCEL, new BigDecimal(testData.getRateOfPriceIncrease()), new BigDecimal(testData.getRateOfPriceVariation()), "12345");
		
		Assert.assertTrue("Price was: " + cut.getPricePerSquareMeter() + "Expected: " + testData.getExpected(), new BigDecimal(testData.getExpected()).equals(cut.getPricePerSquareMeter()));
		Assert.assertTrue("Total Price was: " + cut.getTotalPrice() + "Expected: " + testData.getExpectedTotal(), new BigDecimal(testData.getExpectedTotal()).equals(cut.getTotalPrice()));
	}
	
	@DataPoints
	public static ParcelTestData[] parcels() {
		BuildingType btOne = new BuildingType(1, BigDecimal.ZERO);
		BuildingType btTwo = new BuildingType(2, BigDecimal.ZERO);
		BuildingType btThree = new BuildingType(3, BigDecimal.ZERO);
		BuildingType btFour = new BuildingType(4, BigDecimal.ZERO);
		
		return new ParcelTestData[] {
			new ParcelTestData(true, EstateType.ONE_PARCEL, btOne),
			new ParcelTestData(false, EstateType.ONE_PARCEL, btTwo),
			new ParcelTestData(false, EstateType.ONE_PARCEL, btThree),
			new ParcelTestData(false, EstateType.ONE_PARCEL, btFour),
			new ParcelTestData(true, EstateType.TWO_PARCEL, btOne),
			new ParcelTestData(true, EstateType.TWO_PARCEL, btTwo),
			new ParcelTestData(false, EstateType.TWO_PARCEL, btThree),
			new ParcelTestData(false, EstateType.TWO_PARCEL, btFour),
			new ParcelTestData(true, EstateType.THREE_PARCEL, btOne),
			new ParcelTestData(true, EstateType.THREE_PARCEL, btTwo),
			new ParcelTestData(true, EstateType.THREE_PARCEL, btThree),
			new ParcelTestData(false, EstateType.THREE_PARCEL, btFour),
			new ParcelTestData(true, EstateType.FOUR_PARCEL, btOne),
			new ParcelTestData(true, EstateType.FOUR_PARCEL, btTwo),
			new ParcelTestData(true, EstateType.FOUR_PARCEL, btThree),
			new ParcelTestData(true, EstateType.FOUR_PARCEL, btFour),
		};
	}
	
	@Theory
	public void whenCanBuildShouldValidateSize(ParcelTestData testData) {
		Estate estate = new Estate(testData.estateType, BigDecimal.ONE, BigDecimal.ONE, "");
		
		Assert.assertEquals(testData.getExpected(), estate.canBuild(testData.buildingType));
	}
	
	static class ParcelTestData {
		
		private boolean expected;
		private EstateType estateType;
		private BuildingType buildingType;
		
		public ParcelTestData(boolean expected, EstateType estateType, BuildingType buildingType) {
			this.expected = expected;
			this.estateType = estateType;
			this.buildingType = buildingType;
		}

		public boolean getExpected() {
			return expected;
		}

		public EstateType getEstateType() {
			return estateType;
		}

		public BuildingType getBuildingType() {
			return buildingType;
		}
		
	}
	
	static class PriceTestData {
		private String expected;
		private String rateOfPriceIncrease;
		private String rateOfPriceVariation;
		private String expectedTotal;
		
		public PriceTestData(String expected, String rateOfPriceIncrease, String rateOfPriceVariation, String expectedTotal) {
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
