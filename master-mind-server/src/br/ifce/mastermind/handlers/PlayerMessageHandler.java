package br.ifce.mastermind.handlers;

import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.util.MessageUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by jrocha on 25/07/14.
 */
public class PlayerMessageHandler extends AbstractMessageHandler {

    public PlayerMessageHandler(Socket socket) {
        super(ClientType.PLAYER, socket);
    }

    @Override
    public void process() {

        try {
            MessageUtil.sendMessage(getSocket(), Thread.currentThread().getName());

            MasterMindMessage message;

            while (true) {
                message = MessageUtil.getMasterMindMessage(getSocket());
                message.setType(getType());

                getLogger().info("Adding the follow message....  " + message);
                getEngine().addMessage(message);
                getEngine().checkMessage(message);

                MessageUtil.sendMasterMindMessage(getSocket(), message);
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, Thread.currentThread().getName() + " is aborting the game...");
            Thread.currentThread().interrupt();
        }
    }
}
