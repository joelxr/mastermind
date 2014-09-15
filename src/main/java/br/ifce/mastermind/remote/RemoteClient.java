package br.ifce.mastermind.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.ifce.mastermind.entities.Message;

public interface RemoteClient extends Remote {
	
	public void addMessage(Message message) throws RemoteException;
		
	public void setEnableControls(boolean flag) throws RemoteException;
}
