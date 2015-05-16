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
		return monthlyCosts;
	}
	
	public BigDecimal getTotalMonthlyCosts() {
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
		return capacity;
	}
	
	public int getTotalCapacity() {
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
	
	public int getTotalRequiredSpaceUnits() {
		return requiredSpaceUnits * quantity;
	}

	public void setRequiredSpaceUnits(int requiredSpaceUnits) {
		this.requiredSpaceUnits = requiredSpaceUnits;
	}
	
	public int getQuantityToBuy() {
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departmentType == null) ? 0 : departmentType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cabinet other = (Cabinet) obj;
		if (departmentType != other.departmentType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
