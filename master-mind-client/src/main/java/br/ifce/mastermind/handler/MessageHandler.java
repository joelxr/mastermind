package br.ifce.mastermind.handler;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;
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

                MasterMindMessage message = MessageUtil.getMasterMindMessage(socket);

                if (message.getRaw().equals(Constants.WINNER)) {
                    clientWindow.disableControls();
                    clientWindow.setPasswordRow(message);
                } else if (message.getType().equals(ClientType.MASTER) && Thread.currentThread().getName().contains(Constants.PLAYER)) {
                    clientWindow.addMasterPasswordMessage(message);
                } else {
                    clientWindow.addServerConfirmationColors(message);
                }

                if (Thread.currentThread().getName().equals(Constants.MASTER)) {
                    clientWindow.disableControls();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't read a message from stream!", e);
            Thread.currentThread().interrupt();
        }
    }
}
