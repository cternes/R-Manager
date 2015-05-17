package de.slackspace.rmanager.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	private long id;
	
	@NotNull
	@Size(max=36)
	@JsonProperty(value="id")
	private String token;
	
	@NotNull
	@Size(max=255)
	private String name;
	
	@NotNull
	@Size(max=55)
	private String password;
	
	@NotNull
	private int wins;
	
	@NotNull
	private int losses;
	
	@NotNull
	private int draws;
	
	protected Player() {
	}
	
	public Player(String name, String password) {
		setName(name);
		setPassword(password);
		setToken(UUID.randomUUID().toString());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public void increaseWins() {
		setWins(++wins);
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}
	
	public void increaseLosses() {
		setLosses(++losses);
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}
	
	public void increaseDraws() {
		setDraws(++draws);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
