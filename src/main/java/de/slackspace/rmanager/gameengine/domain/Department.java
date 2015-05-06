package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Department {

	private List<Cabinet> cabinets = new ArrayList<Cabinet>();
	
	private List<Person> personnel = new ArrayList<Person>();
	
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

	public List<Cabinet> getCabinets() {
		return cabinets;
	}

	public void setCabinets(List<Cabinet> cabinets) {
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
}
