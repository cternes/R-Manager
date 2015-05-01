package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class Estate {

	private EstateType estateType;
	
	private BigDecimal pricePerSquareMeter;
	
	public Estate(EstateType estateType, BigDecimal rateOfPriceIncrease, BigDecimal rateOfPriceVariation) {
		setEstateType(estateType);
		
		// calculate price per square meter
		pricePerSquareMeter = EstateType.getMeanPricePerSquareMeter().multiply(rateOfPriceIncrease).multiply(rateOfPriceVariation);
	}
	
	public BigDecimal getSquareMeter() {
		return getEstateType().getSquareMeters();
	}

	public BigDecimal getPricePerSquareMeter() {
		return pricePerSquareMeter;
	}

	public EstateType getEstateType() {
		return estateType;
	}

	public void setEstateType(EstateType estateType) {
		this.estateType = estateType;
	}

	public BigDecimal getTotalPrice() {
		return getPricePerSquareMeter().multiply(estateType.getSquareMeters());
	}

	@Override
	public String toString() {
		return "Estate [estateType=" + estateType + ", pricePerSquareMeter=" + pricePerSquareMeter + ", getTotalPrice()=" + getTotalPrice() + "]";
	}
	
}
