package de.slackspace.rmanager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Game {

	@Id
    @GeneratedValue
	private long id;

	public Game() {
    }
	
	public long getId() {
		return id;
	}
	
}
