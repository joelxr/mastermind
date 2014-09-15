package br.ifce.mastermind.entities;

import java.io.Serializable;

import br.ifce.mastermind.enums.PlayerType;

public class MasterMindPlayer implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	
	private String				name;
	
	private PlayerType			type;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public PlayerType getType() {
		return type;
	}
	
	public void setType(PlayerType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Player{" + "name='" + name + '\'' + ", type=" + type + '}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		MasterMindPlayer player = (MasterMindPlayer) o;
		
		if (name != null ? !name.equals(player.name) : player.name != null) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
