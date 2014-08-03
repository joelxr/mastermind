package br.ifce.mastermind.handler;

import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.util.MessageUtil;
import br.ifce.mastermind.window.ClientWindow;

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
                if (Thread.currentThread().getName().equals("MASTER")) {

                    String result = MessageUtil.getMessage(socket);

                    if ("MASTER".equalsIgnoreCase(result)) {
                        clientWindow.disableControls();
                        continue;
                    }

                } else if (Thread.currentThread().getName().contains("PLAYER")) {
                    MasterMindMessage message = MessageUtil.getMasterMindMessage(socket);
                    clientWindow.addServerConfirmationColors(message);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't read a message from stream!", e);
            Thread.currentThread().interrupt();
        }
    }
}
