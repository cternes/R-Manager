package de.slackspace.rmanager.gameengine.domain;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum DepartmentType {

	Laundry,
	Kitchen,
	Reefer,
	Facilities,
	Dininghall;
	
	private static final List<DepartmentType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new SecureRandom();

	public static DepartmentType randomType()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
