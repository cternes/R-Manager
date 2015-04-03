package de.slackspace.rmanager.domain;

import java.util.Calendar;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GameMatch {

	@Id
	@GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	
	@NotNull
	private Calendar createdDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	
	@ManyToOne
	@NotNull
	private Player player1;
	
	@ManyToOne
	private Player player2;
	
	@NotNull
	private MatchStatus status = MatchStatus.WAITINGFORPLAYERS;

	public GameMatch() {
	}
	
	public GameMatch(Player player) {
		setPlayer1(player);
    }
	
	public String getId() {
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
	
}
