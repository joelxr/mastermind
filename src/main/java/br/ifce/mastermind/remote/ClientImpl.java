package br.ifce.mastermind.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.ifce.mastermind.entities.ChatMessage;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.entities.Message;
import br.ifce.mastermind.enums.PlayerType;
import br.ifce.mastermind.window.ClientWindow;

public class ClientImpl extends UnicastRemoteObject implements RemoteClient {
	
	private static final long	serialVersionUID	= 1L;
	
	public ClientImpl() throws RemoteException {
		super();
	}
	
	@Override
	public void addMessage(Message message) throws RemoteException {
		
		if (message instanceof ChatMessage) {
			ClientWindow.getInstance().addChatMessage(message);
		} else if (message instanceof MasterMindMessage) {
			
			MasterMindMessage msg = (MasterMindMessage) message;
			
			if (msg.isPassword() && ClientWindow.getInstance().getPlayer().getType().equals(PlayerType.MASTER)) {
				ClientWindow.getInstance().setPasswordRow(msg);
			} else {
				if (msg.getPlayer().getType().equals(PlayerType.PLAYER)) {
					ClientWindow.getInstance().addServerConfirmationMessage(message);
				}
				
				if (msg.isWinner()) {
					ClientWindow.getInstance().setPasswordRow(msg);
					ClientWindow.getInstance().setEnableControls(false);
				}
			}
		}
	}
	
	@Override
	public void setEnableControls(boolean flag) throws RemoteException {
		ClientWindow.getInstance().setEnableControls(flag);		
	}
}
