package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class City {

	private String id = UUID.randomUUID().toString();
	private String name;
	private BigDecimal rateOfPriceIncrease;

	private List<Estate> estates = new ArrayList<>();
	private List<Person> availablePersonnel = new ArrayList<>();
	private Map<DepartmentType, List<Cabinet>> availableCabinet = new HashMap<>();
	
	protected City() {
	}

	public City(String name, BigDecimal rateOfPriceIncrease) {
		setName(name);
		this.rateOfPriceIncrease = rateOfPriceIncrease;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Estate> getEstates() {
		return estates;
	}

	public void setEstates(List<Estate> estates) {
		this.estates = estates;
	}

	public BigDecimal getRateOfPriceIncrease() {
		return rateOfPriceIncrease;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", rateOfPriceIncrease=" + rateOfPriceIncrease + ", estates=" + estates + "]";
	}

	public String getId() {
		return id;
	}

	public List<Person> getAvailablePersonnel() {
		return availablePersonnel;
	}

	public void setAvailablePersonnel(List<Person> availablePersonnel) {
		this.availablePersonnel = availablePersonnel;
	}

	public List<Cabinet> getAvailableCabinetByType(DepartmentType type) {
		return availableCabinet.get(type);
	}
	
	public void setAvailableCabinet(DepartmentType type, List<Cabinet> cabinets) {
		availableCabinet.put(type, cabinets);
	}
	
	public Map<DepartmentType, List<Cabinet>> getAvailableCabinet() {
		return availableCabinet;
	}
	
	public Cabinet getAvailableCabinetById(String id) {
		Iterator<Entry<DepartmentType, List<Cabinet>>> iter = availableCabinet.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<DepartmentType, List<Cabinet>> entry = iter.next();
			
			for (Cabinet cabinet : entry.getValue()) {
				if(cabinet.getId().equals(id)) {
					return cabinet;
				}
			}
		}
		
		return null;
	}
}
