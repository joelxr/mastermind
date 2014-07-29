package br.ifce.mastermind.handler;

import br.ifce.mastermind.util.MessageUtil;
import br.ifce.mastermind.window.ClientWindow;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jrocha on 25/07/14.
 */
public class MessageHandler implements Runnable {

    private static Logger logger = Logger.getLogger(MessageHandler.class.getName());

    private Socket socket;

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        process();
    }

    private void process() {

        try {

            ClientWindow clientWindow = ClientWindow.getInstance();

            while (true) {
                String result = MessageUtil.getMessage(socket) ;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't read a message from stream!" , e);
            Thread.currentThread().interrupt();
        }
    }
}
