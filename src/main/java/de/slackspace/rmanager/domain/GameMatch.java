package de.slackspace.rmanager.domain;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class GameMatch {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	private long id;
	
	@NotNull
	@Size(max=36)
	@JsonProperty(value="id")
	private String token;
	
	@NotNull
	private Calendar createdDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	
	@ManyToOne
	@NotNull
	private Player player1;
	
	@ManyToOne
	private Player player2;
	
	@NotNull
	private MatchStatus status = MatchStatus.WAITINGFORPLAYERS;
	
	@Column(name="match_data",columnDefinition="blob")
	private byte[] matchData;
	
	private MatchResult matchResult;
	
	@Size(max=500)
	private String message;

	protected GameMatch() {
	}
	
	public GameMatch(Player player) {
		setPlayer1(player);
		setToken(UUID.randomUUID().toString());
    }
	
	public long getId() {
		return id;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public MatchStatus getStatus() {
		return status;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public byte[] getMatchData() {
		return matchData;
	}

	public void setMatchData(byte[] matchData) {
		this.matchData = matchData;
	}

	public MatchResult getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(MatchResult matchResult) {
		this.matchResult = matchResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
