package br.ifce.mastermind.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.ifce.mastermind.entities.Message;
import br.ifce.mastermind.entities.MasterMindPlayer;

public interface RemoteServer extends Remote {
	
	public MasterMindPlayer connect(RemoteClient client) throws RemoteException;
	
	public RemoteClient getMaster() throws RemoteException;
	
	public Message getPassword() throws RemoteException;
	
	public void processMessage(Message message) throws RemoteException;
	
	
	
}