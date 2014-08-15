package br.ifce.mastermind.main;

import br.ifce.mastermind.server.Server;

/**
 * Created by jrocha on 25/07/14.
 */
public class Main {


    public static void main(String args[]) {
        Server server = Server.getInstance();
        server.start();
    }
}
