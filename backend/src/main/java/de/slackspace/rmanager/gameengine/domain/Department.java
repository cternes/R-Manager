package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Department {

	private Set<Cabinet> cabinets = new HashSet<Cabinet>();
	private List<Person> personnel = new ArrayList<Person>();
	private DepartmentType type;
	private int maxSpaceUnits;
	
	protected Department() {
	}
	
	public Department(DepartmentType type, int maxSpaceUnits) {
		setType(type);
		setMaxSpaceunits(maxSpaceUnits);
	}
	
	public BigDecimal getMonthlyCosts() {
		BigDecimal costs = BigDecimal.ZERO;
				
		for (Person person : personnel) {
			costs = costs.add(person.getMonthlyCosts());
		}
		
		for (Cabinet cabinet : cabinets) {
			costs = costs.add(cabinet.getTotalMonthlyCosts());
		}
		
		return costs;
	}
	
	/**
	 * Calculates the number of meals produced by this department. Number of meals
	 * is the sum of all equipment hold by this department.
	 * 
	 * Each equipment needs a person to produce meals. One person can handle
	 * 1 equipment as a minimum and 5 as maximum (depends on person stats). 
	 *  
	 * @return the number of meals produced by all equipment
	 */
	public BigDecimal getMonthlyOutput() {
		int meals = 0;
		int numberOfMachines = 0;
		
		for (Person person : getPersonnel()) {
			numberOfMachines += person.getCapacity();
		}
		
		if(numberOfMachines == 0) {
			return BigDecimal.ZERO;
		}

		int i = 0;
		for (Cabinet cabinet : cabinets) {
			meals += cabinet.getCapacity();
			
			if(i == numberOfMachines -1) {
				return new BigDecimal(meals);
			}
			
			i++;
		}
		
		return new BigDecimal(meals);
	}

	public Set<Cabinet> getCabinets() {
		return cabinets;
	}

	public void setCabinets(Set<Cabinet> cabinets) {
		this.cabinets = cabinets;
	}

	public List<Person> getPersonnel() {
		return personnel;
	}

	public void setPersonnel(List<Person> personnel) {
		this.personnel = personnel;
	}
	
	public DepartmentType getType() {
		return type;
	}

	public void setType(DepartmentType type) {
		this.type = type;
	}

	public int getMaxSpaceunits() {
		return maxSpaceUnits;
	}

	public void setMaxSpaceunits(int maxSpaceunits) {
		this.maxSpaceUnits = maxSpaceunits;
	}
	
	public boolean canAddCabinet(int requiredSpaceUnits) {
		int spaceUnitsInUse = 0;
		for (Cabinet cabinet : getCabinets()) {
			spaceUnitsInUse += cabinet.getTotalRequiredSpaceUnits();
		}
		
		if(spaceUnitsInUse + requiredSpaceUnits <= maxSpaceUnits) {
			return true;
		}
		
		return false;
	}
}
