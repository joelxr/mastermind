package br.ifce.mastermind.main;

import br.ifce.mastermind.client.Client;
import br.ifce.mastermind.window.ClientWindow;

/**
 * Created by jrocha on 26/07/14.
 */
public class Main {

    private static Client client;

    private static ClientWindow window;

    public static void main (String args[]) {

        client = Client.getInstance();
        window = new ClientWindow();

        client.start();
        window.start();
    }
}
