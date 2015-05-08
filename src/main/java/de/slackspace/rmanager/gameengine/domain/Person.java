package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Person {

	private String id = UUID.randomUUID().toString();
	private BigDecimal monthlyCosts;
	private int age;
	private int quality;
	private int power;
	private DepartmentType departmentType;
	
	protected Person() {
	}
	
	public Person(BigDecimal monthlyCosts, int age, int quality, int power, DepartmentType departmentType) {
		setMonthlyCosts(monthlyCosts);
		setAge(age);
		setQuality(quality);
		setPower(power);
		setDepartmentType(departmentType);
	}

	public BigDecimal getMonthlyCosts() {
		return monthlyCosts;
	}

	public void setMonthlyCosts(BigDecimal monthlyCosts) {
		this.monthlyCosts = monthlyCosts;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	/**
	 * Calculates the number of machines that could be 
	 * handled by this person. Calculation is based on power, quality
	 * and age of the person.
	 * 
	 * @return number of machines that could be handled
	 */
	public int getCapacity() {
		int capacity = (power + quality) / 4;
		
		double ageInPercent = (double) age / 100;
		double dCapacity = capacity - ageInPercent;
		
		capacity = (int) Math.round(dCapacity);
		
		if(capacity < 1) {
			capacity = 1;
		}
		
		return capacity;
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

}
