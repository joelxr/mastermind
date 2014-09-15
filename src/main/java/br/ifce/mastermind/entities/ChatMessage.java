package br.ifce.mastermind.entities;

import java.util.Date;

public class ChatMessage extends Message {
	
	private static final long	serialVersionUID	= 1L;
	
	private Date				time;
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
}
