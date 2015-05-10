package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Cabinet {

	private String id = UUID.randomUUID().toString();
	private BigDecimal price;
	private BigDecimal monthlyCosts;
	private int capacity;
	private DepartmentType departmentType;
	private int quantity;
	private int requiredSpaceUnits = 1;
	
	protected Cabinet() {
	}
	
	public Cabinet(BigDecimal price, BigDecimal monthlyCosts, int capacity, DepartmentType departmentType)  {
		this(price, monthlyCosts, capacity, departmentType, 0);
	}
	
	public Cabinet(BigDecimal price, BigDecimal monthlyCosts, int capacity, DepartmentType departmentType, int quantity)  {
		setMonthlyCosts(monthlyCosts);
		setPrice(price);
		setCapacity(capacity);
		setDepartmentType(departmentType);
		this.quantity = quantity;
	}

	public BigDecimal getMonthlyCosts() {
		return monthlyCosts.multiply(new BigDecimal(quantity));
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
		return capacity * quantity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getId() {
		return id;
	}

	public DepartmentType getDepartmentType() {
		return departmentType;
	}

	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void increaseQuantity(int num) {
		quantity += num;
	}
	
	public void decreaseQuantity(int num) {
		quantity -= num;
	}

	public int getRequiredSpaceUnits() {
		return requiredSpaceUnits;
	}

	public void setRequiredSpaceUnits(int requiredSpaceUnits) {
		this.requiredSpaceUnits = requiredSpaceUnits;
	}
}
