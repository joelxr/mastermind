package br.ifce.mastermind.client;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.handler.MessageHandler;
import br.ifce.mastermind.message.ControlMessage;
import br.ifce.mastermind.util.MessageUtil;
import br.ifce.mastermind.window.ClientWindow;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 24/07/14.
 */
public class Client {

    private static Logger logger = Logger.getLogger(Client.class.getName());
    private static Client instance;

    private ClientWindow clientWindow;
    private Socket clientSocket;
    private String name;

    private Client() {
        this.connect();
        this.retrieveClientName();
        this.clientWindow = ClientWindow.getInstance();
        this.clientWindow.setNameLabelValue(name);
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }

    private void retrieveClientName() {

        try {
            ControlMessage controlMessage = (ControlMessage) MessageUtil.getMasterMindMessage(clientSocket);
            this.name = controlMessage.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't retrieve the thread name!", e);
        }
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void start() {
        MessageHandler messageHandler = new MessageHandler(clientSocket);
        Thread messages = new Thread(messageHandler, name);
        messages.start();

        this.clientWindow.start();
    }

    private void connect() {
        try {
            this.clientSocket = new Socket(Constants.LOCALHOST, Constants.SERVER_PORT);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't connect to the server, make sure the server has been fully started!", e);
        }
    }
}
