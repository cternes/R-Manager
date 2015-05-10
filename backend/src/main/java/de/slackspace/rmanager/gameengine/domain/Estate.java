package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Estate {

	private String id = UUID.randomUUID().toString();
	private EstateType estateType;
	private BigDecimal pricePerSquareMeter;
	private String cityId;
	private Building building;
	
	protected Estate() {
	}
	
	public Estate(EstateType estateType, BigDecimal rateOfPriceIncrease, BigDecimal rateOfPriceVariation, String cityId) {
		setEstateType(estateType);
		setCityId(cityId);
		
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

	public String getId() {
		return id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
	
	public boolean canBuild(BuildingType buildingType) {
		return buildingType.getRequiredParcels() <= getEstateType().getParcels();
	}

}
