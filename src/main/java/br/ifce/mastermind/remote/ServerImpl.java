package br.ifce.mastermind.remote;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.ChatMessage;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.entities.MasterMindPlayer;
import br.ifce.mastermind.entities.Message;
import br.ifce.mastermind.enums.PlayerType;

public class ServerImpl extends UnicastRemoteObject implements
		RemoteServer {
	
	private static final long	serialVersionUID	= 1L;
	
	private RemoteClient		player;
	
	private RemoteClient		master;
	
	private MasterMindMessage	password;
	
	public ServerImpl() throws RemoteException {
		super();
	}
	
	@Override
	public MasterMindPlayer connect(RemoteClient client) throws RemoteException {
		MasterMindPlayer masterMindPlayer = new MasterMindPlayer();
		
		if (this.master == null) {
			masterMindPlayer.setName(Constants.MASTER);
			masterMindPlayer.setType(PlayerType.MASTER);
			this.master = client;
		} else if (this.player == null) {
			masterMindPlayer = new MasterMindPlayer();
			masterMindPlayer.setName(Constants.PLAYER);
			masterMindPlayer.setType(PlayerType.PLAYER);
			this.player = client;
		} else {
			return null;
		}
		
		return masterMindPlayer;
	}
	
	@Override
	public Message getPassword() throws RemoteException {
		return this.password;
	}
	
	@Override
	public void processMessage(Message message) throws RemoteException {
		
		if (message instanceof MasterMindMessage) {
			MasterMindMessage m = (MasterMindMessage) message;
			
			if (message.getPlayer().getType().equals(PlayerType.MASTER)) {
				m.setPassword(true);
				this.password = m;
				this.master.addMessage(m);
				this.player.addMessage(m);
				
				if (this.player != null) {
					this.player.setEnableControls(true);
				}
					
			} else {
				this.checkMessage(m);
				this.master.addMessage(m);
				this.player.addMessage(m);
			}
			
		} else if (message instanceof ChatMessage) {
			
			this.master.addMessage(message);
			this.player.addMessage(message);
		}
		
	}
	
	@Override
	public RemoteClient getMaster() {
		return this.master;
	}
	
	public void checkMessage(MasterMindMessage message) {
		
		if (this.password != null) {
			Color[] passwordColors = password.getColors();
			Color[] messageColors = message.getColors();
			Color[] response = new Color[Constants.COLORS_QUANTITY];
			
			for (int i = 0; i < Constants.COLORS_QUANTITY; i++) {
				for (int j = 0; j < Constants.COLORS_QUANTITY; j++) {
					if (messageColors[i].equals(passwordColors[j])) {
						if (i == j) {
							response[i] = Color.BLACK;
							break;
						} else {
							response[i] = Color.WHITE;
							break;
						}
					} else {
						if (j == Constants.COLORS_QUANTITY - 1) { // last
																	// interaction
							response[i] = null;
							break;
						}
					}
				}
			}
			
			message.setResponse(response);
			message.setWinner(isWinner(message.getResponse()));
		}
	}
	
	private boolean isWinner(Color[] response) {
		
		boolean result = false;
		
		if (response != null && response.length > 0) {
			
			result = true;
			
			for (int i = 0; i < Constants.COLORS_QUANTITY; i++) {
				if (response[i] != null) {
					if (!response[i].equals(Color.BLACK)) {
						result = false;
						break;
					}
				} else {
					result = false;
					break;
				}
			}
		}
		
		return result;
		
	}
	
}