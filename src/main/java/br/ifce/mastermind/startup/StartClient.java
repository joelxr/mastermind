package br.ifce.mastermind.startup;

import java.rmi.Naming;

import br.ifce.mastermind.entities.MasterMindPlayer;
import br.ifce.mastermind.enums.PlayerType;
import br.ifce.mastermind.remote.ClientImpl;
import br.ifce.mastermind.remote.RemoteClient;
import br.ifce.mastermind.remote.RemoteServer;
import br.ifce.mastermind.window.ClientWindow;

public class StartClient {
	
	public static void main(String[] argv) {
		
		try {
			System.setSecurityManager(new SecurityManager());
			RemoteClient client = new ClientImpl();
			RemoteServer server = (RemoteServer) Naming.lookup("rmi://localhost/" + RemoteServer.class.getSimpleName());
			MasterMindPlayer player = server.connect(client);
			
			if (player != null) {
				ClientWindow.getInstance().setPlayer(player);
				ClientWindow.getInstance().setServer(server);
				ClientWindow.getInstance().start();
				
				if (player.getType().equals(PlayerType.PLAYER)) {
					ClientWindow.getInstance().setEnableControls(false);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}