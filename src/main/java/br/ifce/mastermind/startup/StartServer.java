package br.ifce.mastermind.startup;

import java.rmi.Naming;

import br.ifce.mastermind.remote.RemoteServer;
import br.ifce.mastermind.remote.ServerImpl;

public class StartServer {
	
	public static void main(String[] argv) {
		try {
			System.setSecurityManager(new SecurityManager());
			
			ServerImpl server = new ServerImpl();
			Naming.rebind("rmi://localhost/" + RemoteServer.class.getSimpleName(), server);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}