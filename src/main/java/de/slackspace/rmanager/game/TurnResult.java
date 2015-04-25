package de.slackspace.rmanager.game;

import de.slackspace.rmanager.domain.MatchResult;

public class TurnResult {

	private boolean hasMatchEnded;
	
	private MatchResult matchResult;
	
	private byte[] matchData;

	protected TurnResult() {
	}
	
	public TurnResult(byte[] matchData, MatchResult matchResult) {
		setMatchData(matchData);
		setMatchResult(matchResult);
		
		if(matchResult != null) {
			setHasMatchEnded(true);
		}
	}
	
	public boolean hasMatchEnded() {
		return hasMatchEnded;
	}

	public void setHasMatchEnded(boolean hasMatchEnded) {
		this.hasMatchEnded = hasMatchEnded;
	}

	public MatchResult getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(MatchResult matchResult) {
		this.matchResult = matchResult;
	}

	public byte[] getMatchData() {
		return matchData;
	}

	public void setMatchData(byte[] gameData) {
		this.matchData = gameData;
	}
	
}
