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
	
	protected Department() {
	}
	
	public Department(DepartmentType type) {
		setType(type);
	}
	
	public BigDecimal getMonthlyCosts() {
		BigDecimal costs = BigDecimal.ZERO;
				
		for (Person person : personnel) {
			costs = costs.add(person.getMonthlyCosts());
		}
		
		for (Cabinet cabinet : cabinets) {
			costs = costs.add(cabinet.getMonthlyCosts());
		}
		
		return costs;
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
	
	public int getMonthlyCapacity() {
		int sumCapacity = 0;
		for (Cabinet cabinet : cabinets) {
			sumCapacity += cabinet.getCapacity();
		}
		
		return sumCapacity;
	}

	public DepartmentType getType() {
		return type;
	}

	public void setType(DepartmentType type) {
		this.type = type;
	}
}
