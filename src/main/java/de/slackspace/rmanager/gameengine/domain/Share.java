package de.slackspace.rmanager.gameengine.domain;

import java.math.BigDecimal;

public class Share {

	private String id;
	private int sharePercent;
	private BigDecimal value = BigDecimal.ZERO;
	
	protected Share() {
	}
	
	public Share(String id, int sharePercent, BigDecimal value) {
		this.id = id;
		setSharePercent(sharePercent);
		setValue(value);
	}
	
	public String getId() {
		return id;
	}
	
	public int getSharePercent() {
		return sharePercent;
	}
	
	public void setSharePercent(int sharePercent) {
		this.sharePercent = sharePercent;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTotalPrice(int percent) {
		return value.multiply(new BigDecimal(percent));
	}
	
	public boolean canReduceShareBy(int percent) {
		if(sharePercent - percent < 0) {
			return false;
		}
		
		return true;
	}

	public void reduceShare(int percent) {
		sharePercent = sharePercent - percent;
	}
}
