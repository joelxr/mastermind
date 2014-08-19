package br.ifce.mastermind.handler;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.message.AbstractMessage;
import br.ifce.mastermind.message.ChatMessage;
import br.ifce.mastermind.message.ControlMessage;
import br.ifce.mastermind.message.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;
import br.ifce.mastermind.window.ClientWindow;

import javax.swing.*;
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

                AbstractMessage message = MessageUtil.getMasterMindMessage(socket);

                if (message instanceof MasterMindMessage) {
                    MasterMindMessage masterMindMessage = (MasterMindMessage) message;

                    if (masterMindMessage.getRaw().equals(Constants.WINNER)) {
                        clientWindow.disableControls();
                        clientWindow.setPasswordRow(masterMindMessage);
                        JOptionPane.showMessageDialog(null, "Use: " + masterMindMessage.getClientName() + " won the game!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else if (message.getClientType().equals(ClientType.MASTER) && Thread.currentThread().getName().contains(Constants.PLAYER)) {
                        clientWindow.addMasterPasswordMessage(masterMindMessage);
                        JOptionPane.showMessageDialog(null, "Password defined!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        clientWindow.addServerConfirmationColors(masterMindMessage);
                    }

                    if (Thread.currentThread().getName().equals(Constants.MASTER)) {
                        clientWindow.disableControls();
                    }

                } else if (message instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) message;
                    clientWindow.addChatMessage(chatMessage);
                } else if (message instanceof ControlMessage) {
                    ControlMessage controlMessage = (ControlMessage) message;

                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't read a message from stream!", e);
            Thread.currentThread().interrupt();
        }
    }
}
