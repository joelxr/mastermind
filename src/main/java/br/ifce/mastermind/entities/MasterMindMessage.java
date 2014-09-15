package br.ifce.mastermind.entities;

import java.awt.Color;

public class MasterMindMessage extends Message {

	private static final long serialVersionUID = 1L;

	private Color[] colors;

    private Color[] response;
    
    private boolean isPassword;
    
    private boolean isWinner;

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Color[] getResponse() { return this.response; }

    public void setResponse(Color[] response) {
        this.response = response;
    }

	public boolean isPassword() {
		return isPassword;
	}

	public void setPassword(boolean isPassword) {
		this.isPassword = isPassword;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
    
    
}
