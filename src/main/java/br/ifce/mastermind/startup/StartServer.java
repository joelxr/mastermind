package br.ifce.mastermind.startup;

import java.rmi.Naming;

import br.ifce.mastermind.remote.RemoteServer;
import br.ifce.mastermind.remote.ServerImpl;

public class StartServer {

	public static void main(String[] argv) {
		try {
			String host = argv[0];
			String port = argv[1];

			ServerImpl server = new ServerImpl();
			Naming.rebind("rmi://" + host + ":" + port + "/"
					+ RemoteServer.class.getSimpleName(), server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}