package br.ifce.mastermind.main;

import br.ifce.mastermind.client.Client;
import br.ifce.mastermind.window.ClientWindow;

/**
 * Created by jrocha on 26/07/14.
 */
public class Main {

    private static Client client;

    public static void main(String args[]) {

        client = Client.getInstance();
        client.start();

    }
}
