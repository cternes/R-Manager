package de.slackspace.rmanager.domain;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Turn {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	private long id;
	
	@ManyToOne
	@NotNull
	private GameMatch gameMatch;
	
	@ManyToOne
	@NotNull
	private Player player;
	
	@Column(name="turn_data", columnDefinition="blob")
	private byte[] turnData;
	
	@NotNull
	private Calendar createdDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	
	protected Turn() {
	}
	
	public Turn(GameMatch match, Player player) {
		this.gameMatch = match;
		this.player = player;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public byte[] getTurnData() {
		return turnData;
	}

	public void setTurnData(byte[] turnData) {
		this.turnData = turnData;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public GameMatch getGameMatch() {
		return gameMatch;
	}

	public void setGameMatch(GameMatch gameMatch) {
		this.gameMatch = gameMatch;
	}
	
}
