package br.ifce.mastermind.client;

import br.ifce.mastermind.constants.NetConstants;
import br.ifce.mastermind.handler.KeywordHandler;
import br.ifce.mastermind.handler.MessageHandler;

import java.io.IOException;
import java.net.Socket;


/**
 * Created by jrocha on 24/07/14.
 */
public class Client {

    private static Client instance;

    private Client () {}

    public static Client getInstance (){
        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }


    public void start() {
        Socket clientSocket = null;

        try {
            clientSocket = new Socket(NetConstants.LOCALHOST, NetConstants.SERVER_PORT);

            KeywordHandler keywordHandler = new KeywordHandler(clientSocket);
            Thread keyword = new Thread(keywordHandler);
            keyword.start();

            MessageHandler messageHandler = new MessageHandler(clientSocket);
            Thread messages = new Thread(messageHandler);
            messages.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
